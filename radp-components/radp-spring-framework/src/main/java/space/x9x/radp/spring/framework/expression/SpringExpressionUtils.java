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

package space.x9x.radp.spring.framework.expression;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.collections.MapUtils;
import space.x9x.radp.commons.lang.ArrayUtil;
import space.x9x.radp.commons.lang.StringUtil;

/**
 * Utility class for working with Spring Expression Language (SpEL). Provides methods to
 * parse and evaluate SpEL expressions in different contexts, including within AOP join
 * points and from the Spring bean factory.
 *
 * @author x9x
 * @since 2024-10-23 14:40
 */
@UtilityClass
public class SpringExpressionUtils {

	/**
	 * SpEL 表达式解析器.
	 */
	private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

	/**
	 * 参数名发现器.
	 */
	private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

	/**
	 * Parses a single SpEL expression in the context of a join point.
	 * @param joinPoint the join point providing the context for expression evaluation
	 * @param expressionString the SpEL expression to parse
	 * @return the result of evaluating the expression
	 */
	public static Object parseExpression(JoinPoint joinPoint, String expressionString) {
		return parseExpression(joinPoint, Collections.singletonList(expressionString)).get(expressionString);
	}

	/**
	 * 从切面中, 批量解析 SpEL 表达式的结果.
	 * @param joinPoint 切面点
	 * @param expressionStringList spEL 表达式数组
	 * @return 解析结果. key-表达式, value-表达式结果
	 */
	public static Map<String, Object> parseExpression(JoinPoint joinPoint, List<String> expressionStringList) {
		// 如果为空, 则不进行解析
		if (CollectionUtils.isEmpty(expressionStringList)) {
			return Collections.emptyMap();
		}

		// 第一步: 构建解析上下文 EvaluationContext
		// 通过 joinPoint 获取被注解方法
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		// 使用 Spring 的 ParameterNameDiscoverer 获取被注解方法参数名
		String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
		// Spring 的表达式上下文对象
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 给上下文赋值
		if (ArrayUtil.isNotEmpty(parameterNames)) {
			Object[] args = joinPoint.getArgs();
			for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
				context.setVariable(parameterNames[i], args[i]);
			}
		}

		// 第二步: 逐个参数解析
		HashMap<String, Object> result = MapUtils.newHashMap(expressionStringList.size(), true);
		expressionStringList.forEach(key -> {
			Object value = EXPRESSION_PARSER.parseExpression(key).getValue(context);
			result.put(key, value);
		});

		return result;
	}

	/**
	 * 从 Spring Bean 工厂, 解析 SpEL 表达式的结果.
	 * @param expressionString spEL 表达式
	 * @return 执行结果
	 */
	public static Object parseExpression(String expressionString) {
		if (StringUtil.isBlank(expressionString)) {
			return null;
		}

		Expression expression = EXPRESSION_PARSER.parseExpression(expressionString);
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setBeanResolver(new BeanFactoryResolver(SpringUtil.getApplicationContext()));
		return expression.getValue(context);
	}

}
