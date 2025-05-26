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
 * Extended MyBatis-Plus base mapper interface.
 * This interface extends MPJBaseMapper to provide additional utility methods for
 * common database operations, including pagination, join queries, simple CRUD operations,
 * and batch processing. It simplifies database access patterns by providing convenient
 * default method implementations.
 *
 * @param <T> the entity type that this mapper operates on
 * @author IO x9x
 * @since 2024-12-24 14:38
 */
public interface BaseMapperX<T> extends MPJBaseMapper<T> {

    // ================================ Pagination Methods ============================== //

    /**
     * Selects a page of records using the provided page parameters and query wrapper.
     * This method uses the sorting fields from the page parameters for ordering the results.
     *
     * @param pageParam    the pagination and sorting parameters
     * @param queryWrapper the query conditions
     * @return a page result containing the records and total count
     */
    default PageResult<T> selectPage(SortablePageParam pageParam, @Param("ew") Wrapper<T> queryWrapper) {
        return selectPage(pageParam, pageParam.getSortingFields(), queryWrapper);
    }

    /**
     * Selects a page of records using the provided page parameters and query wrapper.
     * This method does not apply any sorting to the results.
     *
     * @param pageParam    the pagination parameters
     * @param queryWrapper the query conditions
     * @return a page result containing the records and total count
     */
    default PageResult<T> selectPage(PageParam pageParam, @Param("ew") Wrapper<T> queryWrapper) {
        return selectPage(pageParam, null, queryWrapper);
    }

    /**
     * Selects a page of records using the provided page parameters, sorting fields, and query wrapper.
     * This is the core pagination method that handles both regular pagination and the special case
     * where all records are requested (PAGE_SIZE_NONE).
     *
     * @param pageParam     the pagination parameters
     * @param sortingFields the fields to sort by (can be null for no sorting)
     * @param queryWrapper  the query conditions
     * @return a page result containing the records and total count
     */
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
     * Performs a paginated join query using MyBatis Plus Join.
     * This method supports retrieving data from multiple tables with a lambda-style wrapper.
     * It handles both regular pagination and the special case where all records are requested.
     *
     * @param <D>           the type of the result objects
     * @param pageParam     the pagination parameters
     * @param clazz         the class of the result objects
     * @param lambdaWrapper the lambda-style join query wrapper
     * @return a page result containing the joined records and total count
     */
    default <D> PageResult<D> selectJoinPage(PageParam pageParam, Class<D> clazz, MPJLambdaWrapper<T> lambdaWrapper) {
        if (pageParam.getPageSize().equals(PageParam.PAGE_SIZE_NONE)) {
            List<D> totalList = selectJoinList(clazz, lambdaWrapper);
            return PageResult.build(totalList, (long) totalList.size());
        }

        // MyBatis Plus Join query
        IPage<D> mpPage = MybatisUtils.buildPage(pageParam);
        mpPage = selectJoinPage(mpPage, clazz, lambdaWrapper);
        // Convert and return
        return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
    }

    /**
     * Performs a paginated join query using a base join query wrapper.
     * This method is a more generic version of the join query that works with any
     * implementation of MPJBaseJoin.
     *
     * @param <DTO>            the type of the result objects
     * @param pageParam        the pagination parameters
     * @param resultTypeClass  the class of the result objects
     * @param joinQueryWrapper the join query wrapper
     * @return a page result containing the joined records and total count
     */
    @SuppressWarnings("java:S119")
    default <DTO> PageResult<DTO> selectJoinPage(PageParam pageParam, Class<DTO> resultTypeClass, MPJBaseJoin<T> joinQueryWrapper) {
        IPage<DTO> mpPage = MybatisUtils.buildPage(pageParam);
        selectJoinPage(mpPage, resultTypeClass, joinQueryWrapper);
        // Convert and return
        return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
    }

    // ================================ Simple Query Methods ============================== //

    /**
     * Selects a single entity by field name and value.
     * This method creates a query with an equality condition on the specified field.
     *
     * @param field the name of the field to filter by
     * @param value the value to match
     * @return the matching entity, or null if none found
     */
    default T selectOne(String field, Object value) {
        return selectOne(new QueryWrapper<T>().eq(field, value));
    }

