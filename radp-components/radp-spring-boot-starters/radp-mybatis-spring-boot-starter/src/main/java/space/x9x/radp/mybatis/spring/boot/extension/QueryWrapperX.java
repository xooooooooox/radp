package space.x9x.radp.mybatis.spring.boot.extension;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * Extended MyBatis Plus QueryWrapper class with additional functionality.
 * <p>
 * Main enhancements:
 * <li>Adds conditional methods with {@code xxxIfPresent} naming pattern that only
 * add conditions to the query when the provided values are not null or empty</li>
 * <p>
 * This wrapper simplifies building dynamic queries where conditions should only
 * be applied when the corresponding values are present.
 *
 * @param <T> the entity type that this wrapper operates on
 * @author x9x
 * @since 2024-11-20 15:53
 */
public class QueryWrapperX<T> extends QueryWrapper<T> {

    /**
     * Adds a LIKE condition to the query only if the value is not null or empty.
     * This method only applies the condition when the provided string has text.
     *
     * @param column the column name to apply the LIKE condition to
     * @param val    the value to match with LIKE
     * @return the current wrapper instance for chaining
     */
    public QueryWrapperX<T> likeIfPresent(String column, String val) {
        if (StringUtils.hasText(val)) {
            return (QueryWrapperX<T>) super.like(column, val);
        }
        return this;
    }

    /**
     * Adds an IN condition to the query only if the collection of values is not null or empty.
     * This method only applies the condition when the provided collection contains elements.
     *
     * @param column the column name to apply the IN condition to
     * @param values the collection of values to match with IN
     * @return the current wrapper instance for chaining
     */
    public QueryWrapperX<T> inIfPresent(String column, Collection<?> values) {
        if (!CollectionUtils.isEmpty(values)) {
            return (QueryWrapperX<T>) super.in(column, values);
        }
        return this;
    }

    /**
     * Adds an IN condition to the query only if the array of values is not null or empty.
     * This method only applies the condition when the provided array contains elements.
     *
     * @param column the column name to apply the IN condition to
     * @param values the array of values to match with IN
     * @return the current wrapper instance for chaining
     */
    public QueryWrapperX<T> inIfPresent(String column, Object... values) {
        if (!ArrayUtils.isEmpty(values)) {
            return (QueryWrapperX<T>) super.in(column, values);
        }
        return this;
    }

    /**
     * Adds an equality (=) condition to the query only if the value is not null.
     * This method only applies the condition when the provided value is present.
     *
     * @param column the column name to apply the equality condition to
     * @param val    the value to match with equality
     * @return the current wrapper instance for chaining
     */
    public QueryWrapperX<T> eqIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.eq(column, val);
        }
        return this;
    }

    public QueryWrapperX<T> neIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.ne(column, val);
        }
        return this;
    }

    public QueryWrapperX<T> gtIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.gt(column, val);
        }
        return this;
    }

    public QueryWrapperX<T> geIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.ge(column, val);
        }
        return this;
    }

    public QueryWrapperX<T> ltIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.lt(column, val);
        }
        return this;
    }

    public QueryWrapperX<T> leIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.le(column, val);
        }
        return this;
    }

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

    // ========== 重写父类方法，方便链式调用 ==========

    @Override
    public QueryWrapperX<T> eq(boolean condition, String column, Object val) {
        super.eq(condition, column, val);
        return this;
    }

    @Override
    public QueryWrapperX<T> eq(String column, Object val) {
        super.eq(column, val);
        return this;
    }

    @Override
    public QueryWrapperX<T> orderByDesc(String column) {
        super.orderByDesc(true, column);
        return this;
    }

    @Override
    public QueryWrapperX<T> last(String lastSql) {
        super.last(lastSql);
        return this;
    }

    @Override
    public QueryWrapperX<T> in(String column, Collection<?> coll) {
        super.in(column, coll);
        return this;
    }
}
