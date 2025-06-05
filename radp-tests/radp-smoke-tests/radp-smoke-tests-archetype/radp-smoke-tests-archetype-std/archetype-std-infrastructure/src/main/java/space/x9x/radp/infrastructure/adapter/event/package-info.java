
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
