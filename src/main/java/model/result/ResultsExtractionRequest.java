package model.result;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.test.Test;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultsExtractionRequest {
    private int runId;
    private long maxErrors;
    private long maxFailedTransactions;
    private Test test;
    private List<String> ccRecipients;
    private List<String> bccRecipients;

}
