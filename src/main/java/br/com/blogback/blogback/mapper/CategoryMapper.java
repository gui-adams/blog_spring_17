package br.com.blogback.blogback.mapper;

import br.com.blogback.blogback.controller.request.CategoryRequest;
import br.com.blogback.blogback.controller.response.CategoryResponse;
import br.com.blogback.blogback.entity.Category;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryMapper {

    public static Category toCategory(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.name())
                .slug(categoryRequest.slug())
                .build();
    }

    public static CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .build();
    }
}
