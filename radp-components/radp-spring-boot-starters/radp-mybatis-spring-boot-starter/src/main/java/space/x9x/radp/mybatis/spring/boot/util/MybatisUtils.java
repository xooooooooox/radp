package space.x9x.radp.mybatis.spring.boot.util;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;
import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.spring.framework.dto.PageParam;
import space.x9x.radp.spring.framework.dto.PageResult;
import space.x9x.radp.spring.framework.dto.SortingField;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author x9x
 * @since 2024-12-24 15:29
 */
@UtilityClass
public class MybatisUtils {

    /**
     * Builds a MyBatis-Plus Page object from a PageParam without sorting.
     * This is a convenience method that delegates to the overloaded version with null sorting fields.
     *
     * @param <T>       the type of elements in the page
     * @param pageParam the pagination parameters
     * @return a configured MyBatis-Plus Page object
     */
    public static <T> Page<T> buildPage(PageParam pageParam) {
        return buildPage(pageParam, null);
    }

    /**
     * Builds a MyBatis-Plus Page object from a PageParam with optional sorting.
     * This method configures the page with pagination parameters and applies any sorting fields
     * by converting them to MyBatis-Plus OrderItem objects.
     *
     * @param <T> the type of elements in the page
     * @param pageParam the pagination parameters
     * @param sortingFields the collection of sorting fields to apply (can be null)
     * @return a configured MyBatis-Plus Page object with sorting applied if specified
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
     * Transforms a MyBatis-Plus IPage object into a PageResult.
     * This method extracts the records and total count from the MyBatis-Plus page
     * and creates a new PageResult with these values.
     *
     * @param <T> the type of elements in the page
     * @param mpPage the MyBatis-Plus page to transform
     * @return a PageResult containing the records and total count from the MyBatis-Plus page
     */
    public static <T> PageResult<T> transformPage(IPage<T> mpPage) {
        return PageResult.build(mpPage.getRecords(), mpPage.getTotal());
    }
}
