package core.Classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

public class User extends MySQLRecord {
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String idUSer;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String email;
    @MySqlAnnotation(type = MySQLUtility.type_string)
    private String nick;
    @MySqlAnnotation(type = MySQLUtility.type_text)
    private String propic;

    @JsonCreator
    public User(@JsonProperty("email") String email,
                @JsonProperty("nickname") String nick,
                @JsonProperty("propic") String propic){
        this.email=email;
        this.nick=nick;
        this.propic=propic;

    }

    public String getIdUSer() {
        return idUSer;
    }

    public void setIdUSer(String idUSer) {
        this.idUSer = idUSer;
    }

    public String getEmail() {
        return email;
    }

    public void setMail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPropic() {
        return propic;
    }

    public void setPropic(String propic) {
        this.propic = propic;
    }
}
