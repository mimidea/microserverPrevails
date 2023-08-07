package com.prevails.Prevails.api;

import com.prevails.Prevails.dao.UserRepository;
import com.prevails.Prevails.model.PasswordVerification;
import com.prevails.Prevails.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("api/v1")
@RestController
public class UserController implements PasswordVerification {

    final String UUIDPARAM = "?UUID=";
    final String USERNAMEPARM = "&UN=";
    final String FIRSTNAMEPARAM = "?FN=";
    final String LASTNAMEPARAM = "&LN=";
    final String AGEPARAM = "&AG=";
    final String EMAILPARAM = "&EM=";
    final String PASSWORDPARAM = "&PW";

    @Autowired
    UserRepository userRepository;
    private final org.slf4j.Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;

    }
    @GetMapping(value = "/register")
    public String registerForUUID (@RequestParam("FN") String firstName,
                                   @RequestParam("LN") String lastName,
                                   @RequestParam("EM") String email, @RequestParam ("PW")
                                   String password, @RequestParam ("UN") String username,
                                   @RequestParam("AG") String age ) {

        if (userRepository.findByEmail(email) == null) {
            UUID uuid = UUID.randomUUID();
            User newUser = null;
            String token = getJWTToken(username);
            LOG.info("Trying to save user.");
            if (isPasswordValid(password) == 200) {
                if (userRepository.findByUserName(username) == null) {
                    newUser = new User(uuid, token, username, firstName, lastName, age, password, email);
                    System.out.println(newUser.getToken());
                    userRepository.save(newUser);
                    return token;
                }
            } else {
                return "Username is already taken";
            }
        } else {
            switch (isPasswordValid(password)) {
                case 1:
                    return "Password does not meet the required length rules";
                case 2:
                    return "Password does not contain an upper case";
                case 3:
                    return "Password does not contain a lower case";
                case 4:
                    return "Password does not contain a number";
                case 5:
                    return "Password does not contain a symbol";
                default:
                    return "Please only use ASCII format symbols";
            }
        }
        return "Username is already registered";
    }
    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Token:" + token;
    }
    @GetMapping(value = "/login")
    public String registerForUUID (@RequestParam("EM") String email,
                                   @RequestParam("PW") String password) {
        LOG.info("Verifying user " + email);
        if (userRepository.findByEmail(email)!=null){
            if (userRepository.findByEmail(email).getPassword().equals(password)) {
                User tempUser = userRepository.findByEmail(email);
                System.out.println(tempUser);
                String token = getJWTToken(tempUser.getUsername());
                tempUser.setToken(token);
                userRepository.save(tempUser);
                return (tempUser.getToken());
            }
            else {
                return "Password entered is incorrect";
            }
        }
        else {
            return "Email address has not been registered";
        }
    };

@PostMapping(value = "/addToFriendGroupByEmail")
    public String addToFriendGroup(@RequestHeader("Authorization") String authorization, @RequestParam("EM") String addFriend) {
        LOG.info("Fetching Account to add to friend group to");
        User tempUser = userRepository.findByUserToken(authorization);

        if (tempUser==null){
            System.out.println("no user found");
            return "faild to find user";
        } else {
            LOG.info("searching for user");
                User userToAdd= userRepository.findByEmail(addFriend);
                if(userToAdd!=null){
                    if (!tempUser.getFriends().contains(userToAdd)){
                        userToAdd.getId();
                        userRepository.save(tempUser);
                        LOG.info("Friend Added");
                        return "success";
                    }
                }
                LOG.info("Failed to add");
                return "Failed to add";
        }
    }

    @GetMapping(value = "/searchForFriend")
    public JSONObject searchForTribesmen (@RequestHeader("Authorization") String authorization, @RequestParam("EM") String searchQuery) {
        LOG.info("Fetching Account to get tribe from");
        User tempUser = userRepository.findByUserToken(authorization);
        JSONObject jsonObj = new JSONObject();

        if (tempUser==null) {
            jsonObj.put("success", "false");
            System.out.println(jsonObj);
            return jsonObj;
        } else {
            LOG.info("looking for tribe now");
            List<String> myfriends = tempUser.getFriends();
            List<User> myList;
            if (searchQuery.equals("app")) {
                myList = userRepository.findTheFirstTwenty();
            } else {
                myList = userRepository.searchForFriend(searchQuery);
            }
            System.out.println("Group size = " + myList.size());
            int i = 0;
            boolean friendFound = false;
            for (User user: myList) {

                if (!user.getId().equals(tempUser.getId()) && !myfriends.contains(UUID.fromString(user.getEmail()))) {
                    jsonObj.put(i, user.getId().toString());
                    i++;
                    friendFound=true;
                }
                LOG.info("Tribe found");
            }
            if (friendFound) {
                LOG.info("Tribe found");
                return jsonObj;
            }
            jsonObj.put("success", "false");
            return jsonObj;
        }
    }
    @GetMapping(value = "/accountDeletion")
    public String removeAccount (@RequestParam("EM") String email, @RequestHeader("Authorization") String authorization) {
        LOG.info("Deleting Account");
        User tempUser = userRepository.findByEmail(email);
        if (userRepository.findByEmail(email)!=null){
            String id = tempUser.getId().toString();
            System.out.println("ID "+id);
            userRepository.deleteById(id);
            System.out.println("Users found with findAll():");
            System.out.println("-------------------------------");

            for (User user : userRepository.findAll()) {
                System.out.println(user.getId());
                System.out.println(user.getToken());
            }
            User deletedUser = userRepository.findByEmail(email);
            if (deletedUser==null) {
                return "Success";
            } else {
                return "Failed";
            }
        }
            return "Failed";
    };

    @GetMapping(value = "/accountRetrieval")
    public String returnAccount (@RequestHeader("Authorization") String authorization) {
        LOG.info("Fetching Account");
        User tempUser = userRepository.findByUserToken(authorization);
        if (tempUser==null) {
            return "Failed";
        } else {
            return "User:" + tempUser.getFirstName() + ":" + tempUser.getLastName()
                    + ":" + tempUser.getEmail() + ":" + tempUser.getUsername() + ":" +
                    tempUser.getAge() + ":" + tempUser.getToken();
        }
    }

    @GetMapping("/friends/search")
    public ResponseEntity<String> searchFriend(
            @PathVariable String token,
            @RequestParam(value = "EM", required = false) String email
    ) {
        Optional<User> userOptional = userRepository.findById(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<String> friends = user.getFriends();

            if (email != null) {
                Optional<User> friendOptional = Optional.ofNullable(userRepository.findByUserName(email));
                if (friendOptional.isPresent()) {
                    User friend = friendOptional.get();
                    if (friends.contains(friend.getId())) {
                        return ResponseEntity.ok("Friend found!");
                    }
                }
            }



            return ResponseEntity.ok("Friend not found.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
