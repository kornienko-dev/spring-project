package com.kornienko.spring.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse<T> {

    List<T> content;
    Metadata metadata;

    public static <T> PageResponse<T> of(Page<T> page) {
        Metadata metadata = new Metadata(page.getNumber(), page.getSize(), page.getTotalElements());
        return new PageResponse<>(page.getContent(), metadata);
    }

    @Value
    public static class Metadata {

        int page;
        int size;
        long totalElements;
    }
}
