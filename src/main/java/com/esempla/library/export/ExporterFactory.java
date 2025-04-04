package com.esempla.library.export;

public interface ExporterFactory {
    Exporter createExporter(String fileType);
}
