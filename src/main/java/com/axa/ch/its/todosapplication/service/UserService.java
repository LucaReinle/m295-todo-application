package com.axa.ch.its.todosapplication.service;

import com.axa.ch.its.todosapplication.model.User;
import com.axa.ch.its.todosapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.getAllUsers();
    }

    public User findById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean delete(int id) {
        Optional<User> toDelete = userRepository.deleteById(id);
        return toDelete.isPresent();
    }

    public String login(String email, String password) {
        Optional<User> user = userRepository.findByEmailAndPassword(email, password);
        if (user.isPresent()) {
            user.get().setLoggedIn(true);
            String token = UUID.randomUUID().toString();
            user.get().setToken(token);
            userRepository.save(user.get());
            return token;
        }
        return null;
    }

    public boolean logout(String token) {
        Optional<User> user = userRepository.findByToken(token);
        if (user.isPresent()) {
            user.get().setLoggedIn(false);
            user.get().setToken("");
            userRepository.save(user.get());
            return true;
        }
        return false;
    }
}
