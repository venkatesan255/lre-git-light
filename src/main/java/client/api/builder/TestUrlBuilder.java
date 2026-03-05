package client.api.builder;

import util.network.UrlUtils;

/**
  * URL builder for test-related endpoints
  */
public record TestUrlBuilder(String baseUrl) {

    // Get Tests
    // http://localhost:8080/api/lre/domains/test/projects/test/tests
    // Get Test by name
    // http://localhost:8080/api/lre/domains/test/projects/test/tests/search
    // Get Test by ID
    // http://localhost:8080/api/lre/domains/test/projects/test/tests/1
    // Create Test
    // http://localhost:8080/api/lre/domains/test/projects/test/tests/create
    // Update Test
    // http://localhost:8080/api/lre/domains/test/projects/test/tests/1/update
    // Delete Test
    // http://localhost:8080/api/lre/domains/test/projects/test/tests/1
    // Check Test Validity
    // http://localhost:8080/api/lre/domains/test/projects/test/tests/1/validity
    // Import Excel Test
    // http://localhost:8080/api/lre/domains/test/projects/test/excel/import

    private static final String LRE_API_TESTS = "tests";

    /**
     * Builds URL to get all tests for a domain and project
     */
    public String getTests() {
        return UrlUtils.path(baseUrl, LRE_API_TESTS);
    }

    /**
     * Builds URL to search for a test by name
     */
    public String searchTest() {
        return UrlUtils.path(baseUrl, LRE_API_TESTS, "search");
    }

    /**
     * Builds URL to get a specific test by ID
     */
    public String getTestById(int testId) {
        return UrlUtils.path(baseUrl, LRE_API_TESTS, testId);
    }

    /**
     * Builds URL to create a new test
     */
    public String createTest() {
        return UrlUtils.path(baseUrl, LRE_API_TESTS, "create");
    }

    /**
     * Builds URL to update an existing test
     */
    public String updateTest(int testId) {
        return UrlUtils.path(baseUrl, LRE_API_TESTS, testId, "update");
    }

    /**
     * Builds URL to delete a test
     */
    public String deleteTest(int testId) {
        return UrlUtils.path(baseUrl, LRE_API_TESTS, testId);
    }

    /**
     * Builds URL to check test validity
     */
    public String checkTestValidity(int testId) {
        return UrlUtils.path(baseUrl, LRE_API_TESTS, testId, "validity");
    }

    /**
     * Builds URL to import tests from Excel
     */
    public String createTestFromExcel() {
        return UrlUtils.path(baseUrl, LRE_API_TESTS, "excel", "import");
    }
}