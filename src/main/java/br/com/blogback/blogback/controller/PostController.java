package br.com.blogback.blogback.controller;

import br.com.blogback.blogback.controller.request.PostRequest;
import br.com.blogback.blogback.controller.response.PostResponse;
import br.com.blogback.blogback.entity.Post;
import br.com.blogback.blogback.mapper.PostMapper;
import br.com.blogback.blogback.service.FileStorageService;
import br.com.blogback.blogback.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/backblog/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<PostResponse> save(@ModelAttribute PostRequest request) {
        try {
            String imagePath = null;
            if (request.image() != null && !request.image().isEmpty()) {
                imagePath = fileStorageService.saveFile(request.image());
            }

            Post post = PostMapper.toPost(request);
            post.setImage(imagePath);
            Post savedPost = postService.save(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(PostMapper.toPostResponse(savedPost));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll() {
        return ResponseEntity.ok(postService.findAll()
                .stream()
                .map(PostMapper::toPostResponse)
                .toList());
    }
}
