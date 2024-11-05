package br.com.blogback.blogback.controller.request;

public record PasswordUpdateRequest(String oldPassword, String newPassword) {}

