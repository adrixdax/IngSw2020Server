package MovieDB;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbPeople;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonCredits;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CineMatesTheMovieDB {

    private static TmdbMovies singleMovieSearch;
    private final TmdbApi api;
    private final TmdbSearch generalSearchService;

    public CineMatesTheMovieDB() {
        api = new TmdbApi("cc5fb6d50718c39062251de8b4d2995c");
        singleMovieSearch = api.getMovies();
        generalSearchService = api.getSearch();
    }

    public static MovieDb searchFilmById(int filmId) {
        return singleMovieSearch.getMovie(filmId, "it", TmdbMovies.MovieMethod.credits);
    }

    public List<MovieDb> searchFilmByName(String filmName, int searchYear, boolean adultEnable) {
        if (filmName.length() > 0) {
            MovieResultsPage newmovies = generalSearchService.searchMovie(filmName, searchYear, "it", adultEnable, 0);
            if (newmovies.getTotalResults() > 0) {
                List<MovieDb> films = new ArrayList<>();
                for (int i = 0; i < newmovies.getResults().size(); i++) {
                    films.add(searchFilmById(newmovies.getResults().get(i).getId()));
                }
                return films;
            } else
                return new ArrayList<>();
        } else
            return new ArrayList<>();
    }

    public List<MovieDb> searchFilmByName(String filmName) {
        return searchFilmByName(filmName, 0, false);
    }

    public List<MovieDb> searchActorFilmography(String actorName, boolean adultEnable) {
        if (actorName.length() > 0) {
            TmdbPeople.PersonResultsPage resultActors = generalSearchService.searchPerson(actorName, adultEnable, 0);
            if (resultActors.getResults().size() > 0) {
                Person actor = resultActors.getResults().get(0);
                PersonCredits credits = api.getPeople().getCombinedPersonCredits(actor.getId());
                List<PersonCredit> actorFilmography = credits.getCast();
                List<Integer> toDelete = new ArrayList<>();
                for (int i = 0; i < actorFilmography.size(); i++) {
                    if (actorFilmography.get(i).getMovieTitle() == null || (!(actorFilmography.get(i).getMediaType().equals("movie"))))
                        toDelete.add(i);
                }
                toDelete.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return Integer.compare(o2, o1);
                    }
                });
                for (Integer deleting : toDelete) {
                    actorFilmography.remove(deleting.intValue());
                }
                List<MovieDb> films = new ArrayList<>();
                for (PersonCredit credit : actorFilmography)
                    films.add(singleMovieSearch.getMovie(credit.getId(), "it", TmdbMovies.MovieMethod.credits));
                return films;
            } else
                return new ArrayList<>();
        } else
            return new ArrayList<>();
    }

    public List<MovieDb> searchActorFilmography(String actorName) {
        return searchActorFilmography(actorName, false);
    }

}

