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

package space.x9x.radp.extension.wrapper;

import space.x9x.radp.extension.active.ActivateComparator;

import java.util.Comparator;

/**
 * 针对 @Wrapper 排序
 * <p>
 * Comparator for sorting elements annotated with @Wrapper.
 * This class extends ActivateComparator to provide specialized sorting
 * for extension points that use the Wrapper annotation.
 *
 * @author IO x9x
 * @since 2024-09-24 13:59
 */
public class WrapperComparator extends ActivateComparator {
    /**
     * Static instance of the WrapperComparator for convenient use.
     * This comparator can be used to sort objects annotated with @Wrapper.
     */
    public static final Comparator<Object> COMPARATOR = new WrapperComparator();
}
