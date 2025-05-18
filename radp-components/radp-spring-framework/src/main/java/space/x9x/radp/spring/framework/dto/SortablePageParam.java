package space.x9x.radp.spring.framework.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

/**
 * Extended pagination parameter object that adds sorting capabilities.
 * This class extends PageParam to allow specifying sort fields and directions.
 *
 * @author x9x
 * @since 2024-12-24 14:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SortablePageParam extends PageParam {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * List of fields to sort by, with their respective sort directions
     */
    private List<SortingField> sortingFields;
}
