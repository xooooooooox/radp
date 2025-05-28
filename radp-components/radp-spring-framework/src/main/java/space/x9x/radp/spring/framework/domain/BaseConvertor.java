package space.x9x.radp.spring.framework.domain;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MapperConfig;

/**
 * @param <S> source
 * @param <T> target
 * @author IO x9x
 * @since 2024-10-27 10:53
 */
@MapperConfig
public interface BaseConvertor<S, T> {

    // ============= source to target ===============

    /**
     * Converts a source object to a target object.
     *
     * @param s the source object to convert
     * @return the converted target object
     */
    T sourceToTarget(S s);

    /**
     * Converts a list of source objects to a list of target objects.
     * Inherits configuration from the sourceToTarget method.
     *
     * @param list the list of source objects to convert
     * @return a list of converted target objects
     */
    @InheritConfiguration(name = "sourceToTarget")
    List<T> sourceToTarget(List<S> list);

    /**
     * Converts a stream of source objects to a list of target objects.
     *
     * @param stream the stream of source objects to convert
     * @return a list of converted target objects
     */
    List<T> sourceToTarget(Stream<S> stream);

    // ============= target to source ===============

    /**
     * Converts a target object back to a source object.
     * Uses inverse configuration from the sourceToTarget method.
     *
     * @param t the target object to convert back
     * @return the converted source object
     */
    @InheritInverseConfiguration(name = "sourceToTarget")
    S targetToSource(T t);

    /**
     * Converts a list of target objects back to a list of source objects.
     * Inherits configuration from the targetToSource method.
     *
     * @param list the list of target objects to convert back
     * @return a list of converted source objects
     */
    @InheritConfiguration(name = "targetToSource")
    List<S> targetToSource(List<T> list);

    /**
     * Converts a stream of target objects back to a list of source objects.
     *
     * @param stream the stream of target objects to convert back
     * @return a list of converted source objects
     */
    List<S> targetToSource(Stream<T> stream);
}
