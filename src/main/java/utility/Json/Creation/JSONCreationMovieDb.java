package utility.Json.Creation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import core.Classes.MovieDbExtended;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;

import java.util.ArrayList;
import java.util.List;

class JSONCreationMovieDb {

    private static final String firstPartOfPath = "https://image.tmdb.org/t/p/original";

    public static com.fasterxml.jackson.databind.node.ObjectNode getJsonActorInfo(PersonCast instance, ObjectMapper mapper) {
        com.fasterxml.jackson.databind.node.ObjectNode actorNode = mapper.createObjectNode();
        actorNode.put("name", instance.getName());
        actorNode.put("character",instance.getCharacter());
        actorNode.put("link",firstPartOfPath+instance.getProfilePath());
        return actorNode;
    }

    public static com.fasterxml.jackson.databind.node.ArrayNode getJsonActorList(List<PersonCast> instance, ObjectMapper mapper) {
        com.fasterxml.jackson.databind.node.ArrayNode actorList = mapper.createArrayNode();
        for (int i = 0; i < instance.size() && i<5; i++) {
            actorList.add(getJsonActorInfo(instance.get(i), mapper));
        }
        return actorList;
    }

    public static com.fasterxml.jackson.databind.node.ArrayNode getJsonGenreList(List<Genre> instance, ObjectMapper mapper) {
        com.fasterxml.jackson.databind.node.ArrayNode genreList = mapper.createArrayNode();
        for (Genre genre : instance) {
            genreList.add(genre.getName());
        }
        return genreList;
    }

    public static com.fasterxml.jackson.databind.node.ObjectNode getJsonFilmInfo(MovieDb instance, ObjectMapper mapper) {
        com.fasterxml.jackson.databind.node.ObjectNode film = mapper.createObjectNode();
        film.put("id_Film", instance.getId());
        film.put("film_Title", instance.getTitle());
        film.put("plot", instance.getOverview());
        if (instance.getPosterPath() != null) {
            film.put("posterPath", firstPartOfPath + instance.getPosterPath());
        }
        film.put("release_Date", instance.getReleaseDate());
        film.put("runtime", instance.getRuntime());
        if ((instance.getCast() != null) && (instance.getGenres().size() > 0)) {
            film.put("genres", getJsonGenreList(instance.getGenres(), mapper));
        }
        if (instance.getCast() != null) {
            film.put("cast", getJsonActorList(instance.getCast(), mapper));
        }
        if (instance instanceof MovieDbExtended){
            film.put("counter", ((MovieDbExtended) instance).getCounter());
        }
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(film);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return film;
    }

    public static String getJSONFilmList(ArrayList<?> instance, ObjectMapper mapper) {
        try {
            ArrayNode filmList = mapper.createArrayNode();
            if (instance.get(0).getClass().getSimpleName().equals("MovieDb")) {
                for (Object movieDb : instance) filmList.add(getJsonFilmInfo((MovieDb) movieDb, mapper));
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filmList);
            } else {
                for (Object movie : instance) filmList.add(getJsonFilmInfo((MovieDbExtended) movie, mapper));
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filmList);
            }
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }


}
