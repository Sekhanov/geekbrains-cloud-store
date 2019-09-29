package ru.skhanov.mycloudstoreserver.service;

import lombok.Getter;
import lombok.Setter;

/**
 * User
 */
@Getter
@Setter
public class User {
    String login;
    String password;

    public User(){};

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    
}