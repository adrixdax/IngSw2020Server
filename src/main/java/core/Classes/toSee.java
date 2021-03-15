package core.Classes;

import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

public class toSee extends MySQLRecord {
    @MySqlAnnotation(type = MySQLUtility.type_int)
    int idFilm;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    String idUser;

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
