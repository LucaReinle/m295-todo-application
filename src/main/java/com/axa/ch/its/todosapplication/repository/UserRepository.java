package com.axa.ch.its.todosapplication.repository;

import com.axa.ch.its.todosapplication.model.User;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Repository
public class UserRepository {
    private List<User> allUsers;

    public UserRepository() {
        allUsers = new ArrayList<>();
        allUsers.add(new User(1, "admin@axa.ch", "admin", false, ""));
    }

    public Optional<User> findById(int id) {
        return allUsers.stream().filter(u -> u.getId() == id).findFirst();
    }

    public Optional<User> findByEmailAndPassword(String email, String password) {
        return allUsers.stream().filter(u -> u.getEmail().equals(email) && u.getPassword().equals(password)).findFirst();
    }

    public Optional<User> findByToken(String token) {
        return allUsers.stream().filter(u -> u.getToken().equals(token)).findFirst();
    }

    public User save(User entity) throws IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("The given Entity is null");
        }
        Optional<User> existingUser = findById(entity.getId());
        if (existingUser.isPresent()) {
            allUsers.set(allUsers.indexOf(existingUser.get()), entity);
        } else {
            int index = 0;
            for (User t : allUsers) {
                if (t.getId() - index > 1) {
                    index++;
                    break;
                }
                index++;
            }
            if (allUsers.size() == index) {
                index++;
            }
            entity.setId(index);
            allUsers.add(entity.getId() - 1, entity);
        }
        return entity;
    }

    public Optional<User> deleteById(int id) {
        Optional<User> toDelete = findById(id);
        toDelete.ifPresent(user -> allUsers.remove(user));
        return toDelete;
    }
}
