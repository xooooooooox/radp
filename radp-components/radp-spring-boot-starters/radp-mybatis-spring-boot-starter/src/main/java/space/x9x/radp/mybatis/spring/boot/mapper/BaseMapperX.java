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

    default T selectOne(SFunction<T, ?> field, Object value) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default T selectOne(String field1, Object value1, String field2, Object value2) {
        return selectOne(new QueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2,
                        SFunction<T, ?> field3, Object value3) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2)
                .eq(field3, value3));
    }

    default Long selectCount() {
        return selectCount(new QueryWrapper<>());
    }

    default Long selectCount(String field, Object value) {
        return selectCount(new QueryWrapper<T>().eq(field, value));
    }

    default Long selectCount(SFunction<T, ?> field, Object value) {
        return selectCount(new LambdaQueryWrapper<T>().eq(field, value));
    }


    default List<T> selectList() {
        return selectList(new QueryWrapper<>());
    }

    default List<T> selectList(String field, Object value) {
        return selectList(new QueryWrapper<T>().eq(field, value));
    }

    default List<T> selectList(SFunction<T, ?> field, Object value) {
        return selectList(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default List<T> selectList(String field, Collection<?> values) {
        if (CollUtil.isEmpty(values)) {
            return CollUtil.newArrayList();
        }
        return selectList(new QueryWrapper<T>().in(field, values));
    }

    default List<T> selectList(SFunction<T, ?> field, Collection<?> values) {
        if (CollUtil.isEmpty(values)) {
            return CollUtil.newArrayList();
        }
        return selectList(new LambdaQueryWrapper<T>().in(field, values));
    }

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


    default int delete(String field, String value) {
        return delete(new QueryWrapper<T>().eq(field, value));
    }

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
