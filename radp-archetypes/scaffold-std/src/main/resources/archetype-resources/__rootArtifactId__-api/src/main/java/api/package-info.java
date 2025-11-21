#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * 对外暴露的接口, format {@code IXxxApi}, 提供给 <strong>xxx-trigger</strong> 层使用, 在 <strong>xxx-case</strong> 层使用
 *
 * <pre><code>
 * public interface IRaffleApi {
 *      SingleResult&lt;List&lt;RaffleAwardListDTO&gt;&gt; queryRaffleAwardList(RaffleAwardListQry query);
 * }
 * </code></pre>
 */
package ${package}.api;
