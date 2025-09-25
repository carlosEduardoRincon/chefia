package com.chefia.core.port.dto;

import java.util.List;

public record ValidationErrorDTO(
        List<String> errors,
        int status
) {
}
