package com.prevails.Prevails.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private final UUID id;
    private ArrayList<String> friends;
    @NotBlank
    private  String token;
    @NotBlank
    private final String username;
    @NotBlank
    private final String firstName;
    @NotBlank
    private final String lastName;
    @NotBlank
    private final String age;
    @NotBlank
    private final String password;
    @NotBlank
    private final String email;

    public User(@JsonProperty("id") UUID id,
                @JsonProperty("token") String token,
                @JsonProperty("username") String username,
                @JsonProperty("firstName")String firstName,
                @JsonProperty("lastName")String lastName,
                @JsonProperty("age")String age,
                @JsonProperty("password")String password,
                @JsonProperty("email")String email) {


        this.id = id;
        this.token = token;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.friends = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public String getAge() {
        return age;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public ArrayList<String> getFriends(){
        return friends;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setFriends(List<String> friends) {
        this.friends= (ArrayList<String>) friends;
    }

    public void addToFriends(UUID uuid) {
        this.friends.add(String.valueOf(uuid));
    }
}