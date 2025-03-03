package com.silverviles.af_assignment.service;

import com.silverviles.af_assignment.common.BudgetType;
import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dao.*;
import com.silverviles.af_assignment.dto.ExpenseReport;
import com.silverviles.af_assignment.dto.IncomeReport;
import com.silverviles.af_assignment.notification.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.silverviles.af_assignment.common.ExceptionCode.*;
import static com.silverviles.af_assignment.util.DateUtil.convertStringToDate;

@Service
@Slf4j
public class MasterServiceImpl implements MasterService {
    private final UserService userService;
    private final CategoryService categoryService;
    private final EmailService emailService;

    @Autowired
    public MasterServiceImpl(UserService userService, CategoryService categoryService, EmailService emailService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.emailService = emailService;
    }

    @Override
    public User findByUsername(String username) throws ServiceException {
        return userService.findByUsername(username);
    }

    @Override
    public User save(User user) throws ServiceException {
        return userService.save(user);
    }

    @Override
    public User update(User user) throws ServiceException {
        return userService.update(user);
    }

    @Override
    public void deleteByUsername(String username) throws ServiceException {
        userService.deleteByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userService.findAll();
    }

    @Override
    public void addIncome(String username, Income income) throws ServiceException {
        User user = userService.findByUsername(username);
        user.addIncome(income);
        userService.updateInternal(user);
    }

