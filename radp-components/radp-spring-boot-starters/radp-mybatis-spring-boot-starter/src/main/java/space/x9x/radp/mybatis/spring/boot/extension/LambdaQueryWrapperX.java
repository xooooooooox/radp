package space.x9x.radp.mybatis.spring.boot.extension;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.util.StringUtils;
import space.x9x.radp.commons.lang.ArrayUtils;

import java.io.Serial;
import java.util.Collection;

/**
 * 拓展 MyBatis Plus LambdaQueryWrapper 类，主要增加如下功能
 * <li>拼接条件的方法, 增加 {@code xxxIfPresent} 方法, 用于判断值不存在的时候, 不要拼接到查询条件中</li>
 * <p>
 * Extended version of MyBatis Plus LambdaQueryWrapper with additional features:
 * <li>Adds {@code xxxIfPresent} methods that only include conditions in the query when values are present</li>
 * This wrapper simplifies conditional query building by automatically handling null/empty value checks.
 *
 * @author x9x
 * @since 2024-11-20 15:49
 */
public class LambdaQueryWrapperX<T> extends LambdaQueryWrapper<T> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Adds a LIKE condition to the query only if the value is not empty.
     * This method is a conditional version of the standard like() method.
     *
     * @param column the entity field to compare
     * @param val    the value to compare with
     * @return this wrapper instance for method chaining
     */
    public LambdaQueryWrapperX<T> likeIfPresent(SFunction<T, ?> column, String val) {
        if (StringUtils.hasText(val)) {
            return (LambdaQueryWrapperX<T>) super.like(column, val);
        }
        return this;
    }

    /**
     * Adds an IN condition to the query only if the collection of values is not empty.
     * This method is a conditional version of the standard in() method.
     *
     * @param column the entity field to compare
     * @param values the collection of values to compare with
     * @return this wrapper instance for method chaining
     */
    public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
        if (ObjectUtil.isAllNotEmpty(values) && !ArrayUtil.isEmpty(values)) {
            return (LambdaQueryWrapperX<T>) super.in(column, values);
        }
        return this;
    }

    /**
     * Adds an IN condition to the query only if the array of values is not empty.
     * This method is a conditional version of the standard in() method.
     *
     * @param column the entity field to compare
     * @param values the array of values to compare with
     * @return this wrapper instance for method chaining
     */
    public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Object... values) {
        if (ObjectUtil.isAllNotEmpty(values) && !ArrayUtil.isEmpty(values)) {
            return (LambdaQueryWrapperX<T>) super.in(column, values);
        }
        return this;
    }

    /**
     * Adds an equality condition to the query only if the value is not empty.
     * This method is a conditional version of the standard eq() method.
     *
     * @param column the entity field to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    public LambdaQueryWrapperX<T> eqIfPresent(SFunction<T, ?> column, Object val) {
        if (ObjectUtil.isNotEmpty(val)) {
            return (LambdaQueryWrapperX<T>) super.eq(column, val);
        }
        return this;
    }

    /**
     * Adds a not-equal condition to the query only if the value is not empty.
     * This method is a conditional version of the standard ne() method.
     *
     * @param column the entity field to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    public LambdaQueryWrapperX<T> neIfPresent(SFunction<T, ?> column, Object val) {
        if (ObjectUtil.isNotEmpty(val)) {
            return (LambdaQueryWrapperX<T>) super.ne(column, val);
        }
        return this;
    }

    /**
     * Adds a greater-than condition to the query only if the value is not null.
     * This method is a conditional version of the standard gt() method.
     *
     * @param column the entity field to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    public LambdaQueryWrapperX<T> gtIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (LambdaQueryWrapperX<T>) super.gt(column, val);
        }
        return this;
    }

    /**
     * Adds a greater-than-or-equal condition to the query only if the value is not null.
     * This method is a conditional version of the standard ge() method.
     *
     * @param column the entity field to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    public LambdaQueryWrapperX<T> geIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (LambdaQueryWrapperX<T>) super.ge(column, val);
        }
        return this;
    }

    /**
     * Adds a less-than condition to the query only if the value is not null.
     * This method is a conditional version of the standard lt() method.
     *
     * @param column the entity field to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    public LambdaQueryWrapperX<T> ltIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (LambdaQueryWrapperX<T>) super.lt(column, val);
        }
        return this;
    }

    /**
     * Adds a less-than-or-equal condition to the query only if the value is not null.
     * This method is a conditional version of the standard le() method.
     *
     * @param column the entity field to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    public LambdaQueryWrapperX<T> leIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (LambdaQueryWrapperX<T>) super.le(column, val);
        }
        return this;
    }

    /**
     * Adds a between condition to the query based on the presence of values.
     * If both values are present, adds a BETWEEN condition.
     * If only val1 is present, adds a greater-than-or-equal condition.
     * If only val2 is present, adds a less-than-or-equal condition.
     * If neither value is present, no condition is added.
     *
     * @param column the entity field to compare
     * @param val1 the lower bound value
     * @param val2 the upper bound value
     * @return this wrapper instance for method chaining
     */
    public LambdaQueryWrapperX<T> betweenIfPresent(SFunction<T, ?> column, Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            return (LambdaQueryWrapperX<T>) super.between(column, val1, val2);
        }
        if (val1 != null) {
            return (LambdaQueryWrapperX<T>) ge(column, val1);
        }
        if (val2 != null) {
            return (LambdaQueryWrapperX<T>) le(column, val2);
        }
        return this;
    }

    /**
     * Adds a between condition to the query using an array of values.
     * This method extracts the first two elements from the array and
     * delegates to the other betweenIfPresent method.
     *
     * @param column the entity field to compare
     * @param values an array containing the lower and upper bound values
     * @return this wrapper instance for method chaining
     */
    public LambdaQueryWrapperX<T> betweenIfPresent(SFunction<T, ?> column, Object[] values) {
        Object val1 = ArrayUtils.get(values, 0);
        Object val2 = ArrayUtils.get(values, 1);
        return betweenIfPresent(column, val1, val2);
    }

    // ========== Overridden Parent Methods for Method Chaining ==========

    /**
     * Overridden method to add an equality condition to the query.
     * This override ensures proper return type for method chaining.
     *
     * @param condition whether to add this condition
     * @param column the entity field to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    @Override
    public LambdaQueryWrapperX<T> eq(boolean condition, SFunction<T, ?> column, Object val) {
        super.eq(condition, column, val);
        return this;
    }

    /**
     * Overridden method to add an equality condition to the query.
     * This override ensures proper return type for method chaining.
     *
     * @param column the entity field to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    @Override
    public LambdaQueryWrapperX<T> eq(SFunction<T, ?> column, Object val) {
        super.eq(column, val);
        return this;
    }

    /**
     * Overridden method to add a descending order by clause to the query.
     * This override ensures proper return type for method chaining.
     *
     * @param column the entity field to order by
     * @return this wrapper instance for method chaining
     */
    @Override
    public LambdaQueryWrapperX<T> orderByDesc(SFunction<T, ?> column) {
        super.orderByDesc(true, column);
        return this;
    }

    /**
     * Overridden method to append a custom SQL fragment at the end of the query.
     * This override ensures proper return type for method chaining.
     *
     * @param lastSql the SQL fragment to append
     * @return this wrapper instance for method chaining
     */
    @Override
    public LambdaQueryWrapperX<T> last(String lastSql) {
        super.last(lastSql);
        return this;
    }

    /**
     * Overridden method to add an IN condition to the query.
     * This override ensures proper return type for method chaining.
     *
     * @param column the entity field to compare
     * @param coll the collection of values to compare with
     * @return this wrapper instance for method chaining
     */
    @Override
    public LambdaQueryWrapperX<T> in(SFunction<T, ?> column, Collection<?> coll) {
        super.in(column, coll);
        return this;
    }

}
