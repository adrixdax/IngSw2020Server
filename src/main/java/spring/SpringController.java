package spring;


import DataBase.DbConnectionForBackEnd;
import MovieDB.CineMatesTheMovieDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import core.Classes.User;
import core.Classes.reviews;
import core.Classes.userlist;
import core.sql.FactoryRecord;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utility.Json.Creation.JSONCreation;
import utility.Json.Decode.JSONDecoder;
import utility.Json.Requests.HTTPRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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
                    return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, User.class, "nick like '%" + query.get("nickname") + "%'"));
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

    @PostMapping(value = "/film")  //verificare se è il caso di farla diventare una POST
    @ResponseBody
    public String film(@RequestBody String query) {
        HTTPRequest request = null;
        try {
            request = (HTTPRequest) JSONDecoder.getDecodedJson(query);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert request != null;
        Map<String, String> myMap = request.getMap();
            if (myMap.containsKey("filmId"))
                return JSONCreation.getJSONToCreate(CineMatesTheMovieDB.searchFilmById(Integer.parseInt(myMap.get("filmId"))));
            if (myMap.containsKey("year") && myMap.containsKey("adult")) {
                String str = JSONCreation.getJSONToCreate(searchFilmByName(myMap.get("name"), Integer.parseInt(myMap.get("year")), Boolean.parseBoolean(myMap.get("adult"))));
                System.out.println(str);
                return str;
            }
            else {
                if (myMap.containsKey("year"))
                    return JSONCreation.getJSONToCreate(searchFilmByName(myMap.get("name"), Integer.parseInt(myMap.get("year"))));
                else if (myMap.containsKey("adult"))
                    return JSONCreation.getJSONToCreate(searchFilmByName(myMap.get("name"), Boolean.parseBoolean(myMap.get("adult"))));
                else
                    return JSONCreation.getJSONToCreate(searchFilmByName(myMap.get("name")));
            }
    }
    //ip:8080/film?filmId=id
    //ip:8080/film?name=name
    //ip:8080/film?name=name&adult=true
    //ip:8080/film?name=name&year=0000
    //ip:8080/film?name=name&year=0000&adult=false

    @PostMapping(value = "/actor")
    @ResponseBody
    public String actor(@RequestBody String query) {
        HTTPRequest request = null;
        try {
            request = (HTTPRequest) JSONDecoder.getDecodedJson(query);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert request != null;
        Map<String, String> myMap = request.getMap();
        if (myMap.containsKey("adult"))
            return JSONCreation.getJSONToCreate(searchActorFilmography(String.valueOf(myMap.get("name")), Boolean.parseBoolean(myMap.get("adult"))));
        else
            return JSONCreation.getJSONToCreate(searchActorFilmography(String.valueOf(myMap.get("name"))));
    }

    //ip:8080/actor?name=name
    //ip:8080/actor?name=name&adult=value


    @GetMapping(value = "/review")
    @ResponseBody
    public String review(@RequestParam Map<String, String> query) {
        if (query.containsKey("idFilm")) {
            return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, reviews.class,"idFilm="+query.get("idFilm")));
        }else {
            return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn,reviews.class,"iduser="+query.get("iduser")));
        }
    }

    //ip:8080/review?filmId=id
    //ip:8080/review?iduser=id


    @GetMapping(value = "/list")
    @ResponseBody
    public String list(@RequestParam Map<String, String> query) {
        return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, userlist.class,"idUser="+query.get("idUser")));
    }

    //ip:8080/list?userId=id


    @PostMapping(value = "/todecide")
    @ResponseBody
    public String insertFilm(@RequestBody String json) {
        System.out.println(json);
        return json;
    }

    //ip:8080/insertFilm      json
    //better to build with HTTP Builder apache tomcat
    //mapping with application/json

}
