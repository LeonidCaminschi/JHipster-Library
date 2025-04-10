package com.esempla.library.export;

import org.springframework.stereotype.Service;

@Service
public class ExporterFactoryImpl implements ExporterFactory {

    @Override
    public <T> Exporter<T> createExporter(String fileType) {
        return switch (fileType.toLowerCase()) {
            case "csv" -> new CsvExporter<>();
            case "excel" -> new ExcelExporter<>();
            default -> throw new IllegalArgumentException("Unsupported file type: " + fileType);
        };
    }
}
