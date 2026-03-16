package Reservation_System;

public class UserData extends BaseUser {
    int id;

    public UserData(int id, String name, String password, String email) {
        super(name, email, password);
        this.id = id;
    }
}