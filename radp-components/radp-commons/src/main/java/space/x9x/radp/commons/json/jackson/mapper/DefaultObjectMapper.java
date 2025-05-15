package space.x9x.radp.commons.json.jackson.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.Serial;

/**
 * @author x9x
 * @since 2024-09-23 13:50
 */
public class DefaultObjectMapper extends ObjectMapper {

    @Serial
    private static final long serialVersionUID = -4849410602482226754L;

    /**
     * Constructs a new DefaultObjectMapper with pre-configured settings.
     * <p>
     * This constructor initializes an ObjectMapper with the following configurations:
     * <ul>
     *   <li>Serialization inclusion set to ALWAYS (includes all properties)</li>
     *   <li>Ignores unknown properties during deserialization</li>
     *   <li>Disables failure on empty beans during serialization</li>
     *   <li>Configures dates to be serialized as ISO-8601 strings rather than timestamps</li>
     *   <li>Registers JavaTimeModule for Java 8 date/time API support</li>
     * </ul>
     */
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
