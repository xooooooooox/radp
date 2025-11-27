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

import java.util.Collection;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
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

/**
 * 扩展的 MyBatis-Plus 基础 mapper 接口. 在 {@link MPJBaseMapper}
 * 的基础上，提供常见数据库操作的便捷方法，包括分页、关联查询、简单的 CRUD 操作以及批处理。 通过提供默认方法实现，简化了数据访问的常见模式。
 *
 * @author x9x
 * @since 2024-12-24 14:38
 * @param <T> 该 Mapper 操作的实体类型
 * @see BaseMapper mybatis-plus 基础接口，提供基本的 CRUD 特性
 * @see MPJBaseMapper 为 mybatis-plus 基础接口，提供表关联 join 能力
 */
public interface BaseMapperX<T> extends MPJBaseMapper<T> {

	// ============================= Pagination Methods ==============================

	/**
	 * 基于给定的分页参数与查询条件，查询分页数据. 会使用分页参数中提供的排序字段对结果进行排序。
	 * @param pageParam 分页与排序参数
	 * @param queryWrapper 查询条件
	 * @return 包含记录列表与总条数的分页结果
	 */
	default PageResult<T> selectPage(SortablePageParam pageParam, @Param("ew") Wrapper<T> queryWrapper) {
		return selectPage(pageParam, pageParam.getSortingFields(), queryWrapper);
	}

	/**
	 * 基于给定的分页参数与查询条件，查询分页数据. 不对结果应用任何排序。
	 * @param pageParam 分页参数
	 * @param queryWrapper 查询条件
	 * @return 包含记录列表与总条数的分页结果
	 */
	default PageResult<T> selectPage(PageParam pageParam, @Param("ew") Wrapper<T> queryWrapper) {
		return selectPage(pageParam, null, queryWrapper);
	}

	/**
	 * 基于分页参数、排序字段与查询条件查询分页数据. 这是核心的分页方法，既处理常规分页，也处理请求全部数据的特殊场景（NO_PAGINATION）。
	 * @param pageParam 分页参数
	 * @param sortingFields 需要排序的字段（为 null 表示不排序）
	 * @param queryWrapper 查询条件
	 * @return 包含记录列表与总条数的分页结果
	 */
	default PageResult<T> selectPage(PageParam pageParam, List<SortingField> sortingFields,
			@Param("ew") Wrapper<T> queryWrapper) {
		if (pageParam.getPageSize().equals(PageParam.NO_PAGINATION)) {
			MybatisUtils.addOrder(queryWrapper, sortingFields);
			List<T> totalList = selectList(queryWrapper);
			return PageResult.ok(totalList, (long) totalList.size());
		}

		IPage<T> mpPage = MybatisUtils.buildPage(pageParam, sortingFields);
		selectPage(mpPage, queryWrapper);
		return PageResult.ok(mpPage.getRecords(), mpPage.getTotal());
	}

	// ============================= Join Query Methods ===========================

	/**
	 * 使用 mybatis plus join 执行分页的关联查询. 支持通过 lambda 风格的 wrapper 从多表检索数据。
	 * 同时支持常规分页与请求全部数据的特殊场景。
	 * @param <D> 结果对象类型
	 * @param pageParam 分页参数
	 * @param clazz 结果对象的 class
	 * @param lambdaWrapper lambda 风格的关联查询包装器
	 * @return 包含关联查询结果与总条数的分页结果
	 */
	default <D> PageResult<D> selectJoinPage(PageParam pageParam, Class<D> clazz, MPJLambdaWrapper<T> lambdaWrapper) {
		// 部分也, 直接查询所有
		if (pageParam.getPageSize().equals(PageParam.NO_PAGINATION)) {
			List<D> totalList = selectJoinList(clazz, lambdaWrapper);
			return PageResult.ok(totalList, (long) totalList.size());
		}

		// execute mybatis plus join query
		IPage<D> mpPage = MybatisUtils.buildPage(pageParam);
		mpPage = selectJoinPage(mpPage, clazz, lambdaWrapper);
		// convert and return the result
		return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
	}

	/**
	 * 使用基础的关联查询包装器执行分页的关联查询. 这是一个更通用的版本，适用于任何 {@link MPJBaseJoin} 的实现。
	 * @param <DTO> 结果对象类型
	 * @param pageParam 分页参数
	 * @param resultTypeClass 结果对象的 class
	 * @param joinQueryWrapper 关联查询包装器
	 * @return 包含关联查询结果与总条数的分页结果
	 */
	@SuppressWarnings("java:S119")
	default <DTO> PageResult<DTO> selectJoinPage(PageParam pageParam, Class<DTO> resultTypeClass,
			MPJBaseJoin<T> joinQueryWrapper) {
		IPage<DTO> mpPage = MybatisUtils.buildPage(pageParam);
		selectJoinPage(mpPage, resultTypeClass, joinQueryWrapper);
		// convert and return the result
		return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
	}

