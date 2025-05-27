
/**
 * 实体对象； 1. 一般和数据库持久化对象1v1的关系.(但因各自开发系统的不同，也有1vn的可能.) <br/>
 * 2. 如果是老系统改造，那么旧的库表冗余了太多的字段，可能会有nv1的情况.<br/>
 * 3. 对象名称 <code>XxxEntity</code> <br/>
 * 4. 实体对象唯一标识, 一般不使用数据库自增ID, 而是使用业务唯一标识。
 */
package space.x9x.radp.domain.yyy.model.entity;