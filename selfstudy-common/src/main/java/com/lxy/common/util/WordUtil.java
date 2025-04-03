package com.lxy.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.lxy.common.properties.AliYunProperties;
import org.apache.poi.xwpf.usermodel.*;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2024/11/16 16:38
 * @version 1.0
 */
public class WordUtil {

    private final static Logger LOG = LoggerFactory.getLogger(WordUtil.class);

    private static final String CONVERT_PATH = "/opt/convert/OMML2MML.XSL";
    private static final String MML2TEX_PATH = "/opt/convert/mml2tex/";
    private static final String MMLTEX_PATH = "/opt/convert/mml2tex/mmltex.xsl";

    /**
     * Description: 解析word：文本、图片、表格、数学公式内容
     * author: jiacheng yang.
     * Date: 2024/11/16 16:39
     * Param: [inputStream]
     */
    public static List<String> wordAnalysisAutomation(InputStream inputStream) throws Exception{
        List<String> paragraphs = new ArrayList<>();
        XWPFDocument word = new XWPFDocument(inputStream);
        for (IBodyElement ibodyelement : word.getBodyElements()) {
            if (ibodyelement.getElementType().equals(BodyElementType.PARAGRAPH)) {  //段落
                XWPFParagraph paragraph = (XWPFParagraph) ibodyelement;
                //段落解析
                //每一个段落的图片
                List<XWPFPicture> sortedPictures = geSortPictures(paragraph);
                String paragraphStr = parseParagraph(paragraph,sortedPictures);
                if (StrUtil.isNotBlank(paragraphStr)){
                    paragraphs.add(paragraphStr);
                }
            }else if (ibodyelement.getElementType().equals(BodyElementType.TABLE)) {   //表格
                XWPFTable table = (XWPFTable) ibodyelement;
                for (XWPFTableRow row : table.getRows()) {  //行
                    for (XWPFTableCell cell : row.getTableCells()) {    //cell
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {  //段落
                            //cell段落解析
                            //每一个段落的图片
                            List<XWPFPicture> sortedPictures = geSortPictures(paragraph);
                            String paragraphStr = parseParagraph(paragraph, sortedPictures);
                            if (StrUtil.isNotBlank(paragraphStr)){
                                paragraphs.add(paragraphStr);
                            }
                        }

                    }
                }
            }
        }
        word.close();
        return paragraphs;
    }

    /**
     * Description: 段落解析
     * author: jiacheng yang.
     * Date: 2024/11/16 16:59
     * Param: [paragraph, sortedPictures]
     */
    public static String parseParagraph(XWPFParagraph xwpfParagraph, List<XWPFPicture> allPictures) throws Exception{
        StringBuilder result = new StringBuilder();
        CTP ctp = xwpfParagraph.getCTP();
        String xmlText = ctp.xmlText();
        //得到根节点的值
        SAXReader saxReader = new SAXReader();
        //将String类型的字符串转换成XML文本对象
        Document doc = saxReader.read(new ByteArrayInputStream(xmlText.getBytes()));
        Element root = doc.getRootElement();
        //用xpath得到OMML节点
        List<Element> elements = root.elements();
        if (elements != null && !elements.isEmpty()) {
            for (Element ele : elements) {
                /*
                 * OMML -> MathML -> LaTex
                 * Office在安装目录中提供了将OMML转为MathML的xsl工具：MML2OMML.XSL
                 * MathML转LaTex使用网上找到另一个xsl工具mmltex.xsl。
                 */
                String xml = ele.asXML();
                String qualifiedName = ele.getQName().getQualifiedName();
                if(qualifiedName.equals("m:oMath")){
                    //xml转 mathml
                    String mathml = xmlConvertMathML(xml);
                    //mathml转latex
                    String latex = mathMLConvertLatex(mathml);
                    //latex转图片，返回img标签
                    String imgStr = lateConvertImg(latex);
                    result.append(imgStr);
                }else if (qualifiedName.equals("w:r")){
                    //处理文本和图片
                    List<Element> runElements = ele.elements();
                    for (Element runEle : runElements) {
                        String qualifiedNameChild = runEle.getQName().getQualifiedName();

                        switch (qualifiedNameChild) {
                            case "w:drawing":
                                //图片
                                XWPFPicture pictureData = allPictures.get(0);
                                String imgStr = readImageToOSS(pictureData.getPictureData().getData());
                                result.append(imgStr);
                                allPictures.remove(0);
                                break;
                            case "w:t":
                                //文本
                                String runStr = runEle.getStringValue();
                                if (StrUtil.isNotEmpty(runStr)) {
                                    result.append(runStr);
                                }
                                break;
                            case "w:tab":
                                //制表符 \t
                                result.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                                break;
                        }
                    }

                }else {
                    String stringValue = ele.getStringValue();
                    if (StrUtil.isNotEmpty(stringValue)){
                        result.append(stringValue);
                    }

                }
            }
        }
        return result.toString();
    }