    @Override
    public void updateIncome(String username, String id, Income income) throws ServiceException {
        User user = userService.findByUsername(username);
        Income savedIncome = user.getIncomes()
                .stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ServiceException(INCOME_NOT_FOUND));
        if (income.getAmount() != null) {
            savedIncome.setAmount(income.getAmount());
        }
        if (income.getDate() != null) {
            savedIncome.setDate(income.getDate());
        }
        userService.updateInternal(user);
    }

    @Override
    public void deleteIncome(String username, String id) throws ServiceException {
        User user = userService.findByUsername(username);
        Income income = user.getIncomes()
                .stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ServiceException(INCOME_NOT_FOUND));
        user.removeIncome(income);
        userService.updateInternal(user);
    }

    @Override
    public void addExpense(String username, Expense expense) throws ServiceException, IOException {
        if (!categoryService.existsCategory(expense.getCategory())) {
            throw new ServiceException(CATEGORY_NOT_FOUND);
        } else {
            expense.setCategory(categoryService.getCategory(expense.getCategory().getName()));
        }
        User user = userService.findByUsername(username);
        user.addExpense(expense);

        // Check if the expense falls under a certain budget
        List<Budget> budgets = user.getBudgets();
        for (Budget budget : budgets) {
            String message = "";
            if (budget.getCategory().equals(expense.getCategory()) &&
                    budget.getStartDate().isBefore(expense.getDate()) &&
                    budget.getEndDate().isAfter(expense.getDate())) {

                double totalExpenses = user.getExpenses().stream()
                        .filter(e -> e.getCategory().equals(expense.getCategory()) &&
                                e.getDate().isAfter(budget.getStartDate()) &&
                                e.getDate().isBefore(budget.getEndDate()))
                        .mapToDouble(Expense::getAmount)
                        .sum();

                // Provide budget adjustment recommendations
                double averageMonthlySpending = user.getExpenses().stream()
                        .filter(e -> e.getCategory().equals(expense.getCategory()))
                        .mapToDouble(Expense::getAmount)
                        .average()
                        .orElse(0.0);

                if (averageMonthlySpending > budget.getAmount() && averageMonthlySpending != 0) {
                    message += "Based on your spending trends, we recommend increasing your budget for category: "
                            + expense.getCategory().getName() + " to " + averageMonthlySpending + "\n";
                }

                if (totalExpenses > budget.getAmount()) {
                    sendEmail(username, "Budget exceeded",
                            "Budget exceeded for category: " + expense.getCategory().getName()
                                    + "\n" + message);
                    log.warn("Budget exceeded for category: {}", expense.getCategory().getName());
                } else if (totalExpenses > budget.getAmount() * 0.5) {
                    sendEmail(username, "50% of the budget exceeded",
                            "50% of the budget exceeded for category: " + expense.getCategory().getName()
                                    + "\n" + message);
                    log.warn("50% of the budget exceeded for category: {}", expense.getCategory().getName());
                }
            }
        }

        userService.updateInternal(user);
    }

    @Override
    public void updateExpense(String username, String id, Expense expense) throws ServiceException {
        User user = userService.findByUsername(username);
        Expense savedExpense = user.getExpenses()
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ServiceException(EXPENSE_NOT_FOUND));
        if (expense.getAmount() != null) {
            savedExpense.setAmount(expense.getAmount());
        }
        if (expense.getDate() != null) {
            savedExpense.setDate(expense.getDate());
        }
        if (expense.getCategory() != null) {
            if (!categoryService.existsCategory(expense.getCategory())) {
                throw new ServiceException(CATEGORY_NOT_FOUND);
            }
            savedExpense.setCategory(expense.getCategory());
        }
        userService.updateInternal(user);
    }

    @Override
    public void deleteExpense(String username, String id) throws ServiceException {
        User user = userService.findByUsername(username);
        Expense expense = user.getExpenses()
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ServiceException(EXPENSE_NOT_FOUND));
        user.removeExpense(expense);
        userService.updateInternal(user);
    }

    @Override
    public void addCategory(Category category) throws ServiceException {
        categoryService.addCategory(category);
    }

    @Override
    public List<Category> getCategories() throws ServiceException {
        return categoryService.getCategories();
    }

    @Override
    public void deleteCategory(String categoryId) throws ServiceException {
        categoryService.deleteCategory(categoryId);
    }

    @Override
    public Category updateCategoryName(String categoryId, String newName) throws ServiceException {
        return categoryService.updateCategoryName(categoryId, newName);
    }

    @Override
    public boolean existsCategory(Category category) {
        return categoryService.existsCategory(category);
    }

    @Override
    public void addBudget(String username, Budget budget) throws ServiceException {
        User user = userService.findByUsername(username);
        if (budget.getType() == BudgetType.CATEGORY_SPECIFIC && !categoryService.existsCategory(budget.getCategory())) {
            throw new ServiceException(CATEGORY_NOT_FOUND);
        } else if (budget.getType() == BudgetType.CATEGORY_SPECIFIC) {
            budget.setCategory(categoryService.getCategory(budget.getCategory().getName()));
        }
        user.addBudget(budget);
        userService.updateInternal(user);
    }

    @Override
    public List<Budget> getBudgets(String username, String date) throws ServiceException {
        User user = userService.findByUsername(username);
        LocalDate localDate = convertStringToDate(date);
        return user.getBudgets()
                .stream()
                .filter(budget -> budget.getStartDate().isBefore(localDate) && budget.getEndDate().isAfter(localDate))
                .toList();
    }

    @Override
    public IncomeReport getIncomeReport(String name, String start, String end, String tag) throws ServiceException {
        LocalDate startDate = start != null ? convertStringToDate(start) : null;
        LocalDate endDate = end != null ? convertStringToDate(end) : null;
        User user = userService.findByUsername(name);
        List<Income> incomes = user.getIncomes()
                .stream()
                .filter(i -> (start == null || i.getDate().isAfter(startDate)))
                .filter(i -> (end == null || i.getDate().isBefore(endDate)))
                .filter(i -> (tag == null || i.getTags().contains(tag)))
                .toList();
        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        return new IncomeReport("", startDate, endDate, incomes, totalIncome);
    }

    @Override
    public ExpenseReport getExpenseReport(String name, String start, String end, String category, String tag) throws ServiceException {
        if (category != null && !categoryService.existsCategory(new Category(category))) {
            throw new ServiceException(CATEGORY_NOT_FOUND);
        }
        LocalDate startDate = start != null ? convertStringToDate(start) : null;
        LocalDate endDate = end != null ? convertStringToDate(end) : null;
        User user = userService.findByUsername(name);
        List<Expense> expenses = user.getExpenses()
                .stream()
                .filter(e -> (start == null || e.getDate().isAfter(startDate)))
                .filter(e -> (end == null || e.getDate().isBefore(endDate)))
                .filter(e -> (category == null || e.getCategory().getName().equals(category)))
                .filter(e -> (tag == null || e.getTags().contains(tag)))
                .toList();
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        return new ExpenseReport("", startDate, endDate, expenses, totalExpenses);
    }

    @Override
    public void sendEmail(String to, String subject, String body) throws IOException, ServiceException {
        User user = userService.findByUsername(to);
        emailService.sendEmail(user.getEmail(), subject, body);
    }
}
