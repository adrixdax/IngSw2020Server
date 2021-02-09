import DataBase.DbConnectionForBackEnd;
import MovieDB.CineMatesTheMovieDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import core.Classes.User;
import core.sql.FactoryRecord;
import info.movito.themoviedbapi.model.MovieDb;
import org.springframework.boot.SpringApplication;
import spring.SpringController;
import utility.Json.Creation.JSONCreation;
import utility.Json.Decode.JSONDecoder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class StartClass {


    public static void main(String[] args) {
        SpringApplication.run(SpringController.class, args);
        //DbConnectionForBackEnd db = new DbConnectionForBackEnd();
        //db.test();
        /*List<MovieDb> actorFilmography = CineMatesTheMovieDB.searchActorFilmography("Bonnie Wright");
        System.out.println("Size: " + actorFilmography.size());
        String film = JSONCreation.getJSONToCreate(actorFilmography);
        System.out.println(film);
        User u = (User) FactoryRecord.getNewIstance(db.getConnection()).getSingleRecord(db.getConnection(), User.class, "where idUSer=1");
        User u2 = (User) FactoryRecord.getNewIstance(db.getConnection()).getSingleRecord(db.getConnection(), User.class, "where idUSer=2");
        String newjson = JSONCreation.getJSONToCreate(u);
        System.out.println(newjson);
        List<User> users = new ArrayList<>();
        users.add(u);
        users.add(u2);
        String anotherjson = JSONCreation.getJSONToCreate(users);
        System.out.println(anotherjson);
        String test = JSONCreation.getJSONToCreate(u);
        User testingUser;
        /*try {
            testingUser = (User) JSONDecoder.getDecodedJson(test);
            for (Field field : testingUser.getClass().getDeclaredFields()){
                System.out.println(testingUser.getNick());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

         */
    }

}