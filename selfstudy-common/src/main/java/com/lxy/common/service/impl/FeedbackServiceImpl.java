package com.lxy.common.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxy.common.po.Feedback;
import com.lxy.common.mapper.FeedbackMapper;
import com.lxy.common.vo.FeedbackVO;
import com.lxy.common.service.FeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.util.ImgConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 意见反馈 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-12-21
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    private final FeedbackMapper feedbackMapper;

    @Autowired
    public FeedbackServiceImpl(FeedbackMapper feedbackMapper) {
        this.feedbackMapper = feedbackMapper;
    }

    @Override
    public PageInfo<FeedbackVO> getFeedBackList(String content, Integer replyStatus,Integer status, Integer userId,Integer page, Integer limit) {
        //开始分页
        PageHelper.startPage(page,limit,"id desc");
        Page<FeedbackVO> pg = (Page<FeedbackVO>) getFeedBackList(content, replyStatus,status, userId);
        return new PageInfo<>(pg);
    }

    @Override
    public FeedbackVO getFeedBackDetail(Integer id) {
        FeedbackVO feedback = feedbackMapper.getFeedBackDetail(id);
        feedback.setPic(ImgConfigUtil.joinUploadUrl(feedback.getPic()));
        feedback.setProfilePath(ImgConfigUtil.joinUploadUrl(feedback.getProfilePath()));
        return feedback;
    }

    private List<FeedbackVO> getFeedBackList(String content, Integer replyStatus,Integer status,Integer userId) {
        List<FeedbackVO> list = feedbackMapper.getFeedBackList(content, replyStatus,status, userId);
        list.forEach(vo -> {
            vo.setPic(ImgConfigUtil.joinUploadUrl(vo.getPic()));
            vo.setProfilePath(ImgConfigUtil.joinUploadUrl(vo.getProfilePath()));
        });
        return list;
    }

    private Integer getFeedBackListCount(String content, Integer replyStatus,Integer status,Integer userId) {
        return feedbackMapper.getFeedBackList_COUNT(content, replyStatus, status,userId);
    }
}
