package space.x9x.radp.spring.framework.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

/**
 * @author x9x
 * @since 2024-12-24 14:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SortablePageParam extends PageParam {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<SortingField> sortingFields;
}
