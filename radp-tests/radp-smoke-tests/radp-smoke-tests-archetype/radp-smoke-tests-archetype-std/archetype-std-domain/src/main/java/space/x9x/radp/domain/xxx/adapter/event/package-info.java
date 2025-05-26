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
 * MQ 消息(下面代码示例以 {@code Guava} 的 {@code EventBus} 为例
 * <p>
 * 1. 在领域 {@code domain} 层的 {@code adapter/event} 包中, 定义事件消息(消息体).
 * <pre>{@code
 *
 * @Data
 * public abstract class BaseEvent<T> {
 *
 *     public abstract EventMessage<T> buildEventMessage(T data);
 *
 *     public abstract String topic();
 *
 *     @Data
 *     @Builder
 *     @NoArgsConstructor
 *     @AllArgsConstructor
 *     public static class EventMessage<T> {
 *         private String id;
 *         private LocalDateTime timestamp;
 *         private T data;
 *     }
 * }
 *
 * import space.x9x.types.event.BaseEvent;
 *
 * @Component
 * public class PaySuccessMessageEvent extends BaseEvent<PaySuccessMessageEvent.PaySuccessMessage> {
 *
 *     @Override
 *     public EventMessage<PaySuccessMessage> buildEventMessage(PaySuccessMessage data) {
 *         return EventMessage.<PaySuccessMessage>builder()
 *                 .id(RandomStringUtils.randomNumeric(11))
 *                 .timestamp(LocalDateTime.now())
 *                 .data(data)
 *                 .build();
 *     }
 *
 *     @Override
 *     public String topic() {
 *         return "pay_success";
 *     }
 *
 *     @Data
 *     @Builder
 *     @NoArgsConstructor
 *     @AllArgsConstructor
 *     public static class PaySuccessMessage {
 *         private String userId;
 *         private String tradeNo;
 *     }
 * }
 * }</pre>
 * <p>
 * 2. 在仓储 {@code infrastructure} 层的 {@code adapter/repository} 包中, 发丝MQ 消息
 * <pre>{@code
 * @Repository
 * @RequiredArgsConstructor
 * public class XxRepository implements IXxRepository {
 *      // 注入 Guava 消息总线
 *      private final EventBus eventBus;
 *
 *      @Override
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
 * }</pre>
 * 3. 在触发 {@code trigger} 层的 {@code listener} 包中监听/接收消息
 * <pre>{@code
 * @Configuration
 * public class GuavaConfig {
 *     @Bean
 *     public EventBus eventBusListener(OrderPaySuccessListener listener) {
 *         EventBus eventBus = new EventBus();
 *         eventBus.register(listener);
 *         return eventBus;
 *     }
 * }
 *
 * @Slf4j
 * @Component
 * public class OrderPaySuccessListener {
 *
 *     @Subscribe
 *     public void handleEvent(String paySuccessMsg) {
 *         log.info("收到支付成功消息 {}. \n 可以做接下来的业务,如:发货,充值,开户,返利等..", paySuccessMsg);
 *     }
 * }
 * }</pre>
 */
package space.x9x.radp.domain.xxx.adapter.event;