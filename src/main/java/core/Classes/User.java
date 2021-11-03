package core.Classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import core.sql.MySQLRecord;
import core.sql.MySqlAnnotation;
import utility.MySQLUtility;

public class User{

    private String idUser;
    private String email;
    private String nick;
    private String propic;
    private boolean isAdmin;

    @JsonCreator
    public User(@JsonProperty("email") String email,
                @JsonProperty("nickname") String nick,
                @JsonProperty("propic") String propic,
                @JsonProperty("isAdmin") boolean isAdmin){
        this.email=email;
        this.nick=nick;
        this.propic=propic;
        this.isAdmin=isAdmin;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(this.getClass())) {
            if (this.idUser != null && ((User) obj).idUser != null) {
                return this.idUser.equals(((User) obj).idUser);
            }
            return (this.nick.equals(((User) obj).nick) && this.email.equals(((User) obj).email) && this.propic.equals(((User) obj).propic));
        }
        else
            return false;
    }
}
