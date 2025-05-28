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

/**
 * 配置发送消息. 比如 kafka <pre>{@code
 * &#64;Slf4j
 * &#64;Component
 * public class EventPublisher {
 *
 *     &#64;Resource
 *     private KafkaTemplate<String, String> kafkaTemplate;
 *
 *     public void publish(String topic, BaseEvent.EventMessage<?> eventMessage) {
 *         try {
 *             String messageJson = JSON.toJSONString(eventMessage);
 *             kafkaTemplate.send(topic, messageJson);
 *             log.info("发送MQ消息 topic:{} message:{}", topic, messageJson);
 *         } catch (Exception e) {
 *             log.error("发送MQ消息失败 topic:{} message:{}", topic, JSON.toJSONString(eventMessage), e);
 *             throw e;
 *         }
 *     }
 *
 * }
 * }</pre>
 */
package space.x9x.radp.infrastructure.adapter.event;
