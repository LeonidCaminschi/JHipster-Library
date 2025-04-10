package com.esempla.library.export;

public interface ExporterFactory {
    <T> Exporter<T> createExporter(String fileType);
}
