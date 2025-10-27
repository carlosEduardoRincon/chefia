package com.chefia.core.dto;

import java.util.List;

public record ValidationErrorDTO(
        List<String> errors,
        int status
) {
}
