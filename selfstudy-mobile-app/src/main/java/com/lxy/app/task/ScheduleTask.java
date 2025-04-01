package com.lxy.app.task;

import cn.hutool.core.date.DateUtil;
import com.lxy.common.constant.CronConstant;
import com.lxy.system.service.UserService;
import com.lxy.system.vo.UserRankVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/23 17:39
 * @Version: 1.0
 */

@Component
@EnableScheduling
public class ScheduleTask {

    private final static Logger LOG = LoggerFactory.getLogger(ScheduleTask.class);

    private final UserService userService;

    @Autowired
    public ScheduleTask(UserService userService) {
        this.userService = userService;
    }

    /**
     * 每天0点执行，刷新总排行榜缓存
     */
    @Scheduled(cron = CronConstant.DAY)
    private void getRankingsTotalDuration() {
        String msg = "执行了定时任务 getRankingsTotalDuration 时间："+ DateUtil.now();
        LOG.error(msg);
        List<UserRankVO> users = userService.getRankingsTotalDurationInDb();
        userService.insertRankingsCache(users);
    }


}
