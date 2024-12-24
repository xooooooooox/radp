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

    public static <T> Page<T> buildPage(PageParam pageParam) {
        return buildPage(pageParam, null);
    }

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

    public static <T> PageResult<T> transformPage(IPage<T> mpPage) {
        return PageResult.build(mpPage.getRecords(), mpPage.getTotal());
    }
}
