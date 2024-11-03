package br.com.blogback.blogback.controller;

import br.com.blogback.blogback.controller.request.UserRequest;
import br.com.blogback.blogback.controller.response.UserResponse;
import br.com.blogback.blogback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private UserService userService;

    public ResponseEntity<UserResponse> register (UserRequest request){

    }
}
