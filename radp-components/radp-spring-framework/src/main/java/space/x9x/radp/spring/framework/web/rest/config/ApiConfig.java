package space.x9x.radp.spring.framework.web.rest.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author x9x
 * @since 2024-09-30 23:20
 */
@EqualsAndHashCode
@ToString
@Setter
@Getter
public class ApiConfig {
    /**
     * API 前缀
     * 实现所有 Controller 提供的 RESTFul API 的统一前缀
     */
    private String prefix;
}
