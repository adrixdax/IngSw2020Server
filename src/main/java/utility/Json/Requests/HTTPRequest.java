package utility.Json.Requests;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {
    private String request = "";
    private final Map<String, String> map;

    public HTTPRequest(String command, String req){
        request=req;
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
