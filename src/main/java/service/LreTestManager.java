package service;

import client.LreTestApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.LreTest;
import util.TestInputResolver;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LreTestManager {

    private final LreTestApiClient client;


    public LreTest resolveTest(LreTest input) {
        // testId takes priority
        if (input.hasTestId()) return resolveById(input.testId());

        return switch (TestInputResolver.detect(input.testName())) {
            case EXISTING -> resolveByPath(input);
            case EXCEL -> throw new UnsupportedOperationException("Excel import not yet implemented");
            case BY_ID -> resolveById(Integer.parseInt(input.testName()));
        };
    }


    private LreTest resolveById(int testId) {
        log.info("Resolving test by id: {}", testId);
        return client.findTestById(testId);
    }

    private LreTest resolveByPath(LreTest input) {
        String testName = TestInputResolver.extractName(input.testName());
        String normalizedPath = input.folderPath().isBlank()
                ? TestInputResolver.normalizePath(input.testName())
                : input.folderPath();

        log.info("Resolving test by name: '{}', path: '{}'", testName, normalizedPath);

        List<LreTest> matches = client.searchTestsByName(testName).stream()
                .filter(t -> t.folderPath().equalsIgnoreCase(normalizedPath))
                .toList();

        if (matches.isEmpty()) {
            throw new TestResolutionException(
                    "No test found for path: " + normalizedPath);
        }

        if (matches.size() > 1) {
            throw new TestResolutionException(
                    "Unexpected: multiple tests matched path: " + normalizedPath);
        }

        return matches.get(0);
    }
}