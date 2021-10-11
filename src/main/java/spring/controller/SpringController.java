package spring.controller;


import DataBase.DbConnectionForBackEnd;
import MovieDB.CineMatesTheMovieDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import core.Classes.*;
import core.FireBase.FireBaseUserService;
import core.sql.AbstractSQLRecord;
import core.sql.FactoryRecord;
import info.movito.themoviedbapi.model.MovieDb;
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

    private static Connection conn;
    private int onlineMembers=0;

    public SpringController() {
        conn = new DbConnectionForBackEnd().getConnection();
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(25200000);
                    FactoryRecord.getNewIstance(conn).getSingleRecord(conn, MostViewed.class, "");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean checkConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = new DbConnectionForBackEnd().getConnection();
        }
        return true;
    }

    private Map<String, String> getHttpRequestMap(String query) {
        HTTPRequest request = null;
        try {
            request = (HTTPRequest) JSONDecoder.getDecodedJson(query, HTTPRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert request != null;
        return request.getMap();
    }

    @PostMapping(value = "/user")
    @ResponseBody
    public String user(@RequestBody String query) {
        HTTPRequest request = null;
        try {
            request = (HTTPRequest) JSONDecoder.getDecodedJson(query, HTTPRequest.class);
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
                    return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, UserList.class,
                            "idUser = '" + myMap.get("idUser") + "' " +
                                    " and (type='" + UserListType.PREFERED + "' " +
                                    " OR type='" + UserListType.WATCH +
                                    "' OR type='" + UserListType.TOWATCH + "' )"), UserList.class.getCanonicalName());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (myMap.containsKey("addList") && myMap.get("addList").equals("true") && myMap.containsKey("idUser") && myMap.containsKey("listTitle")) {
            UserList list = new UserList();
            list.setSql_connection(conn);
            list.setType(UserListType.CUSTOM.toString());
            list.setTitle(myMap.get("listTitle"));
            list.setDescription(myMap.getOrDefault("listDescription", "\0"));
            list.setIdUser(myMap.get("idUser"));
            list.addRecord();
            return "Lista aggiunta con successo";
        }
        if (myMap.containsKey("removeList") && myMap.get("removeList").equals("true") && myMap.containsKey("idUser") && myMap.containsKey("idList")) {
            UserList list = (UserList) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, UserList.class, "where idUser='" + myMap.get("idUser") + "' and idUserList='" + myMap.get("idList") + "'");
            if (list != null) {
                list.deleteRecord();
            }
        } else if (myMap.containsKey("addFriends") && myMap.get("addFriends").equals("true") && myMap.containsKey("idUser") && myMap.containsKey("idOtherUser")) {
            Contact contact = (Contact) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, Contact.class, "where (user1 ='" + myMap.get("idUser") + "' And user2 ='" + myMap.get("idOtherUser") + "') " +
                    "OR (user1 = '" + myMap.get("idOtherUser") + "' AND user2 ='" + myMap.get("idUser") + "' )");
            Notify not = (Notify) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, Notify.class, "where (id_sender='" + myMap.get("idUser") + "' and id_receiver='" + myMap.get("idOtherUser") + "') or (id_sender='" + myMap.get("idOtherUser") + "' and id_receiver='" + myMap.get("idUser") + "')");
            if (contact == null) {
                not.setState(NotifyStatusType.ACCEPTED.toString());
                not.setSql_connection(conn);
                not.updateRecord();
                contact = new Contact();
                contact.setUser1(myMap.get("idUser"));
                contact.setUser2(myMap.get("idOtherUser"));
                contact.setSql_connection(conn);
                contact.addRecord();
            } else {
                not.setState(NotifyStatusType.ACCEPTED.toString());
                not.setSql_connection(conn);
                not.updateRecord();
                return "Utenti già Amici";
            }
        } else if (myMap.containsKey("isFriends") && myMap.get("isFriends").equals("true") && myMap.containsKey("idUser") && myMap.containsKey("idOtherUser")) {
            Contact contact = (Contact) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, Contact.class, "where (user1 ='" + myMap.get("idUser") + "' And user2 ='" + myMap.get("idOtherUser") + "') " +
                    "OR (user1 = '" + myMap.get("idOtherUser") + "' AND user2 ='" + myMap.get("idUser") + "' )");
            if (contact != null) {
                return "true";
            } else {
                return "false";
            }
        } else if (myMap.containsKey("isFriends") && myMap.get("isFriends").equals("true") && myMap.containsKey("idUser")) {
            return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Contact.class, "(user1  ='" + myMap.get("idUser") + "') OR (user2 = '" + myMap.get("idUser") + "')"), Contact.class.getCanonicalName());
        }
        return "";
    }

    @PostMapping(value = "/registration")
    @ResponseBody
    public String registration(@RequestBody String query) {
        Map<String, String> myMap = getHttpRequestMap(query);
        if (myMap.containsKey("registration")) {
            try {
                if (checkConnection()) {
                    UserList list = new UserList();
                    list.setIdUser(myMap.get("registration"));
                    list.setSql_connection(conn);
                    list.setDescription("Preferiti");
                    list.setTitle("Preferiti");
                    list.setType(UserListType.PREFERED.toString());
                    list.addRecord();

                    list = new UserList();
                    list.setIdUser(myMap.get("registration"));
                    list.setSql_connection(conn);
                    list.setDescription("Da Vedere");
                    list.setTitle("Da Vedere");
                    list.setType(UserListType.TOWATCH.toString());
                    list.addRecord();

                    list = new UserList();
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
                    UserList list = (UserList) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, UserList.class, "where idUser='" + myMap.get("google") + "' and type='PREFERED'");
                    if (list == null) {
                        list = new UserList();
                        list.setIdUser(myMap.get("google"));
                        list.setSql_connection(conn);
                        list.setDescription("Preferiti");
                        list.setTitle("Preferiti");
                        list.setType(UserListType.PREFERED.toString());
                        list.addRecord();
                    }
                    list = (UserList) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, UserList.class, "where idUser='" + myMap.get("google") + "' and type='TOWATCH'");
                    if (list == null) {
                        list = new UserList();
                        list.setIdUser(myMap.get("google"));
                        list.setSql_connection(conn);
                        list.setDescription("Da Vedere");
                        list.setTitle("Da Vedere");
                        list.setType(UserListType.TOWATCH.toString());
                        list.addRecord();
                    }
                    list = (UserList) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, UserList.class, "where idUser='" + myMap.get("google") + "' and type='WATCH'");
                    if (list == null) {
                        list = new UserList();
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

    @PostMapping(value = "/film")
    @ResponseBody
    public String film(@RequestBody String query) {
        Map<String, String> myMap = getHttpRequestMap(query);
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
        if (myMap.containsKey("filmId")) {
            return JSONCreation.getJSONToCreate(CineMatesTheMovieDB.searchFilmById(Integer.parseInt(myMap.get("filmId"))), MovieDb.class.getSimpleName());
        }if (myMap.containsKey("year") && myMap.containsKey("adult")) {
            return JSONCreation.getJSONToCreate(searchFilmByName(myMap.get("name"), Integer.parseInt(myMap.get("year")), Boolean.parseBoolean(myMap.get("adult"))), MovieDb.class.getSimpleName());
        } else {
            if (myMap.containsKey("year"))
                return JSONCreation.getJSONToCreate(searchFilmByName(myMap.get("name"), Integer.parseInt(myMap.get("year"))), MovieDb.class.getSimpleName());
            else if (myMap.containsKey("adult"))
                return JSONCreation.getJSONToCreate(searchFilmByName(myMap.get("name"), Boolean.parseBoolean(myMap.get("adult"))), MovieDb.class.getSimpleName());
            else
                return JSONCreation.getJSONToCreate(searchFilmByName(myMap.get("name")), MovieDb.class.getSimpleName());
        }
    }

    @PostMapping(value = "/review")
    @ResponseBody
    public String review(@RequestBody String query) {
        Map<String, String> myMap = getHttpRequestMap(query);
        if (myMap.containsKey("idRecordRef") && myMap.containsKey("title") && myMap.containsKey("description") && myMap.containsKey("val") && myMap.containsKey("idUser") && myMap.containsKey("insert") && myMap.get("insert").equals("true")) {
            try {
                checkConnection();
                Reviews rew = new Reviews();
                rew.setSql_connection(conn);
                rew.setTitle(myMap.get("title"));
                rew.setDescription(myMap.getOrDefault("description",""));
                rew.setIdRecordRef(Integer.parseInt(myMap.get("idRecordRef")));
                rew.setIduser(myMap.get("idUser"));
                rew.setVal(Double.parseDouble(myMap.get("val")));
                rew.setTypeOfReview(myMap.get("typeOfReview"));
                rew.addRecord();
                if(myMap.get("typeOfReview").equals("LIST")){

                    UserList user = (UserList) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, UserList.class,"where idUserList='"+myMap.get("idRecordRef")+"'");
                    if(user!=null) {
                        Notify notify = new Notify();
                        notify.setSql_connection(conn);
                        notify.setId_recordref(rew.getIdReviews());
                        notify.setId_sender(myMap.get("idUser"));
                        notify.setId_receiver(user.getIdUser());
                        notify.setDateOfSend(System.currentTimeMillis());
                        notify.setType("LIST_REVIEW");
                        notify.setState(NotifyStatusType.PENDING.toString());
                        notify.addRecord();

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            checkConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (myMap.containsKey("idRecordRef") && myMap.containsKey("insert") && myMap.get("insert").equals("false")) {
            return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Reviews.class, "idRecordRef=" + myMap.get("idRecordRef") +" and obscured=false"), Reviews.class.getCanonicalName());
        } else if (myMap.containsKey("idUser") && myMap.containsKey("insert") && myMap.get("insert").equals("false")) {
            return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Reviews.class, "iduser='" + myMap.get("idUser") + "' and TypeOfReview='"+myMap.get("typeOfReview")+"' and obscured=false"), Reviews.class.getCanonicalName());
        } else if (myMap.containsKey("idReviews") && myMap.containsKey("delete") && myMap.get("delete").equals("true")){
            Reviews r = (Reviews) FactoryRecord.getNewIstance(conn).getSingleRecord(conn,Reviews.class,"idReviews="+myMap.get("idReviews"));
            r.deleteRecord();
            return "Review deleted correctly";
        }
        return "";
    }

    @GetMapping(value = "/review")
    @ResponseBody
    public String review(@RequestParam Map<String, String> query) {
        if (query.containsKey("idReviews")) {
            try {
                checkConnection();
                return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getSingleRecord(conn, Reviews.class, "idReviews=" + query.get("idReviews")), Reviews.class.getCanonicalName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @GetMapping(value = "/list")
    @ResponseBody
    public String list(@RequestParam Map<String, String> query) {
        try {
            checkConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (query.containsKey("idUser"))
            return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, UserList.class, "idUser=" + query.get("idUser")), UserList.class.getCanonicalName());
        else {
            UserList u = (UserList) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, UserList.class, "idUserList='" + query.get("idUserList") + "'");
            return JSONCreation.getJSONToCreate(u, UserList.class.getCanonicalName());
        }
    }

    @PostMapping(value="/notify")
    @ResponseBody
    public String newNotify(@RequestBody String param){
        Map<String, String> query = getHttpRequestMap(param);
        try {
            checkConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (query.containsKey("id_sender") && query.containsKey("id_receiver") && query.containsKey("type") && query.containsKey("sendNotify") && query.get("sendNotify").equals("true")) {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
            Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
            List<AbstractSQLRecord> sql = FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Notify.class,
                    "where id_receiver='" + query.get("id_receiver") +
                            "' and id_sender='" + query.get("id_sender") +
                            "' and type='" + query.get("type") +
                            "' and id_recordref='" + (Integer.parseInt(query.get("id_recordref").isEmpty() ? "0" : query.get("id_recordref"))) +
                            "' order by  dateOfSend DESC");

            Notify not = new Notify();
            for (AbstractSQLRecord record : sql) {
                Notify tmp = (Notify) record;
                if(tmp!=null){
                    if(not.getDateOfSend()< tmp.getDateOfSend()){
                        not=tmp;
                    }
                }else{
                    not=tmp;
                }
            }
            cal2.setTimeInMillis(not.getDateOfSend());
            cal2.add(Calendar.WEEK_OF_YEAR, 1);
            if (cal2.getTimeInMillis() < cal.getTimeInMillis()) {
                not = new Notify();
                not.setSql_connection(conn);
                not.setId_receiver(query.get("id_receiver"));
                not.setId_sender(query.get("id_sender"));
                not.setType(String.valueOf(query.get("type")));
                not.setId_recordref(Integer.parseInt(query.get("id_recordref").isEmpty() ? "0" : query.get("id_recordref")));
                not.setState(NotifyStatusType.PENDING.toString());
                not.setDateOfSend(cal.getTimeInMillis());
                not.addRecord();
                return "Notifica inviata con successo";

            } else if (not.getType().equals("LIST")) {
                not = new Notify();
                not.setSql_connection(conn);
                not.setId_receiver(query.get("id_receiver"));
                not.setId_sender(query.get("id_sender"));
                not.setType(String.valueOf(query.get("type")));
                not.setId_recordref(Integer.parseInt(query.get("id_recordref").isEmpty() ? "0" : query.get("id_recordref")));
                not.setState(NotifyStatusType.PENDING.toString());
                not.setDateOfSend(cal.getTimeInMillis());
                not.addRecord();
            }
            return "Content Shared";
        }
        return "";
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
        } else if (query.containsKey("friendId")){
            List<AbstractSQLRecord> rec = FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Notify.class, "id_receiver='" + query.get("friendId") + "' and (state = 'PENDING' OR state = 'SEEN') and type='FRIENDSHIP_REQUEST' order by state asc");
            rec.addAll(FactoryRecord.getNewIstance(conn).getListOfRecord(conn, Notify.class, "id_sender='" + query.get("friendId") + "' and (state = 'PENDING' OR state = 'SEEN') and type='FRIENDSHIP_REQUEST' order by state asc"));
            if (rec.size() > 0) {
                for (AbstractSQLRecord record : rec) {
                    list.add((Notify) record);
                }
                return JSONCreation.getJSONToCreate(list, Notify.class.getCanonicalName());
            }
        else return "[]";
        } else if (query.containsKey("Seen")) {
            Notify not = (Notify) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, Notify.class, "id_Notify=" + query.get("Seen"));
            not.setSql_connection(conn);
            not.setState(NotifyStatusType.SEEN.toString());
            not.updateRecord();
            return "Status Changed";
        } else if (query.containsKey("Accepted")) {
            Notify not = (Notify) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, Notify.class, "id_Notify=" + query.get("Accepted"));
            not.setSql_connection(conn);
            not.setState(NotifyStatusType.ACCEPTED.toString());
            not.updateRecord();
            switch (not.getType()) {
                case "LIST": {
                    UserList esisteLista = (UserList) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, UserList.class, "dependency_List=" + not.getId_recordref() + " and idUser='" + not.getId_receiver() + "'");
                    if (esisteLista != null)
                        esisteLista.deleteRecord();
                    UserList nuovaLista = new UserList();
                    nuovaLista.setSql_connection(conn);
                    nuovaLista.setIdUser(not.getId_receiver());
                    nuovaLista.setType(String.valueOf(UserListType.CUSTOM));
                    UserList listaDaClonare = (UserList) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, UserList.class, "where idUserList=" + not.getId_recordref());
                    nuovaLista.setTitle(listaDaClonare.getTitle());
                    nuovaLista.setDescription(listaDaClonare.getDescription() + "\nTi è stata suggerita da: " + Objects.requireNonNull(FireBaseUserService.getFireBaseUser(not.getId_sender())).getNick());
                    nuovaLista.setDependency_List(listaDaClonare.getIdUserList());
                    nuovaLista.addRecord();
                    List<AbstractSQLRecord> listaDiFilm = FactoryRecord.getNewIstance(conn).getListOfRecord(conn, filminlist.class, "idList=" + not.getId_recordref());
                    for (AbstractSQLRecord film : listaDiFilm) {
                        filminlist nuovoFilm = new filminlist();
                        nuovoFilm.setSql_connection(conn);
                        nuovoFilm.setIdFilm(((filminlist) (film)).getIdFilm());
                        nuovoFilm.setIdList(nuovaLista.getIdUserList());
                        nuovoFilm.addRecord();
                    }
                    break;
                }
                case "FILM": {
                    UserList toWatchList = (UserList) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, UserList.class, "where idUser='" + not.getId_receiver() + "' and type='TOWATCH'");
                    filminlist isAlreadyInList = (filminlist) FactoryRecord.getNewIstance(conn).getSingleRecord(conn,filminlist.class,"where idFilm="+not.getId_recordref()+" and idList="+toWatchList.getIdUserList());
                    if (isAlreadyInList == null) {
                        filminlist film = new filminlist();
                        film.setSql_connection(conn);
                        film.setIdList(toWatchList.getIdUserList());
                        film.setIdFilm(not.getId_recordref());
                        film.addRecord();
                    }
                    break;
                }
            }
            return "Status Changed";
        } else if (query.containsKey("Refused")) {
            Notify not = (Notify) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, Notify.class, "id_Notify=" + query.get("Refused"));
            not.setSql_connection(conn);
            not.setState(NotifyStatusType.REFUSED.toString());
            not.updateRecord();
            return "Status Changed";
        }
        return "[]";
    }

    @PostMapping(value = "/list")
    @ResponseBody
    public String list(@RequestBody String query) {
        Map<String, String> myMap = getHttpRequestMap(query);
        if (myMap.containsKey("custom") && myMap.get("custom").equals("true") && myMap.containsKey("idUser") && myMap.containsKey("idFilm")) {

            List<AbstractSQLRecord> lists = FactoryRecord.getNewIstance(conn).getListOfRecord(conn, UserList.class, "idUser = '" + myMap.get("idUser") + "' " +
                    " and type='" + UserListType.CUSTOM + "'");

            if (!myMap.get("idFilm").equals("-1")) {
                if (lists != null) {
                    List<UserList> listToReturn = new ArrayList<>();
                    for (AbstractSQLRecord single : lists) {
                        UserList s = (UserList) single;
                        filminlist listToJump = (filminlist) FactoryRecord.getNewIstance(conn).getSingleRecord(conn, filminlist.class, " idList ='" + s.getIdUserList() + "' and idFilm='" + myMap.get("idFilm") + "' ");
                        if (listToJump == null) {
                            listToReturn.add(s);
                        }

                    }
                    return JSONCreation.getJSONToCreate(listToReturn, UserList.class.getCanonicalName());
                }


            } else {
                return JSONCreation.getJSONToCreate(lists, UserList.class.getCanonicalName());

            }
        } else if (myMap.containsKey("idList") && myMap.containsKey("idFilm") && ((!myMap.containsKey("addFilm")) && (!myMap.containsKey("removeFilm")))) {
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
            return "true";

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


    @PostMapping(value = "/report")
    public String report(@RequestBody String query){
        Map<String,String> map = getHttpRequestMap(query);
        try {
            checkConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (map.containsKey("addReport") && map.get("addReport").equals("true")) {
            try {
                if (checkConnection()) {
                    Report report = new Report();
                    report.setSql_connection(conn);
                    report.setId_user(map.get("idUser"));
                    report.setId_recordRef(Integer.parseInt(map.get("id_recordRef")));
                    report.setReportType(map.get("reportType"));
                    report.addRecord();
                    return "Grazie della segnalazione";
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (map.containsKey("visible") && map.get("visible").equals("true")){
            Report r = (Report) FactoryRecord.getNewIstance(conn).getSingleRecord(conn,Report.class,"idReport="+map.get("idReport"));
            Reviews rv = (Reviews) FactoryRecord.getNewIstance(conn).getSingleRecord(conn,Reviews.class,"idReviews="+r.getId_recordRef());
            rv.setObscured(false);
            rv.updateRecord();
            r.deleteRecord();
            return "content updated";
        }
        return "";
    }

    @GetMapping(value = "/report")
    public String report(@RequestParam Map<String, String> query){
        try {
            checkConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }if(Boolean.parseBoolean(query.get("getReports"))){
            try{
                return  JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getListOfRecord(conn,Report.class, "","id_recordRef asc"), Report.class.getCanonicalName());
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        }
        return "";
    }

    @GetMapping(value = "/online")
    public String onlineUsers(@RequestParam Map<String, Boolean> query) {
        if (query.containsKey("loggingIn")) {
            if (Boolean.parseBoolean(String.valueOf(query.get("loggingIn"))))
                onlineMembers++;
            else {
                if (onlineMembers <= 0) {
                    onlineMembers = 0;
                } else {
                    onlineMembers--;
                }
            }
            return "Ok";
        }
        if (query.containsKey("getOnlineUsers")) {
            return String.valueOf(onlineMembers);
        }
        return "error";
    }

}