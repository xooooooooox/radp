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

package space.x9x.radp.types.exception.asserts;

import lombok.experimental.UtilityClass;
import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;

/**
 * RedisKey 常量
 * 格式: 引用名:模块名(或功能点):前缀
 *
 * @author x9x
 * @since 2024-10-20 18:45
 */
@UtilityClass
public class RedisKeyConstants {
    // TODO 2024/10/20: 这只是个示例
    public static final String SCAFFOLD_DEMO_REDIS_KEY = "scaffold:lite:demo";


    public static <T> String buildRedisKey(String keyPrefix, T key) {
        return StringUtils.join(keyPrefix, Strings.COLON, String.valueOf(key));
    }
}
