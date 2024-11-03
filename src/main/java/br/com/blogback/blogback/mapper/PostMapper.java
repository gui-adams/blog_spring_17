package br.com.blogback.blogback.mapper;

import br.com.blogback.blogback.controller.request.PostRequest;
import br.com.blogback.blogback.controller.response.PostResponse;
import br.com.blogback.blogback.entity.Category;
import br.com.blogback.blogback.entity.Post;
import lombok.experimental.UtilityClass;

import java.util.stream.Collectors;

@UtilityClass
public class PostMapper {

    public Post toPost(PostRequest request) {
        return Post.builder()
                .title(request.title())
                .content(request.content())
                .slug(request.slug())
                .published(request.published())
                .build();
    }

    public PostResponse toPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .slug(post.getSlug())
                .published(post.isPublished())
                .imagePath(post.getImage())
                .categories(post.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toList()))
                .build();
    }
}
