package service.lre.test;

import client.api.lre.TestApiClient;
import exception.LreTestResolutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import client.model.test.LreTest;
import model.test.Test;
import org.apache.commons.lang3.StringUtils;
import util.TestInputResolver;
import util.path.PathUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class TestManager {

    private final TestApiClient client;

    public LreTest resolveTest(LreTest input) {

        if (input.hasTestId()) return resolveById(input.testId());

        String testName = input.testName();
        if (StringUtils.isBlank(testName)) throw new LreTestResolutionException("test name is empty");

        return switch (TestInputResolver.detect(testName)) {
            case EXISTING -> resolveByPath(input);
            case EXCEL -> throw new UnsupportedOperationException("Excel import not yet implemented");
            case BY_ID -> resolveById(Integer.parseInt(testName));
        };
    }

    public LreTest resolveById(int testId) {
        log.info("Resolving test by id: {}", testId);
        return mapToLreTest(client.findTestById(testId));
    }

    private LreTest resolveByPath(LreTest input) {
        String rawName = validateAndNormalizeTestName(input.testName());
        int lastSlashIndex = rawName.lastIndexOf('/');

        String testName;
        String normalizedPath;

        if (lastSlashIndex > 0) {
            testName = rawName.substring(lastSlashIndex + 1);
            normalizedPath = PathUtils.normalizeToUnixPath(
                    rawName.substring(0, lastSlashIndex)
            );
        } else {
            testName = rawName;
            normalizedPath = StringUtils.isBlank(input.folderPath())
                    ? null
                    : PathUtils.normalizeToUnixPath(input.folderPath());
        }

        log.debug("Resolving test - name: '{}', path: '{}'", testName, normalizedPath);
        return resolveWithPathFiltering(testName, normalizedPath);
    }

    private LreTest resolveWithPathFiltering(String testName, String normalizedPath) {
        List<Test> results = client.searchTestsByName(testName);

        if (results.isEmpty()) throw new LreTestResolutionException("No test found with name: " + testName);
        if (results.size() == 1) return mapToLreTest(results.get(0));

        if (StringUtils.isBlank(normalizedPath)) {
            throw new LreTestResolutionException(String.format(
                    "Multiple tests found with name: %s. Please provide a folder path to disambiguate. Available paths: %s",
                    testName, formatAvailablePaths(results)));
        }

        log.info("Multiple tests found ({}), filtering by path: '{}'", results.size(), normalizedPath);

        return results.stream()
                .filter(t -> PathUtils.normalizeToUnixPath(t.getTestFolderPath())
                        .equalsIgnoreCase(normalizedPath))
                .findFirst()
                .map(this::mapToLreTest)
                .orElseThrow(() -> new LreTestResolutionException(String.format(
                        "Multiple tests found with name '%s' but none in path: '%s'. Available paths: %s",
                        testName, normalizedPath, formatAvailablePaths(results))));
    }

    private LreTest mapToLreTest(Test test) {
        return LreTest.builder()
                .testId(test.getId())
                .testName(test.getName())
                .folderPath(test.getTestFolderPath())
                .build();
    }

    private String formatAvailablePaths(List<Test> tests) {
        return tests.stream()
                .map(Test::getTestFolderPath)
                .distinct()
                .collect(Collectors.joining(", "));
    }

    private String validateAndNormalizeTestName(String testName) {
        String normalized = PathUtils.normalizeSlashesToUnix(testName);
        if (normalized.isEmpty()) {
            throw new LreTestResolutionException("Test name is empty after normalization");
        }
        return normalized;
    }
}