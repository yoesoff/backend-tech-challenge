package com.mhyusuf.auth.service;

import com.mhyusuf.auth.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    User createUser(User user);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserById(Integer id);
    void deleteUserById(Integer id);
}
