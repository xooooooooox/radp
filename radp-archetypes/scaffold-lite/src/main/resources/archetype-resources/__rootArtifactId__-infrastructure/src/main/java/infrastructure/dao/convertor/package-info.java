#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * 1. 在这里定义 PO <-> DomainObject 转换器
 * 2. 命名 {@code IXxxPOConvertor}
 * 3. 示例
 * <pre>{@code
 * import org.mapstruct.Mapper;
 *
 * @Mapper(componentModel = "spring")
 * public interface IStrategyConvertor extends BaseConvertor<StrategyPO,StrategyEntity> {
 * }
 * }
 * </pre>
 */
package ${package}.infrastructure.convertor;