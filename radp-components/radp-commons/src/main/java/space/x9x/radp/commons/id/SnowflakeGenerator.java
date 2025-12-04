/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.commons.id;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

import lombok.Builder;
import lombok.Getter;
import lombok.Synchronized;

/**
 * Twitter Snowflake ID Generator.
 * <p>
 * The Snowflake ID is a 64-bit value: 0 + 41 bits timestamp + 10 bits machine ID (5 bits
 * worker ID + 5 bits datacenter ID) + 12 bits sequence number <pre>
 * ┌─────────────── 41 bits ────────────────┐┌──── 10 bits ────┐┌─ 12 bits ─┐
 * │         timestamp (milliseconds)       ││datacenter+worker││ sequence  │
 * └────────────────────────────────────────┘└─────────────────┘└───────────┘
 * </pre>
 *
 * <p>
 * Example usage: <pre>{@code
 * public class SnowflakeDemo {
 *     public static void main(String[] args) {
 * 			// 方式A
 *         	// Create a default SnowflakeGenerator (dataCenterId=1, workerId=1)
 *         	SnowflakeGenerator generator = new SnowflakeGenerator(1, 1);
 *         	// Generate a Snowflake ID using nextId() method
 *         	long id1 = generator.nextId();
 *         	System.out.println("Snowflake ID: " + id1);
 *
 * 			// 方式B
 *         	// Generate using static method
 *         	long id2 = SnowflakeGenerator.nextId(2, 3);
 *         	System.out.println("Snowflake ID: " + id2);
 *     }
 * }
 * }</pre>
 *
 * @author RADP x9x
 * @since 2024-12-27 12:36
 */
public class SnowflakeGenerator {

	/**
	 * Epoch timestamp (2018-01-01) used as the starting point for ID generation.
	 */
	private static final long EPOCH = 1514736000000L;

	/**
	 * 机器 ID 占 5 位（机器 ID 和数据中心 ID 加起来不能超过 10）
	 */
	private static final long WORKER_ID_BITS = 5L; // 0..31

	/**
	 * 数据中心 ID 占 5 位
	 */
	private static final long DATACENTER_ID_BITS = 5L; // 0..31

	/**
	 * 序列号占 12 位
	 */
	private static final long SEQUENCE_BITS = 12L; // 0..4095

	/**
	 * 支持的最大机器 ID
	 */
	private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS); // 31

	/**
	 * 支持的最大数据标识 ID
	 */
	private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS); // 31

	/**
	 * 机器 ID 向左移 12 位
	 */
	private static final long WORKER_ID_SHIFT = SEQUENCE_BITS; // 12

	/**
	 * 数据标识 ID 向左移 17 位
	 */
	private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS; // 17

	/**
	 * 时间截向左移 22 位
	 */
	private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS; // 22

	/**
	 * 生成序列的掩码
	 */
	private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS); // 4095

	/**
	 * 允许容忍的最大回拨毫秒数 (<=5ms 等待, >5ms 才报错)
	 */
	private static final long MAX_BACKWARD_MS = 5L;

	/**
	 * 机器 ID
	 */
	@Getter
	private final long workerId;

	/**
	 * 数据中心 ID
	 */
	@Getter
	private final long dataCenterId;

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
	 * @param dataCenterId 数据中心 ID
	 * @param workerId 工作 ID
	 */
	@Builder
	public SnowflakeGenerator(long dataCenterId, long workerId) {
		if (dataCenterId > MAX_DATACENTER_ID || dataCenterId < 0) {
			throw new IllegalArgumentException(String.format("dataCenterId 不能大于 %d 或者小于 0", MAX_DATACENTER_ID));
		}
		if (workerId > MAX_WORKER_ID || workerId < 0) {
			throw new IllegalArgumentException(String.format("workerId 不能大于 %d 或者小于 0", MAX_WORKER_ID));
		}
		this.dataCenterId = dataCenterId;
		this.workerId = workerId;
	}

	/**
	 * 获得下一个 ID (Recommend)
	 * @param dataCenterId 数据中心 ID
	 * @param workerId 工作 ID
	 * @return snowflakeId
	 */
	public static long nextId(long dataCenterId, long workerId) {
		SnowflakeGenerator gen = REGISTRY.computeIfAbsent(key(dataCenterId, workerId),
				(k) -> new SnowflakeGenerator(dataCenterId, workerId));
		return gen.nextId();
	}

	/**
	 * 获得下一个 ID
	 * @return snowflakeId
	 */
	@Synchronized
	public long nextId() {
		long ts = currentTime();

		// 处理时钟回拨
		if (ts < this.lastTimestamp) {
			long diff = this.lastTimestamp - ts;
			if (diff <= MAX_BACKWARD_MS) {
				ts = waitUntil(this.lastTimestamp); // 等到不小于 lastTimestamp
			}
			else {
				throw new IllegalStateException("检测到时钟回拨 " + diff + "ms，大于允许阈值 " + MAX_BACKWARD_MS + "ms");
			}
		}

		if (ts == this.lastTimestamp) {
			// 同一毫秒内递增
			this.sequence = (this.sequence + 1) & SEQUENCE_MASK;
			if (this.sequence == 0) {
				// 溢出，等到下一毫秒
				ts = waitUntil(this.lastTimestamp + 1);
			}
		}
		else {
			this.sequence = 0L; // 新毫秒，序列归零（也可改为随机起点减冲突，但非必须）
		}

		this.lastTimestamp = ts;

		// 组装 64 位 ID（最高位始终为 0）
		return ((ts - EPOCH) << TIMESTAMP_LEFT_SHIFT) | (this.dataCenterId << DATACENTER_ID_SHIFT)
				| (this.workerId << WORKER_ID_SHIFT) | this.sequence;
	}

	/**
	 * 缓存 SnowflakeGenerator, 复用创建过的 generator, 避免每次 new 带来丢状态/重复的风险.
	 */
	private static final ConcurrentHashMap<Long, SnowflakeGenerator> REGISTRY = new ConcurrentHashMap<>();

	private static long key(long dataCenterId, long workerId) {
		return (dataCenterId << 5) | workerId;
	}

	private static long currentTime() {
		return System.currentTimeMillis();
	}

	private static long waitUntil(long targetTime) {
		long now = currentTime();
		while (now < targetTime) {
			relax();
			now = System.currentTimeMillis();
		}
		return now;
	}

	private static void relax() {
		try {
			Method method = Thread.class.getMethod("onSpinWait");
			method.invoke(null);
		}
		catch (Exception ignore) {
			LockSupport.parkNanos(100_000L); // -0.1ms
		}
	}

}
