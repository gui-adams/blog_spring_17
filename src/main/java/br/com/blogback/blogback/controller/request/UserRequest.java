// UserRequest.java
package br.com.blogback.blogback.controller.request;

import br.com.blogback.blogback.entity.UserRole;

public record UserRequest(String name, String email, String password, UserRole role) {
}
