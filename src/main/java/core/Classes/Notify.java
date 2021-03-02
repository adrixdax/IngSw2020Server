package core.Classes;

import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

public class Notify extends MySQLRecord {

    @MySqlAnnotation(type = MySQLUtility.type_int)
    int id_notify;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    String id_sender;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    String id_receiver;
    @MySqlAnnotation(type = MySQLUtility.type_text)
    String type;
    @MySqlAnnotation(type = MySQLUtility.type_int)
    int id_recordref;
    @MySqlAnnotation(type = MySQLUtility.type_text)
    String state;


    public int getId_notify() {
        return id_notify;
    }

    public void setId_notify(int id_notify) {
        this.id_notify = id_notify;
    }

    public String getId_sender() {
        return id_sender;
    }

    public void setId_sender(String id_sender) {
        this.id_sender = id_sender;
    }

    public String getId_receiver() {
        return id_receiver;
    }

    public void setId_receiver(String id_receiver) {
        this.id_receiver = id_receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId_recordref() {
        return id_recordref;
    }

    public void setId_recordref(int id_recordref) {
        this.id_recordref = id_recordref;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
