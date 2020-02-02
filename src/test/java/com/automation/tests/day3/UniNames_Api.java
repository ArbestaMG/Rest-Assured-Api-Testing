package com.automation.tests.day3;


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

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
public class UniNames_Api {

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("ui.names");
    }

    @Test
    @DisplayName("No Parameter Test")
    public void test1() {
        given().
                accept("application/json").
                get(baseURI).
                then().
                assertThat().statusCode(200).
                and().assertThat().body("name", anything()).
                and().assertThat().body("surname", anything()).
                and().assertThat().body("gender", anything()).
                and().assertThat().body("region", anything()).
                log().all(true);
    }

    @Test
    @DisplayName("Gender Test")
    public void test2() {
        Response response = given()
                .queryParam("gender", "male")
                .get();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @DisplayName("2 Parameter Test")
    public void test3() {
        Response response = given()
                .queryParam("gender", "male")
                .queryParam("region", "germany").
                        get();
        assertEquals(200, response.getStatusCode());

    }

    @Test
    @DisplayName("Invalid Gender")
    public void test4() {

        Response response = given().accept(ContentType.JSON).
                queryParam("gender", "invalid").
                get();

        assertEquals(400, response.getStatusCode());
        assertEquals("HTTP/1.1 400 Bad Request", response.statusLine());

    }

    @Test
    @DisplayName("Invalid Region Test")
    public void test5() {
    Response response = given().accept(ContentType.JSON).
            queryParam("region", "invalid").
            get();

    assertEquals(400,response.getStatusCode());
    assertEquals("HTTP/1.1 400 Bad Request",response.statusLine());
}
    @Test
    @DisplayName("Amount and Regions Test")
    public void test6(){
        Response response = given().accept(ContentType.JSON).
                queryParam("region", "Mexico").
                queryParam("amount","2").
                get();
        response.then().
                assertThat().statusCode(200).
                contentType("application/json; charset=utf-8");
        JsonPath json = response.thenReturn().jsonPath();

        List<String > listOfName = response.jsonPath().getList("name");
        List<String > listOfSurname = response.jsonPath().getList("surname");
        List<String > list = new LinkedList<>();
        for (int i=0; i<listOfName.size(); i++){
            list.add(listOfName.get(i)+listOfSurname.get(i));
        }
        System.out.println(list.toString());
        int count =0;
        for (int i=0; i<listOfName.size(); i++) {
            if(list.contains(list.get(i))){
                count++;
            }
            if (count==1){
                assertTrue(true);
            }else{
                assertTrue(false);
            }
            count=0;
        }
    }
    @Test
    @DisplayName("3 Params Test")
    public void test7() {
        Response response = given().accept(ContentType.JSON).
                queryParam("region", "Turkey").
                queryParam("amount", "5").
                queryParam("gender", "male").
                get();
        assertEquals(200, response.getStatusCode());
        assertEquals("application/json; charset=utf-8", response.getContentType());

        response.prettyPrint();
        JsonPath json = response.thenReturn().jsonPath();

        List<Map<?,?>>list = json.get();
        Set<String> regions = new LinkedHashSet<>();
        Set<String> genders = new TreeSet<>();
        for(int i= 0; i < list.size();i++){
            regions.add(list.get(i).get("region").toString());
            genders.add(list.get(i).get("gender").toString());
        }
        assertTrue(regions.size() == 1 && genders.size() == 1);

    }
        @Test
        @DisplayName("Amount Count Test")
    public void test9(){
            Response response = given().accept(ContentType.JSON).
                    queryParam("amount", "5").
                    get();
            assertEquals(200, response.getStatusCode());
            assertEquals("application/json; charset=utf-8", response.getContentType());

            JsonPath jsonPath=response.thenReturn().jsonPath();
            jsonPath.prettyPrint();

            List<Map<?,?>>list = jsonPath.get();
            assertTrue(list.size() ==5 );
        }

}