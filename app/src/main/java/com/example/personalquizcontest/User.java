package com.example.personalquizcontest;

import java.util.Calendar;

public class User {

    public int id;
    public String firstname;
    public String lastname;
    public String nickname;
    public int age;

    public User(String firstname, String lastname, String nickname, int age) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.nickname = nickname;
        this.age = age;
    }

    public User (){}

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstname(String firstname) { this.firstname = firstname; }

    public void setLastname(String lastname) { this.lastname = lastname; }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() { return id; }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAge() {
        return age;
    }
}