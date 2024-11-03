package br.com.blogback.blogback.service;

import br.com.blogback.blogback.controller.request.PostRequest;
import br.com.blogback.blogback.entity.Category;
import br.com.blogback.blogback.entity.Post;
import br.com.blogback.blogback.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final FileStorageService fileStorageService;

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post savePost(Post post, Optional<MultipartFile> imageFile, List<Long> categoryIds) throws IOException {
        imageFile.ifPresent(file -> {
            try {
                String imagePath = fileStorageService.saveFile(file);
                post.setImage(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao salvar imagem", e);
            }
        });

        List<Category> categories = categoryService.findAllById(categoryIds);
        post.setCategories(categories);

        return postRepository.save(post);
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public Optional<Post> updatePost(Long id, PostRequest request, Optional<MultipartFile> imageFile, List<Long> categoryIds) throws IOException {
        return postRepository.findById(id).map(existingPost -> {
            existingPost.setTitle(request.title());
            existingPost.setContent(request.content());
            existingPost.setSlug(request.slug());
            existingPost.setPublished(request.published());

            // Atualizar imagem
            imageFile.ifPresent(file -> {
                try {
                    String imagePath = fileStorageService.saveFile(file);
                    existingPost.setImage(imagePath);
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao salvar imagem", e);
                }
            });

            // Atualizar categorias
            List<Category> categories = categoryService.findAllById(categoryIds);
            existingPost.setCategories(categories);

            return postRepository.save(existingPost);
        });
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
