package config;

import lombok.Getter;

import java.util.List;

@Getter
public class ConfigurationValidationException extends RuntimeException {

    private final List<String> missingKeys;

    public ConfigurationValidationException(List<String> missingKeys) {
        super("Missing required configuration parameters: " +
                String.join(", ", missingKeys));
        this.missingKeys = missingKeys;
    }

}