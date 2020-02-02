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

public class ORDSTestDay5 {
    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("ords.uri");
    }
    /*
     * WARMUP
     * given path parameter is "/employees"
     * when user makes get request
     * then user verifies that status code is 200
     * and user verifies that average salary is grater than $5000

     */
    @Test
    @DisplayName("")
    public void  test1(){

       Response response= given().
                                accept(ContentType.JSON).
                         when().get("/employees");

       JsonPath jsonPath=response.jsonPath();

       List<Integer> salaries=jsonPath.getList("items.salary");

       int sum=0;

       for(int salary:salaries){
           sum+=salary;
       }
       int avg=sum/salaries.size();

       assertTrue(avg>5000,"Error: actually average salary is lower than 5k");
    }
}

