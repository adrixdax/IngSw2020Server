package spring;


import DataBase.DbConnectionForBackEnd;
import MovieDB.CineMatesTheMovieDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import core.Classes.*;
import core.sql.AbstractSQLRecord;
import core.sql.FactoryRecord;
import core.sql.MySQLRecord;
import info.movito.themoviedbapi.model.MovieDb;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utility.Json.Creation.JSONCreation;
import utility.Json.Decode.JSONDecoder;
import utility.Json.Requests.HTTPRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public String user(@org.jetbrains.annotations.NotNull @RequestParam Map<String, String> query) {
        if (query.containsKey("nickname")) {
            try {
                if ((conn != null) && (!conn.isClosed())) {
                    return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, User.class, "nick like '%" + query.get("nickname") + "%'"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                if ((conn != null) && (!conn.isClosed())) {
                    return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Contact.class, "where user2="+query.get("IdUser")+" union (select * from Contact where user1=2)"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
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
        if (myMap.containsKey("latest") && myMap.get("latest").equals("true")){
            return JSONCreation.getJSONToCreate(CineMatesTheMovieDB.comingSoon());
        }
        if (myMap.containsKey("most") && myMap.get("most").equals("true")){
            List<AbstractSQLRecord> sql = (FactoryRecord.getNewIstance(conn).getListOfRecord(conn,MostViewed.class,""));
            List<MovieDb> movies = new ArrayList<>();
            for (AbstractSQLRecord record : sql){
                movies.add(CineMatesTheMovieDB.searchFilmById(((MostViewed)record).getIdFilm()));
            }
            return JSONCreation.getJSONToCreate(movies);
        }
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
