package br.com.blogback.blogback.controller.request;

import java.util.List;

public record PostRequest(
        String title,
        String content,
        String slug,
        boolean published,
        List<Long> categories

) {

}
