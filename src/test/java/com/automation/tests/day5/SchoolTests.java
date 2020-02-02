package com.automation.tests.day5;
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

public class SchoolTests {

    @BeforeAll
    public static void setup(){
            baseURI = ConfigurationReader.getProperty("school.uri");
    }
    @Test
    @DisplayName("Delete Student")
    public void test1(){
        Response response=given().
                                accept(ContentType.JSON).
                            when().
                                delete("/student/delete/{id}",56).prettyPeek();

}
}
