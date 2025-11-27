# radp-solution-dict

字典（Dict）方案组件。在保持 DB 实现的同时，额外提供开箱即用的内存（memory）实现，便于快速集成与本地开发调试。

- 支持两种数据来源：memory | db（可通过配置切换）
- 提供统一的 DictService API：值⇄标签 互转、按类型获取条目/标签
- 与 radp-solution-excel 无缝集成：支持 @DictFormat 转换、Excel 下拉（通过 @ExcelColumnSelect + DictService）

## 快速开始

1. 引入依赖（若使用 radp-solutions 聚合已包含则忽略）

```xml
<dependency>
	<groupId>space.x9x.radp</groupId>
	<artifactId>radp-solution-dict</artifactId>
</dependency>
```

2. 配置数据来源

- memory（默认）：直接在配置文件中维护字典项

```yaml
radp:
  dict:
    provider: memory
    types:
      gender:
        - { label: 男, value: "1" }
        - { label: 女, value: "2" }
      status:
        - { label: 启用, value: ENABLED }
        - { label: 停用, value: DISABLED }
```

- db：应用侧提供查询实现（见下文 DictDataQuery）

```yaml
radp:
  dict:
    provider: db
```

### DictDataQuery（DB 模式）

当 provider=db 时，需要在业务应用中提供一个 `DictDataQuery` 的 Bean，用于按类型从数据库查询字典项：

```java
@Component
public class MyDictDataQuery implements DictDataQuery {

	@Override
	public List<DictItem> getItemsByType(String type) {
		// TODO: 从你的数据表查询
		return jdbcTemplate.query("select label, value from sys_dict where type=? order by sort", rs -> {
			List<DictItem> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new DictItem(rs.getString("label"), rs.getString("value")));
			}
			return list;
		}, type);
	}

}
```

若启用了 db 但未提供 `DictDataQuery` Bean，组件会记录告警并返回空列表（不抛异常），以免影响应用启动。

## API 说明

`DictService` 提供统一访问：

```java
@Autowired
private DictService dictService;

String label = dictService.getLabel("gender", "1"); // => 男
String value = dictService.getValue("gender", "男"); // => 1
List<DictItem> items = dictService.getItems("status");
List<String> labels = dictService.getLabels("status");
```

边界约定：
- 当传入值/标签为 null 时返回 null；
- 当找不到匹配项时，返回原始入参（便于容错）。

## 与 Excel 的集成

- 在导出/导入时，可通过 `radp-solution-excel` 的 `@DictFormat` 注解完成 值⇄标签 自动转换；
- 在导出时，可通过 `@ExcelColumnSelect(dictType=...)` 自动为列生成下拉选项（数据来自本组件的 DictService）。

详见 `radp-solution-excel` 的 README 示例。

## 设计特点

- 同时支持 memory 与 db 两种 Provider，默认 memory 可开箱即用；
- 自动装配：`radp.dict.provider` 可在不同环境自由切换，不影响上层代码；
- 当无 DB 查询实现时不会阻塞启动，返回空数据并给出日志提示。

## 自动装配

- `DictAutoConfiguration` 会根据 `radp.dict.provider` 装配：
  - memory：`MemoryDictDataProvider` 从配置 `radp.dict.types` 读取
  - db：`DbDictDataProvider` 依赖应用提供的 `DictDataQuery`
- `DefaultDictService` 统一封装 API 供上层（含 Excel 模块）调用。

## 许可证

本项目遵循 Apache-2.0 许可证。
