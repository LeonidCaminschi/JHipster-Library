package com.esempla.library.export;
//import com.opencsv.bean.*;
//import com.opencsv.bean.comparator.LiteralComparator;
//import com.opencsv.exceptions.CsvBadConverterException;
//import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
//import org.apache.commons.lang3.StringUtils;
//
//import java.lang.reflect.Field;
//import java.util.Arrays;
//
//public class HeaderColumnNameAndOrderMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {
//
//    public HeaderColumnNameAndOrderMappingStrategy(Class<T> type) {
//        setType(type);
//    }
//
//    @Override
//    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
//
//        String[] header = super.generateHeader(bean);
//        final int numColumns = findMaxFieldIndex();
//        if (!isAnnotationDriven() || numColumns == -1) {
//            return header;
//        }
//
//        header = new String[numColumns + 1];
//
//        BeanField beanField;
//        for (int i = 0; i <= numColumns; i++) {
//            beanField = findField(i);
//            String columnHeaderName = extractHeaderName(beanField);
//            header[i] = columnHeaderName;
//        }
//        return header;
//    }
//
//
//    @Override
//    protected void loadFieldMap() throws CsvBadConverterException {
//        // overriding this method to support setting column order by the custom `CsvBindByNameOrder` annotation
//        if (writeOrder == null && type.isAnnotationPresent(CsvBindByNameOrder.class)) {
//            setColumnOrderOnWrite(
//                new LiteralComparator<>(Arrays.stream(type.getAnnotation(CsvBindByNameOrder.class).value())
//                    .map(String::toUpperCase).toArray(String[]::new)));
//        }
//        super.loadFieldMap();
//    }
//
//    @Override
//    protected CsvConverter determineConverter(Field field, Class<?> elementType, String locale,
//                                              Class<? extends AbstractCsvConverter> customConverter) throws CsvBadConverterException {
//        // overrides the converter for the `CsvDate` to use our custom converter that supports java.time api
//
//        // A custom converter always takes precedence if specified.
//        if (customConverter != null && !customConverter.equals(AbstractCsvConverter.class)) {
//            return super.determineConverter(field, elementType, locale, customConverter);
//        }
//
//        // Perhaps a date instead
//        else if (field.isAnnotationPresent(CsvDate.class)) {
//            String formatString = field.getAnnotation(CsvDate.class).value();
//            return new ConverterDateAndJavaTime(elementType, locale, errorLocale, formatString);
//        }
//
//        return super.determineConverter(field, elementType, locale, customConverter);
//    }
//
//    private String extractHeaderName(final BeanField beanField) {
//        if (beanField == null || beanField.getField() == null) {
//            return StringUtils.EMPTY;
//        }
//
//        if (beanField.getField().isAnnotationPresent(CsvBindByName.class)) {
//            return beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class)[0].column();
//        } else if (beanField.getField().isAnnotationPresent(CsvCustomBindByName.class)) {
//            return beanField.getField().getDeclaredAnnotationsByType(CsvCustomBindByName.class)[0].column();
//        }
//        return StringUtils.EMPTY;
//
//    }
//}
//
