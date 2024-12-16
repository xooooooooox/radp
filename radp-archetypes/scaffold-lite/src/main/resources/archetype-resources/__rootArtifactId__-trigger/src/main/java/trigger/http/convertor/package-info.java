#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * 1. 在这里定义 Entity <-> DTO 转换器
 * 2. 命名 {@code IXxxEntityConvertor}
 * 3. 示例
 * <pre>{@code
 * import org.mapstruct.Mapper;
 *
 * @Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
 * public interface IStrategyAwardEntityConvertor extends BaseConvertor<StrategyAwardEntity, RaffleAwardListResponseDTO> {
 * }
 * }
 * </pre>
 */
package ${package}.trigger.http.convertor;