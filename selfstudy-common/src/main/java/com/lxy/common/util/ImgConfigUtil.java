package com.lxy.common.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lxy.common.domain.GraphicsTextParameter;
import com.lxy.common.properties.AliYunProperties;
import com.lxy.common.properties.CustomProperties;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/02 9:38
 */

@Slf4j
public class ImgConfigUtil {

    public final static String UPLOAD_FOLDER = "/upload";

    // 图片格式
    private final static List<String> IMG_FORMAT = Arrays.asList(".BMP", ".JPEG", ".GIF", ".PSD", ".PNG", ".TIFF", ".TGA",
            ".EPS", ".JPG", ".PCX", ".EXIF", ".FPX", ".CDR", ".PCD", ".DXF", ".UFO", ".EPS", ".AI", ".RAW");

    /**
     * 压缩图片并返回
     */
    public static InputStream uploadImageReStream(InputStream originalImageStream, String fileName) {
        try {
            String extension = FileNameUtil.getSuffix(fileName);
            File tempFile = File.createTempFile("compressed-", "." + extension);
            Thumbnails.of(originalImageStream)
                    .scale(1f)
                    .outputQuality(0.75)
                    .toFile(tempFile);

            // 自动清理临时文件的 InputStream
            return new AutoDeleteFileInputStream(tempFile);
        } catch (IOException e) {
            log.error("压缩图片失败", e);
            return null;
        }
    }

    /**
     * 判断文件是否图片
     */
    public static boolean isImage(String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toUpperCase();
        return IMG_FORMAT.contains(suffix);
    }

    /**
     * 拼接图片路径
     *
     * @param url 图片相对路径
     * @return 图片访问路径
     */
    public static String joinUploadUrl(String url) {
        if (StrUtil.isEmpty(url)) {
            return null;
        }
        if (!url.contains(UPLOAD_FOLDER)) {
            return null;
        }
        url = url.substring(url.indexOf(UPLOAD_FOLDER));
        return getPrefix() + url;
    }

    public static String generateDimensionalCode(String uniCode) {
        //文件名
        String fileName = generateRandomString() + ".jpg";
        fileName = FileUtil.getRandomFileName(fileName);
        QrConfig config = new QrConfig();
        // 高纠错级别
        config.setErrorCorrection(ErrorCorrectionLevel.H);
        ByteArrayOutputStream os = null;
        ByteArrayInputStream inputStream = null;
        try {
            os = new ByteArrayOutputStream();
            //生成二维码到输出流
            QrCodeUtil.generate(uniCode, config, ImgUtil.IMAGE_TYPE_JPG, os);
            byte[] bytes = os.toByteArray();
            inputStream = new ByteArrayInputStream(bytes);
            String prefix = "/upload/DimensionalCode/";
            String ossUrl = prefix + fileName;
            //传到oss
            OssUtil.uploadFileToOss(ossUrl, inputStream);
            return ossUrl;
        } catch (Exception e) {
            log.error("生成二维码失败", e);
            return null;
        } finally {
            IoUtil.close(os);
            IoUtil.close(inputStream);
        }
    }

