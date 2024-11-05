package br.com.blogback.blogback.controller;

import br.com.blogback.blogback.config.TokenService;
import br.com.blogback.blogback.controller.request.LoginRequest;
import br.com.blogback.blogback.controller.request.PasswordUpdateRequest;
import br.com.blogback.blogback.controller.request.UserRequest;
import br.com.blogback.blogback.controller.response.LoginResponse;
import br.com.blogback.blogback.controller.response.UserResponse;
import br.com.blogback.blogback.entity.User;
import br.com.blogback.blogback.entity.UserRole;
import br.com.blogback.blogback.mapper.UserMapper;
import br.com.blogback.blogback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backblog/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest request) {
        User newUser = UserMapper.toUser(request);
        newUser.setRole(UserRole.USER);  // Define o role como USER
        User savedUser = userService.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserResponse(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(
                request.email(), request.password()
        );
        Authentication authenticate = authenticationManager.authenticate(userAndPass);

        User user = (User) authenticate.getPrincipal();
        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        boolean isValid = tokenService.verifyToken(token).isPresent();
        return isValid ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Endpoint para atualizar a senha do usuário autenticado
    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(
            @AuthenticationPrincipal User currentUser,
            @RequestBody PasswordUpdateRequest passwordUpdateRequest) {

        // Verifica se a senha antiga está correta
        if (!passwordEncoder.matches(passwordUpdateRequest.oldPassword(), currentUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Senha atual incorreta.");
        }

        // Atualiza a senha para a nova
        currentUser.setPassword(passwordEncoder.encode(passwordUpdateRequest.newPassword()));
        userService.save(currentUser);

        return ResponseEntity.ok("Senha atualizada com sucesso.");
    }
}
