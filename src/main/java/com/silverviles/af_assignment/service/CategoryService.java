package com.silverviles.af_assignment.service;

import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dao.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(Category category) throws ServiceException;

    List<Category> getCategories(String username) throws ServiceException;

    void deleteCategory(String categoryId) throws ServiceException;
    Category updateCategoryName(String categoryId, String newName) throws ServiceException;

    boolean existsCategory(Category category);
}
