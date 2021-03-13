package core.Classes;

import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;

import java.util.List;

public class MovieDbExtended extends MovieDb {

    private int id;
    private String title;
    private String posterPath;
    private String releaseDate;
    private String overview;
    private List<Genre> genres;
    private List<PersonCast> cast;
    private int runtime;
    private int counter;

    public MovieDbExtended(MovieDb movie, int extCounter) {
        id = movie.getId();
        title = movie.getTitle();
        overview = movie.getOverview();
        genres = movie.getGenres();
        releaseDate = movie.getReleaseDate();
        runtime = movie.getRuntime();
        cast = movie.getCast();
        counter = extCounter;
        posterPath = movie.getPosterPath();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getPosterPath() {
        return posterPath;
    }

    @Override
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String getOverview() {
        return overview;
    }

    @Override
    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public List<Genre> getGenres() {
        return genres;
    }

    @Override
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public List<PersonCast> getCast() {
        return cast;
    }

    public void setCast(List<PersonCast> cast) {
        this.cast = cast;
    }

    @Override
    public int getRuntime() {
        return runtime;
    }

    @Override
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
