package com.axa.ch.its.todosapplication.controller;

import com.axa.ch.its.todosapplication.model.User;
import com.axa.ch.its.todosapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/users")
public class UsersRESTController {
    private UserService userService;

    @Autowired
    public UsersRESTController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllTodos() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getTodoById(@PathVariable int id) {
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createTodo(@RequestBody User newUser) {
        newUser = userService.save(newUser);
        if (newUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateTodo(@PathVariable int id, @RequestBody User toUpdate) {
        toUpdate.setId(id);
        int todoSize = userService.getAll().size();
        toUpdate = userService.save(toUpdate);
        if (toUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (todoSize < toUpdate.getId()) {
            return new ResponseEntity<>(toUpdate, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(toUpdate, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable int id) {
        boolean success = userService.delete(id);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginData) {
        String token = userService.login(loginData.get("email"), loginData.get("password"));
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Map<String, String> token) {
        boolean success = userService.logout(token.get("token"));
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> changePasswordOrEmail(@PathVariable int id, @RequestBody Map<String, String> dataToChange) {
        User user = userService.findById(id);
        if (dataToChange.get("email") != null) {
            user.setEmail(dataToChange.get("email"));
        }
        if (dataToChange.get("password") != null) {
            user.setPassword(dataToChange.get("password"));
        }
        if (dataToChange.get("email") == null && dataToChange.get("password") == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
