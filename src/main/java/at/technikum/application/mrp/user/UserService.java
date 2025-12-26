package at.technikum.application.mrp.user;

import at.technikum.application.mrp.authentification.AuthService;
import at.technikum.application.todo.exception.EntityNotFoundException;

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
        this.auth = new AuthService();
    }

    public User create(User user) {
        // is user valid?
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
    }

    public User get(String id) {
        return userRepository.find(id)
                .orElseThrow(EntityNotFoundException::new);
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

    //Hier noch, weil im Moment hier die User in einer Liste gepsiechert werden.
    public boolean logIn(User user) {
        if(!AuthService.checkData(user)){
            throw new IllegalArgumentException("Username or Password Illegal");
        }
        System.out.println("Username and password passed verification");
        boolean unExists = users.stream()
                .anyMatch(u -> u.getUsername().equals(user.getUsername()));
        System.out.println("Username exists");
        if(unExists==true){
            User foundUser = users.stream()
                    .filter(u -> u.getUsername().equals(user.getUsername()))
                    .findFirst()//first element of the Stream, optional zurück, weil es einen Stream zurückgiebt
                    .orElse(null); //wenn kein Wert dann null
            if(foundUser.getPassword().equals(user.getPassword())){
                System.out.println("User loged in");
                auth.createToken(user);
                return true;
            }
            else{
                System.out.println("Username and Password Mismatch");
                return false;
            }
        }
        System.out.println("User not found");
        //hier noch Exception einbauen zum Werfen.
        return false;
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

