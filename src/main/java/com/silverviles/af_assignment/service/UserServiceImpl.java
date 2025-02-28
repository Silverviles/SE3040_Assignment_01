package com.silverviles.af_assignment.service;

import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dao.User;
import com.silverviles.af_assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.silverviles.af_assignment.common.ExceptionCode.USER_ALREADY_EXISTS;
import static com.silverviles.af_assignment.common.ExceptionCode.USER_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) throws ServiceException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
        user.setPassword(null);
        return user;
    }

    @Override
    public User save(User user) throws ServiceException {
        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new ServiceException(USER_ALREADY_EXISTS);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(User user) throws ServiceException {
        if (!userRepository.existsUserByUsername(user.getUsername())) {
            throw new ServiceException(USER_NOT_FOUND);
        }
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public User updateInternal(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteByUsername(String username) throws ServiceException {
        if (!userRepository.existsUserByUsername(username)) {
            throw new ServiceException(USER_NOT_FOUND);
        }
        userRepository.deleteUserByUsername(username);
    }

    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
