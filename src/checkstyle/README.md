# Checkstyle.xml 配置详解

这个 checkstyle.xml 文件是一个代码风格检查配置，基于 Spring 项目的代码规范。下面我将详细解释每个配置的作用：

## 顶层配置

```xml
<module name="com.puppycrawl.tools.checkstyle.Checker">
```

`Checker` 是 Checkstyle 的顶级模块，所有其他检查模块都是它的子模块。

## 抑制过滤器

```xml
<module name="SuppressionFilter">
	<property name="file" value="${config_loc}/checkstyle-suppressions.xml"/>
</module>
```

- 用于指定哪些文件或代码可以忽略特定的检查规则
- `${config_loc}` 是配置文件所在目录的路径
- 引用了一个名为 `checkstyle-suppressions.xml` 的文件，该文件定义了需要忽略的检查规则

### checkstyle-suppressions.xml 文件详解

checkstyle-suppressions.xml 文件定义了哪些文件或代码可以忽略特定的检查规则。以下是主要的抑制规则：

1. **生成的源代码**：
   ```xml
   <suppress files="[\\/]build[\\/]generated[\\/]sources[\\/]" checks=".*"/>
   ```
   - 忽略所有生成的源代码文件的所有检查

2. **package-info.java 文件**：
   ```xml
   <suppress files="(^(?!.+[\\/]src[\\/]main[\\/]java[\\/]).*)|(.*framework-docs.*)" checks="JavadocPackage"/>
   ```
   - 对非主源代码目录和框架文档相关的文件忽略 JavadocPackage 检查

3. **测试和测试固件**：
   ```xml
   <suppress files="[\\/]src[\\/](test|testFixtures)[\\/](java|java21)[\\/]"
             checks="AnnotationLocation|AnnotationUseStyle|AtclauseOrder|AvoidNestedBlocks|FinalClass|HideUtilityClassConstructor|InnerTypeLast|JavadocStyle|JavadocType|JavadocVariable|LeftCurly|MultipleVariableDeclarations|NeedBraces|OneTopLevelClass|OuterTypeFilename|RequireThis|SpringCatch|SpringJavadoc|SpringNoThis"/>
   ```
   - 对测试和测试固件目录下的文件忽略多种检查，包括注解位置、Javadoc 样式、代码块等

4. **应用程序类**：
   ```xml
   <suppress files=".+Application\.java" checks="HideUtilityClassConstructor"/>
   ```
   - 对应用程序类忽略工具类构造函数检查

5. **特定组件的特定文件**：
   - radp-spring-cloud 组件：
     ```xml
     <suppress files="DubboEnvironment\.java" checks="HideUtilityClassConstructor"/>
     ```
   - radp-spring-test 组件：
     ```xml
     <suppress files="EmbeddedServerHelper\.java" checks="HideUtilityClassConstructor"/>
     ```
   - radp-spring-boot 组件：
     ```xml
     <suppress files="ManagementServerEnvironment\.java" checks="HideUtilityClassConstructor"/>
     <suppress files="Conditions\.java" checks="HideUtilityClassConstructor"/>
     <suppress files="SpringBootProfileUtils\.java" checks="HideUtilityClassConstructor"/>
     <suppress files="DatasourceEnvironment\.java" checks="HideUtilityClassConstructor"/>
     <suppress files="WebServerEnvironment\.java" checks="HideUtilityClassConstructor"/>
     <suppress files="MybatisUtils\.java" checks="HideUtilityClassConstructor"/>
     <suppress files="RedisKeyConstants\.java" checks="HideUtilityClassConstructor"/>
     <suppress files="DubboAttachments\.java" checks="HideUtilityClassConstructor"/>
     ```

这些抑制规则确保了特定文件或目录可以忽略某些检查，从而避免不必要的警告或错误。

## 根级别检查

```xml
<module name="io.spring.javaformat.checkstyle.check.SpringHeaderCheck">
	<property name="fileExtensions" value="java"/>
	<property name="headerType" value="apache2"/>
	<property name="headerCopyrightPattern" value="20\d\d-20\d\d"/>
	<property name="packageInfoHeaderType" value="none"/>
</module>
```

- 检查 Java 文件的文件头是否符合 Spring 项目的标准
- 使用 Apache 2.0 许可证格式
- 版权年份格式必须是 "20xx-20xx" 格式
- `package-info.java` 文件不需要头部注释

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.NewlineAtEndOfFileCheck"/>
```

- 确保每个文件末尾有一个空行

```xml
<module name="JavadocPackage"/>
```

- 检查每个包是否有对应的 `package-info.java` 文件

## TreeWalker 检查

`TreeWalker` 是一个特殊的模块，它解析 Java 源文件并创建抽象语法树，然后其子模块可以检查这个语法树的各个部分。

### 注解检查

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck">
	<property name="elementStyle" value="compact"/>
</module>
```

