package MovieDB;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

import java.util.ArrayList;
import java.util.List;

public class CineMatesTheMovieDB {

    private static final TmdbApi api = new TmdbApi("cc5fb6d50718c39062251de8b4d2995c");
    private static final TmdbMovies singleMovieSearch = api.getMovies();
    private static final TmdbSearch generalSearchService = api.getSearch();

    public static MovieDb searchFilmById(int filmId) {
        return singleMovieSearch.getMovie(filmId, "it", TmdbMovies.MovieMethod.credits);
    }

    public static List<MovieDb> searchFilmByName(String filmName, int searchYear, boolean adultEnable) {
        if (filmName.length() > 0) {
            List<MovieDb> films = new ArrayList<>();
                MovieResultsPage newmovies = generalSearchService.searchMovie(filmName, searchYear, "it", adultEnable, 0);
                if (newmovies.getTotalResults() > 0) {
                    for (int j = 0; j < newmovies.getResults().size(); j++) {
                        films.add(searchFilmById(newmovies.getResults().get(j).getId()));
                    }
                }
            return films;
        }
    else
            return new ArrayList<>();
    }

    public static List<MovieDb> searchFilmByName(String filmName, boolean adult) {
        return searchFilmByName(filmName, 0, adult);
    }

    public static List<MovieDb> searchFilmByName(String filmName, int searchYear) {
        return searchFilmByName(filmName, searchYear, false);
    }

    public static List<MovieDb> searchFilmByName(String filmName) {
        return searchFilmByName(filmName, 0, false);
    }

    public static List<MovieDb> comingSoon(){
        int value=singleMovieSearch.getUpcoming("it",0,"").getTotalResults();
        ArrayList<MovieDb> list = new ArrayList<>();
        //list= (ArrayList<MovieDb>) singleMovieSearch.getUpcoming("it",0,"").getResults();
        for (int i=0; i<10; i++){
            list.add(singleMovieSearch.getUpcoming("it",0,"").getResults().get(i));
        }

        return list;
    }

}

