package com.lxy.admin.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @Description: Excel工具类
 * @author: jiacheng yang.
 * @Date: 2022/09/06 17:37
 * @Version: 1.0
 */
public class ExcelUtil {

    private final static Logger LOG = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * <P>居中对齐薄边框样式</P>
     * @param wb wb
     * @return CellStyle
     */
    public static CellStyle getCenterBorderThinStyle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    /**
     * 设置四周薄边框
     * @param firstRow 开始行
     * @param lastRow 结束行
     * @param firstCol 开始单元格
     * @param lastCol 结束单元格
     * @param sheet excel工作表
     */
    public static void setAroundBorderStyle(int firstRow, int lastRow, int firstCol, int lastCol, Sheet sheet) {
        RegionUtil.setBorderBottom(BorderStyle.THIN,new CellRangeAddress(firstRow,lastRow,firstCol,lastCol), sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN,new CellRangeAddress(firstRow,lastRow,firstCol,lastCol), sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN,new CellRangeAddress(firstRow,lastRow,firstCol,lastCol), sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN,new CellRangeAddress(firstRow,lastRow,firstCol,lastCol), sheet);
    }

    /**
     * <P>黑色粗体</P>
     * @param wb wb
     * @return Font
     */
    public static Font getBlackBoldFont(Workbook wb){
        Font blackTitleFont = wb.createFont();
        blackTitleFont.setColor(XSSFFont.DEFAULT_FONT_COLOR);
        blackTitleFont.setBold(true);
        return blackTitleFont;
    }

    /**
     * <P>黑色粗体行高20磅</P>
     * @param wb wb
     * @return XSSFFont
     */
    public static Font getBlackBoldHeight20Font(Workbook wb){
        Font blackTitleFont = wb.createFont();
        blackTitleFont.setColor(XSSFFont.DEFAULT_FONT_COLOR);
        blackTitleFont.setBold(true);
        blackTitleFont.setFontHeightInPoints((short) 20);
        return blackTitleFont;
    }

    /**
     * <P>红色字体</P>
     * @param wb wb
     * @return Font
     */
    public static Font getRedFont(Workbook wb){
        Font redFont = wb.createFont();
        redFont.setColor(XSSFFont.COLOR_RED);
        return redFont;
    }

    /**
     * <P>红色粗体</P>
     * @param wb wb
     * @return XSSFFont
     */
    public static XSSFFont getRedBoldFont(XSSFWorkbook wb){
        XSSFFont redTitleFont = wb.createFont();
        redTitleFont.setColor(XSSFFont.COLOR_RED);
        redTitleFont.setBold(true);
        return redTitleFont;
    }

    /**
     * 创建sheet的row
     * @param sheet sheet
     * @param row 真实行数
     * @return XSSFRow
     */
    public static Row createRow(Sheet sheet, int row){
        return sheet.createRow(row - 1);
    }

    /**
     * 创建单元格并写入数据和样式
     * @param row 行
     * @param line 真实列
     * @param value 数据
     * @param style 样式
     * @return XSSFCell
     */
    public static Cell createCellValueAndStyle(Row row,int line,String value,CellStyle style){
        Cell cell = row.createCell(line - 1);
        if (StrUtil.isEmpty(value)){
            value = "-";
        }
        cell.setCellValue(value);
        if (style !=null ){
            cell.setCellStyle(style);
        }
        return cell;
    }

    /**
     * 导出excel
     * @param response HttpServletResponse
     * @param wb Workbook
     * @param fileName 文件名
     */
    public static void exportExcel(HttpServletResponse response, Workbook wb, String fileName){
        OutputStream output = null;
        try {
            //清空response
            response.reset();
            response.setCharacterEncoding("UTF-8");
            //告知浏览器以下载的方式打开文件，文件名如果包含中文需要指定编码
            response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName,"UTF-8"));
            //类型为xlsx
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            output = response.getOutputStream();
            wb.write(output);
            output.flush();
        }catch (Exception e){
            LOG.error("导出excel失败: "+fileName,e);
        }finally {
            IoUtil.close(output);
            IoUtil.close(wb);
        }
    }

}
