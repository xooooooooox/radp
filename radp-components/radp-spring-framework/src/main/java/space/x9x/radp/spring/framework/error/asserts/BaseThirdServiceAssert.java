package space.x9x.radp.spring.framework.error.asserts;

import space.x9x.radp.spring.framework.error.ThirdServiceException;
import space.x9x.radp.spring.framework.error.util.ExceptionUtils;

import java.util.function.BiFunction;

/**
 * Third-party service assertion interface that throws ThirdServiceException when assertions fail.
 *
 * @author x9x
 * @since 2024-09-26 23:50
 */
public interface BaseThirdServiceAssert extends BaseAssert<ThirdServiceException> {

    @Override
    default BiFunction<String, String, ThirdServiceException> getExceptionCreator() {
        return ExceptionUtils::thirdServiceException;
    }
}
