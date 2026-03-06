package com.aviation.routing.flight.path.engine.common.payload;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageData<T>(
    List<T> content,
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages
) {
    public static <T> PageData<T> from(Page<T> page) {
        return new PageData<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}