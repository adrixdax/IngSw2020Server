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
    private String desc;
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int val;
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int idFilm;
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int iduser;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }
}
