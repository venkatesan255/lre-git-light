package client.model.connection;

import lombok.Builder;

@Builder
public record LreConnection(String serverUrl,
                            SecretValue gitToken,
                            String domain,
                            String project) {

    public String buildApiUrl() {
        String base = serverUrl.endsWith("/") ? serverUrl.stripTrailing() : serverUrl;
        return String.format("%s/api/lre/domains/%s/projects/%s", base, domain, project);
    }
}