package utility.Json.Decode;

import MovieDB.CineMatesTheMovieDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import core.Classes.User;
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
                if (c == '\\')
                    c++;
                array[i]=c;
        }
        generatedString = new String(array);
    }

    @Test
    public void checkHttpRequestForUserAndDefaultList() throws JsonProcessingException {
        Assert.assertEquals(new HTTPRequest("idUser="+generatedString+"&searchDefaultList=true"),JSONDecoder.getDecodedJson("Type=PostRequest&idUser="+generatedString+"&searchDefaultList=true", HTTPRequest.class));
    }

    @Test
    public void checkNullHttpRequest() throws JsonProcessingException {
        Assert.assertNull(JSONDecoder.getDecodedJson(generatedString, HTTPRequest.class));
    }

    @Test
    public void checkNotValidClass() throws JsonProcessingException {
        Assert.assertNull(JSONDecoder.getDecodedJson(generatedString, CineMatesTheMovieDB.class));
    }

    @Test
    public void checkForAValidUser() throws JsonProcessingException{
        Assert.assertEquals(new User(generatedString,generatedString,generatedString,false),JSONDecoder.getDecodedJson("{\"email\":\""+generatedString+"\",\"nickname\":\""+generatedString+"\",\"propic\":\""+generatedString+"\"}", User.class));
    }

    @Test
    public void checkForANonValidUser() throws JsonProcessingException{
        Assert.assertNotEquals(new User(generatedString+"a",generatedString+"b",generatedString+"c",false),JSONDecoder.getDecodedJson("{\"email\":\""+generatedString+"\",\"nickname\":\""+generatedString+"\",\"propic\":\""+generatedString+"\"}", User.class));
    }

}