	// ============================= Simple Query Methods =============================

	/**
	 * 根据字段名与值查询单个实体. 该方法会在指定字段上构建等值查询条件。
	 * @param field 字段名
	 * @param value 匹配值
	 * @return 匹配的实体；若未找到则返回 null
	 */
	default T selectOne(String field, Object value) {
		return selectOne(new QueryWrapper<T>().eq(field, value));
	}

	/**
	 * 使用类型安全的 lambda 表达式，根据字段与值查询单个实体. 会在指定字段上构建等值条件。
	 * @param field 以 lambda 表达式指定的字段
	 * @param value 匹配值
	 * @return 匹配的实体；若未找到则返回 null
	 */
	default T selectOne(SFunction<T, ?> field, Object value) {
		return selectOne(new LambdaQueryWrapper<T>().eq(field, value));
	}

	/**
	 * 根据两个字段名与其值查询单个实体. 会在两个字段上分别构建等值条件。
	 * @param field1 第一个字段名
	 * @param value1 第一个字段的匹配值
	 * @param field2 第二个字段名
	 * @param value2 第二个字段的匹配值
	 * @return 匹配的实体；若未找到则返回 null
	 */
	default T selectOne(String field1, Object value1, String field2, Object value2) {
		return selectOne(new QueryWrapper<T>().eq(field1, value1).eq(field2, value2));
	}

