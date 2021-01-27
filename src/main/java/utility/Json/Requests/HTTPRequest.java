package utility.Json.Requests;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {
    private Map<String,String> map;

    public HTTPRequest(){

    }

    public HTTPRequest(String command){
        map = new HashMap<>();
        String[] pairs = command.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue[1].contains("+")) {
                keyValue[1] = keyValue[1].replace("+", " ");
            }
            map.put(keyValue[0], keyValue[1]);
        }
    }

    public Map<String, String> getMap() {
        return map;
    }

}
