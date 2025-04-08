package com.esempla.library.export;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.io.StringWriter;
import java.util.List;

public class CsvExporter implements Exporter {

    @Override
    public byte[] export(List<?> data) {
        if (data == null || data.isEmpty()) {
            return new byte[0];
        }

        Object firstRow = data.get(0);
        if (!firstRow.getClass().getPackageName().startsWith("com.esempla.library.service.dto")) {
            throw new IllegalArgumentException("Unsupported data type for CSV export");
        }

        try (StringWriter stringWriter = new StringWriter()) {
            StatefulBeanToCsv<Object> beanToCsv = new StatefulBeanToCsvBuilder<Object>(stringWriter).withApplyQuotesToAll(false).build();

            beanToCsv.write(data);

            return stringWriter.toString().getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error exporting data to CSV", e);
        }
    }
}
