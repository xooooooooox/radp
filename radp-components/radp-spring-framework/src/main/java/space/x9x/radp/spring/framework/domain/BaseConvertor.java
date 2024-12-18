package space.x9x.radp.spring.framework.domain;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MapperConfig;

import java.util.List;
import java.util.stream.Stream;

/**
 * @param <S> source
 * @param <T> target
 * @author x9x
 * @since 2024-10-27 10:53
 */
@MapperConfig
public interface BaseConvertor<S, T> {

    // ============= source to target ===============

    T sourceToTarget(S s);

    @InheritConfiguration(name = "sourceToTarget")
    List<T> sourceToTarget(List<S> list);

    List<T> sourceToTarget(Stream<S> stream);

    // ============= target to source ===============

    @InheritInverseConfiguration(name = "sourceToTarget")
    S targetToSource(T t);

    @InheritConfiguration(name = "targetToSource")
    List<S> targetToSource(List<T> list);

    List<S> targetToSource(Stream<T> stream);
}
