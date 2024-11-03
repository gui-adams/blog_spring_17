package br.com.blogback.blogback.service;

import br.com.blogback.blogback.entity.Category;
import br.com.blogback.blogback.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category updateCategory(Category category) {
        // Verifica se a categoria existe antes de atualizar
        if (categoryRepository.existsById(category.getId())) {
            return categoryRepository.save(category);
        } else {
            throw new IllegalArgumentException("Categoria não encontrada com o ID: " + category.getId());
        }
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
