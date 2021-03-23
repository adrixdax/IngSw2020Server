package core.Classes;

import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

public class MostReviewed extends MySQLRecord {

    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int counter;
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int idFilm;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }
}
