#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * 聚合对象；
 * <p>
 * 1. 聚合实体和值对象 <br/>
 * 2. 聚合是聚合的对象，和提供基础处理对象的方法。但不建议在聚合中引入仓储和接口来做过大的逻辑。而这些复杂的操作应该放到service中处理 <br/>
 * 3. 对象名称 <code>XxxAggregate</code> <br/>
 * <p>
 * 当你对数据库的操作需要使用到多个实体时，可以创建聚合对象。一个聚合对象代表着一个数据库事务，具有事务一致性。
 * 聚合中的实体可以由聚合根提供创建操作，实体也被称为聚合根对象。一个订单的聚合会涵盖：下单用户实体、订单实体、订单明细实体
 * 和订单收货的四级地址值对象。而作为入参的购物车实体对象，已经被转换为实体对象了。-- 聚合内事务一致性，聚合外最终一致性。
 * <p>
 * <h4>概念</h4>
 * <p><strong>聚合</strong>是领域模型中的一个关键概念，它是一组具有内聚性的相关对象的集合，这些对象一起工作以执行某些业务规则或操作。
 * 聚合定义了一组对象的边界，这些对象可以被视为一个单一的单元进行处理。</p>
 *
 * <h4>特征</h4>
 * <ul>
 *   <li><strong>一致性边界：</strong>聚合确保其内部对象的状态变化是一致的。当对聚合内的对象进行操作时，这些操作必须保持聚合内所有对象的一致性。</li>
 *   <li><strong>根实体：</strong>每个聚合都有一个根实体（Aggregate Root），它是聚合的入口点。根实体拥有一个全局唯一的标识符，其他对象通过根实体与聚合交互。</li>
 *   <li><strong>事务边界：</strong>聚合也定义了事务的边界。在聚合内部，所有的变更操作应该是原子的，即它们要么全部成功，要么全部失败，以此来保证数据的一致性。</li>
 * </ul>
 *
 * <h4>用途</h4>
 * <ul>
 *   <li><strong>封装业务逻辑：</strong>聚合通过将相关的对象和操作封装在一起，提供了一个清晰的业务逻辑模型，有助于业务规则的实施和维护。</li>
 *   <li><strong>保证一致性：</strong>聚合确保内部状态的一致性，通过定义清晰的边界和规则，聚合可以在内部强制执行业务规则，从而保证数据的一致性。</li>
 *   <li><strong>简化复杂性：</strong>聚合通过组织相关的对象，简化了领域模型的复杂性。这有助于开发者更好地理解和扩展系统。</li>
 * </ul>
 *
 * <h4>实现手段</h4>
 * <ul>
 *   <li><strong>定义聚合根：</strong>选择合适的聚合根是实现聚合的第一步。聚合根应该是能够代表整个聚合的实体，并且拥有唯一标识。</li>
 *   <li><strong>限制访问路径：</strong>只能通过聚合根来修改聚合内的对象，不允许直接修改聚合内部对象的状态，以此来维护边界和一致性。</li>
 *   <li><strong>设计事务策略：</strong>在聚合内部实现事务一致性，确保操作要么全部完成，要么全部回退。对于聚合之间的交互，可以采用领域事件或其他机制来实现最终一致性。</li>
 *   <li><strong>封装业务规则：</strong>在聚合内部实现业务规则和逻辑，确保所有的业务操作都遵循这些规则。</li>
 *   <li><strong>持久化：</strong>聚合根通常与数据持久化层交互，以保存聚合的状态。这通常涉及到对象-关系映射（ORM）或其他数据映射技术。</li>
 * </ul>
 */
package ${package}.domain.xxx.model.aggregate;