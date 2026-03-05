package config.source;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class EnvConfigSource implements ConfigSource {

    @Override
    public Optional<String> get(String key) {
        String envKey = key.replaceAll("([a-z])([A-Z])", "$1_$2")
                .toUpperCase()
                .replace(".", "_");

        log.debug("key: {}, envKey: {}", key, envKey);
        return Optional.ofNullable(System.getenv(envKey));
    }
}