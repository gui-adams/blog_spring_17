package br.com.blogback.blogback.controller;

import br.com.blogback.blogback.controller.request.PostRequest;
import br.com.blogback.blogback.controller.response.PostResponse;
import br.com.blogback.blogback.entity.Post;
import br.com.blogback.blogback.mapper.PostMapper;
import br.com.blogback.blogback.service.CloudinaryService;
import br.com.blogback.blogback.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/backblog/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CloudinaryService cloudinaryService;  // Usar CloudinaryService para uploads

    @PostMapping
    public ResponseEntity<PostResponse> save(@ModelAttribute PostRequest request) {
        try {
            String imageUrl = null;
            if (request.image() != null && !request.image().isEmpty()) {
                imageUrl = cloudinaryService.uploadImage(request.image());
            }

            Post post = PostMapper.toPost(request);
            post.setImage(imageUrl);  // Define a URL pública da imagem
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

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        return postService.findById(id)
                .map(post -> ResponseEntity.ok(PostMapper.toPostResponse(post)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> update(
            @PathVariable Long id,
            @ModelAttribute PostRequest request) {
        try {
            String imageUrl = null;
            if (request.image() != null && !request.image().isEmpty()) {
                imageUrl = cloudinaryService.uploadImage(request.image());
            }

            Post postToUpdate = PostMapper.toPost(request);
            postToUpdate.setImage(imageUrl);  // Define a URL pública da imagem se atualizada
            return postService.update(id, postToUpdate)
                    .map(post -> ResponseEntity.ok(PostMapper.toPostResponse(post)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> findByCategory(@RequestParam Long category) {
        return ResponseEntity.ok(postService.findByCategory(category)
                .stream()
                .map(PostMapper::toPostResponse)
                .toList());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Post> optPost = postService.findById(id);
        if (optPost.isPresent()) {
            postService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
