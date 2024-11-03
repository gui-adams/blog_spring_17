package br.com.blogback.blogback.controller.response;


import lombok.Builder;

@Builder
public record UserResponse(Long id, String name, String email) {
}
