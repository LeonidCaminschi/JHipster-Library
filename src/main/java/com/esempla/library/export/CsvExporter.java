package com.esempla.library.export;

import java.lang.reflect.Field;
import java.util.List;

public class CsvExporter implements Exporter {

    @Override
    public byte[] export(List<?> data) {
        if (data == null || data.isEmpty()) {
            return new byte[0];
        }

        StringBuilder csvData = new StringBuilder();

        Field[] fields = data.get(0).getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            csvData.append(fields[i].getName());
            if (i < fields.length - 1) {
                csvData.append(",");
            }
        }
        csvData.append("\n");

        for (Object obj : data) {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                try {
                    Object value = field.get(obj);
                    csvData.append(value != null ? value.toString() : "");
                } catch (IllegalAccessException e) {
                    csvData.append("");
                }

                if (i < fields.length - 1) {
                    csvData.append(",");
                }
            }
            csvData.append("\n");
        }

        return csvData.toString().getBytes();
    }
}
