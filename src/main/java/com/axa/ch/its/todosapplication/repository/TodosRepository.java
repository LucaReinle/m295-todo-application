package com.axa.ch.its.todosapplication.repository;

import com.axa.ch.its.todosapplication.model.Todo;
import lombok.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Repository
public class TodosRepository {
    private List<Todo> allTodos;

    public TodosRepository() {
        allTodos = new ArrayList<>();
        allTodos.add(new Todo(1, 1, "delectus aut autem", false, LocalDateTime.now()));
        allTodos.add(new Todo(1, 2, "quis ut nam facilis et officia qui", false, LocalDateTime.now()));
        allTodos.add(new Todo(2, 3, "fugiat veniam minus", true, LocalDateTime.now()));
        allTodos.add(new Todo(2, 4, "laboriosam mollitia et enim quasi adipisci quia provident illum", true, LocalDateTime.now()));
        allTodos.add(new Todo(3, 5, "qui ullam ratione quibusdam voluptatem quia omnis", false, LocalDateTime.now()));
        allTodos.add(new Todo(3, 6, "illo expedita consequatur quia in", true, LocalDateTime.now()));
        allTodos.add(new Todo(3, 7, "quo adipisci enim quam ut ab", false, LocalDateTime.now()));
        allTodos.add(new Todo(4, 8, "molestiae perspiciatis ipsa", false, LocalDateTime.now()));
        allTodos.add(new Todo(4, 9, "illo est ratione doloremque quia maiores aut", true, LocalDateTime.now()));
    }

    public Optional<Todo> findById(int id) {
        return allTodos.stream().filter(t -> t.getId() == id).findFirst();
    }

    public Todo save(Todo entity) throws IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("The given Entity is null");
        }
        Optional<Todo> existingTodo = findById(entity.getId());
        if (existingTodo.isPresent()) {
            allTodos.set(allTodos.indexOf(existingTodo.get()), entity);
        } else {
            int index = 0;
            for (Todo t : allTodos) {
                if (t.getId() - index > 1) {
                    index++;
                    break;
                }
                index++;
            }
            if (allTodos.size() == index) {
                index++;
            }
            entity.setId(index);
            allTodos.add(entity.getId() - 1, entity);
        }
        return entity;
    }

    public Optional<Todo> deleteById(int id) {
        Optional<Todo> toDelete = findById(id);
        toDelete.ifPresent(todo -> allTodos.remove(todo));
        return toDelete;
    }
}
