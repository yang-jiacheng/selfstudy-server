package com.lxy.common.util;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Slf4j
public class RetryUtil {

    /**
     * 通用的重试机制方法
     *
     * @param maxRetries 最大重试次数
     * @param maxDelay   最大延迟时间（毫秒）
     * @param operation  执行的操作（可以是任意抛出异常的操作）
     * @param <T>        返回值类型
     * @return 返回操作结果
     */
    public static <T> boolean retryOperation(int maxRetries, long maxDelay, RetryableOperation<T> operation, String methodName, String json) {
        int attempt = 0;
        boolean success = false;

        while (attempt <= maxRetries && !success) {
            try {
                if (attempt > 0) {
                    log.error("{}：第{}次重试", methodName, attempt);
                }
                attempt++;
                operation.execute();
                success = true;  // 如果没有抛异常，表示操作成功
            } catch (Exception e) {
                log.error("{}：操作异常,数据：{}", methodName, json);
                log.error(e.getMessage(), e);
                if (attempt <= maxRetries) {
                    long delay = (long) (Math.pow(2, attempt) * 1000);  // 指数退避
                    long finalDelay = Math.min(delay, maxDelay);
                    log.error("{}：延迟{}毫秒后重试", methodName, finalDelay);
                    ThreadUtil.sleep(finalDelay, TimeUnit.MILLISECONDS);
                }
            }
        }

        log.error("{}：重试{}次, 执行结果：{}", methodName, (attempt - 1), success);

        return success;
    }


    @FunctionalInterface
    public interface RetryableOperation<T> {
        T execute() throws Exception;
    }

//    public static void main(String[] args) {
//        // 重试机制 最多3次，最多延迟6秒
//        final int maxRetries = 3;
//        final long maxDelay = 6000;
//        boolean flag = RetryUtil.retryOperation(maxRetries, maxDelay, () -> {
//            return checkinGradeDailyService.saveCheckinGradeDaily(statisticsDTO);
//        }, "saveCheckinGradeDaily", "");
//        if (flag) {
//            log.info("成功");
//        }
//    }
}
