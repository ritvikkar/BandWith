package com.ritvikkar.bandwith;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserAccount extends RealmObject {

    @PrimaryKey
    private String id;
    private String email;
    private String password;

    public UserAccount() {
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }
}
