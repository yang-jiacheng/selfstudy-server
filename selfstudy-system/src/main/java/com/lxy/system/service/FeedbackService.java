package com.lxy.system.service;

import com.github.pagehelper.PageInfo;
import com.lxy.system.po.Feedback;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.common.vo.FeedbackVO;

/**
 * <p>
 * 意见反馈 服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-12-21
 */
public interface FeedbackService extends IService<Feedback> {

    /**
     * 获取反馈列表
     * @param content 反馈内容
     * @param replyStatus 回复状态
     * @param page 当前页
     * @param limit 每页数量
     */
    PageInfo<FeedbackVO> getFeedBackList(String content,Integer replyStatus,Integer status,Integer userId,Integer page,Integer limit);

    /**
     * 反馈详情
     */
    FeedbackVO getFeedBackDetail(Integer id);

}
