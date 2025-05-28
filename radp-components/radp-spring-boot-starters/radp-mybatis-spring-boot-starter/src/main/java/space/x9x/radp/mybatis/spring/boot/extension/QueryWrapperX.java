package space.x9x.radp.mybatis.spring.boot.extension;

import java.io.Serial;
import java.util.Collection;

import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

/**
 * 拓展 MyBatis Plus QueryWrapper 类，主要增加如下功能
 * <ul>
 * <li>拼接条件的方法, 增加 {@code xxxIfPresent} 方法, 用于判断值不存在的时候, 不要拼接到查询条件中</li>
 * </ul>
 * <p>
 * The extended version of MyBatis Plus QueryWrapper with additional features:
 * <ul>
 * <li>Adds {@code xxxIfPresent} methods that only include conditions in the query when
 * values are present</li>
 * </ul>
 * This wrapper simplifies conditional query building by automatically handling null/empty
 * value checks.
 *
 * @author IO x9x
 * @since 2024-11-20 15:53
 */
public class QueryWrapperX<T> extends QueryWrapper<T> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Adds a LIKE condition to the query only if the value is not empty.
     * This method is a conditional version of the standard like() method.
     *
     * @param column the database column name to compare
     * @param val    the value to compare with
     * @return this wrapper instance for method chaining
     */
    public QueryWrapperX<T> likeIfPresent(String column, String val) {
        if (StringUtils.hasText(val)) {
            return (QueryWrapperX<T>) super.like(column, val);
        }
        return this;
    }

    /**
     * Adds an IN condition to the query only if the collection of values is not empty.
     * This method is a conditional version of the standard in() method.
     *
     * @param column the database column name to compare
     * @param values the collection of values to compare with
     * @return this wrapper instance for method chaining
     */
    public QueryWrapperX<T> inIfPresent(String column, Collection<?> values) {
        if (!CollectionUtils.isEmpty(values)) {
            return (QueryWrapperX<T>) super.in(column, values);
        }
        return this;
    }

    /**
     * Adds an IN condition to the query only if the array of values is not empty.
     * This method is a conditional version of the standard in() method.
     *
     * @param column the database column name to compare
     * @param values the array of values to compare with
     * @return this wrapper instance for method chaining
     */
    public QueryWrapperX<T> inIfPresent(String column, Object... values) {
        if (!ArrayUtils.isEmpty(values)) {
            return (QueryWrapperX<T>) super.in(column, values);
        }
        return this;
    }

    /**
     * Adds an equality condition to the query only if the value is not null.
     * This method is a conditional version of the standard eq() method.
     *
     * @param column the database column name to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    public QueryWrapperX<T> eqIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.eq(column, val);
        }
        return this;
    }

    /**
     * Adds a not-equal condition to the query only if the value is not null.
     * This method is a conditional version of the standard ne() method.
     *
     * @param column the database column name to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    public QueryWrapperX<T> neIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.ne(column, val);
        }
        return this;
    }

    /**
     * Adds a greater-than condition to the query only if the value is not null.
     * This method is a conditional version of the standard gt() method.
     *
     * @param column the database column name to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    public QueryWrapperX<T> gtIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.gt(column, val);
        }
        return this;
    }

    /**
     * Adds a greater-than-or-equal condition to the query only if the value is not null.
     * This method is a conditional version of the standard ge() method.
     *
     * @param column the database column name to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    public QueryWrapperX<T> geIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.ge(column, val);
        }
        return this;
    }

    /**
     * Adds a less-than condition to the query only if the value is not null.
     * This method is a conditional version of the standard lt() method.
     *
     * @param column the database column name to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    public QueryWrapperX<T> ltIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.lt(column, val);
        }
        return this;
    }

    /**
     * Adds a less-than-or-equal condition to the query only if the value is not null.
     * This method is a conditional version of the standard le() method.
     *
     * @param column the database column name to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    public QueryWrapperX<T> leIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.le(column, val);
        }
        return this;
    }

    /**
     * Adds a between condition to the query based on the presence of values.
     * If both values are present, add a BETWEEN condition.
     * If only val1 is present, adds a greater-than-or-equal condition.
     * If only val2 is present, adds a less-than-or-equal condition.
     * If neither value is present, no condition is added.
     *
     * @param column the database column name to compare
     * @param val1 the lower-bound value
     * @param val2 the upper-bound value
     * @return this wrapper instance for method chaining
     */
    public QueryWrapperX<T> betweenIfPresent(String column, Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            return (QueryWrapperX<T>) super.between(column, val1, val2);
        }
        if (val1 != null) {
            return (QueryWrapperX<T>) ge(column, val1);
        }
        if (val2 != null) {
            return (QueryWrapperX<T>) le(column, val2);
        }
        return this;
    }

    /**
     * Adds a between condition to the query using an array of values.
     * This method extracts the first two elements from the array and
     * applies appropriate conditions based on their presence.
     *
     * @param column the database column name to compare
     * @param values an array containing the lower and upper bound values
     * @return this wrapper instance for method chaining
     */
    public QueryWrapperX<T> betweenIfPresent(String column, Object[] values) {
        if (values != null && values.length != 0 && values[0] != null && values[1] != null) {
            return (QueryWrapperX<T>) super.between(column, values[0], values[1]);
        }
        if (values != null && values.length != 0 && values[0] != null) {
            return (QueryWrapperX<T>) ge(column, values[0]);
        }
        if (values != null && values.length != 0 && values[1] != null) {
            return (QueryWrapperX<T>) le(column, values[1]);
        }
        return this;
    }

    // ========== Overridden Parent Methods for Method Chaining ==========

    /**
     * Overridden method to add an equality condition to the query.
     * This override ensures a proper return type for method chaining.
     *
     * @param condition whether to add this condition
     * @param column the database column name to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    @Override
    public QueryWrapperX<T> eq(boolean condition, String column, Object val) {
        super.eq(condition, column, val);
        return this;
    }

    /**
     * Overridden method to add an equality condition to the query.
     * This override ensures a proper return type for method chaining.
     *
     * @param column the database column name to compare
     * @param val the value to compare with
     * @return this wrapper instance for method chaining
     */
    @Override
    public QueryWrapperX<T> eq(String column, Object val) {
        super.eq(column, val);
        return this;
    }

    /**
     * Overridden method to add a descending order by clause to the query.
     * This override ensures a proper return type for method chaining.
     *
     * @param column the database columns name to order by
     * @return this wrapper instance for method chaining
     */
    @Override
    public QueryWrapperX<T> orderByDesc(String column) {
        super.orderByDesc(true, column);
        return this;
    }

    /**
     * Overridden method to append a custom SQL fragment at the end of the query.
     * This override ensures a proper return type for method chaining.
     *
     * @param lastSql the SQL fragment to append
     * @return this wrapper instance for method chaining
     */
    @Override
    public QueryWrapperX<T> last(String lastSql) {
        super.last(lastSql);
        return this;
    }

    /**
     * Overridden method to add an IN condition to the query.
     * This override ensures a proper return type for method chaining.
     *
     * @param column the database column name to compare
     * @param coll the collection of values to compare with
     * @return this wrapper instance for method chaining
     */
    @Override
    public QueryWrapperX<T> in(String column, Collection<?> coll) {
        super.in(column, coll);
        return this;
    }
}
