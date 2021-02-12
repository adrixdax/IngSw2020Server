package utility.Json.Creation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;

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

    public static com.fasterxml.jackson.databind.node.ObjectNode getJsonGenreInfo(Genre instance, ObjectMapper mapper) {
        com.fasterxml.jackson.databind.node.ObjectNode genreNode = mapper.createObjectNode();
        genreNode.put("name", instance.getName());
        return genreNode;
    }

    public static com.fasterxml.jackson.databind.node.ArrayNode getJsonGenreList(List<Genre> instance, ObjectMapper mapper) {
        com.fasterxml.jackson.databind.node.ArrayNode genreList = mapper.createArrayNode();
        for (Genre genre : instance) {
            genreList.add(getJsonGenreInfo(genre, mapper));
        }
        return genreList;
    }

    public static com.fasterxml.jackson.databind.node.ObjectNode getJsonFilmInfo(Object instance, ObjectMapper mapper) {
        MovieDb toConvert = (MovieDb) instance;
        com.fasterxml.jackson.databind.node.ObjectNode film = mapper.createObjectNode();
        film.put("id_Film", toConvert.getId());
        film.put("film_Title", toConvert.getTitle());
        film.put("plot",toConvert.getOverview());
        if (toConvert.getPosterPath() != null) {
            film.put("posterPath", firstPartOfPath + toConvert.getPosterPath());
        } else {
            film.put("posterPath", "");
        }
        film.put("release_Date", toConvert.getReleaseDate());
        film.put("runtime", toConvert.getRuntime());
        if ((toConvert.getCast() != null) && (toConvert.getGenres().size()>0)) {
            film.put("genres", getJsonGenreList(toConvert.getGenres(), mapper));
        }
        else{
            film.put("genres","");
        }
        if (toConvert.getCast() != null) {
            film.put("cast", getJsonActorList(toConvert.getCast(), mapper));
        } else {
            film.put("cast", "");
        }
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(film);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return film;
    }

    public static String getJSONFilmList(Object instance, ObjectMapper mapper) {
        try {
            List<MovieDb> list = (List<MovieDb>) instance;
            ArrayNode filmList = mapper.createArrayNode();
            for (MovieDb movieDb : list) {
                filmList.add(getJsonFilmInfo(movieDb, mapper));
            }
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filmList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "{}";
    }


}
