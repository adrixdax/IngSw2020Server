package core.Classes;

import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

public class Contact extends MySQLRecord {

    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String user1;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String user2;


    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }
}
