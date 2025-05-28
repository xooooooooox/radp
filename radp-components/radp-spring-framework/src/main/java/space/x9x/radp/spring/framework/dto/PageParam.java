package space.x9x.radp.spring.framework.dto;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Parameter object for pagination requests that contains page index and page size
 * information. This class provides default values and validation constraints to ensure
 * proper pagination behavior.
 *
 * @author IO x9x
 * @since 2024-09-26 20:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class PageParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Default page index value (1-based indexing).
     */
    public static final int DEFAULT_PAGE_INDEX = 1;

    /**
     * Default number of items per page.
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * Minimum allowed page index value.
     */
    public static final int MIN_PAGE_INDEX = 1;

    /**
     * Minimum allowed page size value.
     */
    public static final int MIN_PAGE_SIZE = 1;

    /**
     * Maximum allowed page size value to prevent excessive data loading.
     */
    public static final int MAX_PAGE_SIZE = 500;

    /**
     * Special value indicating that pagination should be disabled (return all results).
     */
    public static final int PAGE_SIZE_NONE = -1;

    /**
     * The current page number (1-based indexing).
     * Must be at least {@link #MIN_PAGE_INDEX}.
     */
    @Min(value = MIN_PAGE_INDEX, message = "页码最小值为 {value}")
    private Integer pageIndex = DEFAULT_PAGE_INDEX;

    /**
     * The number of items to display per page.
     * Must be between {@link #MIN_PAGE_SIZE} and {@link #MAX_PAGE_SIZE},
     * or can be {@link #PAGE_SIZE_NONE} to disable pagination.
     */
    @Min(value = MIN_PAGE_SIZE, message = "每页最小条数为 {value}")
    @Max(value = MAX_PAGE_SIZE, message = "每页最大条数为 {value}")
    private Integer pageSize = DEFAULT_PAGE_SIZE;
}