- 检查注解的使用风格，要求使用紧凑格式

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.annotation.MissingOverrideCheck"/>
```

- 检查重写父类方法时是否使用了 `@Override` 注解

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.annotation.PackageAnnotationCheck"/>
```

- 确保包注解在 `package-info.java` 文件中

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationLocationCheck"/>
```

- 检查注解的位置是否正确

### 代码块检查

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.blocks.EmptyBlockCheck">
	<property name="option" value="text"/>
</module>
```

- 检查空代码块，要求空块中必须有注释说明

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.blocks.LeftCurlyCheck"/>
```

- 检查左花括号的位置

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.blocks.RightCurlyCheck">
	<property name="option" value="alone"/>
</module>
```

- 检查右花括号的位置，要求单独一行

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.blocks.NeedBracesCheck"/>
```

- 检查条件语句是否使用了花括号

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.blocks.AvoidNestedBlocksCheck"/>
```

- 避免不必要的嵌套代码块

### 类设计检查

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.design.FinalClassCheck"/>
```

- 如果类的构造函数是私有的，则该类应该被声明为 final

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.design.InterfaceIsTypeCheck"/>
```

- 确保接口不仅仅是常量的容器

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.design.HideUtilityClassConstructorCheck"/>
```

- 工具类（只有静态方法的类）应该有私有构造函数

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.design.MutableExceptionCheck">
	<property name="format" value="^.*Exception$"/>
</module>
```

- 异常类应该是不可变的

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.design.InnerTypeLastCheck"/>
```

- 内部类应该在类的最后定义

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.design.OneTopLevelClassCheck"/>
```

- 每个 Java 文件只能有一个顶级类

### 类型命名检查

```xml
<module name="TypeName">
	<property name="format" value="^[A-Z][a-zA-Z0-9_$]*(?&lt;!Test)$"/>
	<property name="tokens" value="CLASS_DEF"/>
	<message key="name.invalidPattern"
			 value="Class name ''{0}'' must not end with ''Test'' (checked pattern ''{1}'')."/>
</module>
```

- 检查类名格式，必须以大写字母开头，不能以 "Test" 结尾

### 编码规范检查

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.coding.CovariantEqualsCheck"/>
```

- 检查是否正确重写了 equals 方法

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.coding.EmptyStatementCheck"/>
```

- 检查空语句（如单独的分号）

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.coding.EqualsHashCodeCheck"/>
```

- 如果重写了 equals 方法，也必须重写 hashCode 方法

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.coding.SimplifyBooleanExpressionCheck"/>
```

- 简化布尔表达式

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.coding.SimplifyBooleanReturnCheck"/>
```

- 简化布尔返回语句

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.coding.StringLiteralEqualityCheck"/>
```

- 字符串比较应使用 equals() 而不是 == 或 !=

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.coding.NestedForDepthCheck">
	<property name="max" value="3"/>
</module>
```

- 嵌套 for 循环最多 3 层

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.coding.NestedIfDepthCheck">
	<property name="max" value="5"/>
</module>
```

- 嵌套 if 语句最多 5 层

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.coding.NestedTryDepthCheck">
	<property name="max" value="3"/>
</module>
```

- 嵌套 try 块最多 3 层

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.coding.MultipleVariableDeclarationsCheck"/>
```

- 禁止在一行中声明多个变量

```xml
<module name="io.spring.javaformat.checkstyle.filter.RequiresOuterThisFilter"/>
```

- 检查对外部类成员的引用是否使用了 this 限定符

```xml
<module name="io.spring.javaformat.checkstyle.filter.IdentCheckFilter">
	<property name="names" value="logger"/>
	<module name="com.puppycrawl.tools.checkstyle.checks.coding.RequireThisCheck">
		<property name="checkMethods" value="false"/>
		<property name="validateOnlyOverlapping" value="false"/>
	</module>
</module>
```

- 对名为 "logger" 的字段应用特殊的 this 检查规则

```xml
<module name="io.spring.javaformat.checkstyle.check.SpringNoThisCheck">
	<property name="names" value="logger"/>
</module>
```

- 确保对 logger 字段的引用不使用 this 限定符

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.coding.OneStatementPerLineCheck"/>
```

- 每行只允许一条语句

### 导入检查

```xml
<module name="AvoidStarImport"/>
```

- 避免使用星号导入（如 `import java.util.*`）

```xml
<module name="UnusedImports"/>
```

- 检查未使用的导入

```xml
<module name="RedundantImport"/>
```

- 检查冗余导入

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.imports.ImportOrderCheck">
	<property name="groups" value="java,javax,org.springframework,*,space.x9x"/>
	<property name="ordered" value="true"/>
	<property name="separated" value="true"/>
	<property name="option" value="bottom"/>
	<property name="sortStaticImportsAlphabetically" value="true"/>
</module>
```

