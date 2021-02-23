/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.Classes;

import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

/**
 * @author Antimo
 */
public class User extends MySQLRecord {
    @MySqlAnnotation(type = MySQLUtility.type_int)
    private int idUSer;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String mail;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String nick;

    public int getIdUSer() {
        return idUSer;
    }

    public void setIdUSer(int idUSer) {
        this.idUSer = idUSer;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public void afterRecordInsert() {

    }

}
