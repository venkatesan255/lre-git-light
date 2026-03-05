package client.model.sync;

import lombok.Builder;

@Builder
public record SyncConfig(boolean syncEnabled) {}