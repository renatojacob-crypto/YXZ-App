package com.enzo.yxzapp.dto.common;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(
        List<T> items,
        long totalElements,
        int totalPages,
        int currentPage,
        int pageSize,
        boolean first,
        boolean last,
        boolean empty
) {
    // Método helper para converter Page do Spring para PageResponse
    public static <T, R> PageResponse<R> fromPage(Page<T> page, Function<T, R> mapper) {
        List<R> items = page.getContent()
                .stream()
                .map(mapper)
                .toList();

        return new PageResponse<>(
                items,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty()
        );
    }

    // Sobrecarga para quando já tem os items mapeados
    public static <T> PageResponse<T> fromPage(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty()
        );
    }
}