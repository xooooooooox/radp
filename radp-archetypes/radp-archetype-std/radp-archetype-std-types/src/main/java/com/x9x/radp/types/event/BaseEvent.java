package com.x9x.radp.types.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 定义 MQ 消息结构
 *
 * @author x9x
 * @since 2024-11-28 14:43
 */
@Data
public abstract class BaseEvent<T> {

    public abstract EventMessage<T> buildEventMessage(T data);

    public abstract String topic();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventMessage<T> {
        private String id;
        private LocalDateTime timestamp;
        private T data;
    }
}
