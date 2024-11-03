package br.com.blogback.blogback.service;


import br.com.blogback.blogback.entity.User;
import br.com.blogback.blogback.repository.UseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UseRepository useRepository;

    public User save (User user){
        return useRepository.save(user);
    }
}
