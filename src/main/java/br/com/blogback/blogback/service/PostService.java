package br.com.blogback.blogback.service;

import br.com.blogback.blogback.entity.Category;
import br.com.blogback.blogback.entity.Post;
import br.com.blogback.blogback.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;

    public Post save(Post post) {
        post.setCategories(this.findCategories(post.getCategories()));
        post.setCreatedAt(LocalDateTime.now());  // Certifique-se de definir a data de criação no primeiro salvamento
        return postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
    public List<Post> findByCategory(Long categoryId) {
        return postRepository.findPostByCategories(List.of(Category.builder().id(categoryId).build()));
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public void delete (Long postId) {
        postRepository.deleteById(postId);
    }

    public Optional<Post> update(Long postId, Post updatePost) {
        Optional<Post> optPost = postRepository.findById(postId);

        if (optPost.isPresent()) {
            Post post = optPost.get();

            // Atualizando campos principais
            post.setTitle(updatePost.getTitle());
            post.setContent(updatePost.getContent());
            post.setSlug(updatePost.getSlug());
            post.setPublished(updatePost.isPublished());
            post.setUpdatedAt(LocalDateTime.now());  // Atualize a data de modificação

            // Atualizando categorias
            List<Category> categories = this.findCategories(updatePost.getCategories());
            post.getCategories().clear();
            post.getCategories().addAll(categories);

            // Atualizando imagem condicionalmente
            if (updatePost.getImage() != null) {
                post.setImage(updatePost.getImage());
            } else {
            }

            postRepository.save(post);
            return Optional.of(post);

        } else {
            return Optional.empty();
        }
    }

    private List<Category> findCategories(List<Category> categories) {
        List<Category> categoriesFound = new ArrayList<>();
        categories.forEach(category -> categoryService.findById(category.getId())
                .ifPresentOrElse(
                        categoriesFound::add,
                        () -> log.warn("Categoria com ID: {} não encontrada", category.getId())
                ));
        return categoriesFound;
    }
}
