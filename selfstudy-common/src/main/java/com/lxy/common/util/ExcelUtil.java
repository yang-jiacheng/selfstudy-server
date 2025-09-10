package com.lxy.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.lxy.common.annotation.ExcelHeader;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * Excel工具类
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/09/06 17:37
 */

@Slf4j
public class ExcelUtil {


    /**
     * <P>居中对齐薄边框样式</P>
     *
     * @param wb wb
     * @return CellStyle
     */
    public static CellStyle getCenterBorderThinStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    /**
     * <P>表头专用样式：灰色背景，粗体，居中对齐，薄边框</P>
     *
     * @param wb 工作簿
     * @return CellStyle 表头样式
     */
    public static CellStyle getHeaderStyle(SXSSFWorkbook wb) {
        CellStyle headerStyle = wb.createCellStyle();

        // 设置背景色为灰色
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 设置字体为粗体
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        headerStyle.setFont(headerFont);

        // 设置对齐方式为居中
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置边框
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);

        // 设置数据格式为文本
        DataFormat format = wb.createDataFormat();
        headerStyle.setDataFormat(format.getFormat("@"));

        return headerStyle;
    }

    /**
     * 设置四周薄边框
     *
     * @param firstRow 开始行
     * @param lastRow  结束行
     * @param firstCol 开始单元格
     * @param lastCol  结束单元格
     * @param sheet    excel工作表
     */
    public static void setAroundBorderStyle(int firstRow, int lastRow, int firstCol, int lastCol, Sheet sheet) {
        RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(firstRow, lastRow, firstCol, lastCol), sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(firstRow, lastRow, firstCol, lastCol), sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, new CellRangeAddress(firstRow, lastRow, firstCol, lastCol), sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, new CellRangeAddress(firstRow, lastRow, firstCol, lastCol), sheet);
    }

    /**
     * <P>黑色粗体</P>
     *
     * @param wb wb
     * @return Font
     */
    public static Font getBlackBoldFont(Workbook wb) {
        Font blackTitleFont = wb.createFont();
        blackTitleFont.setColor(XSSFFont.DEFAULT_FONT_COLOR);
        blackTitleFont.setBold(true);
        return blackTitleFont;
    }

    /**
     * <P>黑色粗体行高20磅</P>
     *
     * @param wb wb
     * @return XSSFFont
     */
    public static Font getBlackBoldHeight20Font(Workbook wb) {
        Font blackTitleFont = getBlackBoldFont(wb);
        blackTitleFont.setFontHeightInPoints((short) 20);
        return blackTitleFont;
    }

    /**
     * <P>红色字体</P>
     *
     * @param wb wb
     * @return Font
     */
    public static Font getRedFont(Workbook wb) {
        Font redFont = wb.createFont();
        redFont.setColor(XSSFFont.COLOR_RED);
        return redFont;
    }

    /**
     * <P>红色粗体</P>
     *
     * @param wb wb
     * @return XSSFFont
     */
    public static XSSFFont getRedBoldFont(XSSFWorkbook wb) {
        XSSFFont redTitleFont = wb.createFont();
        redTitleFont.setColor(XSSFFont.COLOR_RED);
        redTitleFont.setBold(true);
        return redTitleFont;
    }

    /**
     * 创建sheet的row
     *
     * @param sheet sheet
     * @param row   真实行数
     * @return XSSFRow
     */
    public static Row createRow(Sheet sheet, int row) {
        return sheet.createRow(row - 1);
    }

    /**
     * 创建单元格并写入数据和样式
     *
     * @param row   行
     * @param line  真实列
     * @param value 数据
     * @param style 样式
     * @return XSSFCell
     */
    public static Cell createCell(Row row, int line, String value, CellStyle style) {
        Cell cell = row.createCell(line - 1);
        if (StrUtil.isEmpty(value)) {
            value = "";
        }
        cell.setCellValue(value);
        if (style != null) {
            cell.setCellStyle(style);
        }
        return cell;
    }

    /**
     * 导出excel
     *
     * @param response HttpServletResponse
     * @param wb       Workbook
     * @param fileName 文件名
     */
    public static void exportExcel(HttpServletResponse response, Workbook wb, String fileName) {
        OutputStream output = null;
        try {
            //清空response
            response.reset();
            response.setCharacterEncoding("UTF-8");
            //告知浏览器以下载的方式打开文件，文件名如果包含中文需要指定编码
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            //类型为xlsx
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            output = response.getOutputStream();
            wb.write(output);
            output.flush();
        } catch (Exception e) {
            log.error("导出excel失败: {}", fileName, e);
        } finally {
            IoUtil.close(output);
            // 释放 Workbook 临时文件
            if (wb instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) wb).dispose();
            }
            IoUtil.close(wb);
        }
    }

    /**
     * 根据数据列表导出excel
     * SXSSFWorkbook 专为处理大数据量设计，采用逐行写入磁盘的方式，减少内存占用，避免OOM。
     *
     * @author jiacheng yang.
     * @since 2025/4/30 15:47
     */
    public static <T> void exportExcelByRecords(String sheetName, List<T> dataList, Class<T> clazz, HttpServletResponse response) {
        sheetName = ReUtil.replaceAll(sheetName, "[\\\\/:*?\"<>|]", "_");
        // 创建工作簿
        SXSSFWorkbook wb = new SXSSFWorkbook();
        // 设置单元格样式：居中、薄边框（用于数据行）
        CellStyle dataStyle = getCenterBorderThinStyle(wb);
        // 设置表头专用样式
        CellStyle headerStyle = getHeaderStyle(wb);
        // 创建一个sheet
        Sheet sheet = wb.createSheet(sheetName);
        // ExcelHeader注解的字段
        List<Field> exportFields = Arrays.stream(ReflectUtil.getFields(clazz))
                .filter(field -> {
                    ExcelHeader annotation = field.getAnnotation(ExcelHeader.class);
                    return annotation != null && annotation.required();
                })
                .toList();
        //设置动态列宽（按实际导出字段数量）
        for (int i = 0; i < exportFields.size(); i++) {
            sheet.setColumnWidth(i, 5000);
        }
        //生成表头行（使用title属性和表头专用样式）
        Row headerRow = createRow(sheet, 1);
        // 设置表头行高为25磅
        headerRow.setHeightInPoints(25);
        for (int i = 0; i < exportFields.size(); i++) {
            Field field = exportFields.get(i);
            ExcelHeader annotation = field.getAnnotation(ExcelHeader.class);
            String headerName = StrUtil.isEmpty(annotation.title()) ? field.getName() : annotation.title();
            createCell(headerRow, i + 1, headerName, headerStyle);
        }

        if (CollUtil.isNotEmpty(dataList)) {
            // 填充数据行
            int startRow = 2;
            for (T data : dataList) {
                Row rowData = createRow(sheet, startRow);
                for (int i = 0; i < exportFields.size(); i++) {
                    Field field = exportFields.get(i);
                    try {
                        Object fieldValue = ReflectUtil.getFieldValue(data, field);
                        if (fieldValue == null) {
                            fieldValue = "";
                        }
                        createCell(rowData, i + 1, fieldValue.toString(), dataStyle);
                    } catch (Exception e) {
                        log.error(" 字段[{}]导出失败: {}", field.getName(), e.getMessage());
                    }
                }
                startRow++;
            }
        }

        // 导出 Excel 文件
        exportExcel(response, wb, sheetName + ".xlsx");
    }

    /**
     * Excel导入方法
     *
     * @param file            Excel文件
     * @param handlerSupplier 处理器供应商，接收sheet编号参数
     * @return 解析结果列表
     */
    public static <T> List<T> importExcel(MultipartFile file, Function<Integer, SheetHandlerResult<T>> handlerSupplier) {
        List<T> records = new ArrayList<>();
        try (OPCPackage pkg = OPCPackage.open(file.getInputStream())) {
            XSSFReader reader = new XSSFReader(pkg);
            SharedStrings sst = reader.getSharedStringsTable();
            StylesTable styles = reader.getStylesTable();
            XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator)
                    reader.getSheetsData();

            int sheetIndex = 1; // sheet编号从1开始
            while (sheets.hasNext()) {
                try (InputStream sheetStream = sheets.next()) {
                    // 每个 sheet 取一个全新的 handler，并传递sheet编号
                    SheetHandlerResult<T> hl = handlerSupplier.apply(sheetIndex);
                    XMLReader parser = XMLReaderFactory.createXMLReader();
                    parser.setContentHandler(new XSSFSheetXMLHandler(
                            styles, sst, hl, new DataFormatter(Locale.CHINA), false
                    ));

                    InputSource sheetSource = new InputSource(sheetStream);
                    parser.parse(sheetSource);
                    //解析后的操作
                    List<T> resultList = hl.getResultList();
                    if (CollUtil.isNotEmpty(resultList)) {
                        log.info("解析EXCEL第{}sheet成功, 共解析到{}条数据", sheetIndex, resultList.size());
                        records.addAll(resultList);
                    }
                    sheetIndex++; // 增加sheet编号
                }
            }
        } catch (Exception e) {
            log.error("解析EXCEL出现异常", e);
        }
        return records;
    }


}
