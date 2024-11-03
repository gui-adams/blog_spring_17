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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/backblog/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @ModelAttribute PostRequest request,
            @RequestParam("image") MultipartFile image) {
        try {
            Post post = PostMapper.toPost(request);
            Post savedPost = postService.savePost(post, Optional.of(image), request.categories());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(PostMapper.toPostResponse(savedPost));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.findAll().stream()
                .map(PostMapper::toPostResponse)
                .toList();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        return postService.findById(id)
                .map(post -> ResponseEntity.ok(PostMapper.toPostResponse(post)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @ModelAttribute PostRequest request,
            @RequestParam("image") Optional<MultipartFile> image) {
        try {
            Optional<Post> updatedPost = postService.updatePost(id, request, image, request.categories());
            return updatedPost
                    .map(post -> ResponseEntity.ok(PostMapper.toPostResponse(post)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
