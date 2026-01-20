package com.lxy.app.controller;

import com.lxy.common.model.PageResult;
import com.lxy.common.model.R;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.common.dto.FeedbackPageDTO;
import com.lxy.system.po.Feedback;
import com.lxy.system.service.FeedbackService;
import com.lxy.common.vo.FeedbackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 意见反馈
 *
 * @author jiacheng yang.
 * @since 2022/12/22 17:34
 */

@RequestMapping("/feedBack")
@RestController
public class FeedBackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedBackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * 获取反馈列表
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:29
     * @param page 页码
     * @param limit 每页数量
     * @param mine 是否我的反馈 1是
     */
    @PostMapping(value = "/getFeedBackList", produces = "application/json")
    public R<List<FeedbackVO>> getFeedBackList(
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
        @RequestParam(value = "mine", required = false) Integer mine) {
        Long userId = UserIdUtil.getUserId();
        if (mine == null || mine != 1) {
            userId = null;
        }
        // 可见的反馈
        Integer status = 1;
        FeedbackPageDTO dto = new FeedbackPageDTO(null, null, null, userId, status);
        dto.setLimit(limit);
        dto.setPage(page);
        PageResult<FeedbackVO> pg = feedbackService.getFeedBackList(dto);
        return R.ok(pg.getRecords());
    }

    /**
     * 获取反馈详情
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:29
     * @param id 反馈ID
     */
    @PostMapping(value = "/getFeedBackDetail", produces = "application/json")
    public R<FeedbackVO> getFeedBackDetail(@RequestParam(value = "id") Long id) {
        FeedbackVO feedback = feedbackService.getFeedBackDetail(id);
        return R.ok(feedback);
    }

    /**
     * 提交反馈
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:29
     * @param content 反馈内容
     * @param pic 反馈图片
     */
    @PostMapping(value = "/submitFeedBack", produces = "application/json")
    public R<Object> submitFeedBack(@RequestParam(value = "content") String content,
        @RequestParam(value = "pic", required = false) String pic) {
        long userId = UserIdUtil.getUserId();
        Feedback feedback = new Feedback(userId, content, pic, new Date(), 1, 0);
        feedbackService.save(feedback);
        return R.ok();
    }

}
