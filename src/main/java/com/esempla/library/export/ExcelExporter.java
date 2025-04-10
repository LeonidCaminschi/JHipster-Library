package com.esempla.library.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExporter<T> implements Exporter<T> {

    @Override
    public byte[] export(List<T> data) {
        if (data == null || data.isEmpty()) {
            return new byte[0];
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Exported Data");

            Row headerRow = sheet.createRow(0);
            T firstRow = data.get(0);
            int colIndex = 0;

            for (Method method : firstRow.getClass().getDeclaredMethods()) {
                if (method.getName().startsWith("get")) {
                    String headerName = method.getName().substring(3); // Remove "get" prefix
                    headerRow.createCell(colIndex++).setCellValue(headerName);
                }
            }

            int rowIndex = 1;
            for (T rowData : data) {
                Row row = sheet.createRow(rowIndex++);
                colIndex = 0;

                for (Method method : rowData.getClass().getDeclaredMethods()) {
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
