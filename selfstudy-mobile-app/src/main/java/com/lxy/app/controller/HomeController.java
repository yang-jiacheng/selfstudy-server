package com.lxy.app.controller;

import com.lxy.common.bo.R;
import com.lxy.system.service.ClassifyService;
import com.lxy.system.service.StudyRecordService;
import com.lxy.common.vo.ClassifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description: 主页
 * author: jiacheng yang.
 * Date: 2022/12/19 17:35
 * Version: 1.0
 */

@RequestMapping("/home")
@RestController
public class HomeController {

    private final ClassifyService classifyService;

    private final StudyRecordService studyRecordService;

    @Autowired
    public HomeController(ClassifyService classifyService, StudyRecordService studyRecordService) {
        this.classifyService = classifyService;
        this.studyRecordService = studyRecordService;
    }

    /**
     * Description: 获取图书馆
     * Author: jiacheng yang.
     * Date: 2025/02/20 10:28
     * Param: []
     */
    @PostMapping(value = "/getClassify" , produces = "application/json")
    @ResponseBody
    public R<Object> getClassify(){
        List<ClassifyVO> list = classifyService.getClassifyList();
        return R.ok(list);
    }

}
