package com.phdljr.springbootoauth2clientjwt.repository;

import com.phdljr.springbootoauth2clientjwt.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
}
