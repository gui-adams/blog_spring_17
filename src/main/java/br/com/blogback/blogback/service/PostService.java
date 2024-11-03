package br.com.blogback.blogback.service;


import br.com.blogback.blogback.entity.Post;
import br.com.blogback.blogback.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post save(Post post){
        return postRepository.save(post);
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }
}