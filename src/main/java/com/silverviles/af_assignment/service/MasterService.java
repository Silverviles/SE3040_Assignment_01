package com.silverviles.af_assignment.service;

import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dao.Category;
import com.silverviles.af_assignment.dao.Expense;
import com.silverviles.af_assignment.dao.Income;
import com.silverviles.af_assignment.dao.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface MasterService {
    User findByUsername(String username) throws ServiceException;
    User save(User user) throws ServiceException;
    User update(User user) throws ServiceException;
    void deleteByUsername(String username) throws ServiceException;
    List<User> findAll();
    void addIncome(String username, Income income) throws ServiceException;
    void updateIncome(String username, String id, Income income) throws ServiceException;
    void deleteIncome(String username, String id) throws ServiceException;
    void addExpense(String username, Expense expense) throws ServiceException;
    void updateExpense(String username, String id, Expense expense) throws ServiceException;
    void deleteExpense(String username, String id) throws ServiceException;
    Double getBudget(String username, String startDate, String endDate) throws ServiceException;
    void addCategory(Category category) throws ServiceException;
    List<Category> getCategories(String username) throws ServiceException;
    void deleteCategory(String categoryId) throws ServiceException;
    Category updateCategoryName(String categoryId, String newName) throws ServiceException;
    boolean existsCategory(Category category);
}
