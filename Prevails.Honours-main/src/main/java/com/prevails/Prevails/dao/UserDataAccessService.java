package com.prevails.Prevails.dao;

import com.prevails.Prevails.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository("postgres")
public class UserDataAccessService implements UserDao{
    @Override
    public int insertUser(UUID id, String token, String username, String firstName, String lastName, String age, String password, String email) {
        return 0;
    }

    @Override
    public List<User> selectAllUsers() {
        return List.of(new User(UUID.randomUUID(), "FROM POSTGRES DB", "FROM POSTGRES DB", "FROM POSTGRES DB", "FROM POSTGRES DB", "FROM POSTGRES DB", "FROM POSTGRES DB", "FROM POSTGRES DB"));
    }

    @Override
    public Optional<User> selectUserById(UUID id) {
        return Optional.empty();
    }

    @Override
    public int deleteUserByID(UUID id) {
        return 0;
    }

    @Override
    public int updateUserById(UUID id, User user) {
        return 0;
    }
}
