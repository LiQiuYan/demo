package com.example.demo.service;

import com.example.demo.domain.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getOne(int id);

    void insert(User user);

    void update(User user);

    void delete(int id);
}
