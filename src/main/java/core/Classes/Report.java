package core.Classes;

import core.sql.FactoryRecord;
import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

public class Report extends MySQLRecord {
    @MySqlAnnotation(type = MySQLUtility.type_int)
    int id_segnalazione;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    String id_user;
    @MySqlAnnotation(type = MySQLUtility.type_int)
    int id_recordRef;

    @Override
    public void afterRecordInsert(){
        this.id_segnalazione = ((Report) FactoryRecord.getNewIstance(getSql_connection()).getSingleRecord(getSql_connection(),this.getClass(),"where id_user="+this.id_user+" and id_recordRef="+this.id_recordRef)).getId_segnalazione();
    }

    public int getId_segnalazione() {
        return id_segnalazione;
    }

    public void setId_segnalazione(int id_segnalazione) {
        this.id_segnalazione = id_segnalazione;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public int getId_recordRef() {
        return id_recordRef;
    }

    public void setId_recordRef(int id_recordRef) {
        this.id_recordRef = id_recordRef;
    }
}
