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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.write.handler.SheetWriteHandler;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;

import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.ObjectUtil;
import space.x9x.radp.commons.lang.StringUtil;
import space.x9x.radp.solutions.dict.core.DictService;
import space.x9x.radp.solutions.excel.annotations.ExcelColumnSelect;
import space.x9x.radp.solutions.excel.function.ExcelColumnSelectFunction;

/**
 * Excel下拉框写入处理器.
 * <p>
 * 该处理器通过在固定sheet中创建数据源来实现Excel单元格下拉选择功能
 *
 * @author x9x
 * @since 2025-10-30 16:59
 */
@Slf4j
public class SelectSheetWriteHandler implements SheetWriteHandler {

	/**
	 * 数据起始行默认从 1 开始.
	 * <p>
	 * 约定: excel 第一行为标题行, 所以数据起始行从 1 开始(0为基); 如果有多行标题, 则根据实际进行调整
	 */
	public static final int FIRST_ROW = 1;

	/**
	 * 下拉列表需要创建下拉框的函数. (默认 2000 行)
	 */
	public static final int LAST_ROW = 2000;

	/**
	 * 字典表单名称常量.
	 * <p>
	 * 用于存储下拉选项数据的 Excel 工作表名称
	 */
	private static final String DICT_SHEET_NAME = "dict_sheet";

	/**
	 * 下拉选项数据映射.
	 * <p>
	 * key: Excel列索引 value: 该列的下拉选项列表
	 */
	private final Map<Integer, List<String>> selectMap = new HashMap<>();

	/**
	 * 数据起始行（用于设置下拉验证的首行）。未指定时默认为 {@link #FIRST_ROW}.
	 */
	private final int firstRow;

	/**
	 * 数据终止行（用于设置下拉验证的末行）。未指定时默认为 {@link #LAST_ROW}.
	 */
	private final int lastRow;

	/**
	 * 使用给定表头类型创建选择下拉写入处理器，采用默认的数据起止行范围.
	 * <p>
	 * 默认起始行为 {@link #FIRST_ROW}，默认终止行为 {@link #LAST_ROW}。
	 * </p>
	 * @param head 表头 class
	 */
	public SelectSheetWriteHandler(Class<?> head) {
		this.firstRow = FIRST_ROW; // 默认起始行为 1（第二行，第一行为标题）
		this.lastRow = LAST_ROW; // 默认终止行为 2000 行
		// 解析下拉数据
		parseHead(head);
	}

	private void parseHead(Class<?> head) {
		int colIndex = 0;
		boolean ignoreUnannotated = head.isAnnotationPresent(ExcelIgnoreUnannotated.class);
		for (Field field : head.getDeclaredFields()) {
			// 关联 https://github.com/YunaiV/ruoyi-vue-pro/pull/853
			// 1.1 忽略 static final 或 transient 的字段
			if (isStaticFinalOrTransient(field)) {
				continue;
			}
			// 1.2 忽略的字段跳过
			if ((ignoreUnannotated && !field.isAnnotationPresent(ExcelProperty.class))
					|| field.isAnnotationPresent(ExcelIgnore.class)) {
				continue;
			}
			// 2. 核心: 处理有 ExcelColumnSelect 注解的字段
			if (field.isAnnotationPresent(ExcelColumnSelect.class)) {
				ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
				if (excelProperty != null && excelProperty.index() != -1) {
					colIndex = excelProperty.index();
				}
				getSelectDataList(colIndex, field);
			}
			colIndex++;
		}
	}

	/**
	 * 支持自定义下拉验证的起始行.
	 * @param head 表头 class
	 * @param firstRow 下拉验证的首行（0 基，若传入 null 或 &lt;0 则回退到 {@link #FIRST_ROW}）
	 */
	public SelectSheetWriteHandler(Class<?> head, Integer firstRow) {
		this(head, firstRow, null);
	}

	/**
	 * 支持自定义下拉验证的起始行与终止行.
	 * @param head 表头 class
	 * @param firstRow 下拉验证的首行（0 基，若传入 null 或 &lt;0 则回退到 {@link #FIRST_ROW}）
	 * @param lastRow 下拉验证的末行（0 基，若传入 null、&lt;0 或 &lt; firstRow 则回退到 {@link #LAST_ROW}）
	 */
	public SelectSheetWriteHandler(Class<?> head, Integer firstRow, Integer lastRow) {
		this.firstRow = (firstRow == null || firstRow < 0) ? FIRST_ROW : firstRow;
		this.lastRow = (lastRow == null || lastRow < 0 || lastRow < this.firstRow) ? LAST_ROW : lastRow;
		// 解析下拉数据
		parseHead(head);
	}

