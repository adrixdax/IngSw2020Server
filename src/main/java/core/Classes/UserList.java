package core.Classes;

import core.sql.FactoryRecord;
import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

public class UserList extends MySQLRecord {
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int idUserList;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String title;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String description;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String type;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String idUser;
    @MySqlAnnotation(type=MySQLUtility.type_int)
    private int dependency_List;

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

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser){this.idUser=idUser;};

    public int getDependency_List() {
        return dependency_List;
    }

    public void setDependency_List(int dependency_List) {
        this.dependency_List = dependency_List;
    }

    @Override
    public void afterRecordInsert(){
        this.idUserList = ((UserList)FactoryRecord.getNewIstance(getSql_connection()).getSingleRecord(getSql_connection(),this.getClass(),"where idUser='"+this.idUser+"' and dependency_List='"+this.dependency_List+"'")).getIdUserList();
    }

}

