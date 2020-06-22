package com.zj.domain;

import java.io.Serializable;

/**
 * Created by ZJ on 2020/6/22
 * comment:
 */
public class Account implements Serializable {

    private String username;
    private String password;
    private Double money;
    private User user;

    public Account(String username, String password, Double money, User user) {
        this.username = username;
        this.password = password;
        this.money = money;
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", money=" + money +
                ", user=" + user +
                '}';
    }
}
