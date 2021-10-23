package core.FireBase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Classes.User;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;
import utility.Json.Decode.JSONDecoder;

import java.io.IOException;
import java.util.*;

@Service
public class FireBaseUserService {
    private static final String USER_COL_NAME="Users";

    public static User getFireBaseUser(String uid){
        try {
            Content response = Request.Get("https://ingsw2021-default-rtdb.firebaseio.com/"+USER_COL_NAME+"/"+uid+".json").execute().returnContent();
            User u = (User) JSONDecoder.getDecodedJson(response.toString(),User.class);
            u.setIdUser(uid);
            return u;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<User> getListOfFireBaseUsers(){
        try {
            Map<String,User> prova = new ObjectMapper().readValue(Request.Get("https://ingsw2021-default-rtdb.firebaseio.com/"+USER_COL_NAME+".json").execute().returnContent().toString(), TreeMap.class);
            List<User> pojos = new ObjectMapper().convertValue(prova.values(), new TypeReference<List<User>>() { });
            int index=0;
            for (String s : prova.keySet()){
                pojos.get(index++).setIdUser(s);
            }
            return pojos;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateUser(String uid, String contentToUpdate) throws IOException {
        Request.Patch("https://ingsw2021-default-rtdb.firebaseio.com/Users/"+uid+".json").bodyString("{\"isAdmin\" : \""+contentToUpdate+"\"}", ContentType.APPLICATION_JSON).execute();
    }

}