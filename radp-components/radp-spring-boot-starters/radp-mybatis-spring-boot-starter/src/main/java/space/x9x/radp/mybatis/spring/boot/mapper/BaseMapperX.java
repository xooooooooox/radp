package space.x9x.radp.mybatis.spring.boot.mapper;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Param;
import space.x9x.radp.mybatis.spring.boot.util.MybatisUtils;
import space.x9x.radp.spring.framework.dto.PageParam;
import space.x9x.radp.spring.framework.dto.PageResult;
import space.x9x.radp.spring.framework.dto.SortablePageParam;
import space.x9x.radp.spring.framework.dto.SortingField;

import java.util.Collection;
import java.util.List;

/**
 * Extended base mapper interface that provides additional utility methods for database operations.
 * This interface extends MPJBaseMapper to support complex queries including joins,
 * pagination, and common CRUD operations with simplified syntax.
 *
 * @author x9x
 * @since 2024-12-24 14:38
 */
public interface BaseMapperX<T> extends MPJBaseMapper<T> {

    // ================================ Pagination Methods ============================== //

    default PageResult<T> selectPage(SortablePageParam pageParam, @Param("ew") Wrapper<T> queryWrapper) {
        return selectPage(pageParam, pageParam.getSortingFields(), queryWrapper);
    }

    default PageResult<T> selectPage(PageParam pageParam, @Param("ew") Wrapper<T> queryWrapper) {
        return selectPage(pageParam, null, queryWrapper);
    }

    default PageResult<T> selectPage(PageParam pageParam, List<SortingField> sortingFields, @Param("ew") Wrapper<T> queryWrapper) {
        if (pageParam.getPageSize().equals(PageParam.PAGE_SIZE_NONE)) {
            List<T> totalList = selectList(queryWrapper);
            return PageResult.build(totalList, (long) totalList.size());
        }

        IPage<T> mpPage = MybatisUtils.buildPage(pageParam, sortingFields);
        selectPage(mpPage, queryWrapper);
        return PageResult.build(mpPage.getRecords(), mpPage.getTotal());
    }

    // ================================ Join Query Methods ============================== //

    /**
     * Executes a join query with pagination using a lambda wrapper.
     *
     * @param <D>           the type of the result objects
     * @param pageParam     the pagination parameters
     * @param clazz         the class of the result objects
     * @param lambdaWrapper the lambda wrapper for the join query
     * @return a page result containing the query results
     */
    default <D> PageResult<D> selectJoinPage(PageParam pageParam, Class<D> clazz, MPJLambdaWrapper<T> lambdaWrapper) {
        if (pageParam.getPageSize().equals(PageParam.PAGE_SIZE_NONE)) {
            List<D> totalList = selectJoinList(clazz, lambdaWrapper);
            return PageResult.build(totalList, (long) totalList.size());
        }

        // Execute MyBatis Plus Join query
        IPage<D> mpPage = MybatisUtils.buildPage(pageParam);
        mpPage = selectJoinPage(mpPage, clazz, lambdaWrapper);
        // Convert and return the result
        return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
    }

    /**
     * Executes a join query with pagination using a base join wrapper.
     *
     * @param <DTO> the type of the result objects
     * @param pageParam the pagination parameters
     * @param resultTypeClass the class of the result objects
     * @param joinQueryWrapper the join query wrapper
     * @return a page result containing the query results
     */
    @SuppressWarnings("java:S119")
    default <DTO> PageResult<DTO> selectJoinPage(PageParam pageParam, Class<DTO> resultTypeClass, MPJBaseJoin<T> joinQueryWrapper) {
        IPage<DTO> mpPage = MybatisUtils.buildPage(pageParam);
        selectJoinPage(mpPage, resultTypeClass, joinQueryWrapper);
        // Convert and return the result
        return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
    }

    // ================================ Simple Query Methods ============================== //

    default T selectOne(String field, Object value) {
        return selectOne(new QueryWrapper<T>().eq(field, value));
    }

