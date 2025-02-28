package com.silverviles.af_assignment.service;

import com.silverviles.af_assignment.common.ExceptionCode;
import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dao.Category;
import com.silverviles.af_assignment.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category addCategory(Category category) throws ServiceException {
        if (isCategoryNameExists(category.getName()) && isCategoryExists(category.getId())) {
            throw new ServiceException(ExceptionCode.CATEGORY_ALREADY_EXISTS);
        } else if (isCategoryNameExists(category.getName())) {
            throw new ServiceException(ExceptionCode.DUPLICATE_CATEGORY_NAME);
        } else if (isCategoryExists(category.getId())) {
            throw new ServiceException(ExceptionCode.DUPLICATE_CATEGORY_ID);
        }
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getCategories(String username) {
        return categoryRepository.findAll();
    }

    @Override
    public void deleteCategory(String categoryId) throws ServiceException {
        if (!isCategoryExists(categoryId)) {
            throw new ServiceException(ExceptionCode.CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public Category updateCategoryName(String categoryId, String newName) throws ServiceException {
        if (!isCategoryExists(categoryId)) {
            throw new ServiceException(ExceptionCode.CATEGORY_NOT_FOUND);
        } else if (isCategoryNameExists(newName)) {
            throw new ServiceException(ExceptionCode.DUPLICATE_CATEGORY_NAME);
        }
        Category category = categoryRepository.findCategoryById(categoryId);
        category.setName(newName);
        return categoryRepository.save(category);
    }

    @Override
    public boolean existsCategory(Category category) {
        return isCategoryNameExists(category.getName()) || isCategoryExists(category.getId());
    }

    private boolean isCategoryNameExists(String name) {
        return categoryRepository.findCategoryByName(name).isPresent();
    }

    private boolean isCategoryExists(String id) {
        return categoryRepository.findCategoryById(id) != null;
    }
}
