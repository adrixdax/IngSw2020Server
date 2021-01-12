package MovieDB;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbPeople;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonCredits;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CineMatesTheMovieDB {

    TmdbApi api= new TmdbApi("cc5fb6d50718c39062251de8b4d2995c");
    TmdbMovies movies = api.getMovies();
    TmdbSearch searchService = api.getSearch();
    MovieDb movie = movies.getMovie(5353, "en");

    public List<MovieDb> searchActorFilmographyIncludingAdult(String actorName){
        TmdbPeople.PersonResultsPage resultActors = searchService.searchPerson(actorName,true,0);
        Person actor = resultActors.getResults().get(0);
        System.out.println(actor.getName());
        PersonCredits credits = api.getPeople().getCombinedPersonCredits(actor.getId());
        List<PersonCredit> actorFilmography = credits.getCast();
        List<Integer> toDelete = new ArrayList<>();
        for (int i = 0; i<actorFilmography.size();i++){
            if (actorFilmography.get(i).getMovieTitle() == null || (!(actorFilmography.get(i).getMediaType().equals("movie"))))
                toDelete.add(i);
        }
        toDelete.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o2,o1);
            }
        });
        for (Integer deleting : toDelete){
            actorFilmography.remove(deleting.intValue());
        }
        List<MovieDb> films = new ArrayList<>();
        for (PersonCredit credit : actorFilmography)
            films.add(movies.getMovie(credit.getId(),"it"));
        return films;
    }

    public List<MovieDb> searchActorFilmographyExcludingAdult(String actorName){
        TmdbPeople.PersonResultsPage resultActors = searchService.searchPerson(actorName,false,0);
        if (resultActors.getResults().size()>0)
        {
            Person actor = resultActors.getResults().get(0);
            System.out.println(actor.getName());
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
                films.add(movies.getMovie(credit.getId(), "it"));
            return films;
        }
        else
            return new ArrayList<>();
    }

    /*
    System.out.println(movie.getTitle() + " " + movie.getImdbID() + " " + movie.getId());
    movie = movies.getMovie(550, "en");
        System.out.println(movie.getTitle() + " " + movie.getImdbID() + " " + movie.getId() + " " + movie.getOverview() + " "+movie.getRuntime());
        System.out.println(movie.getGenres().size());
        for (int i = 0; i<movie.getGenres().size(); i++){
        System.out.println(movie.getGenres().get(i)+" "+movie.getTitle());
    }
    //        MovieResultsPage newmovies = searchService.searchMovie("Fight Club",0,"it",true,0);
    TmdbPeople.PersonResultsPage newmovies =searchService.searchPerson("Bonnie Wright",true, 0);
        /*
        System.out.println(newmovies.getTotalResults());
        for (int i = 0; i<newmovies.getResults().size(); i++){
            films.add(movies.getMovie(newmovies.getResults().get(i).getId(),"it"));
        }
        for (MovieDb film : films) {
            System.out.print(film.getTitle() + " ");
            if (film.getGenres().size() != 0) {
                for (int j = 0; j < film.getGenres().size(); j++) {
                    System.out.print(film.getGenres().get(j) + " ");
                }
            }

            System.out.println();
        }*/
    // TmdbPeople person = api.getPeople();
    // PersonCredits credits = person.getCombinedPersonCredits(newmovies.getResults().get(0).getId());
    // List<PersonCredit> listofpersons = credits.getCast();
    /* listofpersons.sort(new Comparator<PersonCredit>() {
         @Override
         public int compare(PersonCredit o1, PersonCredit o2) {
             return Integer.compare(o1.getId(),o2.getId());
         }
     });

    }
    });
        for (PersonCredit persone : listofpersons){
        System.out.println(persone.getId()+" "+persone.getMovieTitle()+" "+persone.getMediaType());
    }
        for (Integer integers: todelete)
    {
        listofpersons.remove(integers.intValue());
    }
        for (Integer integers : todelete){
        System.out.println(integers);
    }
        for (PersonCredit persone : listofpersons){
        System.out.println(persone.getId()+" "+persone.getMovieTitle()+" "+persone.getMediaType());
    }
*/
}

