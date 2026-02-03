package com.lxy.admin.controller;

import com.lxy.common.annotation.Log;
import com.lxy.common.enums.dict.LogBusinessType;
import com.lxy.common.enums.dict.UserType;
import com.lxy.common.model.PageResult;
import com.lxy.common.model.R;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.system.dto.FeedBackReplyDTO;
import com.lxy.system.dto.FeedbackPageDTO;
import com.lxy.system.po.Feedback;
import com.lxy.system.service.FeedbackService;
import com.lxy.system.vo.FeedbackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 意见反馈
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@RequestMapping("/feedBackManage")
@RestController
@PreAuthorize("hasAuthority('feedBackManage')")
public class FeedBackManageController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedBackManageController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PreAuthorize("hasAuthority('feedBackManage:list')")
    @PostMapping(value = "/getFeedBackPageList", produces = "application/json")
    public R<PageResult<FeedbackVO>> getFeedBackPageList(@RequestBody FeedbackPageDTO dto) {
        PageResult<FeedbackVO> pg = feedbackService.getFeedBackList(dto);
        return R.ok(pg);
    }

    @PreAuthorize("hasAuthority('feedBackManage:delete')")
    @PostMapping(value = "/removeFeedBackById", produces = "application/json")
    @Log(title = "删除意见反馈", businessType = LogBusinessType.DELETE, userType = UserType.ADMIN)
    public R<Object> removeFeedBackById(@RequestParam("id") Long id) {
        feedbackService.removeById(id);
        return R.ok();
    }

    @PreAuthorize("hasAuthority('feedBackManage:reply')")
    @PostMapping(value = "/replyFeedBackById", produces = "application/json")
    @Log(title = "意见反馈-回复", businessType = LogBusinessType.UPDATE, userType = UserType.ADMIN)
    public R<Object> replyFeedBackById(@RequestBody FeedBackReplyDTO dto) {
        long userId = UserIdUtil.getUserId();
        Feedback feedback = new Feedback();
        feedback.setId(dto.getId());
        feedback.setAdminId(userId);
        feedback.setReply(dto.getReply());
        feedback.setReplyStatus(1);
        feedback.setReplyTime(new Date());
        feedbackService.updateById(feedback);
        return R.ok();
    }

}