	private void getSelectDataList(int colIndex, Field field) {
		ExcelColumnSelect columnSelect = field.getAnnotation(ExcelColumnSelect.class);
		String dictType = columnSelect.dictType();
		String functionName = columnSelect.functionName();
		Assert.isTrue(ObjectUtil.isNotEmpty(dictType) || ObjectUtil.isNotEmpty(functionName),
				"Field({}) 的 @ExcelColumnSelect 注解, dictType 和 functionName 不能同时为空", field.getName());

		// 情况一: 使用 dictType 获取下拉数据
		if (StringUtil.isNotEmpty(dictType)) { // 字典数据(默认)
			DictService dictService = SpringUtil.getBean(DictService.class);
			List<String> options = dictService.getLabels(dictType);
			this.selectMap.put(colIndex, options);
			return;
		}

		// 情况二: 使用 functionName 获取下拉数据
		Map<String, ExcelColumnSelectFunction> functionMap = SpringUtil.getApplicationContext()
			.getBeansOfType(ExcelColumnSelectFunction.class);
		ExcelColumnSelectFunction function = CollUtil.findOne(functionMap.values(),
				item -> item.getName().equals(functionName));
		Assert.notNull(function, "未找到对应的 function({})", functionName);
		List<String> options = function.getOptions();
		if (options == null) {
			options = Collections.emptyList();
		}
		this.selectMap.put(colIndex, options);
	}

	/**
	 * 判断字段是否为 static final 或者 transient.
	 * <p>
	 * 原因: FastExcel 默认是忽略 static final 或 transient 的字段, 所以需要判断
	 * @param field 字段
	 * @return 是否为 static final 或者 transient
	 */
	private boolean isStaticFinalOrTransient(Field field) {
		return (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
				|| Modifier.isTransient(field.getModifiers());
	}

	@Override
	// @formatter:off
	public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
		if (CollUtil.isEmpty(this.selectMap)) {
			return;
		}

		// 1. 获取响应操作对象
		DataValidationHelper dataValidationHelper = writeSheetHolder.getSheet().getDataValidationHelper(); // 需要设置下拉框的 sheet 页的数据验证助手
		Workbook workbook = writeWorkbookHolder.getWorkbook(); // 获得工作簿
		List<Entry<Integer, List<String>>> entries = CollectionUtils.convertList(this.selectMap.entrySet(),
				entry -> entry);
		entries.sort(Comparator.comparing(entry -> entry.getValue().size())); // 升序不然创建下拉会报错

		// 2. 创建数据字典的 sheet 页
		Sheet dictSheet = workbook.createSheet(DICT_SHEET_NAME);
		for (Map.Entry<Integer, List<String>> entry : entries) {
			Integer colIndex = entry.getKey();
			List<String> values = entry.getValue();
			int rowLength = values == null ? 0 : values.size();
			if (rowLength == 0) {
				// 该列无下拉数据，跳过
				continue;
			}
			// 2.1 设置字典 sheet 页的值, 每列一个字典项
			for (int i = 0; i < rowLength; i++) {
				Row row = dictSheet.getRow(i);
				if (row == null) {
					row = dictSheet.createRow(i);
				}
				row.createCell(colIndex).setCellValue(values.get(i));
			}
			// 2.2 设置单元格下拉选择
			setColumnSelect(writeSheetHolder, workbook, dataValidationHelper, entry);
		}
	}
	// @formatter:on

	private void setColumnSelect(WriteSheetHolder writeSheetHolder, Workbook workbook,
			DataValidationHelper dataValidationHelper, Map.Entry<Integer, List<String>> entry) {
		int colIndex = entry.getKey();
		int rowLength = entry.getValue().size();

		// 1.1 创建可被其他单元格引用的名称
		Name name = workbook.createName();
		String excelColumn = ExcelUtil.indexToColName(colIndex);
		// 1.2 下拉框数据来源 e.g. dict_sheet!$B1:$B2
		String refers = String.format("%s!$%s$1:$%s$%d", DICT_SHEET_NAME, excelColumn, excelColumn, rowLength);
		name.setNameName(String.format("dict%s", colIndex)); // 设置名称的名字
		name.setRefersToFormula(refers); // 设置公式

		// 2.1 设置约束
		DataValidationConstraint constraint = dataValidationHelper
			.createFormulaListConstraint(String.format("dict%s", colIndex));// 设置引用约束
		// 设置下拉单元格的首行,末行,首列,末列
		CellRangeAddressList rangeAddressList = new CellRangeAddressList(this.firstRow, this.lastRow, colIndex,
				colIndex);
		DataValidation validation = dataValidationHelper.createValidation(constraint, rangeAddressList);
		if (validation instanceof HSSFDataValidation) {
			validation.setSuppressDropDownArrow(false);
		}
		else {
			validation.setSuppressDropDownArrow(true);
			validation.setShowErrorBox(true);
		}
		// 2.2 阻止输入非下拉框的值
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("提示", "该值不存在于下拉选择中!");
		// 2.3 添加下拉框约束
		writeSheetHolder.getSheet().addValidationData(validation);
	}

}
