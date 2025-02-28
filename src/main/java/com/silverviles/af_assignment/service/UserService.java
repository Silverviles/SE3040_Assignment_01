package com.silverviles.af_assignment.service;

import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dao.Expense;
import com.silverviles.af_assignment.dao.Income;
import com.silverviles.af_assignment.dao.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findByUsername(String username) throws ServiceException;
    User save(User user) throws ServiceException;
    User update(User user) throws ServiceException;
    User updateInternal(User user) throws ServiceException;
    void deleteByUsername(String username) throws ServiceException;
    List<User> findAll();
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
