package spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StartClass {

    public static void main(String[] args) {
        SpringApplication.run(StartClass.class, args);
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