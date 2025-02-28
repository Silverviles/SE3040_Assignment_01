package com.silverviles.af_assignment.controller;

import com.silverviles.af_assignment.common.BaseController;
import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dao.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController extends BaseController {
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/add")
    public String addCategory(@RequestBody Category category) throws ServiceException {
        log.info("Adding category: {}", category);
        masterService.addCategory(category);
        return HttpStatus.CREATED.getReasonPhrase();
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/delete/{categoryId}")
    public void deleteCategory(@PathVariable String categoryId) throws ServiceException {
        log.info("Deleting category: {}", categoryId);
        masterService.deleteCategory(categoryId);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/update")
    public Category updateCategoryName(@RequestBody Category category) throws ServiceException {
        log.info("Updating category name: {} to {}", category.getId(), category.getName());
        return masterService.updateCategoryName(category.getId(), category.getName());
    }

    @GetMapping("/get")
    public List<Category> getCategories(String username) throws ServiceException {
        log.info("Getting categories for user: {}", username);
        return masterService.getCategories(username);
    }
}
