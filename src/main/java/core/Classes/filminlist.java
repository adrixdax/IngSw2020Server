package core.Classes;

import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

public class filminlist extends MySQLRecord {

    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int idfilminlist;
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int idList;
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int idFilm;

    public int getIdfilminlist() {
        return idfilminlist;
    }

    public void setIdfilminlist(int idfilminlist) {
        this.idfilminlist = idfilminlist;
    }

    public int getIdList() {
        return idList;
    }

    public void setIdList(int idList) {
        this.idList = idList;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }
}
