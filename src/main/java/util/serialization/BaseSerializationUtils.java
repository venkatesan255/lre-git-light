package util.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.LreException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class BaseSerializationUtils {

    private BaseSerializationUtils() {
        // Prevent instantiation
    }

    protected static String serialize(ObjectMapper mapper, Object object, String format) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object to {}: {}", format, e.getMessage());
            throw new LreException(format + " serialization error", e);
        }
    }

    protected static <T> T deserialize(ObjectMapper mapper, String content, Class<T> valueType, String format) {
        try {
            return mapper.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize {} to {}: {}", format, valueType.getSimpleName(), e.getMessage());
            throw new LreException(format + " deserialization error", e);
        }
    }

    protected static <T> List<T> deserializeList(ObjectMapper mapper, String content, Class<T> clazz, String format) {
        try {
            JavaType type = mapper.getTypeFactory()
                                  .constructCollectionType(List.class, clazz);
            return mapper.readValue(content, type);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize {} array to List<{}>: {}", format, clazz.getSimpleName(), e.getMessage());
            throw new LreException(format + " array deserialization error", e);
        }
    }
}