package com.esempla.library.export;

import java.util.List;

public interface Exporter<T> {
    byte[] export(List<T> data);
}
