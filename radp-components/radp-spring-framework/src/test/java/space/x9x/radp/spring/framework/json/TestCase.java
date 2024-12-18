package space.x9x.radp.spring.framework.json;

import lombok.*;

/**
 * @author x9x
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
