package com.prevails.Prevails.dao;

import com.prevails.Prevails.model.User;

    import java.util.List;
    import java.util.Optional;
    import java.util.UUID;

    public interface UserDao {
        int insertUser(UUID id, String token, String username, String firstName, String lastName, String age, String password, String email);
        default int insertUser(User user){
            UUID id = UUID.randomUUID();
            return insertUser(id, user.getToken(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getAge(), user.getPassword(), user.getEmail());
        }
        List<User> selectAllUsers();

        Optional<User> selectUserById(UUID id);

        int deleteUserByID(UUID id);

        int updateUserById(UUID id, User user);
    }
