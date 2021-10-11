package core.Classes;

import core.sql.AbstractSQLRecord;
import core.sql.FactoryRecord;
import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

import java.util.List;

public class Reviews extends MySQLRecord {
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int idReviews;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String title;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String description;
    @MySqlAnnotation(type = MySQLUtility.type_double)
    private double val;
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int idRecordRef;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String iduser;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String typeOfReview;
    @MySqlAnnotation(type = MySQLUtility.type_bool)
    private boolean obscured = false;


    public int getIdReviews() {
        return idReviews;
    }

    public void setIdReviews(int idReviews) {
        this.idReviews = idReviews;
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

    public void setDescription(String desc) {
        this.description = desc;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }

    public int getIdRecordRef() {
        return idRecordRef;
    }

    public void setIdRecordRef(int idRecordRef) {
        this.idRecordRef = idRecordRef;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getTypeOfReview() {
        return typeOfReview;
    }

    public void setTypeOfReview(String typeOfReview) {
        this.typeOfReview = typeOfReview;
    }

    public boolean isObscured() {
        return obscured;
    }

    public boolean getObscured() {
        return obscured;
    }

    public void setObscured(boolean obscured) {
        this.obscured = obscured;
    }
    @Override
    public void afterRecordInsert(){
        this.idReviews = ((Reviews) FactoryRecord.getNewIstance(getSql_connection()).getSingleRecord(getSql_connection(),this.getClass(),"where iduser='"+this.iduser+"' and idRecordRef='"+this.idRecordRef+"' ORDER BY idReviews DESC")).getIdReviews();
    }

    @Override
    public void beforeRecordDeleted() {
        List<AbstractSQLRecord> reports = FactoryRecord.getNewIstance(getSql_connection()).getListOfRecord(getSql_connection(),Report.class,"where id_recordRef="+this.getIdReviews());
        for (AbstractSQLRecord r : reports){
            ((Report)r).deleteRecord();
        }
    }
}
