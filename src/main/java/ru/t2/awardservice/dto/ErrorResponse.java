package ru.t2.awardservice.dto;

import lombok.Builder;

@Builder
public record ErrorResponse(
        Integer code,
        String message
) {
}
