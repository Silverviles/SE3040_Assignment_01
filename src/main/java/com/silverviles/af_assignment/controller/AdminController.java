package com.silverviles.af_assignment.controller;

import com.silverviles.af_assignment.common.BaseController;
import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dao.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController extends BaseController {
    @DeleteMapping("/user")
    public String deleteUser(@RequestBody Map<String, String> payload) throws ServiceException {
        String username = payload.get("username");
        log.info("Delete user by username: {}", username);
        masterService.deleteByUsername(username);
        return HttpStatus.OK.getReasonPhrase();
    }

    @GetMapping("/user")
    public User getUser(@RequestParam String username) throws ServiceException {
        log.info("Get user by username: {}", username);
        return masterService.findByUsername(username);
    }

    @GetMapping("/users")
    public Iterable<User> getUsers() {
        log.info("Get all users");
        return masterService.findAll();
    }
}
