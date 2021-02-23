package utility.Json.Creation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.movito.themoviedbapi.model.MovieDb;

import java.util.ArrayList;

import static utility.Json.Creation.JSONCreationDB.getJsonListDB;

public class JSONCreation {

    private static final String json = null;
    private static final ObjectMapper mapper = new ObjectMapper();


    private static String getJSONList(ArrayList<?> instance, String ClassToConvert) {
        if (instance.size() > 0) {
            if (ClassToConvert.equals("MovieDb")) {
                return JSONCreationMovieDb.getJSONFilmList(instance, mapper);
            } else {
                return getJsonListDB(instance, mapper, ClassToConvert);
            }
        }
        return "";
    }


    public static String getJSONToCreate(Object instance, String ClassToConvert) {
        if (instance != null) {
            switch (instance.getClass().getSimpleName()) {
                case "MovieDb": {
                    try {
                        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(JSONCreationMovieDb.getJsonFilmInfo((MovieDb) instance, mapper));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                case "User": {
                    try {
                        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(JSONCreationDB.getJson(instance, mapper, ClassToConvert));
                    } catch (JsonProcessingException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                case "ArrayList": {
                    return getJSONList((ArrayList<?>) instance, ClassToConvert);
                }
            }
        } else {
            return "";
        }
        return "";
    }
}
