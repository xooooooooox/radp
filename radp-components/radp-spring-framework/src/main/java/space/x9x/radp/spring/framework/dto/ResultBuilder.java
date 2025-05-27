/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.spring.framework.dto;

import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.spring.framework.dto.extension.ResponseBuilder;
import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;

/**
 * @author IO x9x
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
			String errMessage, Object... params) {
		return Result.builder().success(false).code(errCode).msg(MessageFormatUtils.format(errMessage, params)).build();
	}

	@Override
	public Result buildFailure(ErrorCode errorCode) {
		return Result.builder().success(false).code(errorCode.getCode()).msg(errorCode.getMessage()).build();
	}

}
