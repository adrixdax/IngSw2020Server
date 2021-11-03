package utility.Json.Requests;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {
    private final Map<String, String> map;

    public HTTPRequest(String command) {
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

    @Override
    public boolean equals(Object o){
        if (o instanceof HTTPRequest)
            return this.map.equals(((HTTPRequest) o).map);
        return false;
    }

}
