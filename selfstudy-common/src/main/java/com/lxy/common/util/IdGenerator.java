package com.lxy.common.util;

import cn.hutool.core.util.IdUtil;

import java.util.Date;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

public class IdGenerator {

    private static IdGenerator instance = new IdGenerator(0);

    public static IdGenerator initDefaultInstance(int machineId) {
        instance = new IdGenerator(machineId);
        return instance;
    }

    public static long generateId() {
        return instance.nextId();
    }

    private static final long MACHINE_BIT = 5; // max 31
    private static final long SEQUENCE_BIT = 8; // 256/10ms
    private static final long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    private static final long MACHINE_LEFT = SEQUENCE_BIT;
    private static final long TIMESTMP_LEFT = MACHINE_BIT + SEQUENCE_BIT;

    private final long machineId;
    private long sequence = 0L;
    private long lastStmp = -1L;

    private IdGenerator(long machineId) {
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException(
                    "machineId can't be greater than " + MAX_MACHINE_NUM + " or less than 0");
        }
        this.machineId = machineId;
    }

    public synchronized long nextId() {
        long currStmp = getTimestamp();

        if (currStmp <= lastStmp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L) {
                currStmp = getNextTimestamp();
            }
        } else {
            sequence = 0L;
        }

        lastStmp = currStmp;

        return currStmp << TIMESTMP_LEFT | machineId << MACHINE_LEFT | sequence;
    }

    private long getNextTimestamp() {
        long mill = getTimestamp();
        int spinCount = 0;
        while (mill <= lastStmp) {
            // 简单的自旋，最多自旋一定次数，减少无效休眠
            if (spinCount < 100) {
                spinCount++;
            } else {
                try {
                    // 自旋后休眠，减少 CPU 占用
                    Thread.sleep(1);  // 可调整休眠时间
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            mill = getTimestamp();
        }
        return mill;
    }

    private long getTimestamp() {
        return System.currentTimeMillis() / 10;  // 10ms
    }

    public static Date parseIdTimestamp(long id) {
        return new Date((id >>> TIMESTMP_LEFT) * 10);
    }

    public static String uuid() {
        return IdUtil.simpleUUID();
    }
}