    public static String generateRandomString() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 10);
    }

    /**
     * 获取图片前缀
     */
    public static String getPrefix() {
        //开启了OSS
        if (AliYunProperties.ossEnabled) {
            return AliYunProperties.ossPath;
        }
        if ("prod".equals(CustomProperties.activeProfile)) {
            return "http://" + CustomProperties.hostName + CustomProperties.contentPath;
        }
        return "http://" + CustomProperties.hostName + ":" + CustomProperties.port + CustomProperties.contentPath;
    }

    public static String getAccessUrl() {
        if ("prod".equals(CustomProperties.activeProfile)) {
            return "http://" + CustomProperties.hostName + CustomProperties.contentPath;
        }
        return "http://" + CustomProperties.hostName + ":" + CustomProperties.port + CustomProperties.contentPath;
    }

    /**
     * 处理图片模板
     *
     * @param url        图片路径
     * @param parameters 模板参数
     * @return 处理后的图片路径
     */
    public static String processImage(String url, List<GraphicsTextParameter> parameters) {
        String imgStr = "";
        BufferedImage bufferedImage = null;
        BufferedImage newBufferedImage = null;
        try {
            // 加载背景图片
            bufferedImage = ImgUtil.read(new URL(url));


            //处理模板参数
            for (GraphicsTextParameter parameter : parameters) {
                drawTextOnImage(parameter, bufferedImage);
            }
            //png出红色背景解决
            newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            //图片传oss
            imgStr = readBufferedImageToOSS(newBufferedImage);
        } catch (Exception e) {
            log.error("生成图片失败：" + url, e);
        } finally {
            if (bufferedImage != null) {
                try {
                    bufferedImage.flush();
                } catch (Exception e) {
                    // 静默刷出
                }
            }
            if (newBufferedImage != null) {
                try {
                    newBufferedImage.flush();
                } catch (Exception e) {
                    // 静默刷出
                }
            }

        }
        return imgStr;
    }

    /**
     * Description: 上传图片到oss
     * author: jiacheng yang.
     * Date: 2024/11/16 17:35
     * Param: [data]
     */
    public static String readBufferedImageToOSS(BufferedImage image) {
        String imgStr = "";
        if (image == null) {
            return imgStr;
        }
        // try-with-resources 自动关闭资源
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", outputStream);
            try (InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                // 随机文件名
                String path = StrUtil.format("/upload/{}/{}_{}.jpg", DateUtil.today(), DateUtil.current(), RandomUtil.randomInt(1000000, 10000000));
                String realPath = AliYunProperties.ossPath + path;
                // 上传到OSS
                OssUtil.uploadFileToOss(path.substring(1), inputStream);
                // 返回图片绝对路径
                imgStr = realPath;
                log.error(StrUtil.format("上传图片到oss成功: {}", imgStr));
            }
        } catch (Exception e) {
            log.error(StrUtil.format("上传图片到oss失败: {}", imgStr), e);
        }

        return imgStr;
    }

    /**
     * 绘制文本到图片上
     *
     * @param parameter 文本内容和样式的参数
     * @param image     背景图片
     */
    public static void drawTextOnImage(GraphicsTextParameter parameter, BufferedImage image) {
        String text = parameter.getValue();
        // 获取样式参数
        String fontFamily = parameter.getFontKey();
        int fontSize = parameter.getFontSize() != null ? parameter.getFontSize() : 24;
        int x = parameter.getX() != null ? parameter.getX() : 0;
        int y = parameter.getY() != null ? parameter.getY() + fontSize : 0;

        String fontWeight = parameter.getFontWeight();
        int fontStyle = "bold".equalsIgnoreCase(fontWeight) ? Font.BOLD : Font.PLAIN;
        String fontColor = parameter.getFontColor();
        // 使用正则表达式提取 RGB 值
        Color color = getColorFromString(fontColor);

        // 绘制文本到图片上
        Font font = FontUtil.getFont(fontStyle, fontSize);
        Graphics2D graphics = image.createGraphics();
        graphics.setFont(font);
        graphics.setColor(color);
        graphics.drawString(text, x, y);
        graphics.dispose();
    }

    /**
     * 从颜色字符串获取Color对象，支持rgb()和#RRGGBB格式
     */
    public static Color getColorFromString(String fontColor) {
        if (StrUtil.isEmpty(fontColor)) {
            return Color.BLACK; // 默认黑色
        }

        Pattern rgbPattern = Pattern.compile("rgb\\((\\d+),\\s*(\\d+),\\s*(\\d+)\\)");
        Matcher rgbMatcher = rgbPattern.matcher(fontColor);
        if (rgbMatcher.find()) {
            int r = Integer.parseInt(rgbMatcher.group(1));
            int g = Integer.parseInt(rgbMatcher.group(2));
            int b = Integer.parseInt(rgbMatcher.group(3));
            return new Color(r, g, b);
        } else if (fontColor.startsWith("#") && fontColor.length() == 7) {
            return Color.decode(fontColor); // #RRGGBB格式
        }

        return Color.BLACK; // 默认返回黑色
    }


}