- 导入顺序规则：先 java 包，然后 javax 包，然后 Spring 包，然后其他包，最后是 space.x9x 包
- 每组之间要有空行
- 静态导入放在最后并按字母顺序排序

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.imports.IllegalImportCheck">
	<!-- 多个禁止导入的检查 -->
</module>
```

- 禁止导入特定的类，如 JUnit 3/4、TestNG、Hamcrest 等

### Javadoc 注释检查

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocTypeCheck">
	<property name="scope" value="package"/>
	<property name="authorFormat" value=".+\s.+"/>
</module>
```

- 检查类型（类、接口等）的 Javadoc
- @author 标签必须包含至少两个单词

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocMethodCheck">
	<property name="allowMissingParamTags" value="true"/>
	<property name="allowMissingReturnTag" value="true"/>
</module>
```

- 检查方法的 Javadoc，允许缺少参数和返回值标签

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocVariableCheck">
</module>
```

- 检查变量的 Javadoc

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocStyleCheck">
	<property name="checkEmptyJavadoc" value="true"/>
</module>
```

- 检查 Javadoc 的样式，不允许空的 Javadoc

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.javadoc.NonEmptyAtclauseDescriptionCheck"/>
```

- 确保 Javadoc 标签（如 @param, @return 等）有描述内容

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocTagContinuationIndentationCheck">
	<property name="offset" value="0"/>
</module>
```

- 检查 Javadoc 标签续行的缩进

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.javadoc.AtclauseOrderCheck">
	<property name="target" value="CLASS_DEF, INTERFACE_DEF, ENUM_DEF, RECORD_DEF"/>
	<property name="tagOrder" value="@author, @since, @param, @see, @version, @serial, @deprecated"/>
</module>
```

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.javadoc.AtclauseOrderCheck">
	<property name="target" value="METHOD_DEF, CTOR_DEF, VARIABLE_DEF"/>
	<property name="tagOrder" value="@param, @return, @throws, @since, @see, @deprecated"/>
</module>
```

- 检查 Javadoc 标签的顺序

### 修饰符检查

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.modifier.ModifierOrderCheck"/>
```

- 检查修饰符的顺序（如 public static final 而不是 static public final）

### 空白字符检查

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.whitespace.MethodParamPadCheck"/>
```

- 检查方法参数周围的空格

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.whitespace.SingleSpaceSeparatorCheck"/>
```

- 确保单词之间只有一个空格

### 其他检查

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.indentation.CommentsIndentationCheck">
	<property name="tokens" value="BLOCK_COMMENT_BEGIN"/>
</module>
```

- 检查注释的缩进

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.UpperEllCheck"/>
```

- 确保长整型字面量使用大写的 L 而不是小写的 l

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.ArrayTypeStyleCheck"/>
```

- 检查数组类型的声明风格（如 `String[] args` 而不是 `String args[]`）

```xml
<module name="com.puppycrawl.tools.checkstyle.checks.OuterTypeFilenameCheck"/>
```

- 确保文件名与其中的顶级类名匹配

```xml
<module name="UnnecessarySemicolonInEnumeration"/>
```

- 检查枚举中不必要的分号

### 正则表达式检查

配置中包含多个正则表达式检查，用于：

- 确保使用制表符而不是空格进行缩进
- 检查行尾空白字符
- 检查分隔符符号的位置
- 检查 @since 标签的版本格式
- 禁止使用 System.out 和 System.err 打印
- 强制使用 AssertJ 断言而不是 JUnit 或 TestNG 断言

### Spring 特定约定

```xml
<module name="io.spring.javaformat.checkstyle.check.SpringLambdaCheck">
	<property name="singleArgumentParentheses" value="false"/>
</module>
```

- Spring 特定的 Lambda 表达式格式检查，单参数 Lambda 不需要括号

```xml
<module name="io.spring.javaformat.checkstyle.check.SpringCatchCheck"/>
```

- Spring 特定的异常捕获检查

```xml
<module name="io.spring.javaformat.checkstyle.check.SpringJavadocCheck">
	<property name="publicOnlySinceTags" value="true"/>
	<property name="requireSinceTag" value="true"/>
</module>
```

- Spring 特定的 Javadoc 检查
- 要求公共 API 必须有 @since 标签

```xml
<module name="io.spring.javaformat.checkstyle.check.SpringJUnit5Check"/>
```

- Spring 特定的 JUnit 5 使用检查

## 总结

这个 checkstyle.xml 配置文件实施了 Spring 项目的代码规范，涵盖了从代码格式、命名约定、Javadoc
注释到编码实践的各个方面。它确保代码库保持一致的风格和高质量的标准。