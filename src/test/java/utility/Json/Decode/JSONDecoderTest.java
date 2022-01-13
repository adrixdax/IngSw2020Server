package utility.Json.Decode;

import MovieDB.CineMatesTheMovieDB;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import core.Classes.User;
import core.Classes.filminlist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utility.Json.Requests.HTTPRequest;

public class JSONDecoderTest {

    String generatedString;

    @Before
    public void initString() {
        char[] array = new char[12];
        for (int i = 0; i<array.length; i++){
                char c = (char) ((Math.random() * (122 - 48)) + 48);
                if (c == '\\' || c=='\"')
                    c++;
                array[i]=c;
        }
        generatedString = new String(array);
    }

    @Test
    public void checkHttpRequestForUserAndDefaultList() throws JsonProcessingException {
        Assert.assertEquals(new HTTPRequest("idUser="+generatedString+"&searchDefaultList=true"),JSONDecoder.getDecodedJson("Type=PostRequest&idUser="+generatedString+"&searchDefaultList=true", HTTPRequest.class));
    }

    @Test (expected = JsonParseException.class)
    public void checkHttpRequestButUserClass() throws JsonProcessingException {
        Assert.assertNull(JSONDecoder.getDecodedJson("Type=PostRequest&idUser="+generatedString+"&searchDefaultList=true", User.class));
    }

    @Test
    public void checkHttpRequestButOtherNotSupportedClass() throws JsonProcessingException {
        Assert.assertNull(JSONDecoder.getDecodedJson("Type=PostRequest&idUser="+generatedString+"&searchDefaultList=true", CineMatesTheMovieDB.class));
    }


    @Test
    public void checkNullHttpRequest() throws JsonProcessingException {
        Assert.assertNull(JSONDecoder.getDecodedJson(generatedString, HTTPRequest.class));
    }

    @Test (expected = JsonParseException.class)
    public void checkNotValidHttpRequestButUserClass() throws JsonProcessingException {
        Assert.assertNull(JSONDecoder.getDecodedJson("Type=PostRequest&value="+generatedString, User.class));
    }

    @Test
    public void checkInvalidHttpRequestButOtherNotSupportedClass() throws JsonProcessingException {
        Assert.assertNull(JSONDecoder.getDecodedJson(generatedString, CineMatesTheMovieDB.class));
    }

    @Test
    public void checkForAValidUser() throws JsonProcessingException{
        Assert.assertEquals(new User(generatedString,generatedString,generatedString,false),JSONDecoder.getDecodedJson("{\"email\":\""+generatedString+"\",\"nickname\":\""+generatedString+"\",\"propic\":\""+generatedString+"\"}", User.class));
    }

    @Test
    public void checkForANonValidUser() throws JsonProcessingException{
        Assert.assertNotEquals(new User(generatedString,generatedString,generatedString,false),JSONDecoder.getDecodedJson("{\"email\":\""+generatedString+"a"+"\",\"nickname\":\""+generatedString+"b"+"\",\"propic\":\""+generatedString+"c"+"\"}", User.class));
}

    @Test
    public void checkForAValidUserJsonAndHTTPRequestClass() throws JsonProcessingException{
        Assert.assertNotEquals(new HTTPRequest("Type=PostRequest&value="+generatedString),JSONDecoder.getDecodedJson("{\"email\":\""+generatedString+"\",\"nickname\":\""+generatedString+"\",\"propic\":\""+generatedString+"\"}", User.class));
    }

    @Test
    public void checkForANonValidUserJsonAndHTTPRequestClass() throws JsonProcessingException{
        Assert.assertNotEquals(new HTTPRequest("Type=PostRequest&value="+generatedString),JSONDecoder.getDecodedJson("{}{\"email\":\""+generatedString+"\",\"nickname\":\""+generatedString+"\",\"propic\":\""+generatedString+"\"}", User.class));
    }

    @Test
    public void checkForAValidUserJsonAndUnsoppertedClass() throws JsonProcessingException{
        Assert.assertNotEquals(new filminlist(),JSONDecoder.getDecodedJson("{\"email\":\""+generatedString+"\",\"nickname\":\""+generatedString+"\",\"propic\":\""+generatedString+"\"}", User.class));
    }

    @Test
    public void checkNotValidJsonButOtherNotSupportedClass() throws JsonProcessingException {
        Assert.assertNull(JSONDecoder.getDecodedJson(generatedString, CineMatesTheMovieDB.class));
    }
}