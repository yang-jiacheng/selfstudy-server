package com.lxy.admin.controller;

import com.github.pagehelper.PageInfo;
import com.lxy.common.domain.R;
import com.lxy.system.po.Feedback;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.system.vo.FeedbackVO;
import com.lxy.system.service.FeedbackService;
import com.lxy.system.vo.LayUiResultVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/12/20 10:49
 * @version 1.0
 */

@RequestMapping("/feedBackManage")
@Controller
@PreAuthorize("hasAuthority('feedBackManage')")
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
    public LayUiResultVO getFeedBackPageList(@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                  @RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit,
                                  @RequestParam(value = "content",required = false) String content,
                                  @RequestParam(value = "replyStatus",required = false) Integer replyStatus){
        PageInfo<FeedbackVO> pg = feedbackService.getFeedBackList(content, replyStatus, null,null, page, limit);
        return new LayUiResultVO((int) pg.getTotal(),pg.getList());
    }

    @PostMapping(value = "/removeFeedBackById" , produces = "application/json")
    @ResponseBody
    public R<Object> removeFeedBackById(@RequestParam("id") Integer id){
        feedbackService.removeById(id);
        return R.ok();
    }

    @PostMapping(value = "/replyFeedBackById" , produces = "application/json")
    @ResponseBody
    public R<Object> replyFeedBackById(@RequestParam("id") Integer id,@RequestParam("reply") String reply){
        int userId = UserIdUtil.getUserId();
        Feedback feedback = new Feedback();
        feedback.setId(id);
        feedback.setAdminId(userId);
        feedback.setReply(reply);
        feedback.setReplyStatus(1);
        feedback.setReplyTime(new Date());
        feedbackService.updateById(feedback);
        return R.ok();
    }


}
