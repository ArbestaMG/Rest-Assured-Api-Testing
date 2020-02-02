import com.automation.utilities.ConfigurationReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;


public class Practice2 {
    String baser="{"+
            "\"location\": {"+
            "\"lat\": -33.8669710,"+
            "\"lng\": 151.1958750"+
            "},"+
            "\"accuracy\": 50,"+
            "\"name\": \"Google Shoes!\","+
            "\"phone_number\": \"(02) 9374 4000\","+
            "\"address\": \"48 Pirrama Road, Pyrmont, NSW 2009, Australia\","+
            "\"types\": [\"shoe_store\"],"+
            "\"website\": \"http://www.google.com.au/\","+
            "\"language\": \"en-AU\""+
            "}";
    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("udemy.uri");

    }
    @Test
    public void test1(){
        given().

                queryParam("key","qaclick123").

                body("{"+

                        "\"location\": {"+

                        "\"lat\": -33.8669710,"+

                        "\"lng\": 151.1958750"+

                        "},"+

                        "\"accuracy\": 50,"+

                        "\"name\": \"Google Shoes!\","+

                        "\"phone_number\": \"(02) 9374 4000\","+

                        "\"address\": \"48 Pirrama Road, Pyrmont, NSW 2009, Australia\","+

                        "\"types\": [\"shoe_store\"],"+

                        "\"website\": \"http://www.google.com.au/\","+

                        "\"language\": \"en-AU\""+

                        "}").

                when().

                post("/maps/api/place/add/json").

                then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().

                body("status",equalTo("OK"));
    }
    @Test
    @DisplayName("Create a place and delete it")
    public void test2(){
        //Task 1 ==> Grab the response
       Response response= given().

                queryParam("key","qaclick123").

                body(baser).
                when().

                post("/maps/api/place/add/json").

                then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().

                body("status",equalTo("OK")).
                extract().response();


        // Task 2 ==> Grab the place id from response


        String responseString=response.asString();
        System.out.println(responseString);
        JsonPath js=new JsonPath(response.asString());// bunun icine koyan stringi json yapar
        String placeid=js.get("place_id");
        System.out.println(placeid);

        // Task 3 ==> place this id in the delete reques
        given().
                accept(ContentType.JSON).
                queryParam("key","qaclick123").
                body("{"+
                        "\"place_id\": \""+placeid+"\""+
                        "}").
        when().
                post("/maps/api/place/add/json").
        then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().

                body("status",equalTo("OK"));

    }
}
