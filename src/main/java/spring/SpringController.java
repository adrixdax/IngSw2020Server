package spring;


import DataBase.DbConnectionForBackEnd;
import core.Classes.User;
import core.sql.FactoryRecord;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import utility.Json.JSONCreation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static MovieDB.CineMatesTheMovieDB.searchActorFilmography;
import static MovieDB.CineMatesTheMovieDB.searchFilmByName;


@Controller
@EnableAutoConfiguration
public class SpringController {

    Connection conn;

    public SpringController() {
        DbConnectionForBackEnd db = new DbConnectionForBackEnd();
        db.createConnection();
        this.conn = db.getConnection();
    }


    @GetMapping(value = "/user")
    @ResponseBody
    public String user(@RequestParam Map<String, String> query) {
        if (query.containsKey("nickname")) {
            try {
                if ((conn != null) && (!conn.isClosed())) {
                    return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getSingleRecord(conn, User.class, "nick='" + query.get("nickname") + "'"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            //key will be user instead of nickname
            //contact list of user wich passed ID
            return "";
        }
        return "";
    }

    //ip:8080/user?nickname=nick
    //ip:8080/user?user=iduserRequestingContactList

    @GetMapping(value = "/film")
    @ResponseBody
    public String film(@RequestParam Map<String, String> query) {
        if (query.containsKey("year") && query.containsKey("adult"))
            return JSONCreation.getJSONToCreate(searchFilmByName(query.get("name"), Integer.parseInt(query.get("year")), Boolean.parseBoolean(query.get("adult"))));
        else {
            if (query.containsKey("year"))
                return JSONCreation.getJSONToCreate(searchFilmByName(query.get("name"), Integer.parseInt(query.get("year"))));
            else if (query.containsKey("adul"))
                return JSONCreation.getJSONToCreate(searchFilmByName(query.get("name"), Boolean.parseBoolean(query.get("adult"))));
            else
                return JSONCreation.getJSONToCreate(searchFilmByName(query.get("name")));
        }
    }

    //ip:8080/film?name=name
    //ip:8080/film?name=name&adult=true
    //ip:8080/film?name=name&year=0000
    //ip:8080/film?name=name&year=0000&adult=false

    @GetMapping(value = "/actor")
    @ResponseBody
    public String actor(@RequestParam Map<String, String> query) {
        if (query.containsKey("adult"))
            return JSONCreation.getJSONToCreate(searchActorFilmography(String.valueOf(query.get("name")), Boolean.parseBoolean(query.get("adult"))));
        else
            return JSONCreation.getJSONToCreate(searchActorFilmography(String.valueOf(query.get("name"))));
    }

    //ip:8080/actor?name=name
    //ip:8080/actor?name=name&adult=value

}
