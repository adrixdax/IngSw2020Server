package core.Classes;

import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

public class reviews extends MySQLRecord {
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int id_review;
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

    public int getId_review() {
        return id_review;
    }

    public void setId_review(int id_review) {
        this.id_review = id_review;
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
}
