package at.technikum.application.mrp.user;

import at.technikum.application.mrp.authentification.AuthRepositoryC;
import at.technikum.application.mrp.authentification.AuthService;
import at.technikum.application.mrp.media.Media;
import at.technikum.application.mrp.media.MediaService;
import at.technikum.application.mrp.rating.Rating;
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

    public List<Rating> ratingHistory(int id) {
        return userRepository.ratingHistory(id);
    }

    public boolean update(User update, int userId) {
        update.setId(userId);
        userRepository.update(update);
        return true;
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

    public List<Media> getAllFavs(int userId){
        List<Media> favs = new ArrayList<>();
        List<Integer> mIds = userRepository.allFavsMediaId(userId);
        if(!mIds.isEmpty()){
            System.out.println("before for");
            for(int i = 0; i<mIds.toArray().length;i++) {
                //favs = userRepository.findAllMediaByID();
                favs.add(userRepository.findMediaByID(mIds.get(i)));
                System.out.println("i");
            }
        } else {
            throw new EntityNotFoundException("Keine Favs gefunden von diesem User");
        }
        return favs;
    }
}

