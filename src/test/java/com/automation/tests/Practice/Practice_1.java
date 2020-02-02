package com.automation.tests.Practice;
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


public class Practice_1 {

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("google.maps");


    }
    /*
    https://maps.googleapis.com/maps/api/place/nearbysearch/json?
    location=-33.8670522,151.1957362&key=AIzaSyBxUEaP5EKsG5LqghGV9Lf0DDXQvVG02Is&radius=500
     */
    @Test
    public void test(){    given().
            param("location","-33.8670522,151.1957362").
            param("radius","500").
            param("key","AIzaSyBxUEaP5EKsG5LqghGV9Lf0DDXQvVG02Is").
            when().
            get("maps/api/place/nearbysearch/json").
            then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
            body("results[0].name",equalTo("Sydney")).
            and().body("result[0].place_id",equalTo("asdasdas")).
            and().header("Server","pablo");

    }

}
