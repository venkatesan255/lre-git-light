package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LreTestEntity(
        @JsonProperty("CreatedBy")      String createdBy,
        @JsonProperty("ID")             int id,
        @JsonProperty("LastModified")   String lastModified,
        @JsonProperty("Name")           String name,
        @JsonProperty("TestFolderPath") String testFolderPath
) {
}