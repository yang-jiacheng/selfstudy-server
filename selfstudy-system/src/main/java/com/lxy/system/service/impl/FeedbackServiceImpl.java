package com.lxy.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxy.common.domain.PageResult;
import com.lxy.system.dto.FeedbackPageDTO;
import com.lxy.system.po.Feedback;
import com.lxy.system.mapper.FeedbackMapper;
import com.lxy.system.vo.FeedbackVO;
import com.lxy.system.service.FeedbackService;
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
    public PageResult<FeedbackVO> getFeedBackList(FeedbackPageDTO dto) {
        //开始分页
        PageHelper.startPage(dto.getPage(),dto.getLimit(),"id desc");
        List<FeedbackVO> list = feedbackMapper.getFeedBackList(dto);
        list.forEach(vo -> {
            vo.setPic(ImgConfigUtil.joinUploadUrl(vo.getPic()));
            vo.setProfilePath(ImgConfigUtil.joinUploadUrl(vo.getProfilePath()));
        });
        return PageResult.convert(new PageInfo<>(list));
    }

    @Override
    public FeedbackVO getFeedBackDetail(Integer id) {
        FeedbackVO feedback = feedbackMapper.getFeedBackDetail(id);
        feedback.setPic(ImgConfigUtil.joinUploadUrl(feedback.getPic()));
        feedback.setProfilePath(ImgConfigUtil.joinUploadUrl(feedback.getProfilePath()));
        return feedback;
    }
}
