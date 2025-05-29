#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * RPC interface, format {@code IXxxService}
 * 定义 rpc 接口, 提供给 <strong>trigger</strong> 层使用
 *
 * <pre>{@code
 * public interface IRaffleService {
 *      SingleResult<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO);
 * }
 * }</pre>
 */
package ${package}.api;
