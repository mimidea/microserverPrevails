package com.prevails.Prevails.dao;

import com.prevails.Prevails.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends MongoRepository<User, String> {
    public User findByFirstName(String firstName);
    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);

    @Query(value = "{'email' : ?0}")
    public User findByEmail(String email);

    @Query(value = "{'username' : ?0}")
    public User findByUserName(String username);

    @Query(value = "{'token' : ?0}")
    public User findByUserToken(String token);

    @Query(value = "{'id' : ?0}", delete = true)
    public User deleteUserById(String id);
    @Query(value="{'_id' : ?0}", delete = true)
    public void deleteById(String id);

    @Query(value = "{'uuid' : ?0}")
    public User findUserById(UUID uuid);

    @Query("{'username':{'$regex':'?0','$options':'i'}}")
    List <User> searchForFriend(String searchQuery);

    @Query(value = "{}")
    public List <User> findTheFirstTwenty();

    public List<User> findByLastName(String lastName);


}