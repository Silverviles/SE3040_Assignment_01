package com.silverviles.af_assignment.repository;

import com.silverviles.af_assignment.dao.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findCategoryById(String id);
    Optional<Category> findCategoryByName(String name);
}
