package com.lxy.app.controller;

import com.lxy.common.domain.R;
import com.lxy.common.service.ClassifyService;
import com.lxy.common.service.StudyRecordService;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.ClassifyVO;
import com.lxy.common.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/19 17:35
 * @Version: 1.0
 */

@RequestMapping("/home")
@RestController
@Api(tags = "主页")
public class HomeController {

    private final ClassifyService classifyService;

    private final StudyRecordService studyRecordService;

    @Autowired
    public HomeController(ClassifyService classifyService, StudyRecordService studyRecordService) {
        this.classifyService = classifyService;
        this.studyRecordService = studyRecordService;
    }

    @ApiOperation(value = "获取图书馆", notes = "jiacheng yang.")
    @PostMapping(value = "/getClassify" , produces = "application/json")
    @ResponseBody
    public R<Object> getClassify(){
        List<ClassifyVO> list = classifyService.getClassifyList();
        return R.ok(list);
    }

}
