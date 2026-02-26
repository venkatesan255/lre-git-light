package config.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class JsonConfigSource implements ConfigSource {

    private final JsonNode root;

    public JsonConfigSource(Path path) {

        if (path == null || !Files.exists(path)) {
            // File is optional → use empty config
            this.root = new ObjectMapper().createObjectNode();
            return;
        }

        try {
            this.root = new ObjectMapper().readTree(path.toFile());
        } catch (IOException e) {
            throw new ConfigParseException(
                    "Config file exists but failed to parse: " + path, e);
        }
    }

    @Override
    public Optional<String> get(String key) {
        String[] parts = key.split("\\.");
        JsonNode node = root;

        for (String part : parts) {
            if (node == null || !node.isObject()) return Optional.empty();
            node = node.get(part);
        }

        return node == null || node.isNull()
                ? Optional.empty()
                : Optional.of(node.asText());
    }
}