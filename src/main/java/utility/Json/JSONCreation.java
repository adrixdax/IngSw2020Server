package utility.Json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class JSONCreation {

    private static final String json = null;
    private static final ObjectMapper mapper = new ObjectMapper();


    public static String getJSONToCreate(Object instance) {
        if (instance != null) {
            switch (instance.getClass().getSimpleName()) {
                case "MovieDb": {
                    try {
                        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(JSONCreationMovieDb.getJsonFilmInfo(instance, mapper));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                case "User": {
                    try {
                        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(JSONCreationDB.getJsonUser(instance, mapper));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
                case "ArrayList": {
                    ArrayList<Object> list = (ArrayList<Object>) instance;
                    if (list.size() > 0) {
                        if (list.get(0).getClass().getSimpleName().equals("MovieDb")) {
                            return JSONCreationMovieDb.getJSONFilmList(instance, mapper);
                        } else {
                            switch (list.get(0).getClass().getSimpleName()) {
                                case "UserList": {
                                    //return getJSONUserLists(list,mapper);
                                }
                                case "Review": {

                                }
                                case "User": {
                                    return JSONCreationDB.getJsonListOfUsers(list, mapper);
                                }
                            }
                        }
                    } else
                        return "";
                }
            }
            return json;
        } else {
            return "";
        }
    }
}
