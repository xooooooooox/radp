package space.x9x.radp.spring.data.mybatis.autofill;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author IO x9x
 * @since 2024-09-30 14:56
 */
@RequiredArgsConstructor
public class AutofillMetaObjectHandler implements MetaObjectHandler {

    private final String createdDateFieldName;
    private final String lastModifiedDateFieldName;

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.strictInsertFill(metaObject, createdDateFieldName, LocalDateTime.class, now);
        this.strictUpdateFill(metaObject, lastModifiedDateFieldName, LocalDateTime.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.strictUpdateFill(metaObject, lastModifiedDateFieldName, LocalDateTime.class, now);
    }
}
