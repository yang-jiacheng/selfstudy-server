package com.lxy.admin.controller;

import com.github.pagehelper.PageInfo;
import com.lxy.common.po.Feedback;
import com.lxy.common.security.util.UserIdUtil;
import com.lxy.common.vo.FeedbackVO;
import com.lxy.common.service.FeedbackService;
import com.lxy.common.util.CommonUtil;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.LayUiResultVO;
import com.lxy.common.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/20 10:49
 * @Version: 1.0
 */

@RequestMapping("/feedBackManage")
@Controller
@PreAuthorize("hasAuthority('/feedBackManage/toFeedBackManage')")
public class FeedBackManageController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedBackManageController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/toFeedBackManage")
    public String toFeedBackManage(){
        return "feedBack/feedbackList";
    }

    @GetMapping("/toReplyFeed")
    public String toReplyFeed(){
        return "feedBack/replyFeedback";
    }

    @PostMapping(value = "/getFeedBackPageList" , produces = "application/json")
    @ResponseBody
    public String getFeedBackPageList(@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                  @RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit,
                                  @RequestParam(value = "content",required = false) String content,
                                  @RequestParam(value = "replyStatus",required = false) Integer replyStatus){
        PageInfo<FeedbackVO> pg = feedbackService.getFeedBackList(content, replyStatus, null,null, page, limit);
        return JsonUtil.toJson(new LayUiResultVO((int) pg.getTotal(),pg.getList()));
    }

    @PostMapping(value = "/removeFeedBackById" , produces = "application/json")
    @ResponseBody
    public String removeFeedBackById(@RequestParam("id") Integer id){
        feedbackService.removeById(id);
        return JsonUtil.toJson(new ResultVO());
    }

    @PostMapping(value = "/replyFeedBackById" , produces = "application/json")
    @ResponseBody
    public String replyFeedBackById(@RequestParam("id") Integer id,@RequestParam("reply") String reply){
        int userId = UserIdUtil.getUserId();
        Feedback feedback = new Feedback();
        feedback.setId(id);
        feedback.setAdminId(userId);
        feedback.setReply(reply);
        feedback.setReplyStatus(1);
        feedback.setReplyTime(new Date());
        feedbackService.updateById(feedback);
        return JsonUtil.toJson(new ResultVO());
    }


}
