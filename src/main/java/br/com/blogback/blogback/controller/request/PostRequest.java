package br.com.blogback.blogback.controller.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PostRequest(
        String title,
        String content,
        String slug,
        boolean published,
        List<Long> categories,
        MultipartFile image // Permite o upload de imagem
) {

}
