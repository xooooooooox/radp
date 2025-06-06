#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Event types package.
 *
 * This package contains domain event classes that represent significant occurrences
 * within the domain. These events are used for: - Communication between bounded contexts
 * - Implementing event-driven architectures - Maintaining audit logs - Supporting
 * eventual consistency
 *
 * Events are immutable records of something that has happened in the domain.
 */
package ${package}.types.event;
