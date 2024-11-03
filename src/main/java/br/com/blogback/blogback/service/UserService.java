package br.com.blogback.blogback.service;

import br.com.blogback.blogback.entity.User;
import br.com.blogback.blogback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }
}
