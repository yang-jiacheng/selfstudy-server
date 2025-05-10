package com.lxy.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.lxy.system.vo.ExcelErrorInfoVO;
import com.lxy.system.vo.user.UserImportVO;
import com.lxy.system.service.UserService;
import com.lxy.common.util.EncryptUtil;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
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
 * TODO
 * @author jiacheng yang.
 * @since 2023/02/20 16:57
 * @version 1.0
 */

@Service
public class PoiService {

    private final UserService userService;

    @Autowired
    public PoiService(UserService userService) {
        this.userService = userService;
    }




}
