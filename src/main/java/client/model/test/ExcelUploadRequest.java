package client.model.test;

import lombok.Builder;

@Builder
public record ExcelUploadRequest(
        String fileName,
        String sheetName
) {
}
