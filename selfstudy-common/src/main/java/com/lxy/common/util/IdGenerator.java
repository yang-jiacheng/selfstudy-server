package com.lxy.common.util;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

/**
 * 分布式ID生成器（优化版） 核心特性： 1. 10ms时间粒度 | 2. 机器ID隔离 | 3. 无锁CAS并发控制
 */
public class IdGenerator {

    // 常量定义（按业务需求调整）
    private static final long MACHINE_BIT = 5; // 最大支持31台机器
    private static final long SEQUENCE_BIT = 8; // 每10ms 256个序列
    private static final long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    private static final long MACHINE_LEFT = SEQUENCE_BIT;
    private static final long TIMESTMP_LEFT = MACHINE_BIT + SEQUENCE_BIT;
    // 单例模式初始化
    private static IdGenerator instance = new IdGenerator(0);
    // 原子化状态变量
    private final long machineId;
    private final AtomicLong sequence = new AtomicLong(0);
    private final AtomicLong lastStmp = new AtomicLong(-1L);

    /**
     * 构造函数（机器ID校验）
     */
    private IdGenerator(long machineId) {
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("机器ID范围: 0~" + MAX_MACHINE_NUM);
        }
        this.machineId = machineId;
    }

    public static IdGenerator initDefaultInstance(int machineId) {
        instance = new IdGenerator(machineId);
        return instance;
    }

    /**
     * 生成id
     *
     * @author jiacheng yang.
     * @since 2025/11/19 17:16
     */
    public static long generateId() {
        return instance.nextId();
    }

    /**
     * 解析ID中的时间戳
     */
    public static Date parseIdTimestamp(long id) {
        return new Date((id >>> TIMESTMP_LEFT) * 10);
    }

    /**
     * 生成唯一ID（无锁化实现）
     */
    public long nextId() {
        long currStmp;
        long seq;
        long last;
        do {
            currStmp = getTimestamp();
            last = lastStmp.get();

            // 时钟回拨检测（超过10ms抛出异常）
            if (currStmp < last) {
                throw new IllegalStateException("时钟回拨检测: " + (last - currStmp) + "ms");
            }

            // 同一时间窗口内递增序列号
            if (currStmp == last) {
                seq = sequence.updateAndGet(s -> (s + 1) & MAX_SEQUENCE);
                if (seq == 0) {
                    currStmp = getNextTimestamp();
                }
            } else {
                seq = 0;
                sequence.set(0);
            }
        } while (!lastStmp.compareAndSet(last, currStmp));

        return (currStmp << TIMESTMP_LEFT) | (machineId << MACHINE_LEFT) | seq;
    }

    /**
     * 获取下一时间窗口（分层自旋策略）
     */
    private long getNextTimestamp() {
        long mill = getTimestamp();
        int spinCount = 0;
        while (mill <= lastStmp.get()) {
            // 分层优化：短时自旋 -> 微秒级休眠
            if (spinCount < 1000) {
                spinCount++;
                Thread.onSpinWait(); // JDK9+ 自旋优化提示
            } else {
                LockSupport.parkNanos(1000); // 1微秒级等待
            }
            mill = getTimestamp();
        }
        return mill;
    }

    /**
     * 获取当前时间戳（10ms精度）
     */
    private long getTimestamp() {
        return System.currentTimeMillis() / 10;
    }

    public static void main(String[] args) {
        System.out.println(generateId());
    }
}
