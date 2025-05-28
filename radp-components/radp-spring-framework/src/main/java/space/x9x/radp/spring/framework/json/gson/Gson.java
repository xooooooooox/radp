package space.x9x.radp.spring.framework.json.gson;

import java.lang.reflect.Type;
import java.util.List;

import com.google.common.reflect.TypeToken;

import space.x9x.radp.spring.framework.json.JSON;

/**
 * @author IO x9x
 * @since 2024-09-26 13:25
 */
public class Gson implements JSON {

    private final com.google.gson.Gson gson = new com.google.gson.Gson();

    @Override
    public <T> String toJSONString(T object) {
        return gson.toJson(object);
    }

    @Override
    public <T> T parseObject(String text, Class<T> clazz) {
        return gson.fromJson(text, clazz);
    }

    @Override
    public <T> List<T> parseList(String text, Class<T> clazz) {
        Type listType = new TypeToken<List<T>>() {
        }.getType();

        return gson.fromJson(text, listType);
    }
}
