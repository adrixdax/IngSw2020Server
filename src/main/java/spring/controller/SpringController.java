package spring.controller;


import DataBase.DbConnectionForBackEnd;
import MovieDB.CineMatesTheMovieDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import core.Classes.*;
import core.sql.AbstractSQLRecord;
import core.sql.FactoryRecord;
import info.movito.themoviedbapi.model.MovieDb;
import org.mockito.internal.matchers.Not;
import org.springframework.web.bind.annotation.*;
import utility.Json.Creation.JSONCreation;
import utility.Json.Decode.JSONDecoder;
import utility.Json.Requests.HTTPRequest;
import utility.NotifyStatusType;
import utility.UserListType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static MovieDB.CineMatesTheMovieDB.searchFilmByName;


@RestController
public class SpringController {

    Connection conn;
    CineMatesTheMovieDB CineMates;

    public SpringController() {
        this.conn = new DbConnectionForBackEnd().getConnection();
    }


    private boolean checkConnection() throws SQLException {
        if (conn.isClosed() || conn == null) {
            this.conn = new DbConnectionForBackEnd().getConnection();
            return true;
        } else {
            return true;
        }
    }

    @PostMapping(value = "/user")
    @ResponseBody
    public String user(@RequestBody String query) {
        HTTPRequest request = null;
        try {
            request = (HTTPRequest) JSONDecoder.getDecodedJson(query);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert request != null;
        try {
            checkConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Map<String, String> myMap = request.getMap();
        if (myMap.containsKey("idUser") && myMap.containsKey("searchDefaultList") && myMap.get("searchDefaultList").equals("true")) {
            try {
                if (checkConnection()) {
                    return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Userlist.class,
                            "idUser = '" + myMap.get("idUser") + "' " +
                                    " and (type='" + UserListType.PREFERED.toString() + "' " +
                                    " OR type='" + UserListType.WATCH.toString() +
                                    "' OR type='" + UserListType.TOWATCH.toString() + "' )"), Userlist.class.getCanonicalName());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        if (myMap.containsKey("addList") && myMap.get("addList").equals("true") && myMap.containsKey("idUser") && myMap.containsKey("listTitle")) {
            Userlist list = new Userlist();
            list.setSql_connection(conn);
            list.setType(UserListType.CUSTOM.toString());
            list.setTitle(myMap.get("listTitle"));
            list.setDescription(myMap.get("listDescription"));
            list.setIdUser(myMap.get("idUser"));
            list.addRecord();

            return "Lista aggiunta con successo";

        }
        if (myMap.containsKey("custom") && myMap.get("custom").equals("true") && myMap.containsKey("idUser") && myMap.containsKey("idFilm")) {

            List<AbstractSQLRecord> lists = FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Userlist.class, "idUser = '" + myMap.get("idUser") + "' " +
                    " and type='" + UserListType.CUSTOM.toString() + "'");

            if (!myMap.get("idFilm").equals("-1")) {
                if (lists != null) {
                    List<Userlist> listToReturn = new ArrayList<>();
                    for (AbstractSQLRecord single : lists) {
                        Userlist s = (Userlist) single;
                        filminlist listToJump = (filminlist) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, filminlist.class, " idList ='" + s.getIdUserList() + "' and idFilm='" + myMap.get("idFilm") + "' ");
                        if (listToJump == null) {
                            listToReturn.add(s);
                        }

                    }
                    return JSONCreation.getJSONToCreate(listToReturn, Userlist.class.getCanonicalName());
                }


            } else {
                return JSONCreation.getJSONToCreate(lists, Userlist.class.getCanonicalName());

            }
        }else if(myMap.containsKey("addFriends") && myMap.get("addFriends").equals("true") && myMap.containsKey("idUser") && myMap.containsKey("idOtherUser")){

            Contact contact = (Contact) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, Contact.class, "where (user1 ='"+myMap.get("idUser")+"' And user2 ='"+myMap.get("idOtherUser")+"') " +
                    "OR (user1 = '"+myMap.get("idOtherUser")+"' AND user2 ='"+myMap.get("idUser") +"' )");

            if(contact == null) {
                contact = new Contact();
                contact.setUser1(myMap.get("idUser"));
                contact.setUser2(myMap.get("idOtherUser"));
                contact.setSql_connection(conn);
                contact.addRecord();
            }else{
                return "Utenti già Amici";
            }

        }
        else if(myMap.containsKey("isFriends") && myMap.get("isFriends").equals("true") && myMap.containsKey("idUser") && myMap.containsKey("idOtherUser")){

            Contact contact = (Contact) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, Contact.class, "where (user1 ='"+myMap.get("idUser")+"' And user2 ='"+myMap.get("idOtherUser")+"') " +
                    "OR (user1 = '"+myMap.get("idOtherUser")+"' AND user2 ='"+myMap.get("idUser") +"' )");

            if(contact != null) {
                return "true";
            }else{
                return "false";
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
            request = (HTTPRequest) JSONDecoder.getDecodedJson(query);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert request != null;
        Map<String, String> myMap = request.getMap();
        if (myMap.containsKey("registration")) {
            try {
                if (checkConnection()) {
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
                if (checkConnection()) {

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
        try {
            checkConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (myMap.containsKey("mostviewed") && myMap.get("mostviewed").equals("true")) {
            List<AbstractSQLRecord> sql = FactoryRecord.getNewIstance(conn).getListOfRecord(conn, MostViewed.class, "");
            List<MovieDbExtended> movies = new ArrayList<>();
            if (sql.size() < 10) {
                for (AbstractSQLRecord record : sql) {
                    MovieDbExtended movie = new MovieDbExtended(CineMatesTheMovieDB.searchFilmById(((MostViewed) record).getIdFilm()), ((MostViewed) record).getCounter());
                    movies.add(movie);
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    MovieDbExtended movie = new MovieDbExtended(CineMatesTheMovieDB.searchFilmById(((MostViewed) sql.get(i)).getIdFilm()), ((MostViewed) sql.get(i)).getCounter());
                    movies.add(movie);
                }
            }
            return JSONCreation.getJSONToCreate(movies, MovieDbExtended.class.getSimpleName());
        }
        if (myMap.containsKey("mostreviewed") && myMap.get("mostreviewed").equals("true")) {
            List<AbstractSQLRecord> sql = FactoryRecord.getNewIstance(conn).getListOfRecord(conn, MostReviewed.class, "");
            List<MovieDbExtended> movies = new ArrayList<>();
            if (sql.size() < 10) {
                for (AbstractSQLRecord record : sql) {
                    MovieDbExtended movie = new MovieDbExtended(CineMatesTheMovieDB.searchFilmById(((MostReviewed) record).getIdFilm()), ((MostReviewed) record).getCounter());
                    movies.add(movie);
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    MovieDbExtended movie = new MovieDbExtended(CineMatesTheMovieDB.searchFilmById(((MostReviewed) sql.get(i)).getIdFilm()), ((MostReviewed) sql.get(i)).getCounter());
                    movies.add(movie);
                }
            }
            return JSONCreation.getJSONToCreate(movies, MovieDbExtended.class.getSimpleName());
        }
        if (myMap.containsKey("userPrefered") && myMap.get("userPrefered").equals("true")) {
            List<AbstractSQLRecord> sql = FactoryRecord.getNewIstance(conn).getListOfRecord(conn, UserPrefered.class, "");
            List<MovieDbExtended> movies = new ArrayList<>();
            if (sql.size() < 10) {
                for (AbstractSQLRecord record : sql) {
                    MovieDbExtended movie = new MovieDbExtended(CineMatesTheMovieDB.searchFilmById(((UserPrefered) record).getIdFilm()), ((UserPrefered) record).getCounter());
                    movies.add(movie);
                }
                return JSONCreation.getJSONToCreate(movies, MovieDbExtended.class.getSimpleName());
            } else {
                for (int i = 0; i < 10; i++) {
                    MovieDbExtended movie = new MovieDbExtended(CineMatesTheMovieDB.searchFilmById(((UserPrefered) sql.get(i)).getIdFilm()), ((UserPrefered) sql.get(i)).getCounter());
                    movies.add(movie);
                }
                return JSONCreation.getJSONToCreate(movies, MovieDbExtended.class.getSimpleName());
            }
        }
        if (myMap.containsKey("filmId"))
            return JSONCreation.getJSONToCreate(CineMatesTheMovieDB.searchFilmById(Integer.parseInt(myMap.get("filmId"))), MovieDb.class.getSimpleName());
        if (myMap.containsKey("year") && myMap.containsKey("adult")) {
            String str = JSONCreation.getJSONToCreate(searchFilmByName(myMap.get("name"), Integer.parseInt(myMap.get("year")), Boolean.parseBoolean(myMap.get("adult"))), MovieDb.class.getSimpleName());
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

    @PostMapping(value = "/review")
    @ResponseBody
    public String review(@RequestBody String query) {
        HTTPRequest request = null;
        try {
            request = (HTTPRequest) JSONDecoder.getDecodedJson(query);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert request != null;
        Map<String, String> myMap = request.getMap();
        if (myMap.containsKey("idFilm") && myMap.containsKey("title") && myMap.containsKey("description") && myMap.containsKey("val") && myMap.containsKey("idUser") && myMap.containsKey("insert") && myMap.get("insert").equals("true")) {
            try {
                checkConnection();
                reviews rew = new reviews();
                rew.setSql_connection(conn);
                rew.setTitle(myMap.get("title"));
                rew.setDescription(myMap.get("description"));
                rew.setIdFilm(Integer.parseInt(myMap.get("idFilm")));
                rew.setIduser(myMap.get("idUser"));
                rew.setVal(Double.parseDouble(myMap.get("val")));
                rew.addRecord();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            checkConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (myMap.containsKey("idFilm") && myMap.containsKey("insert") && myMap.get("insert").equals("false")) {
            return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, reviews.class, "idFilm=" + myMap.get("idFilm")), reviews.class.getCanonicalName());
        } else if (myMap.containsKey("idUser") && myMap.containsKey("insert") && myMap.get("insert").equals("false")) {
            return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, reviews.class, "iduser=" + myMap.get("iduser")), reviews.class.getCanonicalName());
        }
        return "";
    }

    //ip:8080/review?filmId=id
    //ip:8080/review?iduser=id


    @GetMapping(value = "/list")
    @ResponseBody
    public String list(@RequestParam Map<String, String> query) {
        try {
            checkConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Userlist.class, "idUser=" + query.get("idUser")), Userlist.class.getCanonicalName());
    }

    @GetMapping(value = "/notify")
    @ResponseBody
    public String notify(@RequestParam Map<String, String> query) {
        List<Notify> list = new ArrayList<>();
        try {
            checkConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (query.containsKey("idUser")) {
            List<AbstractSQLRecord> rec = FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Notify.class, "id_receiver='" + query.get("idUser") + "' and (state = 'PENDING' OR state = 'SEEN') order by state asc");
            if (rec.size() > 0) {
                for (AbstractSQLRecord record : rec) {
                    list.add((Notify) record);
                }
                return JSONCreation.getJSONToCreate(list, Notify.class.getCanonicalName());
            } else
                return "[]";
        }
        else if (query.containsKey("Seen")){
            Notify not = (Notify) FactoryRecord.getNewIstance(conn).getSingleRecord(conn,Notify.class,"id_Notify="+query.get("Seen"));
            not.setSql_connection(conn);
            not.setState("SEEN");
            not.updateRecord();
            return "Status Changed";
        }
        else if (query.containsKey("Accepted")){
            Notify not = (Notify) FactoryRecord.getNewIstance(conn).getSingleRecord(conn,Notify.class,"id_Notify="+query.get("Accepted"));
            not.setSql_connection(conn);
                not.setState("ACCEPTED");
            not.updateRecord();
            return "Status Changed";
        }
        else if (query.containsKey("Refused")){
            Notify not = (Notify) FactoryRecord.getNewIstance(conn).getSingleRecord(conn,Notify.class,"id_Notify="+query.get("Refused"));
            not.setSql_connection(conn);
            not.setState("REFUSED");
            not.updateRecord();
            return "Status Changed";
        }
        else if(query.containsKey("id_sender") && query.containsKey("id_receiver") && query.containsKey("type") && query.containsKey("sendNotify") && query.get("sendNotify").equals("true")){

            Notify not = new Notify();
            not.setSql_connection(conn);
            not.setId_receiver(query.get("id_receiver"));
            not.setId_sender(query.get("id_sender"));
            not.setType(query.get("type"));
            not.setId_recordref(Integer.parseInt(query.get("id_recordref").isEmpty()?"0" :query.get("id_recordref")));
            not.setState(NotifyStatusType.PENDING.toString());
            not.addRecord();

            return "Notifica inviata con successo";
        }
        return "[]";
    }

    //ip:8080/list?userId=id


    @PostMapping(value = "/todecide")
    @ResponseBody
    public String insertFilm(@RequestBody String json) {
        System.out.println(json);
        return json;
    }


    @PostMapping(value = "/list")
    @ResponseBody
    public String list(@RequestBody String query) {
        HTTPRequest request = null;
        try {
            System.out.println("Provaaaaaaaaa: " + query);
            request = (HTTPRequest) JSONDecoder.getDecodedJson(query);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert request != null;
        Map<String, String> myMap = request.getMap();
        if (myMap.containsKey("idList") && myMap.containsKey("idFilm") && ((!myMap.containsKey("addFilm")) && (!myMap.containsKey("removeFilm")))) {
            try {
                if (checkConnection()) {
                    filminlist film = (filminlist) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, filminlist.class,
                            "where idList='" + myMap.get("idList") + "' and idFilm='" + myMap.get("idFilm") + "' ");

                    if (film != null) {
                        return "true";
                    } else {
                        return "false";
                    }

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if (myMap.containsKey("idList") && myMap.containsKey("idFilm") && myMap.containsKey("addFilm") && myMap.get("addFilm").equals("true")) {
            try {
                checkConnection();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            filminlist film = new filminlist();
            film.setSql_connection(conn);
            film.setIdFilm(Integer.parseInt(myMap.get("idFilm")));
            film.setIdList(Integer.parseInt(myMap.get("idList")));
            film.addRecord();
            return "Film aggiunto con successo";

        } else if (myMap.containsKey("idList") && myMap.containsKey("idFilm") && myMap.containsKey("removeFilm") && myMap.get("removeFilm").equals("true")) {
            try {
                checkConnection();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            filminlist film = (filminlist) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, filminlist.class,
                    "where idList='" + myMap.get("idList") + "' and idFilm='" + myMap.get("idFilm") + "' ");
            if (film != null) {
                film.deleteRecord();
                return "Film eliminato con successo";
            }
        } else if (myMap.containsKey("idList")) {
            try {
                checkConnection();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            List<AbstractSQLRecord> sql = FactoryRecord.getNewIstance(conn).getListOfRecord(conn, filminlist.class, "idList='" + myMap.get("idList") + "'");
            List<MovieDb> movies = new ArrayList<>();

            for (AbstractSQLRecord record : sql)
                movies.add(CineMatesTheMovieDB.searchFilmById(((filminlist) record).getIdFilm()));
            return JSONCreation.getJSONToCreate(movies, MovieDbExtended.class.getSimpleName());

        }

        return "";
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
