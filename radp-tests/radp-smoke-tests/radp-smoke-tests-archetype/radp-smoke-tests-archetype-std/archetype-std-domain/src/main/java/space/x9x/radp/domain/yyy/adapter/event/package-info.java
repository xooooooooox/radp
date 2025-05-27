
/**
 * MQ 消息(下面代码示例以 {@code Guava} 的 {@code EventBus} 为例
 * <p>
 * 1. 在领域 {@code domain} 层的 {@code adapter/event} 包中, 定义事件消息(消息体). <pre>{@code
 *
 * &#64;Data
 * public abstract class BaseEvent<T> {
 *
 *     public abstract EventMessage<T> buildEventMessage(T data);
 *
 *     public abstract String topic();
 *
 *     &#64;Data
 *     &#64;Builder
 *     &#64;NoArgsConstructor
 *     &#64;AllArgsConstructor
 *     public static class EventMessage<T> {
 *         private String id;
 *         private LocalDateTime timestamp;
 *         private T data;
 *     }
 * }
 *
 * import space.x9x.types.event.BaseEvent;
 *
 * &#64;Component
 * public class PaySuccessMessageEvent extends BaseEvent<PaySuccessMessageEvent.PaySuccessMessage> {
 *
 *     &#64;Override
 *     public EventMessage<PaySuccessMessage> buildEventMessage(PaySuccessMessage data) {
 *         return EventMessage.<PaySuccessMessage>builder()
 *                 .id(RandomStringUtils.randomNumeric(11))
 *                 .timestamp(LocalDateTime.now())
 *                 .data(data)
 *                 .build();
 *     }
 *
 *     &#64;Override
 *     public String topic() {
 *         return "pay_success";
 *     }
 *
 *     &#64;Data
 *     &#64;Builder
 *     &#64;NoArgsConstructor
 *     &#64;AllArgsConstructor
 *     public static class PaySuccessMessage {
 *         private String userId;
 *         private String tradeNo;
 *     }
 * }
 * }</pre>
 * <p>
 * 2. 在仓储 {@code infrastructure} 层的 {@code adapter/repository} 包中, 发丝MQ 消息 <pre>{@code
 * &#64;Repository
 * &#64;RequiredArgsConstructor
 * public class XxRepository implements IXxRepository {
 *      // 注入 Guava 消息总线
 *      private final EventBus eventBus;
 *
 *      &#64;Override
 *      public void changeOrderPaySuccess(String orderId) {
 *          PayOrderPO filter = PayOrderPO.builder().orderId(orderId).build();
 *          orderMapper.updateOrder(PayOrderPO.builder().status(OrderStatusVO.PAY_SUCCESS.getCode()).build(), filter);
 *
 *          // 发送 MQ 消息
 *          BaseEvent.EventMessage<PaySuccessMessageEvent.PaySuccessMessage> paySuccessMessageEventMessage = paySuccessMessageEvent.buildEventMessage(PaySuccessMessageEvent.PaySuccessMessage.builder()
 *                 .tradeNo(orderId).build());
 *          eventBus.post(paySuccessMessageEventMessage.getData());
 *     }
 * }
 * }</pre> 3. 在触发 {@code trigger} 层的 {@code listener} 包中监听/接收消息 <pre>{@code
 * &#64;Configuration
 * public class GuavaConfig {
 *     &#64;Bean
 *     public EventBus eventBusListener(OrderPaySuccessListener listener) {
 *         EventBus eventBus = new EventBus();
 *         eventBus.register(listener);
 *         return eventBus;
 *     }
 * }
 *
 * &#64;Slf4j
 * &#64;Component
 * public class OrderPaySuccessListener {
 *
 *     &#64;Subscribe
 *     public void handleEvent(String paySuccessMsg) {
 *         log.info("收到支付成功消息 {}. \n 可以做接下来的业务,如:发货,充值,开户,返利等..", paySuccessMsg);
 *     }
 * }
 * }</pre>
 */
package space.x9x.radp.domain.yyy.adapter.event;