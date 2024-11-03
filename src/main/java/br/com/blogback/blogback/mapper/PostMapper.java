package br.com.blogback.blogback.mapper;

import br.com.blogback.blogback.controller.request.PostRequest;
import br.com.blogback.blogback.controller.response.CategoryResponse;
import br.com.blogback.blogback.controller.response.PostResponse;
import br.com.blogback.blogback.entity.Category;
import br.com.blogback.blogback.entity.Post;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PostMapper {

    public static Post toPost(PostRequest request) {
        List<Category> categories = request.categories().stream()
                .map(categoryId -> Category.builder().id(categoryId).build())
                .toList();

        return Post.builder()
                .title(request.title())
                .content(request.content())
                .slug(request.slug())
                .published(request.published())
                .categories(categories)
                .build();
    }

    public static PostResponse toPostResponse(Post post) {
        List<CategoryResponse> categories = post.getCategories()
                .stream()
                .map(category -> CategoryMapper.toCategoryResponse(category))
                .toList();

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .slug(post.getSlug())
                .published(post.isPublished())
                .imagePath(post.getImage())
                .categories(categories)
                .build();
    }
}
