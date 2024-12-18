package space.x9x.radp.spring.framework.expression;

import cn.hutool.extra.spring.SpringUtil;
import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.collections.MapUtils;
import space.x9x.radp.commons.lang.ArrayUtils;
import space.x9x.radp.commons.lang.StringUtils;
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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author x9x
 * @since 2024-10-23 14:40
 */
@UtilityClass
public class SpringExpressionUtils {

    /**
     * SpEL 表达式解析器
     */
    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    /**
     * 参数名发现器
     */
    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    public static Object parseExpression(JoinPoint joinPoint, String expressionString) {
        return parseExpression(joinPoint, Collections.singletonList(expressionString)).get(expressionString);
    }

    /**
     * 从切面中, 批量解析 SpEL 表达式的结果
     *
     * @param joinPoint            切面点
     * @param expressionStringList SpEL 表达式数组
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
        if (ArrayUtils.isNotEmpty(parameterNames)) {
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < parameterNames.length; i++) {
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
     * 从 Spring Bean 工厂, 解析 SpEL 表达式的结果
     *
     * @param expressionString SpEL 表达式
     * @return 执行结果
     */
    public static Object parseExpression(String expressionString) {
        if (StringUtils.isBlank(expressionString)) {
            return null;
        }

        Expression expression = EXPRESSION_PARSER.parseExpression(expressionString);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(SpringUtil.getApplicationContext()));
        return expression.getValue(context);
    }

}
