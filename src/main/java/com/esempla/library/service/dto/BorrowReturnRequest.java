package com.esempla.library.service.dto;

import java.util.List;

public record BorrowReturnRequest(String clientFirstName, String clientLastName, List<String> bookNames) {}