	/**
	 * 使用类型安全的 lambda 表达式，根据两个字段与其值查询单个实体. 会在两个字段上分别构建等值条件。
	 * @param field1 第一个字段（lambda 指定）
	 * @param value1 第一个字段的匹配值
	 * @param field2 第二个字段（lambda 指定）
	 * @param value2 第二个字段的匹配值
	 * @return 匹配的实体；若未找到则返回 null
	 */
	default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
		return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
	}

	/**
	 * 使用类型安全的 lambda 表达式，根据三个字段与其值查询单个实体. 会在三个字段上分别构建等值条件。
	 * @param field1 第一个字段（lambda 指定）
	 * @param value1 第一个字段的匹配值
	 * @param field2 第二个字段（lambda 指定）
	 * @param value2 第二个字段的匹配值
	 * @param field3 第三个字段（lambda 指定）
	 * @param value3 第三个字段的匹配值
	 * @return 匹配的实体；若未找到则返回 null
	 */
	default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2,
			SFunction<T, ?> field3, Object value3) {
		return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2).eq(field3, value3));
	}

	/**
	 * 统计表中的全部记录数.
	 * @return 记录总数
	 */
	default Long selectCount() {
		return selectCount(new QueryWrapper<>());
	}

	/**
	 * 统计满足指定字段等值条件的记录数.
	 * @param field 字段名
	 * @param value 匹配值
	 * @return 符合条件的记录数
	 */
	default Long selectCount(String field, Object value) {
		return selectCount(new QueryWrapper<T>().eq(field, value));
	}

	/**
	 * 使用 lambda 指定字段，统计满足等值条件的记录数.
	 * @param field 以 lambda 指定的字段
	 * @param value 匹配值
	 * @return 符合条件的记录数
	 */
	default Long selectCount(SFunction<T, ?> field, Object value) {
		return selectCount(new LambdaQueryWrapper<T>().eq(field, value));
	}

	/**
	 * 查询表中所有实体. 将创建一个空条件的查询包装器以获取全部记录。
	 * @return 全部实体列表
	 */
	default List<T> selectList() {
		return selectList(new QueryWrapper<>());
	}

	/**
	 * 根据字段名与值查询实体列表. 会在指定字段上构建等值条件。
	 * @param field 字段名
	 * @param value 匹配值
	 * @return 满足条件的实体列表
	 */
	default List<T> selectList(String field, Object value) {
		return selectList(new QueryWrapper<T>().eq(field, value));
	}

	/**
	 * 使用类型安全的 lambda 表达式，根据字段与值查询实体列表. 会在指定字段上构建等值条件。
	 * @param field 以 lambda 表达式指定的字段
	 * @param value 匹配值
	 * @return 满足条件的实体列表
	 */
	default List<T> selectList(SFunction<T, ?> field, Object value) {
		return selectList(new LambdaQueryWrapper<T>().eq(field, value));
	}

	/**
	 * 查询指定字段值属于给定集合的实体列表. 会在该字段上构建 IN 条件；若集合为空，则不查询数据库并直接返回空列表。
	 * @param field 字段名
	 * @param values 需要匹配的值集合
	 * @return 满足条件的实体列表；若集合为空则返回空列表
	 */
	default List<T> selectList(String field, Collection<?> values) {
		if (CollUtil.isEmpty(values)) {
			return CollUtil.newArrayList();
		}
		return selectList(new QueryWrapper<T>().in(field, values));
	}

	/**
	 * 使用类型安全的 lambda 表达式，查询指定字段值属于给定集合的实体列表. 会在该字段上构建 IN 条件；若集合为空，则不查询数据库并直接返回空列表。
	 * @param field 以 lambda 表达式指定的字段
	 * @param values 需要匹配的值集合
	 * @return 满足条件的实体列表；若集合为空则返回空列表
	 */
	default List<T> selectList(SFunction<T, ?> field, Collection<?> values) {
		if (CollUtil.isEmpty(values)) {
			return CollUtil.newArrayList();
		}
		return selectList(new LambdaQueryWrapper<T>().in(field, values));
	}

	/**
	 * 使用类型安全的 lambda 表达式，根据两个字段与其值查询实体列表. 会在两个字段上分别构建等值条件。
	 * @param field1 第一个字段（lambda 指定）
	 * @param value1 第一个字段的匹配值
	 * @param field2 第二个字段（lambda 指定）
	 * @param value2 第二个字段的匹配值
	 * @return 同时满足两个条件的实体列表
	 */
	default List<T> selectList(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
		return selectList(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
	}

	/**
	 * 以空条件更新所有记录，将提供的实体中非空字段更新到表的每一条记录上.
	 * @param update 承载更新值的实体
	 * @return 受影响的行数
	 */
	default int updateBatch(T update) {
		return update(update, new QueryWrapper<>());
	}

	/**
	 * 批量更新实体集合. 使用 mybatis plus 的批量更新能力，按 ID 高效更新多条记录。
	 * @param entities 需要更新的实体集合
	 * @return 更新成功返回 true，否则返回 false
	 */
	default Boolean updateBatch(Collection<T> entities) {
		return Db.updateBatchById(entities);
	}

	/**
	 * 以指定批次大小批量更新实体集合. 使用 mybatis plus 的批量更新能力，按 ID 高效更新多条记录，并可控制批次大小。
	 * @param entities 需要更新的实体集合
	 * @param size 批次大小
	 * @return 更新成功返回 true，否则返回 false
	 */
	default Boolean updateBatch(Collection<T> entities, int size) {
		return Db.updateBatchById(entities, size);
	}

	/**
	 * 删除满足指定字段等值条件的记录.
	 * @param field 字段名
	 * @param value 匹配值
	 * @return 删除的记录条数
	 */
	default int delete(String field, String value) {
		return delete(new QueryWrapper<T>().eq(field, value));
	}

	/**
	 * 使用 lambda 指定字段，删除满足等值条件的记录.
	 * @param field 以 lambda 指定的字段
	 * @param value 匹配值
	 * @return 删除的记录条数
	 */
	default int delete(SFunction<T, ?> field, Object value) {
		return delete(new LambdaQueryWrapper<T>().eq(field, value));
	}

	/**
	 * 以指定批次大小批量插入实体集合. 使用 MyBatis Plus 的批量插入能力高效写入多条记录，并可控制批次大小。
	 * @param collections 需要插入的实体集合
	 * @param size 批次大小；在 {@link Db#saveBatch} 中默认值为 1000
	 * @return 插入成功返回 true，否则返回 false
	 */
	default Boolean insertBatch(Collection<T> collections, int size) {
		return Db.saveBatch(collections, size);
	}

	/**
	 * 根据值集合批量删除记录. 会在指定字段上构建 IN 条件；当集合为空时，不执行 SQL 并直接返回 0。
	 * @param field 以 lambda 指定的字段
	 * @param values 需要匹配删除的值集合
	 * @return 实际删除的记录条数；若集合为空返回 0
	 */
	default int deleteBatch(SFunction<T, ?> field, Collection<?> values) {
		if (CollectionUtils.isEmpty(values)) {
			return 0;
		}
		return delete(new LambdaQueryWrapper<T>().in(field, values));
	}

}
