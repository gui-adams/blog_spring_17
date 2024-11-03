package br.com.blogback.blogback.repository;

import br.com.blogback.blogback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
