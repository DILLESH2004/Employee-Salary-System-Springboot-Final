package com.example.springjdbc.DTO;


import java.util.Set;

public class RegisterRequest {
    private String username;
    private String password;
    private Set<String> roleNames;

    // Getters and setters
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

    public Set<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleName(Set<String> roleName) {
        this.roleNames = roleName;
    }
}
