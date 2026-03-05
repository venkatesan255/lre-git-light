package client.api.builder;

import util.network.UrlUtils;

public record SyncUrlBuilder(String baseUrl) {

    private static final String LRE_API_SYNC = "sync";

    public String getSyncUrl() {
        return UrlUtils.path(baseUrl, LRE_API_SYNC);
    }
}