    /**
     * Description: 上传图片到oss
     * author: jiacheng yang.
     * Date: 2024/11/16 17:35
     * Param: [data]
     */
    private static String readImageToOSS(byte[] data) {
        String imgStr = "";
        if (data == null || data.length == 0) {
            return imgStr;
        }
        // try-with-resources 自动关闭资源
        try (InputStream inputStream = new ByteArrayInputStream(data)) {
            // 随机文件名
            String path = StrUtil.format("/upload/{}/{}_{}img.jpg", DateUtil.today(), DateUtil.current(), RandomUtil.randomInt(1000000, 10000000));
            String realPath = AliYunProperties.ossPath + path;

            // 上传到OSS
            OssUtil.uploadFileToOss(path.substring(1), inputStream);

            // 返回HTML标签
            imgStr = "<img src=\"" + realPath + "\" />";
        } catch (Exception e) {
            LOG.error("上传图片到OSS失败", e);
        }

        return imgStr;
    }

    /**
     * Description: latex转图片
     * author: jiacheng yang.
     * Date: 2024/11/16 17:28
     * Param: [latex]
     */
    public static String lateConvertImg(String latex) {
        String imgStr = "";
        if (StrUtil.isEmpty(latex)){
            return "";
        }
        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 45);
        //黑色字体
        icon.setForeground(Color.BLACK);
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 白色背景
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        icon.paintIcon(null, g2, 0, 0);
        // try-with-resources 自动关闭资源
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", outputStream);
            try (InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                // 随机文件名
                String path = StrUtil.format("/latex/{}/{}_{}latex.jpg", DateUtil.today(), DateUtil.current(), RandomUtil.randomInt(1000000, 10000000));
                String realPath = AliYunProperties.ossPath + path;
                // 上传到OSS
                OssUtil.uploadFileToOss(path.substring(1), inputStream);
                // 返回HTML标签
                imgStr = "<img src=\"" + realPath + "\" data-latex=\"" + latex + "\" />";
            }
        } catch (IOException e) {
            LOG.error(StrUtil.format("latex转图片失败: {}", latex), e);
        }

        return imgStr;
    }

    /**
     * Description: MathML -> LaTex
     * author: jiacheng yang.
     * Date: 2024/11/16 17:16
     * Param: [mathml]
     */
    public static String mathMLConvertLatex(String mathml) {
        // 去掉 XML 头节点
        mathml = mathml.substring(mathml.indexOf("?>") + 2);

        // 设置服务器上的 XSLT 依赖文件路径（MML2TEX_PATH 为服务器的绝对路径）
        URIResolver r = (href, base) -> {
            try (InputStream inputStream =Files.newInputStream(Paths.get(MML2TEX_PATH, href))){
                return new StreamSource(inputStream);
            } catch (Exception e) {
                LOG.error("设置服务器上的 XSLT 依赖文件路径失败", e);
                throw new RuntimeException(e);
            }
        };

        // 调用转换方法
        String latex = xslConvert(mathml, MMLTEX_PATH, r);

        // 清理 LaTeX 输出
        if (latex.length() > 1) {
            latex = latex.substring(1, latex.length() - 1);
            latex = latex.replace("\n", "").replace("\r", "");
        }

        return latex;
    }

    /**
     * Description: XML -> MathML
     * author: jiacheng yang.
     * Date: 2024/11/16 17:15
     * Param: [xml]
     */
    public static String xmlConvertMathML(String xml) {
        // 进行转换的过程中需要借助这个文件,一般来说本机安装office就会有这个文件,找到就可以
        return xslConvert(xml,  CONVERT_PATH,null);
    }

    /**
     * Description: xsl转换器
     * author: jiacheng yang.
     * Date: 2024/11/16 17:11
     * Param: [xml]
     */
    @SuppressWarnings("all")
    public static String xslConvert(String xml, String xslpath,URIResolver uriResolver) {
        TransformerFactory tFac = TransformerFactory.newInstance();
        if(uriResolver != null) {
            tFac.setURIResolver(uriResolver);
        }
        try (InputStream asStream = Files.newInputStream(Paths.get(xslpath))){
            StreamSource xslSource = new StreamSource(asStream);
            StringWriter writer = new StringWriter();
            Transformer t = tFac.newTransformer(xslSource);
            Source source = new StreamSource(new StringReader(xml));
            Result result = new StreamResult(writer);
            t.transform(source, result);
            return writer.getBuffer().toString();
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("xsl转换失败", e);
            return "";
        }

    }


    /**
     * Description: 获取段落所有图片
     * author: jiacheng yang.
     * Date: 2024/11/16 16:58
     * Param: [paragraph]
     */
    public static List<XWPFPicture> geSortPictures(XWPFParagraph paragraph) {
        List<XWPFPicture> sortedPictures = new LinkedList<>();
        for (XWPFRun run : paragraph.getRuns()) {
            List<XWPFPicture> embeddedPictures = run.getEmbeddedPictures();
            if (CollUtil.isEmpty(embeddedPictures)) {
                continue;
            }
            sortedPictures.addAll(embeddedPictures);
        }
        return sortedPictures;
    }

}
