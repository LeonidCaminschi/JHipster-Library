package com.esempla.library.export;

import java.util.List;

public interface Exporter {
    byte[] export(List<?> data);
}
