package space.x9x.radp.spring.framework.dto;

import space.x9x.radp.spring.framework.error.GlobalResponseCode;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author x9x
 * @since 2024-09-26 15:53
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class Result implements Serializable {

    private boolean success;
    private String code;
    private String msg;

    public static Result buildSuccess() {
        return Result.builder()
                .code(GlobalResponseCode.SUCCESS.code())
                .msg(GlobalResponseCode.SUCCESS.message())
                .success(true)
                .build();
    }
}
