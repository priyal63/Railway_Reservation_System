package Reservation_System;

public class BaseUser {
    String name;
    String email;
    String password;

    public BaseUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}