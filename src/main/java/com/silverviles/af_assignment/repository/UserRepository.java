package com.silverviles.af_assignment.repository;

import com.silverviles.af_assignment.dao.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByUsername(String username);

    void deleteUserByUsername(String username);

    boolean existsUserByUsername(String username);
}
