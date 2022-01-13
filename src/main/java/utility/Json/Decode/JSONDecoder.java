package utility.Json.Decode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Classes.User;
import utility.Json.Requests.HTTPRequest;

public class JSONDecoder {

    public static Object getDecodedJson(String json,Class toConvert) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();                                                                       //oggetto di Jackson che permette la mappatura
        if (toConvert.equals(HTTPRequest.class)) {                                                                      //verifica che l'oggetto passato sia HTTPRequest
            if (json.contains("Type=PostRequest")) {                                                                    //verifica che sia effettivamente una richiesta HTTP Post tramite convenzione Type=PostRequest
                return new HTTPRequest(json.substring(json.indexOf('&') + 1));                                          //costruzione dell'oggetto HTTPRequest
            }
            return null;                                                                                                //convenzione non rispettata
        }
        else if (toConvert.equals(User.class)){                                                                         //verifica che sia User.class
            return mapper.readValue(json,User.class);                                                                   //ritorno dell'oggetto User sfruttando Jackson
        }
        return null;                                                                                                    //oggetto .class non valido
    }
}
