package com.esempla.library.export;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.StringWriter;
import java.util.List;

public class CsvExporter<T> implements Exporter<T> {

    @Override
    public byte[] export(List<T> data) {
        if (data == null || data.isEmpty()) {
            return new byte[0];
        }

        try (StringWriter stringWriter = new StringWriter()) {
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(stringWriter)
                .withApplyQuotesToAll(false)
                .withEscapechar(CSVWriter.NO_ESCAPE_CHARACTER)
                .withSeparator(';')
                .withOrderedResults(false)
                .build();

            beanToCsv.write(data);

            return stringWriter.toString().getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error exporting data to CSV", e);
        }
    }
}
