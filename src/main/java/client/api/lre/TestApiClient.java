package client.api.lre;

import client.base.BaseClient;
import client.api.builder.TestUrlBuilder;
import client.model.connection.LreConnection;
import client.model.test.ExcelUploadRequest;
import model.test.Test;
import util.serialization.JsonUtils;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;


public class TestApiClient extends BaseClient {

    private final TestUrlBuilder urlBuilder;

    public TestApiClient(LreConnection connection) {
        super(connection);
        this.urlBuilder = new TestUrlBuilder(connection.buildApiUrl());
    }

    public List<Test> searchTestsByName(String name) {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        String path = urlBuilder.searchTest() + "?name=" + encodedName;
        return JsonUtils.readValueList(get(path).body(), Test.class);
    }

    public List<Test> getAllTests() {
        String path = urlBuilder.getTests();
        return JsonUtils.readValueList(get(path).body(), Test.class);
    }

    public Test findTestById(int testId) {
        String path = urlBuilder.getTestById(testId);
        return JsonUtils.readValue(get(path).body(), Test.class);
    }

    public Test createTest(Test test) {
        String path = urlBuilder.createTest();
        return JsonUtils.readValue(post(path, test).body(), Test.class);
    }

    public void deleteTest(int testId) {
        String path = urlBuilder.deleteTest(testId);
        delete(path);
    }

    /**
     * Imports tests from an Excel file
     *
     * @param excelFilePath Path to the Excel file
     * @param sheetName     The sheet name to import
     * @return The created or updated test
     */
    public Test importTestFromExcel(Path excelFilePath, String sheetName) {
        String path = urlBuilder.createTestFromExcel();
        String excelFileName = excelFilePath.getFileName().toString();
        ExcelUploadRequest metadata = new ExcelUploadRequest(excelFileName, sheetName);
        HttpResponse<String> response = postMultipart(path, excelFilePath, metadata);
        return JsonUtils.readValue(response.body(), Test.class);
    }
}