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

package space.x9x.radp.spring.framework.json.fastjson;

import com.alibaba.fastjson.serializer.SerializeFilter;

import space.x9x.radp.extension.SPI;

/**
 * Interface for Fastjson filters that can be used to customize JSON serialization. This
 * interface extends Fastjson's SerializeFilter interface and is marked with the SPI
 * annotation to enable dynamic loading of filter implementations.
 *
 * @author x9x
 * @since 2024-09-26 13:04
 */
@SPI
public interface FastjsonFilter extends SerializeFilter {

}
