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

            Row headerRow = sheet.createRow(0);
            if (!data.isEmpty()) {
                Object firstRow = data.get(0);
                int colIndex = 0;

                for (var method : firstRow.getClass().getDeclaredMethods()) {
                    if (method.getName().startsWith("get")) {
                        String headerName = method.getName().substring(3);
                        headerRow.createCell(colIndex++).setCellValue(headerName);
                    }
                }
            }

            int rowIndex = 1;
            for (Object rowData : data) {
                Row row = sheet.createRow(rowIndex++);
                int colIndex = 0;

                for (var method : rowData.getClass().getDeclaredMethods()) {
                    if (method.getName().startsWith("get")) {
                        Object value = method.invoke(rowData);
                        row.createCell(colIndex++).setCellValue(value != null ? value.toString() : "");
                    }
                }
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException | ReflectiveOperationException e) {
            throw new RuntimeException("Error exporting data to Excel", e);
        }
    }
}
