package space.x9x.radp.spring.framework.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author IO x9x
 * @since 2024-09-26 11:49
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TestCase {
    private Long id;
    private String username;
    private Boolean active;
}
