package client.model.resultextraction;

import lombok.Builder;

import java.util.List;

@Builder
public record ResultNotification(
        int runId,
        List<String> ccRecipients,
        List<String> bccRecipients
) {
}
