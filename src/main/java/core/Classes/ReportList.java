package core.Classes;

import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;
import utility.ReportType;

public class ReportList extends MySQLRecord {
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int id_segnalazione;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private ReportType report;

    public ReportList(int id_segnalazione,ReportType report){
        this.id_segnalazione=id_segnalazione;
        this.report=report;
    }

    public int getId_segnalazione() {
        return id_segnalazione;
    }

    public void setId_segnalazione(int id_segnalazione) {
        this.id_segnalazione = id_segnalazione;
    }

    public ReportType getReport() {
        return report;
    }

    public void setReport(ReportType report) {
        this.report = report;
    }


}
