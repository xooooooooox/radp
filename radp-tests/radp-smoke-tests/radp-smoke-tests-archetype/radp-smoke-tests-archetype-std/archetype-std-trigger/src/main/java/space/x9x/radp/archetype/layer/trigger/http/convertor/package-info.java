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
 * 1. 在这里定义 Entity 与 DTO 转换器
 * 2. 命名 {@code IXxxEntityConvertor}
 * 3. 示例
 * <pre>
 * import org.mapstruct.Mapper;
 *
 * {@literal @}Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
 * public interface IStrategyAwardEntityConvertor extends BaseConvertor&lt;StrategyAwardEntity, RaffleAwardListResponseDTO&gt; {
 * }
 * </pre>
 */
package space.x9x.radp.archetype.layer.trigger.http.convertor;
