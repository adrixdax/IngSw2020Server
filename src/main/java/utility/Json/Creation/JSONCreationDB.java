package utility.Json.Creation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import core.sql.MySqlAnnotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

class JSONCreationDB {

    public static ObjectNode getJsonDB(Object instance, ObjectMapper mapper, String ClassToconvert) throws ClassNotFoundException {
        Class<?> act = Class.forName(ClassToconvert);
        ObjectNode node = mapper.createObjectNode();
        for (Field field : act.getDeclaredFields()) {
            try {
                if (field.getAnnotation(MySqlAnnotation.class) != null) {
                    String attribute = field.getName();
                    attribute = attribute.replaceFirst(String.valueOf(attribute.charAt(0)), String.valueOf(Character.toUpperCase(attribute.charAt(0))));
                    Method m = act.getMethod("get" + attribute);
                    node.put(field.getName(), String.valueOf(m.invoke(instance)));
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }
        return node;
    }

    public static String getJsonListDB(ArrayList<?> list, ObjectMapper mapper, String ClassesToConvert) {
        try {
            ArrayNode arr = mapper.createArrayNode();
            for (Object o : list) {
                arr.add(getJsonDB(o, mapper, ClassesToConvert));
            }
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arr);
        } catch (JsonProcessingException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return "{}";
    }
}
