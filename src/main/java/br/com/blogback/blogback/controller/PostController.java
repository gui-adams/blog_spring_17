package br.com.blogback.blogback.controller;

import br.com.blogback.blogback.controller.request.PostRequest;
import br.com.blogback.blogback.controller.response.PostResponse;
import br.com.blogback.blogback.entity.Post;
import br.com.blogback.blogback.mapper.PostMapper;
import br.com.blogback.blogback.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backblog/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> save(@RequestBody PostRequest request){
        Post savedPost = postService.save(PostMapper.toPost(request));
        return ResponseEntity.ok(PostMapper.toPostResponse(savedPost));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll(){
        return ResponseEntity.ok(postService.findAll()
                .stream()
                .map(PostMapper::toPostResponse)
                .toList());

    }
}



