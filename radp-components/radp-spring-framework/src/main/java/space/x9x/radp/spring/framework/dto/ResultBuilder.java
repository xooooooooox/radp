package space.x9x.radp.spring.framework.dto;

import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;
import space.x9x.radp.spring.framework.web.extension.ResponseBuilder;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author x9x
 * @since 2024-09-26 20:19
 */
public class ResultBuilder implements ResponseBuilder<Result> {
    @Override
    public Result buildSuccess() {
        return Result.buildSuccess();
    }

    @Override
    public <T> Result buildSuccess(T data) {
        if (data == null) {
            return new Result();
        }
        return SingleResult.build(data);
    }

    @Override
    public Result buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                               Object... params) {
        return Result.builder()
                .success(false)
                .code(errCode)
                .msg(ErrorCodeLoader.getErrMessage(errCode, params))
                .build();
    }

    @Override
    public Result buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                               String errMessage,
                               Object... params) {
        return Result.builder()
                .success(false)
                .code(errCode)
                .msg(MessageFormatUtils.format(errMessage, params))
                .build();
    }

    @Override
    public Result buildFailure(ErrorCode errorCode) {
        return Result.builder()
                .success(false)
                .code(errorCode.getCode())
                .msg(errorCode.getMessage())
                .build();
    }
}
