package com.lxy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.common.domain.PageResult;
import com.lxy.system.dto.FeedbackPageDTO;
import com.lxy.system.po.Feedback;
import com.lxy.system.vo.FeedbackVO;

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
     */
    PageResult<FeedbackVO> getFeedBackList(FeedbackPageDTO dto);

    /**
     * 反馈详情
     */
    FeedbackVO getFeedBackDetail(Integer id);

}