    /**
     * Selects a single entity by field and value using type-safe lambda expressions.
     * This method creates a lambda query with an equality condition on the specified field.
     *
     * @param field the field to filter by, specified as a lambda expression
     * @param value the value to match
     * @return the matching entity, or null if none found
     */
    default T selectOne(SFunction<T, ?> field, Object value) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field, value));
    }

    /**
     * Selects a single entity by two field names and values.
     * This method creates a query with equality conditions on both specified fields.
     *
     * @param field1 the name of the first field to filter by
     * @param value1 the value to match for the first field
     * @param field2 the name of the second field to filter by
     * @param value2 the value to match for the second field
     * @return the matching entity, or null if none found
     */
    default T selectOne(String field1, Object value1, String field2, Object value2) {
        return selectOne(new QueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    /**
     * Selects a single entity by two fields and values using type-safe lambda expressions.
     * This method creates a lambda query with equality conditions on both specified fields.
     *
     * @param field1 the first field to filter by, specified as a lambda expression
     * @param value1 the value to match for the first field
     * @param field2 the second field to filter by, specified as a lambda expression
     * @param value2 the value to match for the second field
     * @return the matching entity, or null if none found
     */
    default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    /**
     * Selects a single entity by three fields and values using type-safe lambda expressions.
     * This method creates a lambda query with equality conditions on all three specified fields.
     *
     * @param field1 the first field to filter by, specified as a lambda expression
     * @param value1 the value to match for the first field
     * @param field2 the second field to filter by, specified as a lambda expression
     * @param value2 the value to match for the second field
     * @param field3 the third field to filter by, specified as a lambda expression
     * @param value3 the value to match for the third field
     * @return the matching entity, or null if none found
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
     * Selects all entities from the table.
     * This method creates an empty query wrapper to retrieve all records.
     *
     * @return a list of all entities in the table
     */
    default List<T> selectList() {
        return selectList(new QueryWrapper<>());
    }

    /**
     * Selects entities by field name and value.
     * This method creates a query with an equality condition on the specified field.
     *
     * @param field the name of the field to filter by
     * @param value the value to match
     * @return a list of entities matching the condition
     */
    default List<T> selectList(String field, Object value) {
        return selectList(new QueryWrapper<T>().eq(field, value));
    }

    /**
     * Selects entities by field and value using type-safe lambda expressions.
     * This method creates a lambda query with an equality condition on the specified field.
     *
     * @param field the field to filter by, specified as a lambda expression
     * @param value the value to match
     * @return a list of entities matching the condition
     */
    default List<T> selectList(SFunction<T, ?> field, Object value) {
        return selectList(new LambdaQueryWrapper<T>().eq(field, value));
    }

    /**
     * Selects entities where a field's value is in a collection of values.
     * This method creates a query with an IN condition on the specified field.
     * If the collection is empty, an empty list is returned without querying the database.
     *
     * @param field  the name of the field to filter by
     * @param values the collection of values to match against
     * @return a list of entities matching the condition, or an empty list if values is empty
     */
    default List<T> selectList(String field, Collection<?> values) {
        if (CollUtil.isEmpty(values)) {
            return CollUtil.newArrayList();
        }
        return selectList(new QueryWrapper<T>().in(field, values));
    }

    /**
     * Selects entities where a field's value is in a collection of values using type-safe lambda expressions.
     * This method creates a lambda query with an IN condition on the specified field.
     * If the collection is empty, an empty list is returned without querying the database.
     *
     * @param field the field to filter by, specified as a lambda expression
     * @param values the collection of values to match against
     * @return a list of entities matching the condition, or an empty list if values is empty
     */
    default List<T> selectList(SFunction<T, ?> field, Collection<?> values) {
        if (CollUtil.isEmpty(values)) {
            return CollUtil.newArrayList();
        }
        return selectList(new LambdaQueryWrapper<T>().in(field, values));
    }

    /**
     * Selects entities by two fields and values using type-safe lambda expressions.
     * This method creates a lambda query with equality conditions on both specified fields.
     *
     * @param field1 the first field to filter by, specified as a lambda expression
     * @param value1 the value to match for the first field
     * @param field2 the second field to filter by, specified as a lambda expression
     * @param value2 the value to match for the second field
     * @return a list of entities matching both conditions
     */
    default List<T> selectList(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectList(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }


    /**
     * Updates all entities that match an empty query wrapper with the provided entity.
     * This method effectively updates all records in the table with the non-null values
     * from the provided entity.
     *
     * @param update the entity containing the values to update
     * @return the number of rows affected
     */
    default int updateBatch(T update) {
        return update(update, new QueryWrapper<>());
    }

    /**
     * Updates a collection of entities in batch mode.
     * This method uses MyBatis Plus's batch update functionality to efficiently
     * update multiple entities by their IDs.
     *
     * @param entities the collection of entities to update
     * @return true if the operation was successful, false otherwise
     */
    default Boolean updateBatch(Collection<T> entities) {
        return Db.updateBatchById(entities);
    }

    /**
     * Updates a collection of entities in batch mode with a specified batch size.
     * This method uses MyBatis Plus's batch update functionality to efficiently
     * update multiple entities by their IDs, with control over the batch size.
     *
     * @param entities the collection of entities to update
     * @param size     the batch size for the operation
     * @return true if the operation was successful, false otherwise
     */
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
     * Inserts a collection of entities in batch mode with a specified batch size.
     * This method uses MyBatis Plus's batch insert functionality to efficiently
     * insert multiple entities, with control over the batch size.
     *
     * @param collections the collection of entities to insert
     * @param size        the batch size for the operation, default is 1000 in {@link Db#saveBatch}
     * @return true if the operation was successful, false otherwise
     */
    default Boolean insertBatch(Collection<T> collections, int size) {
        return Db.saveBatch(collections, size);
    }
}
