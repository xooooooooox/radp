#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * 配置发送消息,比如 kafka
 * <pre>{@code
 * @Slf4j
 * @Component
 * public class EventPublisher {
 *
 *     @Resource
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
package ${package}.infrastructure.adapter.event;