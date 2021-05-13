package core.Classes;

import core.cnntodb.ConnectionToMySQL;
import core.sql.AbstractSQLRecord;
import core.sql.FactoryRecord;
import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;
import utility.ReportType;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Report extends MySQLRecord {
    @MySqlAnnotation(type = MySQLUtility.type_int)
    int idReport=0;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    String id_user="";
    @MySqlAnnotation(type = MySQLUtility.type_int)
    int id_recordRef=0;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String reportType ="";


    public int getIdReport() {
        return idReport;
    }

    public void setIdReport(int idReport) {
        this.idReport = idReport;
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

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    @Override
    public void afterRecordInsert() {
        try {
            List<AbstractSQLRecord> findReport = FactoryRecord.getNewIstance(getSql_connection())
                    .getListOfRecord(getSql_connection(), Report.class, "where id_recordRef='"
                            + id_recordRef + "' and id_user='" + id_user + "'");

            if (findReport != null) {
                if (findReport.size() >= 3) {
                    reviews findReview = (reviews) FactoryRecord.getNewIstance(getSql_connection())
                            .getSingleRecord(getSql_connection(), reviews.class, "where id_reviews='" + id_recordRef + "'");
                    findReview.setObscured(true);
                    findReview.setSql_connection(getSql_connection());
                    findReview.updateRecord();
                }
            }
        }catch (Exception ex){
            Logger.getLogger(ConnectionToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
