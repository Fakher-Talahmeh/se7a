package org.health.se7a.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ExportedPdfDTO {
    private String fileName;
    private String content;
}