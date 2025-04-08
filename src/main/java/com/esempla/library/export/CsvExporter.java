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

        try (StringWriter stringWriter = new StringWriter()) {
            StatefulBeanToCsv<Object> beanToCsv = new StatefulBeanToCsvBuilder<Object>(stringWriter).withApplyQuotesToAll(false).build();

            beanToCsv.write(data);

            return stringWriter.toString().getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error exporting data to CSV", e);
        }
    }
}
