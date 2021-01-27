package utility.Json.Creation;

import MovieDB.CineMatesTheMovieDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import core.Classes.User;
import core.Classes.filminlist;
import core.Classes.reviews;
import core.Classes.userlist;
import core.sql.AbstractSQLRecord;
import core.sql.FactoryRecord;
import core.sql.MySqlAnnotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class JSONCreationDB {

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
        return "{}";
    }

    public static ObjectNode getJsonReview(Object instance, ObjectMapper mapper){
        reviews review = (core.Classes.reviews) instance;
        System.out.println(review.getIdFilm()+" "+review.getId_review());
        ObjectNode node = mapper.createObjectNode();
        for (Field field : review.getClass().getDeclaredFields()) {
            try {
                if (field.getAnnotation(MySqlAnnotation.class) != null) {
                    String attribute = field.getName();
                    attribute = attribute.replaceFirst(String.valueOf(attribute.charAt(0)), String.valueOf(Character.toUpperCase(attribute.charAt(0))));
                    Method m = review.getClass().getMethod("get" + attribute);
                    node.put(field.getName(), String.valueOf(m.invoke(review)));
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }
        return node;
    }

    public static String getJsonListOfReviews(Object instance, ObjectMapper mapper) {
        try {
            List<reviews> list = (List<reviews>) instance;
            ObjectNode node = mapper.createObjectNode();
            for (int i = 0; i < list.size(); i++) {
                node.put("review" + (i + 1), getJsonReview(list.get(i), mapper));
            }
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return "{}";
    }

    public static ObjectNode getJsonSingleList(userlist instance, ObjectMapper mapper){
        ObjectNode node = mapper.createObjectNode();
        for (Field field : instance.getClass().getDeclaredFields()) {
            try {
                if (field.getAnnotation(MySqlAnnotation.class) != null) {
                    String attribute = field.getName();
                    attribute = attribute.replaceFirst(String.valueOf(attribute.charAt(0)), String.valueOf(Character.toUpperCase(attribute.charAt(0))));
                    Method m = instance.getClass().getMethod("get" + attribute);
                    node.put(field.getName(), String.valueOf(m.invoke(instance)));
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }
        List<AbstractSQLRecord> filmlist=FactoryRecord.getNewIstance(instance.getSql_connection()).getListOfRecord(instance.getSql_connection(),filminlist.class,"idList="+instance.getIdUserList());
        for (int i = 0; i< filmlist.size();i++) {
            node.put("film"+(i+1), JSONCreationMovieDb.getJsonFilmInfo(CineMatesTheMovieDB.searchFilmById(((filminlist) filmlist.get(i)).getIdFilm()),mapper));
        }
        return node;
    }


    public static String getJsonUserList(ArrayList<?> instance, ObjectMapper mapper) {
        try {
            List<userlist> list = (List<userlist>) instance;
            ObjectNode node = mapper.createObjectNode();
            for (int i = 0; i < list.size(); i++) {
                node.put("list" + (i + 1), getJsonSingleList(list.get(i), mapper));
            }
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return "{}";
    }
}
