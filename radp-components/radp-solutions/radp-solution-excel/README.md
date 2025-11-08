# radp-solution-excel

基于 FastExcel 封装的一套 Excel 导入导出解决方案，并结合本项目字典组件（radp-solution-dict）进行了增强：

- 开箱即用的导出/导入工具 ExcelUtils
- 列宽自适配（小数据量自动开启）
- 列下拉选择（@ExcelColumnSelect），支持字典类型与自定义函数两种数据源
- 字典值 ⇄ 标签 双向转换（@DictFormat + 全局 DictConvert）
- 流式大数据导出（writeLarge）避免内存高峰

## 快速开始

1. 引入依赖

```xml
<dependency>
  <groupId>space.x9x.radp</groupId>
  <artifactId>radp-solution-excel</artifactId>
</dependency>
```

2. 定义导出/导入模型

```java
@Data
public class UserExcelVO {
  @ExcelProperty("用户编号")
  private Long id;

  @ExcelProperty("性别")
  @DictFormat("gender") // 导出为“男/女”，导入时自动还原为 1/2
  private Integer gender;

  @ExcelProperty("状态")
  @ExcelColumnSelect(dictType = "status") // 自动为该列创建下拉
  @DictFormat("status")
  private String status;
}
```

3. 导出

```java
@GetMapping("/export")
public void export(HttpServletResponse response) throws IOException {
  List<UserExcelVO> list = service.listUsers();
  ExcelUtils.write(response, "用户列表.xlsx", "用户", UserExcelVO.class, list);
}
```

4. 导入

```java
@PostMapping("/import")
public List<UserExcelVO> importExcel(@RequestPart MultipartFile file) throws IOException {
  return ExcelUtils.read(file, UserExcelVO.class);
}
```

## 列下拉选择（@ExcelColumnSelect）

为某一列创建下拉选择有两种方式（二选一）：

- 使用字典：`@ExcelColumnSelect(dictType = "gender")`
  - 数据来自 `DictService.getLabels(type)`，支持 memory 与 db 两种 provider
- 使用函数：`@ExcelColumnSelect(functionName = "myOptions")`
  - 提供一个 `ExcelColumnSelectFunction` Bean：

```java
@Component
public class GenderOptions implements ExcelColumnSelectFunction {
  public String getName() { return "genderOptions"; }
  public List<String> getOptions() { return List.of("男", "女"); }
}
```

可选：可在写出时自定义下拉起止行（0 基）。默认首行为 1（即第二行），末行为 2000。

```java
ExcelUtils.write(response, "用户.xlsx", "用户", UserExcelVO.class, list, 1 /* firstRow */, 5000 /* lastRow */);
```

## 字典转换（@DictFormat）

- 在 Excel 写出时，`@DictFormat("gender")` 会把值（如 1）转换为标签（如 男）；
- 在 Excel 读入时，会把标签（如 男）转换为字段类型对应的值（如 1 / "1"）。

本组件已在 `ExcelUtils` 中全局注册 `DictConvert`，使用 `@DictFormat` 即可生效，无需手动注册 Converter。

## 大数据导出（writeLarge）

当数据量较大时，推荐使用流式导出：

```java
ExcelUtils.writeLarge(response, "用户列表.xlsx", "用户", UserExcelVO.class, list /* 大列表 */);
// 或自定义批大小与下拉首行：
ExcelUtils.writeLarge(response, "用户.xlsx", "用户", UserExcelVO.class, list, 2000, 1, 80000);
```

- 小数据量（<= 3000 行）自动开启列宽自适配；大数据量默认关闭以避免计算开销。
- 为避免在写入失败时响应头提前被修改，常规导出会先写入内存缓冲，成功后再设置响应头；流式导出则提前设置响应头并启用分块传输。

## 设计特点

- 在保持核心能力的基础上：
  - 下拉数据源除 DB 字典外，新增 memory 字典与自定义函数能力；
  - ExcelUtils 提供更健壮的响应头设置和大文件策略；
  - 全局注册 DictConvert，@DictFormat 开箱即用。

## 常见问题

- Q：`@ExcelColumnSelect` 的列索引如何确定？
  - A：若字段使用 `@ExcelProperty(index=...)` 指定了索引，则按该索引；否则按字段顺序递增。
- Q：下拉行数过多导致性能问题？
  - A：可通过 `ExcelUtils.write(..., firstRow, lastRow)` 控制下拉的行范围；默认末行为 2000。
- Q：未配置 DB Provider 时是否会报错？
  - A：不会。若 provider=db 但未提供 `DictDataQuery` Bean，会记录告警并返回空数据。

## 许可证

本项目遵循 Apache-2.0 许可证。
