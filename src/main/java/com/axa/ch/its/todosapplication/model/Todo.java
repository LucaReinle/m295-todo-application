package com.axa.ch.its.todosapplication.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    private int userId;
    private int id;
    private String title;
    private boolean completed;
    private LocalDateTime deadline;
}
