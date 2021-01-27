package utility.Json.Creation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import core.Classes.User;
import core.sql.MySqlAnnotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

class JSONCreationDB {
 /*
    public static ObjectNode getJSONList(UserList instance, ObjectMapper mapper){
        ObjectNode node = mapper.createObjectNode();
        node.put("IdUserList", instance.getIdUserList());
        node.put("title", instance.getTitle());
        node.put("description", instance.getDescription());
        for (int i = 0; i<instance.getListOfFilmId().size(); i++){
            node.put("film"+(i+1),JSONCreationMovieDb.getJsonFilmInfo(CineMatesTheMovieDB.searchFilmById(Integer.parseInt(instance.getListOfFilmId().get(i))),mapper));
        }
        return node;
    }

    public static String getJSONUserLists(Object instance, ObjectMapper mapper){
        try{
            List<UserList> list = (List<UserList>) instance;
            ObjectNode listOfList = mapper.createObjectNode();
            listOfList.put("CustomLists","");
            for (int i = 0; i< list.size(); i++)
                listOfList.put("Custom"+(i+1),getJSONList(list.get(i), mapper));
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listOfList);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    public static String getJsonReviews(Object instance, ObjectMapper mapper){
        return "";
    }
*/


    public static ObjectNode getJsonUser(Object instance, ObjectMapper mapper) {
        User toConvert = (User) instance;
        ObjectNode node = mapper.createObjectNode();
        for (Field field : toConvert.getClass().getDeclaredFields()) {
            try {
                if (field.getAnnotation(MySqlAnnotation.class) != null) {
                    String attribute = field.getName();
                    attribute = attribute.replaceFirst(String.valueOf(attribute.charAt(0)), String.valueOf(Character.toUpperCase(attribute.charAt(0))));
                    Method m = toConvert.getClass().getMethod("get" + attribute);
                    node.put(field.getName(), String.valueOf(m.invoke(toConvert)));
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }
        return node;
    }

    public static String getJsonListOfUsers(Object instance, ObjectMapper mapper) {
        try {
            List<User> list = (List<User>) instance;
            ObjectNode node = mapper.createObjectNode();
            for (int i = 0; i < list.size(); i++) {
                node.put("user" + (i + 1), getJsonUser(list.get(i), mapper));
            }
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
