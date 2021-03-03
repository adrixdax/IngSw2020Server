package utility.Json.Decode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import utility.Json.Requests.HTTPRequest;

public class JSONDecoder {

    public static Object getDecodedJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (json.contains("Type=PostRequest")){
            System.out.println(json);
            return new HTTPRequest(json.substring(json.indexOf('&')+1),"Post");
        }
        if (json.contains("Type=PutRequest")){
            System.out.println(json);
            return new HTTPRequest(json.substring(json.indexOf('&')+1),"Put");
        }
        return null;
    }
}
