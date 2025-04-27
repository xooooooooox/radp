package space.x9x.radp.spring.framework.error.asserts;

import space.x9x.radp.spring.framework.error.ClientException;
import space.x9x.radp.spring.framework.error.util.ExceptionUtils;

import java.util.function.BiFunction;

/**
 * Client assertion interface that throws ClientException when assertions fail.
 *
 * @author x9x
 * @since 2024-09-26 23:47
 */
public interface BaseClientAssert extends BaseAssert<ClientException> {

    @Override
    default BiFunction<String, String, ClientException> getExceptionCreator() {
        return ExceptionUtils::clientException;
    }
}
