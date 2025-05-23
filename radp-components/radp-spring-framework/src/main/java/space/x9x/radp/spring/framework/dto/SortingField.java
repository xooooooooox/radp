package space.x9x.radp.spring.framework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author x9x
 * @since 2024-12-24 14:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortingField implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    /**
     * 字段
     */
    private String field;

    /**
     * 排序
     */
    private String order;
}
