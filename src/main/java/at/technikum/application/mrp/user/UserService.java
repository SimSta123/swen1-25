package at.technikum.application.mrp.user;

import at.technikum.application.mrp.authentification.AuthRepositoryC;
import at.technikum.application.mrp.authentification.AuthService;
import at.technikum.application.todo.exception.*;

import java.util.*;

public class UserService {

    private final UserRepositoryC userRepository;
    private List<User> users;
    public AuthService auth;

    public UserService(UserRepositoryC userRepositoryC) {
        this.userRepository = userRepositoryC;
        //this.users = new ArrayList<>();
        //this.auth = new AuthService();
    }

    //zum Testen
    public UserService() {
        this.userRepository = null;
        this.users = new ArrayList<>();
    }

    public User create(User user) {
        // is user valid?
        /*
        System.out.println("try to create");
        if(!AuthService.checkData(user)){
            throw new IllegalArgumentException("Username or Password Illegal");
        }
        boolean unExists = users.stream()
                .anyMatch(u -> u.getUsername().equals(user.getUsername()));
        if(unExists){
            throw new EntityNotFoundException("UserName already Exists");
        }
        user.setUUId(UUID.randomUUID().toString());
        user.setId(users.size()+1);
        user.setDone(true);
        //UUID uuid = UUID.randomUUID();
        //user.setUUId(uuid.toString());
        users.add(user);
        Optional<User> optionalUser = userRepository.save(user);
        User savedUser = optionalUser.get();
        return savedUser;
         */
        return null;
    }

    public User get(int id) {
        return userRepository.find(id)
                .orElseThrow(() -> new EntityNotFoundException("None found with the given ind"));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User update(String id, User update) {
        /*
        User user = userRepository.find(id)
                .orElseThrow(EntityNotFoundException::new);

        user.setUsername(update.getUsername());
        user.setDone(update.isDone());
         */

        return userRepository.update(update);
    }

    public User delete(int id) {
        return userRepository.delete(String.valueOf(id));
    }

    public User findByUsername(String username) {
        User foundUser = new User();
        boolean unExists = users.stream()
                .anyMatch(u -> u.getUsername().equals(username));
        if (unExists == true) {
            foundUser = users.stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst()
                    .orElse(null);
            return foundUser;
        }
        throw new EntityNotFoundException("UserName doesn't exist");
    }

    public User findByID(int id) {

        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("UserID not found"));

        /*for(User u : users){
            System.out.println(u.getId()+"-----------------");
            if(u.getId() == id){
                return u;
            }
        }
         */
        /*
        Optional<User> optionalUser = userRepository.find(String.valueOf(id));
        User foundUser = optionalUser.orElseThrow(() ->
                new EntityNotFoundException("UserID doesn't exist"));
        return foundUser;
        */
    }

    public String getToken(User user){
        return auth.getToken(user.getUsername());
    }
}

