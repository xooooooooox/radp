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

package space.x9x.radp.mybatis.spring.boot.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;

import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.StringConstants;
import space.x9x.radp.commons.lang.StringUtil;
import space.x9x.radp.spring.framework.dto.PageParam;
import space.x9x.radp.spring.framework.dto.PageResult;
import space.x9x.radp.spring.framework.dto.SortingField;

/**
 * MyBatis 操作的工具类. 提供分页构建、排序转换与分页结果转换等常用辅助方法。
 *
 * <p>
 * 主要能力包括：
 * <ul>
 * <li>将分页参数 {@link PageParam} 转为 MyBatis-Plus 的 {@link Page}；</li>
 * <li>将排序字段 {@link SortingField} 转为 MyBatis-Plus 的 {@link OrderItem} 并应用到分页对象；</li>
 * <li>将查询结果 {@link IPage} 转为框架统一的 {@link PageResult}。</li>
 * </ul>
 *
 * @author RADP x9x
 * @since 2024-12-24 15:29
 */
@UtilityClass
public class MybatisUtils {

	/**
	 * 根据分页参数构建不带排序的 MyBatis-Plus Page 对象. 这是一个便捷方法，会委托给带排序参数的重载版本并传入 null 排序字段。
	 * @param <T> 分页中元素的类型
	 * @param pageParam 分页参数
	 * @return 已配置完成的 MyBatis-Plus Page 对象
	 */
	public static <T> Page<T> buildPage(PageParam pageParam) {
		return buildPage(pageParam, null);
	}

	/**
	 * 根据分页参数构建可选排序的 MyBatis-Plus Page 对象. 会依据分页参数设置页码与大小，并将给定的排序字段转换为 {@link OrderItem}
	 * 后应用到分页对象。
	 * @param <T> 分页中元素的类型
	 * @param pageParam 分页参数
	 * @param sortingFields 需要应用的排序字段集合（可为 null）
	 * @return 已配置完成的 MyBatis-Plus Page 对象；若提供排序字段则已应用排序
	 */
	@SuppressWarnings("java:S3252")
	public static <T> Page<T> buildPage(PageParam pageParam, Collection<SortingField> sortingFields) {
		// transform to MybatisPlus Page
		Page<T> page = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
		// sorting
		if (!CollectionUtils.isEmpty(sortingFields)) {
			page.addOrder(sortingFields.stream()
				.map(sortingField -> SortingField.ASC.equals(sortingField.getOrder())
						? OrderItem.asc(StrUtil.toUnderlineCase(sortingField.getField()))
						: OrderItem.desc(StrUtil.toUnderlineCase(sortingField.getField())))
				.collect(Collectors.toList()));
		}
		return page;
	}

	/**
	 * 将 MyBatis-Plus 的 IPage 转换为 PageResult. 会从分页对象中提取记录列表与总条数并构建统一的分页结果。
	 * @param <T> 分页中元素的类型
	 * @param mpPage 需要转换的 MyBatis-Plus 分页对象
	 * @return 包含记录列表与总条数的 PageResult
	 */
	public static <T> PageResult<T> transformPage(IPage<T> mpPage) {
		return PageResult.ok(mpPage.getRecords(), mpPage.getTotal());
	}

	/**
	 * 为给定的查询 wrapper 追加排序条件. 支持 {@link QueryWrapper} 与 {@link LambdaQueryWrapper}； 当为
	 * {@link LambdaQueryWrapper} 时，通过 {@code last} 方法拼接 ORDER BY 子句。
	 * @param <T> 实体类型
	 * @param wrapper 查询条件包装器
	 * @param sortingFields 需要应用的排序字段列表；为空或为 null 时不处理
	 * @throws IllegalArgumentException 当传入的 wrapper 类型不受支持时抛出
	 */
	public static <T> void addOrder(Wrapper<T> wrapper, List<SortingField> sortingFields) {
		if (CollectionUtils.isEmpty(sortingFields)) {
			return;
		}

		if (wrapper instanceof QueryWrapper) {
			QueryWrapper<T> queryWrapper = (QueryWrapper<T>) wrapper;
			sortingFields
				.forEach(sortingField -> queryWrapper.orderBy(true, sortingField.getOrder().equals(SortingField.ASC),
						StrUtil.toUnderlineCase(sortingField.getField())));
		}
		else if (wrapper instanceof LambdaQueryWrapper<T> lambdaQueryWrapper) {
			// LambdaQueryWrapper 不直接支持字符串字段排序, 使用 #last 方法拼接 orderBy
			StringBuilder orderBy = new StringBuilder();
			sortingFields.forEach(sortingField -> {
				if (StringUtil.isNotEmpty(orderBy)) {
					orderBy.append(StringConstants.COMMA);
					orderBy.append(StringConstants.SPACE);
				}
				orderBy.append(StrUtil.toUnderlineCase(sortingField.getField()))
					.append(StringConstants.SPACE)
					.append(SortingField.ASC.equals(sortingField.getOrder()) ? SortingField.ASC : SortingField.DESC);
			});
			lambdaQueryWrapper.last("ORDER BY " + orderBy);
		}
		else {
			throw new IllegalArgumentException("Unsupported wrapper type: " + wrapper.getClass().getName());
		}
	}

}
