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
 * 监听服务；在单体服务中，解耦流程。类似MQ的使用，如Spring的Event，Guava的事件总线都可以。如果使用了 Redis 那么也可以有发布/订阅使用。
 * Guava：https://bugstack.cn/md/road-map/guava.html
 */
package space.x9x.radp.archetype.layer.trigger.listener;