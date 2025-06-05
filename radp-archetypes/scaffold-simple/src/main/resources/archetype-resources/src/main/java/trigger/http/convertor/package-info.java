/**
 * 1. 在这里定义 Entity {@literal <->} DTO 转换器 2. 命名 {@code IXxxEntityConvertor} 3. 示例
 * <pre><code>
 * import org.mapstruct.Mapper;
 *
 * &#64;Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
 * public interface IStrategyAwardEntityConvertor extends BaseConvertor&lt;StrategyAwardEntity, RaffleAwardListResponseDTO&gt; {
 * }
 * </code></pre>
 */
package ${package}.trigger.http.convertor;
