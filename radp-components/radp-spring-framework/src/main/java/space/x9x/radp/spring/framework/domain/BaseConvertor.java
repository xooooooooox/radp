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

package space.x9x.radp.spring.framework.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MapperConfig;

/**
 * Base interface for object conversion between different types. This interface provides
 * methods for converting objects from the source type to the target type. It supports
 * conversion of single objects, lists, and streams.
 *
 * @author x9x
 * @since 2024-10-27 10:53
 * @param <S> source type to convert from
 * @param <T> target type to convert to
 */
@MapperConfig
public interface BaseConvertor<S, T> {

	// ============= source to target ===============

	T sourceToTarget(S s);

	List<T> sourceToTarget(List<S> list);

	default List<T> sourceToTarget(Stream<S> stream) {
		if (stream == null) {
			return null;
		}
		return stream.map(this::sourceToTarget).collect(Collectors.toList());
	}

	// ============= target to source ===============

	@InheritInverseConfiguration(name = "sourceToTarget")
	S targetToSource(T t);

	List<S> targetToSource(List<T> list);

	default List<S> targetToSource(Stream<T> stream) {
		if (stream == null) {
			return null;
		}
		return stream.map(this::targetToSource).collect(Collectors.toList());
	}

}
