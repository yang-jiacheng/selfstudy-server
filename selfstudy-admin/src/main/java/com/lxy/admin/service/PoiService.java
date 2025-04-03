package com.lxy.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.lxy.common.util.ExcelUtil;
import com.lxy.admin.vo.ExcelErrorInfoVO;
import com.lxy.system.vo.UserImportVO;
import com.lxy.system.po.User;
import com.lxy.system.service.UserService;
import com.lxy.common.util.EncryptUtil;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/20 16:57
 * @Version: 1.0
 */

@Service
public class PoiService {

    private final UserService userService;

    @Autowired
    public PoiService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 导出用户
     * SXSSFWorkbook用于百万数据导出，不会消耗太多内存，缺点是无法使用模板，样式也会失效
     * @param titleName 标题名称
     * @return Excel2007
     */
    public Workbook exportUserInExcel(String titleName, List<User> users){
        //创建excel表
        Workbook wb = new SXSSFWorkbook();
        //创建一个DataFormat对象
        DataFormat format2 = wb.createDataFormat();
        //黑色粗体行高20磅
        Font blackBoldHeight20Font = ExcelUtil.getBlackBoldHeight20Font(wb);
        //居中对齐薄边框样式
        CellStyle centerBorderThinStyle = ExcelUtil.getCenterBorderThinStyle(wb);
        centerBorderThinStyle.setDataFormat(format2.getFormat("@"));
        //创建一个sheet
        Sheet sheet = wb.createSheet(titleName);
        //字段个数
        int valNum = 7;
        //单元格宽度
        for (int i = 0; i < valNum; i++) {
            sheet.setColumnWidth(i, 5000);
        }
        //第一行标题
        Row row1 = ExcelUtil.createRow(sheet,1);
        row1.setHeight((short) 600);
        //标题单元格
        Cell titleCell = row1.createCell(0);
        XSSFRichTextString titleText = new XSSFRichTextString(titleName);
        titleText.applyFont(0, titleText.length(), blackBoldHeight20Font);
        titleCell.setCellValue(titleText);
        titleCell.setCellStyle(centerBorderThinStyle);
        //单元格合并
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,valNum - 1));
        ExcelUtil.setAroundBorderStyle(0,0,0,valNum - 1,sheet);
        //第二行用于表头
        Row row2 = ExcelUtil.createRow(sheet,2);
        //第二行第一个单元格：昵称
        ExcelUtil.createCellValueAndStyle(row2,1,"用户昵称",centerBorderThinStyle);
        //第二行第二个单元格：手机号
        ExcelUtil.createCellValueAndStyle(row2,2,"手机号",centerBorderThinStyle);
        //第二行第三个单元格：性别
        ExcelUtil.createCellValueAndStyle(row2,3,"性别",centerBorderThinStyle);
        //第二行第四个单元格：地址
        ExcelUtil.createCellValueAndStyle(row2,4,"地址",centerBorderThinStyle);
        //第二行第五个单元格：总学习时长
        ExcelUtil.createCellValueAndStyle(row2,5,"总学习时长",centerBorderThinStyle);
        //第二行第6个单元格：开户银行
        ExcelUtil.createCellValueAndStyle(row2,6,"加入时间",centerBorderThinStyle);
        //第二行第7个单元格：注册类型
        ExcelUtil.createCellValueAndStyle(row2,7,"注册类型",centerBorderThinStyle);
        //第三行开始是数据
        int startRow = 3;
        for (User user : users) {
            Row rowData = ExcelUtil.createRow(sheet,startRow);
            ExcelUtil.createCellValueAndStyle(rowData,1,user.getName(),centerBorderThinStyle);
            ExcelUtil.createCellValueAndStyle(rowData,2,user.getPhone(),centerBorderThinStyle);
            ExcelUtil.createCellValueAndStyle(rowData,3,user.getGender(),centerBorderThinStyle);
            ExcelUtil.createCellValueAndStyle(rowData,4,user.getAddress(),centerBorderThinStyle);
            Integer totalDuration = user.getTotalDuration()  == null ? 0 : user.getTotalDuration();
            String toa = totalDuration + "分钟";
            ExcelUtil.createCellValueAndStyle(rowData,5,toa,centerBorderThinStyle);
            ExcelUtil.createCellValueAndStyle(rowData,6,DateUtil.format(user.getCreateTime(),"yyyy年MM月dd日 HH:mm"),centerBorderThinStyle);

            int registType = user.getRegistType() == null ? 2 : user.getRegistType();
            String type = "";
            if (registType == 1){
                type = "用户注册";
            }else {
                type = "后台添加";
            }

            ExcelUtil.createCellValueAndStyle(rowData,7,type,centerBorderThinStyle);

            startRow ++ ;
        }
        return wb;
    }

    public List<ExcelErrorInfoVO> importUsersInExcel(MultipartFile file) {
        //错误信息列表，前端展示
        List<ExcelErrorInfoVO> errorList = new ArrayList<>();
        //文件为空
        if (file.isEmpty()){
            errorList.add(new ExcelErrorInfoVO("","文件为空",""));
            return errorList;
        }
        //解析器
        UserSheetHandler hl = new UserSheetHandler();
        try {
            //1.根据 Excel 获取 OPCPackage 对象
            OPCPackage pkg = OPCPackage.open(file.getInputStream());
            //2.创建 XSSFReader 对象
            XSSFReader reader = new XSSFReader(pkg);
            //3.获取 SharedStringsTable 对象
            SharedStringsTable sst = reader.getSharedStringsTable();
            //4.获取 StylesTable 对象
            StylesTable styles = reader.getStylesTable();
            XMLReader parser = XMLReaderFactory.createXMLReader();
            // 处理公共属性
            parser.setContentHandler(new XSSFSheetXMLHandler(styles,sst, hl,
                    false));
            XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator)
                    reader.getSheetsData();

            while (sheets.hasNext()) {
                InputStream sheetStream = sheets.next();
                InputSource sheetSource = new InputSource(sheetStream);
                parser.parse(sheetSource);
                //解析后获取供应商集合
                List<UserImportVO> userList = hl.getUserList();
                //校验数据
                checkUserList(userList,errorList,file.getOriginalFilename());
                //批量新增供应商
                if (CollUtil.isEmpty(errorList) && userList.size()>0){
                    userService.insertBatchUser(userList);
                }
                sheetStream.close();
            }
            pkg.close();
            return errorList ;
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new ExcelErrorInfoVO("解析EXCEL","出现异常","终止"));
            e.printStackTrace();
            return errorList ;
        }

    }

    /**
     * 校验解析的用户数据是否正确
     * @param userList 供应商集合
     * @param errorList 错误信息列表
     * @param fileName excel文件名
     */
    private void checkUserList(List<UserImportVO> userList,List<ExcelErrorInfoVO> errorList,String fileName){
        for (UserImportVO user : userList) {
            //具体哪一行
            int rowIndex = user.getRowIndex() + 1;
            //判断昵称是否为空
            if (StrUtil.isEmpty(user.getName())){
                errorList.add(new ExcelErrorInfoVO(fileName +"第1sheet,第"+rowIndex+"行","昵称为空","此行略过"));
            }
            //判断手机号是否为空,校验手机号
            if (StrUtil.isEmpty(user.getPhone())){
                errorList.add(new ExcelErrorInfoVO(fileName +"第1sheet,第"+rowIndex+"行","手机号为空","此行略过"));
            }else {
                if (!PhoneUtil.isMobile(user.getPhone())){
                    errorList.add(new ExcelErrorInfoVO(fileName +"第1sheet,第"+rowIndex+"行","手机号格式不正确","此行略过"));
                }
            }
        }
    }

    /**
     *  用户excel处理器
     */
    static class UserSheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler{

        private UserImportVO user = null;

        private List<UserImportVO> userList = new ArrayList<>();

        public List<UserImportVO> getUserList() {
            return userList;
        }

        /**
         * 每一行的开始
         * @param rowIndex 代表的是每一个sheet的行索引
         */
        @Override
        public void startRow(int rowIndex) {
            if (rowIndex == 0){
                user = null;
            }else {
                user = new UserImportVO();
            }
        }

        /**
         * 处理每一行的所有单元格
         */
        @Override
        public void cell(String cellName, String cellValue, XSSFComment xssfComment) {
            if (StrUtil.isEmpty(cellValue)){
                return;
            }
            if (cellValue.contains("\n")){
                cellValue = cellValue.replace("\n", "<br>");
            }
            if (cellValue.contains("\r")){
                cellValue=cellValue.replace("\r","<br>");
            }
            if (user !=null ){
                //每个单元名称的首字母 A  B  C
                String letter = cellName.substring(0, 1);
                switch (letter) {
                    //手机号
                    case "A":{
                        user.setPhone(cellValue);
                        break;
                    }
                    //密码
                    case "B":{
                        user.setPassword(EncryptUtil.encryptSha256(cellValue));
                        break;
                    }
                    //昵称
                    case "C":{
                        user.setName(cellValue);
                        break;
                    }
                    //性别
                    case "D":{
                        user.setGender(cellValue);
                        break;
                    }
                    //地址
                    case "E":{
                        user.setAddress(cellValue);
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
        }

        /**
         * 每一行的结束
         * @param rowIndex 代表的是每一个sheet的行索引
         */
        @Override
        public void endRow(int rowIndex) {
            if (rowIndex!=0){
                String defPath = "/upload/defPath.jpg";
                String defCover = "/upload/defCover.jpg";
                user.setRowIndex(rowIndex);
                user.setCreateTime(new Date());
                user.setUpdateTime(new Date());
                user.setProfilePath(defPath);
                user.setRegistType(2);
                user.setTotalDuration(0);
                user.setCoverPath(defCover);

                String password = user.getPassword();
                if (StrUtil.isEmpty(password)) {
                    user.setPassword(EncryptUtil.encryptSha256("123456"));
                }
                userList.add(user);
            }
        }

    }
}
