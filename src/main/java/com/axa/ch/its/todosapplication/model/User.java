package com.axa.ch.its.todosapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String email;
    private String password;
    private boolean loggedIn;
    private String token;
}
