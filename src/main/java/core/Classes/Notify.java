package core.Classes;

import core.sql.FactoryRecord;
import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import org.checkerframework.checker.i18nformatter.qual.I18nChecksFormat;
import utility.MySQLUtility;
import utility.NotifyStatusType;
import utility.NotifyTypes;

import java.util.Date;

public class Notify extends MySQLRecord {

    @MySqlAnnotation(type = MySQLUtility.type_int)
    int id_Notify;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    String id_sender;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    String id_receiver;
    @MySqlAnnotation(type = MySQLUtility.type_text)
    String type;
    @MySqlAnnotation(type = MySQLUtility.type_int)
    int id_recordref;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    String state;
    @MySqlAnnotation(type = MySQLUtility.type_long)
    long dateOfSend = 0;


    public int getId_Notify() {
        return id_Notify;
    }

    public void setId_Notify(int id_Notify) {
        this.id_Notify = id_Notify;
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

    public long getDateOfSend() {
        return dateOfSend;
    }

    public void setDateOfSend(long dateOfSend) {
        this.dateOfSend = dateOfSend;
    }
}
