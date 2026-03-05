package util.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class JsonUtils {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    static {
        JSON_MAPPER.registerModule(new JavaTimeModule());
        JSON_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JSON_MAPPER.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public static String toJson(Object object) {
        return BaseSerializationUtils.serialize(JSON_MAPPER, object, "JSON");
    }

    public static <T> T fromJson(String json, Class<T> valueType) {
        return BaseSerializationUtils.deserialize(JSON_MAPPER, json, valueType, "JSON");
    }

    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        return BaseSerializationUtils.deserializeList(JSON_MAPPER, json, clazz, "JSON");
    }

    public static  <T> T readValue(String body, Class<T> clazz) {
        return fromJson(body, clazz);
    }

    public static  <T> List<T> readValueList(String body, Class<T> clazz) {
        return fromJsonArray(body, clazz);
    }

}