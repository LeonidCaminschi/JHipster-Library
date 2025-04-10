package com.esempla.library.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
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
            Field[] fields = firstRow.getClass().getDeclaredFields();

            int colIndex = 0;
            for (Field field : fields) {
                field.setAccessible(true);
                headerRow.createCell(colIndex++).setCellValue(field.getName());
            }

            int rowIndex = 1;
            for (T rowData : data) {
                Row row = sheet.createRow(rowIndex++);
                colIndex = 0;

                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(rowData);
                    row.createCell(colIndex++).setCellValue(value != null ? value.toString() : "");
                }
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error exporting data to Excel", e);
        }
    }
}
