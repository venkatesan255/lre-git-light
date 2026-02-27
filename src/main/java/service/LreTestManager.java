package service;

import client.LreTestApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.LreTest;
import org.apache.commons.lang3.StringUtils;
import util.TestInputResolver;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class LreTestManager {

    private final LreTestApiClient client;

    public LreTest resolveTest(LreTest input) {
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
        String normalizedTestName = TestInputResolver.normalizePath(input.testName());
        String testName;
        String normalizedPath;

        int lastSlashIndex = normalizedTestName.lastIndexOf("/");
        if (lastSlashIndex > 0) {
            testName = normalizedTestName.substring(lastSlashIndex + 1);
            normalizedPath = normalizedTestName.substring(0, lastSlashIndex);
        } else {
            testName = normalizedTestName;
            normalizedPath = StringUtils.isBlank(input.folderPath())
                    ? null
                    : TestInputResolver.normalizePath(input.folderPath());
        }

        log.info("Resolving test - name: '{}', path: '{}'", testName, normalizedPath);
        return resolveWithPathFiltering(testName, normalizedPath);
    }

    private LreTest resolveWithPathFiltering(String testName, String normalizedPath) {
        List<LreTest> results = client.searchTestsByName(testName);

        if (results.isEmpty()) throw new TestResolutionException("No test found with name: " + testName);

        if (results.size() == 1) return results.get(0);

        if (normalizedPath == null) {
            throw new TestResolutionException(String.format(
                    "Multiple tests found with name: %s. Please provide a folder path to disambiguate. Available paths: %s"
                    , testName, formatAvailablePaths(results)));
        }

        log.info("Multiple tests found ({}), filtering by path: '{}'", results.size(), normalizedPath);

        List<LreTest> matches = results.stream()
                .filter(t -> TestInputResolver.normalizePath(t.folderPath()).equalsIgnoreCase(normalizedPath))
                .toList();

        if (matches.isEmpty()) {
            throw new TestResolutionException(String.format(
                    "Multiple tests found with name '%s' but none in path: '%s'. Available paths: %s",
                    testName, normalizedPath, formatAvailablePaths(results)));
        }

        return matches.get(0);
    }

    private String formatAvailablePaths(List<LreTest> tests) {
        return tests.stream()
                .map(LreTest::folderPath)
                .distinct()
                .collect(Collectors.joining(", "));
    }
}