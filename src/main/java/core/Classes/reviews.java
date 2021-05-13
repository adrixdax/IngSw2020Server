package core.Classes;

import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

public class reviews extends MySQLRecord {
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int id_reviews;
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



    public int getId_reviews() {
        return id_reviews;
    }

    public void setId_reviews(int id_reviews) {
        this.id_reviews = id_reviews;
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
}
