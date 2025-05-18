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
 * Utility class for MyBatis operations.
 * This class provides helper methods for common MyBatis operations,
 * particularly for pagination and result transformation.
 *
 * @author x9x
 * @since 2024-12-24 15:29
 */
@UtilityClass
public class MybatisUtils {

    /**
     * Creates a MyBatis-Plus Page object from a PageParam.
     * This method delegates to the overloaded method with null sorting fields.
     *
     * @param <T>       the type of elements in the page
     * @param pageParam the pagination parameters
     * @return a configured MyBatis-Plus Page object
     */
    public static <T> Page<T> buildPage(PageParam pageParam) {
        return buildPage(pageParam, null);
    }

    /**
     * Creates a MyBatis-Plus Page object from a PageParam with optional sorting.
     * This method converts the framework's pagination and sorting parameters
     * to MyBatis-Plus specific pagination objects.
     *
     * @param <T> the type of elements in the page
     * @param pageParam the pagination parameters
     * @param sortingFields the collection of fields to sort by, or null if no sorting is needed
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
     * Transforms a MyBatis-Plus IPage result to the framework's PageResult.
     * This method converts pagination results from MyBatis-Plus format to the
     * application's standard format.
     *
     * @param <T> the type of elements in the page
     * @param mpPage the MyBatis-Plus page result
     * @return a PageResult containing the same data and total count
     */
    public static <T> PageResult<T> transformPage(IPage<T> mpPage) {
        return PageResult.build(mpPage.getRecords(), mpPage.getTotal());
    }
}
