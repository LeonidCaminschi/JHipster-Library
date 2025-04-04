package com.esempla.library.web.rest;

import com.esempla.library.service.ExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export")
public class ExportResource {

    private final ExportService exportService;

    public ExportResource(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("")
    public ResponseEntity<?> exportData(@RequestParam String tableName, @RequestParam String fileType) {
        byte[] exportedData = exportService.exportData(tableName, fileType);

        HttpHeaders headers = new HttpHeaders();

        if ("excel".equalsIgnoreCase(fileType)) {
            byte[] excelData = exportedData;
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", tableName + ".xlsx");
            return ResponseEntity.ok().headers(headers).body(excelData);
        } else if ("csv".equalsIgnoreCase(fileType)) {
            byte[] csvData = exportedData;
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("attachment", tableName + ".csv");
            return ResponseEntity.ok().headers(headers).body(csvData);
        }

        return ResponseEntity.ok(exportedData);
    }
}
