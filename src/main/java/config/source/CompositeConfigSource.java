package config.source;

import java.util.List;
import java.util.Optional;

public record CompositeConfigSource(List<ConfigSource> sources) implements ConfigSource {

    public CompositeConfigSource(ConfigSource... sources) {
        this(List.of(sources));
    }

    @Override
    public Optional<String> get(String key) {
        return sources.stream()
                .map(source -> source.get(key))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}