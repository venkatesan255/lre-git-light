package model;

import lombok.Builder;

@Builder
public record LreConnection(String serverUrl, SecretValue gitToken, String domain, String project) {

    public String buildApiUrl() {
        return String.format("%s/api/lre/domains/%s/projects/%s",
                serverUrl, domain, project);
    }
}