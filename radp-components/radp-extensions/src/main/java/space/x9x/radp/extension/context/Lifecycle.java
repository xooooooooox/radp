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

package space.x9x.radp.extension.context;

/**
 * 组件生命周期
 *
 * @author x9x
 * @since 2024-09-24 23:16
 */
public interface Lifecycle {

    /**
     * Initializes the component.
     * This method is called during the initialization phase of the component's lifecycle.
     * It should perform any necessary setup operations before the component is started.
     *
     * @throws IllegalStateException if the component is in an invalid state for initialization
     */
    void initialize() throws IllegalStateException;

    /**
     * Starts the component.
     * This method is called after initialization to start the component's operation.
     * It should activate the component and make it ready for use.
     *
     * @throws IllegalStateException if the component is in an invalid state for starting
     */
    void start() throws IllegalStateException;

    /**
     * Destroys the component.
     * This method is called when the component is no longer needed.
     * It should release any resources held by the component and perform cleanup operations.
     *
     * @throws IllegalStateException if the component is in an invalid state for destruction
     */
    void destroy() throws IllegalStateException;
}
