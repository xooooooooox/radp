#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * 外部接口适配器层(仓储和适配器): 当需要调用外部接口时，则创建出这一层，并定义接口，之后由基础设施层的 adapter 层具体实现
 *
 * <p>
 * 在 DDD 的设计方法中, 领域层做到了只关心领域服务实现。最能体现这样设计的就是仓库和适配器的设计。 通常在 `Service + 数据模型` 的设计中, 会在
 * Service 中引入 Redis, RPC, 配置中心等各类其他外部服务。 但在 DDD 中, 通过仓储和适配器以及基础设施层的定义, 解耦了这部分内容。
 *
 * <h4>特征 (Features)</h4>
 * <ul>
 * <li><b>封装持久化操作 (Encapsulation of persistence operations)</b>: Repository
 * 负责封装所有与数据源交互的操作, 如创建, 读取, 更新和删除 (CURD) 操作。 这样, 领域层的代码就可以避免直接处理数据库或者其他存储机制的复杂性。</li>
 * <li><b>领域对象的集合管理 (Domain object collection management)</b>: Repository 通常被视为领域对象的集合,
 * 提供查询和过滤这些对象的方法, 使得领域对象的获取和管理更加方便。</li>
 * <li><b>抽象接口 (Abstract interface)</b>: Repository 定义了一个与持久化机制无关的接口,
 * 使得领域层的代码可以在不同的持久化机制之间切换, 而不需要修改业务逻辑。</li>
 * </ul>
 *
 * <h4>用途 (Usage)</h4>
 * <ul>
 * <li><b>数据访问抽象 (Data access abstraction)</b>: Repository 为领域层提供了一个清晰的数据访问接口,
 * 使得领域对象可以专注于业务逻辑的实现, 而不是数据访问的细节。</li>
 * <li><b>领域对象的查询和管理 (Domain object query and management)</b>: Repository
 * 使得对领域对象的查询和管理变得更加方便和灵活, 支持复杂的查询逻辑。</li>
 * <li><b>领域逻辑与数据存储分离 (Separation of domain logic and data storage)</b>: 通过 Repository 模式,
 * 领域逻辑与数据存储逻辑分离, 提高了领域模型的纯粹性和可测试性。</li>
 * <li><b>优化数据访问 (Optimized data access)</b>: Repository 实现可以包含数据访问的优化策略, 如缓存, 批处理操作等,
 * 以提高应用程序的性能。</li>
 * </ul>
 *
 * <h4>实现手段 (Implementation)</h4>
 * <ul>
 * <li><b>定义 Repository 接口 (Define Repository interface)</b>: 在领域层定义一个或多个 Repository 接口,
 * 这些接口声明了所需的数据访问方法。</li>
 * <li><b>实现 Repository 接口 (Implement Repository interface)</b>: 在基础设施层或数据访问层实现这些接口,
 * 具体实现可能是使用 ORM (对象关系映射) 框架, 如 Mybatis, Hibernate 等, 或者直接使用数据库访问 API, 如 JDBC 等。</li>
 * <li><b>依赖注入 (Dependency injection)</b>: 在应用程序中使用依赖注入 (DI) 来将具体的 Repository
 * 实现注入到需要它们的领域服务或应用服务中。 这样做可以进一步解耦领域层和数据访问层, 同时也便于单元测试。</li>
 * <li><b>使用规范模式 (Using Specification pattern)</b>: 有时候, 为了构建复杂的查询, 可以结合使用规范模式,
 * 这是一种允许将业务规则封装为单独的业务逻辑单元的模式, 这些单元可以被 Repository 用来构建查询。</li>
 * </ul>
 *
 * <strong>Repository 模式是 DDD (领域驱动设计) 中的一个核心概念</strong>: 它有助于保持领域模型的聚焦和清晰, 同时提供了灵活,
 * 可测试和可维护的数据访问策略。
 * <p>
 * 仓储解耦的手段使用了依赖倒置的设计, 所有领域需要的外部服务, 不再直接引入外部的服务, 而是通过定义接口的方式, 让基础设施层实现领域层接口(仓储/适配器) 的方式来处理.
 * <br/>
 * 那么也就是基础设施层负责对接 数据库, 缓存, 配置中心, RPC接口, HTTP接口, MQ推送等各项资源, 并承接领域服务的接口调用各项服务为领域层提供数据能力.
 * <br/>
 * 同时这也会体现出, 领域层的实现是具有业务语义的, 而到了基础设施层则没有业务语义, 都是原子的方法, 通过原子方法的组合为领域业务语义提供支撑. <br/>
 */
package ${package}.domain.yyy.adapter;
