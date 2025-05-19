package space.x9x.radp.spring.framework.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author x9x
 * @since 2024-09-26 20:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class PageParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Default page index (first page).
     */
    public static final int DEFAULT_PAGE_INDEX = 1;

    /**
     * Default number of items per page.
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * Minimum allowed page index.
     */
    public static final int MIN_PAGE_INDEX = 1;

    /**
     * Minimum allowed page size.
     */
    public static final int MIN_PAGE_SIZE = 1;

    /**
     * Maximum allowed page size to prevent excessive data loading.
     */
    public static final int MAX_PAGE_SIZE = 500;

    /**
     * Special value indicating that pagination should not be applied.
     */
    public static final int PAGE_SIZE_NONE = -1;

    /**
     * The current page index (1-based).
     * Must be at least MIN_PAGE_INDEX.
     */
    @Min(value = MIN_PAGE_INDEX, message = "页码最小值为 {value}")
    private Integer pageIndex = DEFAULT_PAGE_INDEX;

    /**
     * The number of items per page.
     * Must be between MIN_PAGE_SIZE and MAX_PAGE_SIZE.
     */
    @Min(value = MIN_PAGE_SIZE, message = "每页最小条数为 {value}")
    @Max(value = MAX_PAGE_SIZE, message = "每页最大条数为 {value}")
    private Integer pageSize = DEFAULT_PAGE_SIZE;
}
