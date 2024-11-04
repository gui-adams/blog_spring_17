// UserMapper.java
package br.com.blogback.blogback.mapper;

import br.com.blogback.blogback.controller.request.UserRequest;
import br.com.blogback.blogback.controller.response.UserResponse;
import br.com.blogback.blogback.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toUser(UserRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .role(request.role())  // Mapeando o campo role
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
