package space.x9x.radp.commons.id;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Synchronized;

/**
 * Twitter 雪花算法生成器
 * <p>
 * 共 64 位, 0 + 41 位时间戳 + 10 位机器 ID（5 位机器 ID + 5 位数据中心 ID）+ 12 位序列号 <pre>
 * ┌─────────────── 41 bits ────────────────┐┌──── 10 bits ────┐┌─ 12 bits ─┐
 * │         时间戳(毫秒数) offset            ││数据中心ID+机器ID  ││  序列号    │
 * └────────────────────────────────────────┘└─────────────────┘└───────────┘
 * </pre> <pre>{@code
 * public class SnowflakeDemo {
 *     public static void main(String[] args) {
 *         // 1) 直接 new 一个默认的 SnowflakeGenerator（dataCenterId=1, workerId=1）
 *         SnowflakeGenerator generator = new SnowflakeGenerator();
 *
 *         // 2) 调用 nextId() 方法生成 Snowflake ID
 *         long id = generator.nextId();
 *         System.out.println("Snowflake ID: " + id);
 *
 *         // 3) 使用静态方法一次生成
 *         long staticId = SnowflakeGenerator.nextId(2, 3);
 *         System.out.println("Snowflake ID (static): " + staticId);
 *     }
 * }
 * }</pre>
 *
 * @author IO x9x
 * @since 2024-12-27 12:36
 */
@NoArgsConstructor
public class SnowflakeGenerator {

    /**
     * 开始时间截（2018-01-01）
     */
    private final long twepoch = 1514736000000L;

    /**
     * 机器 ID 占 5 位（机器 ID 和数据中心 ID 加起来不能超过 10）
     */
    private final long workerIdBits = 5L;

    /**
     * 数据中心 ID 占 5 位
     */
    private final long datacenterIdBits = 5L;

    /**
     * 序列号占 12 位
     */
    private final long sequenceBits = 12L;

    /**
     * 支持的最大机器 ID
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大数据标识 ID
     */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /**
     * 机器 ID 向左移 12 位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识 ID 向左移 17 位
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移 22 位
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 生成序列的掩码
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 机器 ID
     */
    private long workerId = 1L;

    /**
     * 数据中心 ID
     */
    private long dataCenterId = 1L;

    /**
     * 毫秒内序列号
     */
    private long sequence = 0L;

    /**
     * 上次生成 ID 的时间截
     */
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     *
     * @param dataCenterId 数据中心 ID
     * @param workerId     工作 ID
     */
    @Builder
    public SnowflakeGenerator(long dataCenterId, long workerId) {
        if (dataCenterId > maxDatacenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenterId 不能大于 %d 或者小于 0", maxDatacenterId));
        }
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("workerId 不能大于 %d 或者小于 0", maxWorkerId));
        }
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    /**
     * 获得下一个 ID
     *
     * @param dataCenterId 数据中心 ID
     * @param workerId     工作 ID
     * @return snowflakeId
     */
    public static long nextId(long dataCenterId, long workerId) {
        return SnowflakeGenerator.builder().dataCenterId(dataCenterId).workerId(workerId).build().nextId();
    }

    /**
     * 获得下一个 ID
     *
     * @return snowflakeId
     */
    @Synchronized
    public long nextId() {
        long timestamp = timeGen();

        // 如果当前时间小于上一次 ID 生成的时间戳，说明系统时钟回退过，抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("时钟被回退 %d 毫秒，无法生成", lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个毫秒，获得新的时间戳
                timestamp = tillNextMillis(lastTimestamp);
            }
        } else { // 时间戳改变，毫秒内序列重置
            sequence = 0L;
        }

        // 上次生成 ID 的时间截
        lastTimestamp = timestamp;

        // 移位并通过或运算拼到一起组成 64 位的 ID
        return ((timestamp - twepoch) << timestampLeftShift)
                | (dataCenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成 ID 的时间截
     * @return 当前时间戳
     */
    protected long tillNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间戳
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}
