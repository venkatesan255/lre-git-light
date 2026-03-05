package config.source;

import java.util.List;

public record ConfigSourceReader(ConfigSource source) {

    public String getRequired(String key, List<String> missingKeys) {
        return source.get(key)
                .filter(v -> !v.isBlank())
                .orElseGet(() -> {
                    missingKeys.add(key);
                    return null;
                });
    }

    public String getOptionalString(String key, String defaultValue) {
        return source.get(key).orElse(defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return source.get(key)
                .map(String::trim)
                .map(v -> {
                    try {
                        return Integer.parseInt(v);
                    } catch (NumberFormatException e) {
                        throw new ConfigParseException("Invalid integer for key '" + key + "': " + v, e);
                    }
                })
                .orElse(defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return source.get(key)
                .map(String::trim)
                .map(v -> {
                    try {
                        return Long.parseLong(v);
                    } catch (NumberFormatException e) {
                        throw new ConfigParseException("Invalid long for key '" + key + "': " + v, e);
                    }
                })
                .orElse(defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return source.get(key)
                .map(Boolean::parseBoolean)
                .orElse(defaultValue);
    }

    public List<String> getStringList(String key) {
        return source.get(key)
                .filter(v -> !v.isBlank())
                .map(v -> List.of(v.split(",")))
                .orElse(List.of());
    }
}