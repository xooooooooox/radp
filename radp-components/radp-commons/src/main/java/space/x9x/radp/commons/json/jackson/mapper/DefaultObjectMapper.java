package space.x9x.radp.commons.json.jackson.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author x9x
 * @since 2024-09-23 13:50
 */
public class DefaultObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = -4849410602482226754L;

    public DefaultObjectMapper() {
        super();

        // 设置序列化时包含哪些属性 (默认值就是 Always, 硬编码在这里, 是为了更明显的告知使用者)
        this.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // 反序列化时忽略未知属性
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 禁用空对象转换校验. 遇到没有属性的bean时,会输出一个空的JSON对象{},而不会抛出异常
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 设置序列化时处理时间的方式
        this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 添加对 Java8 时间 API 的支持 (如: LocalDateTime)
        this.registerModule(new JavaTimeModule());
    }
}
