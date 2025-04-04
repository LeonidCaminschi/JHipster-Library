package com.esempla.library.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExporter implements Exporter {

    @Override
    public byte[] export(List<?> data) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Exported Data");

            // Create header row
            Row headerRow = sheet.createRow(0);
            if (!data.isEmpty()) {
                Object firstRow = data.get(0);
                int colIndex = 0;
                for (Field field : firstRow.getClass().getDeclaredFields()) {
                    field.setAccessible(true); // Allow access to private fields
                    headerRow.createCell(colIndex++).setCellValue(field.getName()); // Extract field name
                }
            }

            // Create data rows
            int rowIndex = 1;
            for (Object rowData : data) {
                Row row = sheet.createRow(rowIndex++);
                int colIndex = 0;
                for (Field field : rowData.getClass().getDeclaredFields()) {
                    field.setAccessible(true); // Allow access to private fields
                    Object value = field.get(rowData);
                    row.createCell(colIndex++).setCellValue(value != null ? value.toString() : "");
                }
            }

            workbook.write(out); // Write workbook to ByteArrayOutputStream
            return out.toByteArray(); // Return the byte array
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException("Error exporting data to Excel", e);
        }
    }
}
