package com.automation.tests.day2;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class MetaWeatherTests {

    /**
     * /api/location/search/?query=san
     * /api/location/search/?query=london
     * /api/location/search/?lattlong=36.96,-122.02
     * /api/location/search/?lattlong=50.068,-5.316
     * /api/location/44418/ }/
     * "title": "San Francisco",
     * "location_type": "City",
     * "woeid": 2487956,
     * "latt_long": "37.777119, -122.41964"
     * },
     */

    private String baseURI = "https://www.metaweather.com/api/";

    @Test
    public void test1() {
        Response response = given()
                .baseUri(baseURI + "location/search/")//        buraya yazdigimiz keyworde gore sehri bulur
                .queryParam("query", "New")
                .get();
        assertEquals(200, response.getStatusCode());
        System.out.println(response.prettyPrint());

    }

    // /users/100/ - 100 it's a path parameter
    // /users/255/ - 155 it's a path parameter
    // /users/255?name=James | name - query parameter key=value , key it's a query parameter
    //        "woeid": 2514815,
    @Test
    public void test2() {
        Response response = given()
                .pathParam("woeid", "2458833")
                .get(baseURI + "location/{woeid}");
        System.out.println(response.prettyPrint());

    }
}
