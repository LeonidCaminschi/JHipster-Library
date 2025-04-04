package com.esempla.library.service;

import com.esempla.library.export.Exporter;
import com.esempla.library.export.ExporterFactory;
import com.esempla.library.repository.BookRepository;
import com.esempla.library.repository.ClientRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExportService {

    private final BookRepository bookRepository;
    private final ExporterFactory exporterFactory;
    private final BorrowedBookService borrowedBookService;

    public ExportService(BookRepository bookRepository, ExporterFactory exporterFactory, BorrowedBookService borrowedBookService) {
        this.bookRepository = bookRepository;
        this.exporterFactory = exporterFactory;
        this.borrowedBookService = borrowedBookService;
    }

    public byte[] exportData(String tableName, String fileType) {
        List<?> data =
            switch (tableName.toLowerCase()) {
                case "books" -> bookRepository.findAllAsDTO();
                case "borrowedbooks" -> borrowedBookService.findAll();
                default -> throw new IllegalArgumentException("Unsupported table: " + tableName);
            };

        Exporter exporter = exporterFactory.createExporter(fileType);
        return exporter.export(data);
    }
}
