package at.technikum.server.util;

import java.util.Map;
import java.util.*;

public class TokenStore {
    //Keine Concurrent weil nur 1 Thread, Key = String (UserID)
    private final Map<String,String> TOKENS = new HashMap<>();

    public TokenStore() { }

    public void putToken(String key,String token)
    {
        System.out.println("putToken with: " + key + "|" + token);
        TOKENS.put(key,token);
    }

    public String getToken(String key)
    {
        if(TOKENS.containsKey(key)) {
            return TOKENS.get(key);
        } else {
            throw new IllegalArgumentException("Token does not exist");
        }
    }

    public void removeToken(String key)
    {
        TOKENS.remove(key);
    }

    public void removeAllTokens()
    {
        TOKENS.clear();
    }

    public boolean isEmpty()
    {
        return TOKENS.isEmpty();
    }


}
