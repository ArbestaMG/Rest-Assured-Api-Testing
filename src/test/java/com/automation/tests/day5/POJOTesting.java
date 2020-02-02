package com.automation.tests.day5;

import com.automation.pojos.Job;
import com.automation.pojos.Location;
import com.automation.utilities.ConfigurationReader;
import com.google.gson.Gson;
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

import static io.restassured.RestAssured.baseURI;

public class POJOTesting {
    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("ords.uri");
    }

    @Test
    @DisplayName("Get job info from JSON and convert it into POJO")
    public void  test1(){
        Response response= given().
                                    accept(ContentType.JSON).
                            when().
                                    get("/jobs");

        JsonPath jsonPath=response.jsonPath();

        Job job=jsonPath.getObject("items[1]",Job.class);
        System.out.println(job);

        System.out.println("Job_id: "+job.getJobId());

    }
    @Test
    @DisplayName("Convert from POJO the Json")
     public  void test2(){
        Job sdet=new Job("Sdet","Software Engineering in Test",5000,200000);

        Gson gson=new Gson();
        gson.toJson(sdet);
        String json=gson.toJson(sdet);// convert POJO to JSON : serilization

        System.out.println(json);//JSON file
        System.out.println(sdet);// From the String
        System.out.println(gson.toJson(sdet));

    }
    @Test
    @DisplayName("Convert JSON into collection of POJO")
    public  void test3(){
        Response response= given().
                accept(ContentType.JSON).
                when().
                get("/jobs");
        JsonPath jsonPath=response.jsonPath();

        List<Job>jobs=jsonPath.getList("items",Job.class);
        for(Job job: jobs ){
            System.out.println(job.getJob_title());
        }
    }
    /*
    #Task
Create POJO for Location:
public class Location{
}
Write 2 tests:
	#1 get single POJO of Location class
	#2 get collection of POJO’s.
	Same thing like we did with Job class
     */

    @Test
    @DisplayName("Convert from JSON to Location POJO")
    public void test4() {
        Response response = given().
                accept(ContentType.JSON).
                when().
                get("/locations/{location_id}", 2500);

        Location location = response.jsonPath().getObject("",Location.class);
        System.out.println(location);

        Response response2 = given().
                accept(ContentType.JSON).
                when().
                get("/locations");
        List<Location> locations = response2.jsonPath().getList("items", Location.class);
        for(Location l: locations){
            System.out.println(l);
        }
    }
}