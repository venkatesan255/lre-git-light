package client;


import com.fasterxml.jackson.core.type.TypeReference;
import model.LreConnection;
import model.LreTest;
import model.LreTestEntity;
import util.TestInputResolver;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LreTestApiClient extends BaseClient {

    public LreTestApiClient(LreConnection connection) {
        super(connection);
    }

    public List<LreTest> searchTestsByName(String name) {
        String path = "/tests/search?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8);
        return readValue(get(path).body(), new TypeReference<List<LreTestEntity>>() {})
                .stream()
                .map(this::toModel)
                .toList();
    }

    public LreTest findTestById(int testId) {
        return toModel(readValue(get("/tests/" + testId).body(), LreTestEntity.class));
    }

    // -------------------------------------------------------------------------
    // Mapping
    // -------------------------------------------------------------------------

    /**
     * Maps LRE API response to domain model.
     * Builds full path as folderPath/name, normalised to forward slashes.
     */
    private LreTest toModel(LreTestEntity entity) {
        String fullPath = TestInputResolver.normalizePath(
                entity.testFolderPath() + "\\" + entity.name());

        return LreTest.builder()
                .testId(entity.id())
                .testName(entity.name())
                .folderPath(fullPath)
                .build();
    }
}