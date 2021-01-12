import DataBase.DbConnectionForBackEnd;
import MovieDB.CineMatesTheMovieDB;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.Person;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

import static java.awt.Toolkit.*;

/**
 * HelloJavaLogging!
 *
 */

public class StartClass {

    public static void main(String[] args){
        DbConnectionForBackEnd db = new DbConnectionForBackEnd();
        db.test();
        CineMatesTheMovieDB TheMovieDBAPI = new CineMatesTheMovieDB();
        List<MovieDb> actorFilmography = TheMovieDBAPI.searchActorFilmographyIncludingAdult("" );;
        System.out.println("Size: "+actorFilmography.size());
        for (MovieDb films : actorFilmography){
            System.out.println(films.getTitle() +" "+films.getReleaseDate());
        }
    }
}
