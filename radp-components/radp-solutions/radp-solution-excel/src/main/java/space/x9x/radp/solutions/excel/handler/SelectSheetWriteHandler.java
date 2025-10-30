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

package space.x9x.radp.solutions.excel.handler;

import cn.idev.excel.write.handler.SheetWriteHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于固定 sheet 实现下拉框.
 *
 * @author IO x9x
 * @since 2025-10-30 16:59
 */
@Slf4j
public class SelectSheetWriteHandler implements SheetWriteHandler {

	/**
	 * 数据起始行默认从 1 开始.
	 *
	 * 约定: excel 第一行为标题行, 所以数据起始行从 1 开始; 如果有多行标题, 则根据实际进行调整
	 */
	public static final int FIRST_ROW = 1;

}
