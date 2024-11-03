package br.com.blogback.blogback.repository;

import br.com.blogback.blogback.entity.Category;
import br.com.blogback.blogback.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findPostByCategories(List<Category> categories);}
