package com.prevails.Prevails.dao;

import com.prevails.Prevails.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("testUser")
public class TestUserDataAccessService implements UserDao {

    private static List<User> DB = new ArrayList<>();
    @Override
    public int insertUser(UUID id, String token, String username, String firstName, String lastName, String age, String password, String email) {
        DB.add(new User(id, token, username, firstName, lastName, age, password, email));
        return 1;
    }

    @Override
    public List<User> selectAllUsers() {
        return DB;
    }

    @Override
    public Optional<User> selectUserById(UUID id) {
        return DB.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public int deleteUserByID(UUID id) {
        Optional<User> userMaybe = selectUserById(id);
        if (userMaybe.isEmpty()) {
            return 0;
        }
        DB.remove(userMaybe.get());
        return 1;
    }

    @Override
    public int updateUserById(UUID id, User updateUser) {
        return selectUserById(id)
                .map(user -> {
                    int indexOfUserToUpdate = DB.indexOf(user);
                    if (indexOfUserToUpdate >= 0) {
                        DB.set(indexOfUserToUpdate, new User(id, updateUser.getToken(), updateUser.getUsername(), updateUser.getFirstName(), updateUser.getLastName(), updateUser.getAge(), updateUser.getPassword(), updateUser.getEmail()));
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);

    }
}
