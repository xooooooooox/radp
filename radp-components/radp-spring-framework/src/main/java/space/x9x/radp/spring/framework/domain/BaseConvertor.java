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
import java.util.stream.Stream;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MapperConfig;

/**
 * Base interface for object conversion between different types. This interface provides
 * methods for converting objects from source type to target type and vice versa. It
 * supports conversion of single objects, lists, and streams.
 *
 * @author IO x9x
 * @since 2024-10-27 10:53
 * @param <S> source type to convert from
 * @param <T> target type to convert to
 */
@MapperConfig
public interface BaseConvertor<S, T> {

	// ============= source to target ===============

	/**
	 * Converts a single source object to a target object.
	 * @param s the source object to convert
	 * @return the converted target object
	 */
	T sourceToTarget(S s);

	/**
	 * Converts a list of source objects to a list of target objects. This method inherits
	 * the configuration from the sourceToTarget method.
	 * @param list the list of source objects to convert
	 * @return a list of converted target objects
	 */
	@InheritConfiguration(name = "sourceToTarget")
	List<T> sourceToTarget(List<S> list);

	/**
	 * Converts a stream of source objects to a list of target objects.
	 * @param stream the stream of source objects to convert
	 * @return a list of converted target objects
	 */
	List<T> sourceToTarget(Stream<S> stream);

	// ============= target to source ===============

	/**
	 * Converts a single target object back to a source object. This method uses the
	 * inverse configuration of the sourceToTarget method.
	 * @param t the target object to convert back
	 * @return the converted source object
	 */
	@InheritInverseConfiguration(name = "sourceToTarget")
	S targetToSource(T t);

	/**
	 * Converts a list of target objects back to a list of source objects. This method
	 * inherits the configuration from the targetToSource method.
	 * @param list the list of target objects to convert back
	 * @return a list of converted source objects
	 */
	@InheritConfiguration(name = "targetToSource")
	List<S> targetToSource(List<T> list);

	/**
	 * Converts a stream of target objects back to a list of source objects.
	 * @param stream the stream of target objects to convert back
	 * @return a list of converted source objects
	 */
	List<S> targetToSource(Stream<T> stream);

}
