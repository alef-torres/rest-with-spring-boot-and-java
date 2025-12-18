package com.github.alef_torres.data.dto.security;

import java.io.Serializable;
import java.util.Objects;

public class AccountCredencialDTO implements Serializable {

    private String userName;
    private String password;

    public AccountCredencialDTO() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountCredencialDTO that = (AccountCredencialDTO) o;
        return Objects.equals(getUserName(), that.getUserName()) && Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName(), getPassword());
    }
}
