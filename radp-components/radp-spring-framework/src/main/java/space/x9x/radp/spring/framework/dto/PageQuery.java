package space.x9x.radp.spring.framework.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.management.Query;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author x9x
 * @since 2024-09-26 20:35
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public abstract class PageQuery extends Query {

    public static final String ASC = "ASC";
    public static final String DESC = "DESC";
    public static final int DEFAULT_PAGE_INDEX = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MIN_PAGE_INDEX = 1;
    public static final int MIN_PAGE_SIZE = 1;
    public static final int MAX_PAGE_SIZE = 500;

    @Min(value = MIN_PAGE_INDEX, message = "页码最小值为 {value}")
    private int pageIndex = DEFAULT_PAGE_INDEX;

    @Min(value = MIN_PAGE_SIZE, message = "每页最小条数为 {value}")
    @Max(value = MAX_PAGE_SIZE, message = "每页最大条数为 {value}")
    private int pageSize = DEFAULT_PAGE_SIZE;

    private String orderBy;
    private String orderDirection = DESC;
    private String groupBy;

    public void setOrderDirection(String orderDirection) {
        if (ASC.equalsIgnoreCase(orderDirection) || DESC.equalsIgnoreCase(orderDirection)) {
            this.orderDirection = orderDirection;
        }
    }
}