    /**
     * Retrieves a single record that matches the specified field and value using a lambda function.
     *
     * @param field the field to filter by, specified as a lambda function
     * @param value the value to match
     * @return the matching record, or null if not found
     */
    default T selectOne(SFunction<T, ?> field, Object value) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default T selectOne(String field1, Object value1, String field2, Object value2) {
        return selectOne(new QueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    /**
     * Retrieves a single record that matches both specified field-value pairs using lambda functions.
     *
     * @param field1 the first field to filter by, specified as a lambda function
     * @param value1 the value to match for the first field
     * @param field2 the second field to filter by, specified as a lambda function
     * @param value2 the value to match for the second field
     * @return the matching record, or null if not found
     */
    default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    /**
     * Retrieves a single record that matches all three specified field-value pairs using lambda functions.
     *
     * @param field1 the first field to filter by, specified as a lambda function
     * @param value1 the value to match for the first field
     * @param field2 the second field to filter by, specified as a lambda function
     * @param value2 the value to match for the second field
     * @param field3 the third field to filter by, specified as a lambda function
     * @param value3 the value to match for the third field
     * @return the matching record, or null if not found
     */
    default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2,
                        SFunction<T, ?> field3, Object value3) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2)
                .eq(field3, value3));
    }

    /**
     * Counts all records in the table.
     *
     * @return the total count of records
     */
    default Long selectCount() {
        return selectCount(new QueryWrapper<>());
    }

    /**
     * Counts records that match the specified field and value.
     *
     * @param field the field name to filter by
     * @param value the value to match
     * @return the count of matching records
     */
    default Long selectCount(String field, Object value) {
        return selectCount(new QueryWrapper<T>().eq(field, value));
    }

    /**
     * Counts records that match the specified field and value using a lambda function.
     *
     * @param field the field to filter by, specified as a lambda function
     * @param value the value to match
     * @return the count of matching records
     */
    default Long selectCount(SFunction<T, ?> field, Object value) {
        return selectCount(new LambdaQueryWrapper<T>().eq(field, value));
    }


    /**
     * Retrieves all records from the table.
     *
     * @return a list of all records
     */
    default List<T> selectList() {
        return selectList(new QueryWrapper<>());
    }

    /**
     * Retrieves records that match the specified field and value.
     *
     * @param field the field name to filter by
     * @param value the value to match
     * @return a list of matching records
     */
    default List<T> selectList(String field, Object value) {
        return selectList(new QueryWrapper<T>().eq(field, value));
    }

    /**
     * Retrieves records that match the specified field and value using a lambda function.
     *
     * @param field the field to filter by, specified as a lambda function
     * @param value the value to match
     * @return a list of matching records
     */
    default List<T> selectList(SFunction<T, ?> field, Object value) {
        return selectList(new LambdaQueryWrapper<T>().eq(field, value));
    }

    /**
     * Retrieves records where the specified field matches any value in the collection.
     *
     * @param field the field name to filter by
     * @param values the collection of values to match against
     * @return a list of matching records, or an empty list if values is empty
     */
    default List<T> selectList(String field, Collection<?> values) {
        if (CollUtil.isEmpty(values)) {
            return CollUtil.newArrayList();
        }
        return selectList(new QueryWrapper<T>().in(field, values));
    }

    /**
     * Retrieves records where the specified field matches any value in the collection using a lambda function.
     *
     * @param field the field to filter by, specified as a lambda function
     * @param values the collection of values to match against
     * @return a list of matching records, or an empty list if values is empty
     */
    default List<T> selectList(SFunction<T, ?> field, Collection<?> values) {
        if (CollUtil.isEmpty(values)) {
            return CollUtil.newArrayList();
        }
        return selectList(new LambdaQueryWrapper<T>().in(field, values));
    }

    /**
     * Retrieves records that match both specified field-value pairs using lambda functions.
     *
     * @param field1 the first field to filter by, specified as a lambda function
     * @param value1 the value to match for the first field
     * @param field2 the second field to filter by, specified as a lambda function
     * @param value2 the value to match for the second field
     * @return a list of matching records
     */
    default List<T> selectList(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectList(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }


    default int updateBatch(T update) {
        return update(update, new QueryWrapper<>());
    }

    default Boolean updateBatch(Collection<T> entities) {
        return Db.updateBatchById(entities);
    }

    default Boolean updateBatch(Collection<T> entities, int size) {
        return Db.updateBatchById(entities, size);
    }


    /**
     * Deletes records that match the specified field and value.
     *
     * @param field the field name to filter by
     * @param value the value to match
     * @return the number of records deleted
     */
    default int delete(String field, String value) {
        return delete(new QueryWrapper<T>().eq(field, value));
    }

    /**
     * Deletes records that match the specified field and value using a lambda function.
     *
     * @param field the field to filter by, specified as a lambda function
     * @param value the value to match
     * @return the number of records deleted
     */
    default int delete(SFunction<T, ?> field, Object value) {
        return delete(new LambdaQueryWrapper<T>().eq(field, value));
    }

    /**
     * Batch inserts a collection of entities into the database.
     *
     * @param collections the collection of entities to insert
     * @param size        the batch size for the operation, default is 1000 in {@link Db#saveBatch}
     * @return true if the operation was successful, false otherwise
     */
    default Boolean insertBatch(Collection<T> collections, int size) {
        return Db.saveBatch(collections, size);
    }
}
