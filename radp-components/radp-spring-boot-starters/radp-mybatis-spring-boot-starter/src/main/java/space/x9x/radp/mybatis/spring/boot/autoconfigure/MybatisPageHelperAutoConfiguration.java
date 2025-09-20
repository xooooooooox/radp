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

package space.x9x.radp.mybatis.spring.boot.autoconfigure;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

/**
 * Autoconfiguration for MyBatis pagination functionality. This class automatically
 * configures the MyBatis-Plus pagination interceptor to enable pagination support in
 * database queries.
 *
 * @author IO x9x
 * @since 2024-09-30 13:25
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration(after = DataSourceAutoConfiguration.class)
@Slf4j
public class MybatisPageHelperAutoConfiguration {

	/**
	 * Log message used when the MybatisPlusInterceptor is autowired. This message is
	 * logged at debug level when the interceptor is initialized.
	 */
	private static final String AUTOWIRED_MYBATIS_PLUS_INTERCEPTOR = "Autowired mybatisPlusInterceptor";

	/**
	 * Creates and configures a MybatisPlusInterceptor with pagination support. This bean
	 * provides automatic pagination functionality for MyBatis queries, allowing for easy
	 * implementation of paged database results.
	 * @return a configured MybatisPlusInterceptor with pagination capabilities
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		log.debug(AUTOWIRED_MYBATIS_PLUS_INTERCEPTOR);
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
		return interceptor;
	}

}
