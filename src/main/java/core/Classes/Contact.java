package core.Classes;

import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

public class Contact extends MySQLRecord {

    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int user1;
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int user2;


    public int getUser1() {
        return user1;
    }

    public void setUser1(int user1) {
        this.user1 = user1;
    }

    public int getUser2() {
        return user2;
    }

    public void setUser2(int user2) {
        this.user2 = user2;
    }
}
