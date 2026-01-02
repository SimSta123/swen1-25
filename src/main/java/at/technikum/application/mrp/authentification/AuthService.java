package at.technikum.application.mrp.authentification;


import at.technikum.application.mrp.user.User;
import at.technikum.application.todo.exception.EntityNotFoundException;
import at.technikum.server.http.Response;
import at.technikum.server.util.TokenStore;
import java.util.*;
import java.lang.*;


public class AuthService {

    private final AuthRepositoryC authRepository;

    private TokenStore tokenStore = new TokenStore();

    public AuthService(AuthRepositoryC authRepository){
        this.authRepository = authRepository;
    }

    public boolean createUser(User user){
        if(AuthService.checkData(user)){
            user.setUUId(UUID.randomUUID().toString());
            boolean done = authRepository.createUser(user);
            return done;
        } else {
            throw new IllegalArgumentException("UserName or Password invalid (none or null)");
        }
    }

    public String logIn(User user){
        if(!AuthService.checkData(user)){
            throw new IllegalArgumentException("Username or Password Illegal");
        }
        System.out.println("Username and password passed verification");
        if(!authRepository.userLogIn(user)){
            throw new EntityNotFoundException("Username and/or password not found");    //oder UnauthorizedException 401?
        }


        return this.createToken(user);
    }

    public static boolean checkData(User user){
        System.out.println("checkData");
        if(user.getUsername() == null || user.getUsername().trim().equals("")) {
            System.out.println("Username is empty");
            return false;
        } else if (user.getPassword() == null || user.getPassword().trim().equals("")) {
            System.out.println("Password is empty");
            return false;
        } else {
            System.out.println("Username and password are good");
            return true;
        }
    }

    public String createToken(User user){
        System.out.println("createToken");
        String token = user.getUsername() + "-mrpToken";
        tokenStore.putToken(user.getUsername(), token);
        return token;
        //return UUID.randomUUID().toString();
    }

    public boolean checkToken(User user){
        String token =  tokenStore.getToken(user.getUsername());
        if(token.isEmpty()) {
            throw new IllegalArgumentException("Token does not exist");
        }
        System.out.println(token);
        boolean check = token.equals(user.getUsername()+"-mrpToken");
        return check;
    }

    public String getToken(String user){
        String token =  tokenStore.getToken(user);
        if(token.isEmpty()) {
            throw new IllegalArgumentException("Token does not exist");
        } else {
            return token;
        }
    }

    public boolean tokenExists(String s, boolean isHeader) {
        String token;
        if(isHeader) {
            token = s.substring(7);//Aus Headerstring nach Bearer
        }
        else {
            token = s;
        }
        System.out.println("Token: ---"+tokenStore.tokenExists(token));
        System.out.println(token);
        return tokenStore.tokenExists(token);
    }
}
