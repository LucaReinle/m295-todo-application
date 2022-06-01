package com.axa.ch.its.todosapplication.service;

import com.axa.ch.its.todosapplication.model.Todo;
import com.axa.ch.its.todosapplication.repository.TodosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private TodosRepository todosRepository;

    @Autowired
    public TodoService(TodosRepository todosRepository) {
        this.todosRepository = todosRepository;
    }

    public List<Todo> getAll() {
        return todosRepository.getAllTodos();
    }

    public Todo findById(int id) {
        Optional<Todo> todo = todosRepository.findById(id);
        return todo.orElse(null);
    }

    public Todo save(Todo todo) {
        try {
            return todosRepository.save(todo);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Todo setStatus(int id, String status) {
        Optional<Todo> toUpdate = todosRepository.findById(id);
        if (toUpdate.isPresent()) {
            toUpdate.get().setCompleted(status.equals("complete"));
            todosRepository.save(toUpdate.get());
            return toUpdate.get();
        }
        return null;
    }

    public Todo setDeadline(int id, LocalDateTime newDeadline) {
        Optional<Todo> toUpdate = todosRepository.findById(id);
        if (toUpdate.isPresent() && newDeadline != null) {
            toUpdate.get().setDeadline(newDeadline);
            todosRepository.save(toUpdate.get());
            return toUpdate.get();
        }
        return null;
    }

    public boolean delete(int id) {
        Optional<Todo> toDelete = todosRepository.deleteById(id);
        return toDelete.isPresent();
    }
}
