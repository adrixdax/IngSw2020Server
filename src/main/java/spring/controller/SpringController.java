package spring.controller;


import DataBase.DbConnectionForBackEnd;
import MovieDB.CineMatesTheMovieDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import core.Classes.*;
import core.sql.AbstractSQLRecord;
import core.sql.FactoryRecord;
import info.movito.themoviedbapi.model.MovieDb;
import org.springframework.web.bind.annotation.*;
import utility.Json.Creation.JSONCreation;
import utility.Json.Decode.JSONDecoder;
import utility.Json.Requests.HTTPRequest;
import utility.UserListType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static MovieDB.CineMatesTheMovieDB.searchFilmByName;


@RestController
public class SpringController {

    Connection conn;
    CineMatesTheMovieDB CineMates;

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
                    return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, User.class, "nick like '%" + query.get("nickname") + "%'"), User.class.getCanonicalName());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                if ((conn != null) && (!conn.isClosed())) {
                    return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Contact.class, "where user2=" + query.get("IdUser") + " union (select * from Contact where user1=2)"), Contact.class.getCanonicalName());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return "";
    }

    //ip:8080/user?nickname=nick
    //ip:8080/user?user=iduserRequestingContactList

    @PostMapping(value = "/registration")
    @ResponseBody
    public String registration(@RequestBody String query) {
        HTTPRequest request = null;
        try {
            System.out.println("Provaaaaaaaaa: " + query);
            request = (HTTPRequest) JSONDecoder.getDecodedJson(query);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert request != null;
        Map<String, String> myMap = request.getMap();
        if (myMap.containsKey("registration")) {
            try {
                if ((conn != null) && (!conn.isClosed())) {
                    Userlist list = new Userlist();
                    list.setIdUser(myMap.get("registration"));
                    list.setSql_connection(conn);
                    list.setDescription("Preferiti");
                    list.setTitle("Preferiti");
                    list.setType(UserListType.PREFERED.toString());
                    list.addRecord();

                    list = new Userlist();
                    list.setIdUser(myMap.get("registration"));
                    list.setSql_connection(conn);
                    list.setDescription("Da Vedere");
                    list.setTitle("Da Vedere");
                    list.setType(UserListType.TOWATCH.toString());
                    list.addRecord();

                    list = new Userlist();
                    list.setIdUser(myMap.get("registration"));
                    list.setSql_connection(conn);
                    list.setDescription("Visti");
                    list.setTitle("Visti");
                    list.setType(UserListType.WATCH.toString());
                    list.addRecord();

                    return "Registrazione avvenuta con successo";

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if (myMap.containsKey("google")) {
            try {
                if ((conn != null) && (!conn.isClosed())) {

                    Userlist list = (Userlist) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, Userlist.class, "where idUser='" + myMap.get("google") + "' and type='PREFERED'");


                    if (list == null) {
                        list = new Userlist();
                        list.setIdUser(myMap.get("google"));
                        list.setSql_connection(conn);
                        list.setDescription("Preferiti");
                        list.setTitle("Preferiti");
                        list.setType(UserListType.PREFERED.toString());
                        list.addRecord();
                    }

                    list = (Userlist) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, Userlist.class, "where idUser='" + myMap.get("google") + "' and type='TOWATCH'");

                    if (list == null) {
                        list = new Userlist();
                        list.setIdUser(myMap.get("google"));
                        list.setSql_connection(conn);
                        list.setDescription("Da Vedere");
                        list.setTitle("Da Vedere");
                        list.setType(UserListType.TOWATCH.toString());
                        list.addRecord();
                    }

                    list = (Userlist) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, Userlist.class, "where idUser='" + myMap.get("google") + "' and type='WATCH'");

                    if (list == null) {
                        list = new Userlist();
                        list.setIdUser(myMap.get("google"));
                        list.setSql_connection(conn);
                        list.setDescription("Visti");
                        list.setTitle("Visti");
                        list.setType(UserListType.WATCH.toString());
                        list.addRecord();
                    }

                    return "Registrazione avvenuta con successo";
                } else {
                    return "Sono già presenti le liste personalizzate per questo utente";
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

        return "";
    }


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
        if (myMap.containsKey("latest") && myMap.get("latest").equals("true")) {
            return JSONCreation.getJSONToCreate(CineMatesTheMovieDB.comingSoon(), MovieDb.class.getSimpleName());
        }
        if (myMap.containsKey("most") && myMap.get("most").equals("true")) {
            List<AbstractSQLRecord> sql = (FactoryRecord.getNewIstance(conn).getListOfRecord(conn, MostViewed.class, ""));
            List<MovieDb> movies = new ArrayList<>();
            for (AbstractSQLRecord record : sql) {
                movies.add(CineMatesTheMovieDB.searchFilmById(((MostViewed) record).getIdFilm()));
            }
            return JSONCreation.getJSONToCreate(movies, MovieDb.class.getSimpleName());
        }
        if (myMap.containsKey("filmId"))
            return JSONCreation.getJSONToCreate(CineMatesTheMovieDB.searchFilmById(Integer.parseInt(myMap.get("filmId"))), MovieDb.class.getSimpleName());
        if (myMap.containsKey("year") && myMap.containsKey("adult")) {
            String str = JSONCreation.getJSONToCreate(searchFilmByName(myMap.get("name"), Integer.parseInt(myMap.get("year")), Boolean.parseBoolean(myMap.get("adult"))), MovieDb.class.getSimpleName());
            System.out.println(str);
            return str;
        } else {
            if (myMap.containsKey("year"))
                return JSONCreation.getJSONToCreate(searchFilmByName(myMap.get("name"), Integer.parseInt(myMap.get("year"))), MovieDb.class.getSimpleName());
            else if (myMap.containsKey("adult"))
                return JSONCreation.getJSONToCreate(searchFilmByName(myMap.get("name"), Boolean.parseBoolean(myMap.get("adult"))), MovieDb.class.getSimpleName());
            else
                return JSONCreation.getJSONToCreate(searchFilmByName(myMap.get("name")), MovieDb.class.getSimpleName());
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
            return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, reviews.class, "idFilm=" + query.get("idFilm")), reviews.class.getCanonicalName());
        } else {
            return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, reviews.class, "iduser=" + query.get("iduser")), reviews.class.getCanonicalName());
        }
    }

    //ip:8080/review?filmId=id
    //ip:8080/review?iduser=id


    @GetMapping(value = "/list")
    @ResponseBody
    public String list(@RequestParam Map<String, String> query) {
        return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Userlist.class, "idUser=" + query.get("idUser")), Userlist.class.getCanonicalName());
    }

    @GetMapping(value = "/notify")
    @ResponseBody
    public String notify(@RequestParam Map<String, String> query) {
        List<Notify> list = new ArrayList<>();
        List<AbstractSQLRecord> rec = FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Notify.class, "id_receiver=" + query.get("idUser"));
        if (rec.size() > 0) {
            for (AbstractSQLRecord record : rec) {
                list.add((Notify) record);
            }
            for (Notify not : list) {
                System.out.println(not.getId_notify());
            }
            return JSONCreation.getJSONToCreate(list, Notify.class.getCanonicalName());
        } else
            return "[]";
    }

    //ip:8080/list?userId=id


    @PostMapping(value = "/todecide")
    @ResponseBody
    public String insertFilm(@RequestBody String json) {
        System.out.println(json);
        return json;
    }

    @RequestMapping(value = "/test")
    public String test() {
        List<Contact> cont = new ArrayList<>();
        List<AbstractSQLRecord> rec = FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Contact.class, "");
        for (AbstractSQLRecord record : rec)
            cont.add((Contact) record);
        return "Hello World!";
    }
    //ip:8080/insertFilm      json
    //better to build with HTTP Builder apache tomcat
    //mapping with application/json

}
