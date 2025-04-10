package com.esempla.library.export;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CsvBindByNameOrder {
    String[] value() default {};
}
