package config.source;

import java.util.Optional;

public interface ConfigSource {
    Optional<String> get(String key);
}