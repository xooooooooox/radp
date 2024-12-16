#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * 1. 在这里定义 Entity <-> PO 转换器
 * 2. 命名 {@code IXxxConvertor}
 * 3. 示例
 * <pre>{@code
 * import org.mapstruct.Mapper;
 *
 * @Mapper(componentModel = "spring",
 *         nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
 *         nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
 * public interface IStrategyConvertor extends EntityConvertor<StrategyEntity, StrategyPO> {
 * }
 * }
 * </pre>
 */
package ${package}.infrastructure.convertor;