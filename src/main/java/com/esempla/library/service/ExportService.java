package com.esempla.library.service;

import com.esempla.library.export.Exporter;
import com.esempla.library.export.ExporterFactory;
import com.esempla.library.repository.BookRepository;
import com.esempla.library.service.dto.BookDTO;
import com.esempla.library.service.dto.BorrowedBookDTO;
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
        if ("books".equalsIgnoreCase(tableName)) {
            List<BookDTO> books = bookRepository.findAllAsDTO();
            Exporter<BookDTO> exporter = exporterFactory.createExporter(fileType);
            return exporter.export(books);
        } else if ("borrowedbooks".equalsIgnoreCase(tableName)) {
            List<BorrowedBookDTO> borrowedBooks = borrowedBookService.findAllAsDTO();
            Exporter<BorrowedBookDTO> exporter = exporterFactory.createExporter(fileType);
            return exporter.export(borrowedBooks);
        } else {
            throw new IllegalArgumentException("Unsupported table: " + tableName);
        }
    }
}
