package br.com.blogback.blogback.config;

import br.com.blogback.blogback.config.JWTUserData;
import br.com.blogback.blogback.entity.User;
import br.com.blogback.blogback.repository.UserRepository;
import br.com.blogback.blogback.config.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService; // Injeção do TokenService
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Ignora requisições OPTIONS para preflight CORS
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Recupera o token do cabeçalho da requisição
        String token = recoverToken(request);

        // Valida o token e obtém as informações do usuário se o token for válido
        if (token != null) {
            JWTUserData userData = tokenService.validateToken(token);
            if (userData != null) {
                // Busca o usuário no banco de dados pelo e-mail como User
                Optional<User> optionalUser = userRepository.findByEmail(userData.email());

                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();

                    // Define as autoridades com base no papel do usuário
                    var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

                    // Configura o contexto de segurança
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // Usuário não encontrado no banco, responde com 401
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } else {
                // Token inválido, responde com 401
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        // Continua o processamento da requisição
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para recuperar o token do cabeçalho "Authorization"
    @Nullable
    private String recoverToken(@NonNull HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null; // Retorna null se o cabeçalho estiver ausente ou incorreto
        }
        return authHeader.replace("Bearer ", ""); // Remove o prefixo "Bearer " para obter o token
    }
}
