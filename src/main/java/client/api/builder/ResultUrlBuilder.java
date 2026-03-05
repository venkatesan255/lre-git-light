package client.api.builder;

import util.network.UrlUtils;

public record ResultUrlBuilder(String baseUrl) {

    public String getExtractResultsUrl() {
        return UrlUtils.path(baseUrl, "results", "extract");
    }
}
