package utility.Json.Decode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Classes.User;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import utility.Json.Requests.HTTPRequest;

import javax.rmi.CORBA.ValueHandler;

public class JSONDecoder {

    public static Object getDecodedJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (json.contains("Type=PostRequest")){
            return new HTTPRequest(json.substring(json.indexOf('&')+1));
        }
        else{
            return new Object();
        }
    }
}