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
 * 实体对象. 1. 一般和数据库持久化对象1v1的关系.(但因各自开发系统的不同，也有1vn的可能.) <br/>
 * 2. 如果是老系统改造，那么旧的库表冗余了太多的字段，可能会有nv1的情况.<br/>
 * 3. 对象名称 <code>XxxEntity</code> <br/>
 * 4. 实体对象唯一标识, 一般不使用数据库自增ID, 而是使用业务唯一标识。
 */
package space.x9x.radp.domain.yyy.model.entity;
