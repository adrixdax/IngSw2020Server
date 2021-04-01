package utility.Json.Decode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Classes.User;
import utility.Json.Requests.HTTPRequest;

public class JSONDecoder {

    public static Object getDecodedJson(String json,Class toConvert) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (toConvert.equals(HTTPRequest.class)) {
            if (json.contains("Type=PostRequest")) {
                return new HTTPRequest(json.substring(json.indexOf('&') + 1), "Post");
            }
            return null;
        }
        else if (toConvert.equals(User.class)){
            ObjectMapper m = new ObjectMapper();
            return m.readValue(json,User.class);
        }
        return null;
    }
}
