package core.Classes;

import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

public class userlist extends MySQLRecord {
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int idUserList;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String title;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String description;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String type;
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int idUser;

    public int getIdUserList() {
        return idUserList;
    }

    public void setIdUserList(int idUserList) {
        this.idUserList = idUserList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
