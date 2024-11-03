package br.com.blogback.blogback.controller.response;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record CategoryResponse(Long id, String name, String slug, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
