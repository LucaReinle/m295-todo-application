package com.axa.ch.its.todosapplication.controller;

import com.axa.ch.its.todosapplication.model.Todo;
import com.axa.ch.its.todosapplication.service.TodoService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController @RequestMapping("/todos")
public class TodosRESTController {
    private TodoService todoService;

    @Autowired
    public TodosRESTController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        return new ResponseEntity<>(todoService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable int id) {
        Todo todo = todoService.findById(id);
        if (todo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo newTodo) {
        newTodo = todoService.save(newTodo);
        if (newTodo == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newTodo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable int id, @RequestBody Todo toUpdate) {
        toUpdate.setId(id);
        int todoSize = todoService.getAll().size();
        toUpdate = todoService.save(toUpdate);
        if (toUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (todoSize < toUpdate.getId()) {
            return new ResponseEntity<>(toUpdate, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(toUpdate, HttpStatus.OK);
    }

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<Todo> setTodoStatus(@PathVariable int id, @PathVariable String status) {
        if (status.equals("complete") || status.equals("incomplete")) {
            Todo toUpdate = todoService.setStatus(id, status);
            if (toUpdate == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(toUpdate, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/{id}/newDeadline")
    public ResponseEntity<Todo> setDeadlineTodo(@PathVariable int id, @RequestBody Map<String, LocalDateTime> newDeadline) {
        Todo toUpdate;
        try {
            toUpdate = todoService.setDeadline(id,newDeadline.get("deadline"));
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (toUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(toUpdate, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Todo> deleteTodo(@PathVariable int id) {
        boolean success = todoService.delete(id);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
