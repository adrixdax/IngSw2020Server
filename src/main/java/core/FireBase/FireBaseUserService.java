package core.FireBase;

import core.Classes.User;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.springframework.stereotype.Service;
import utility.Json.Decode.JSONDecoder;

import java.io.IOException;

@Service
public
class FireBaseUserService {
    private static final String USER_COL_NAME="Users";

    public static User getFireBaseUser(String uid){
        try {
            Content response = Request.Get("https://ingsw2021-default-rtdb.firebaseio.com/"+USER_COL_NAME+"/"+uid+".json").execute().returnContent();
            User u = (User) JSONDecoder.getDecodedJson(response.toString(),User.class);
            u.setIdUSer(uid);
            return u;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}