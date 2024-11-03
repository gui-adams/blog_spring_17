package br.com.blogback.blogback.controller.response;

import lombok.Builder;
import java.util.List;

@Builder
public record PostResponse(
        Long id,
        String title,
        String content,
        String slug,
        boolean published,
        String imagePath,
        List<Long> categories
) {}
