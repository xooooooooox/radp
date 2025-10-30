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

import java.util.List;
import java.util.Map;

import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.Head;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.util.MapUtils;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;

import space.x9x.radp.commons.collections.CollectionUtils;

/**
 * Excel 自适应列宽处理器. 相较于
 * {@link cn.idev.excel.write.style.column.LongestMatchColumnWidthStyleStrategy}, 额外处理了
 * Date 类型
 *
 * @author IO x9x
 * @since 2025-10-30 16:08
 * @see <a href="https://github.com/YunaiV/yudao-cloud/pull/196/">添加自适应列宽处理器，并替换默认列宽策略</a>
 */
public class ColumnWidthMatchStyleStrategy extends AbstractColumnWidthStyleStrategy {

	/**
	 * Excel 单元格最大可以写入的字符个数.
	 */
	private static final int MAX_COLUMN_WIDTH = 255;

	private final Map<Integer, Map<Integer, Integer>> cache = MapUtils.newHashMapWithExpectedSize(8);

	@Override
	protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell,
			Head head, Integer relativeRowIndex, Boolean isHead) {
		boolean needSetWidth = isHead || CollectionUtils.isNotEmpty(cellDataList);
		if (!needSetWidth) {
			return;
		}
		Map<Integer, Integer> maxColumnWidthMap = this.cache.computeIfAbsent(writeSheetHolder.getSheetNo(),
				key -> MapUtils.newHashMapWithExpectedSize(16));
		Integer columnWidth = dataLength(cellDataList, cell, isHead);
		if (columnWidth < 0) {
			return;
		}
		if (columnWidth > MAX_COLUMN_WIDTH) {
			columnWidth = MAX_COLUMN_WIDTH;
		}
		Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
		if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
			maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
			writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
		}
	}

	private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
		if (isHead) {
			return cell.getStringCellValue().getBytes().length;
		}

		WriteCellData<?> cellData = cellDataList.get(0);
		CellDataTypeEnum cellDataType = cellData.getType();
		if (cellDataType == null) {
			return -1;
		}
		switch (cellDataType) {
			case STRING:
				return cellData.getStringValue().getBytes().length;
			case NUMBER:
				return String.valueOf(cellData.getNumberValue()).getBytes().length;
			case BOOLEAN:
				return String.valueOf(cellData.getBooleanValue()).getBytes().length;
			case DATE:
				return String.valueOf(cellData.getDateValue()).getBytes().length;
			default:
				return -1;
		}
	}

}
