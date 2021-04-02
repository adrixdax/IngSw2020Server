package utility.Json.Creation;

import core.Classes.UserList;
import org.junit.jupiter.api.Test;
import utility.UserListType;
import java.util.ArrayList;
import java.util.List;

class JSONCreationDBTest {

    @Test
    void getJson() {
        UserList u = new UserList();
        u.setDescription("Descrizione");
        u.setTitle("Titolo");
        u.setType(UserListType.CUSTOM.toString());
        u.setDependency_List(0);
        u.setIdUserList(100);
        String json=JSONCreation.getJSONToCreate(u,UserList.class.getCanonicalName());
        assert json.equals("{\n" +
                "  \"idUserList\" : \"100\",\n" +
                "  \"title\" : \"Titolo\",\n" +
                "  \"description\" : \"Descrizione\",\n" +
                "  \"type\" : \"CUSTOM\",\n" +
                "  \"idUser\" : \"null\",\n" +
                "  \"dependency_List\" : \"0\"\n" +
                "}");
    }

    @Test
    void getJsonListDB() {
        UserList u = new UserList();
        u.setDescription("Descrizione");
        u.setTitle("Titolo");
        u.setType(UserListType.CUSTOM.toString());
        u.setDependency_List(0);
        u.setIdUserList(100);
        UserList y = new UserList();
        y.setDescription("Descrizione di y");
        y.setTitle("Lista y");
        y.setType(UserListType.CUSTOM.toString());
        y.setDependency_List(0);
        y.setIdUserList(35);
        y.setIdUser("123456789");
        List<UserList> list = new ArrayList<>();
        list.add(y);
        list.add(u);
        String json=JSONCreation.getJSONToCreate(list,list.get(0).getClass().getCanonicalName());
        assert json.equals("[ {\n" +
                "  \"idUserList\" : \"35\",\n" +
                "  \"title\" : \"Lista y\",\n" +
                "  \"description\" : \"Descrizione di y\",\n" +
                "  \"type\" : \"CUSTOM\",\n" +
                "  \"idUser\" : \"123456789\",\n" +
                "  \"dependency_List\" : \"0\"\n" +
                "}, "+
                "{\n" +
                "  \"idUserList\" : \"100\",\n" +
                        "  \"title\" : \"Titolo\",\n" +
                        "  \"description\" : \"Descrizione\",\n" +
                        "  \"type\" : \"CUSTOM\",\n" +
                        "  \"idUser\" : \"null\",\n" +
                        "  \"dependency_List\" : \"0\"\n" +
                        "}"+
                " ]");
    }
}