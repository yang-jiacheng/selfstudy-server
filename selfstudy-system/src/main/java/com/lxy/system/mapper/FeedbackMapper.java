package com.lxy.system.mapper;

import com.lxy.system.po.Feedback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxy.common.vo.FeedbackVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 意见反馈 Mapper 接口
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-12-21
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {

    List<FeedbackVO> getFeedBackList(@Param("content") String content,@Param("replyStatus") Integer replyStatus,@Param("status")Integer status,@Param("userId") Integer userId);

    Integer getFeedBackList_COUNT(@Param("content") String content,@Param("replyStatus") Integer replyStatus,@Param("status")Integer status,@Param("userId") Integer userId);

    FeedbackVO getFeedBackDetail(@Param("id") Integer id);
}
