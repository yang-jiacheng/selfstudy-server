package com.lxy.app.controller;

import com.github.pagehelper.PageInfo;
import com.lxy.common.domain.R;
import com.lxy.common.po.Feedback;
import com.lxy.common.vo.FeedbackVO;
import com.lxy.common.service.FeedbackService;
import com.lxy.app.security.util.UserIdUtil;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/22 17:34
 * @Version: 1.0
 */

@RequestMapping("/feedBack")
@RestController
@Api(tags = "APP 意见反馈")
public class FeedBackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedBackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @ApiOperation(value = "获取反馈列表",  notes = "jiacheng yang.")
    @PostMapping(value = "/getFeedBackList" , produces = "application/json")
    public R<Object> getFeedBackList(@ApiParam(value = "当前页")@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                     @ApiParam(value = "每页数量")@RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit,
                                     @ApiParam(value = "是否我的反馈 1是 ")@RequestParam(value = "mine",required = false) Integer mine){
        Integer userId = UserIdUtil.getUserId();
        if (mine == null || mine != 1){
            userId = null;
        }
        //可见的反馈
        Integer status = 1;
        PageInfo<FeedbackVO> pg = feedbackService.getFeedBackList(null, null, status,userId, page, limit);
        return R.ok(pg.getList());
    }

    @ApiOperation(value = "获取反馈详情",  notes = "jiacheng yang.")
    @PostMapping(value = "/getFeedBackDetail" , produces = "application/json")
    public R<Object> getFeedBackDetail(Integer id){
        FeedbackVO feedback = feedbackService.getFeedBackDetail(id);
        return R.ok(feedback);
    }

    @ApiOperation(value = "提交反馈",  notes = "jiacheng yang.")
    @PostMapping(value = "/submitFeedBack" , produces = "application/json")
    public R<Object> submitFeedBack(@ApiParam(value = "反馈内容")@RequestParam(value = "content") String content,
                                 @ApiParam(value = "反馈图片")@RequestParam(value = "pic",required = false) String pic){
        int userId = UserIdUtil.getUserId();
        Feedback feedback = new Feedback(userId,content,pic,new Date(),1,0);
        feedbackService.save(feedback);
        return R.ok();
    }

}
