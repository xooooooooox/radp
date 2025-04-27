package space.x9x.radp.spring.framework.error.asserts;

import space.x9x.radp.spring.framework.error.ServerException;
import space.x9x.radp.spring.framework.error.util.ExceptionUtils;

import java.util.function.BiFunction;

/**
 * Server assertion interface that throws ServerException when assertions fail.
 *
 * @author x9x
 * @since 2024-09-26 23:49
 */
public interface BaseServerAssert extends BaseAssert<ServerException> {

    @Override
    default BiFunction<String, String, ServerException> getExceptionCreator() {
        return ExceptionUtils::serverException;
    }
}
