package com.automation.tests.day3;


import com.automation.utilities.ConfigurationReader;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
public class ORDSTestsDay3 {
    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("ords.uri");
    }
    //accept("application/json") shortcut for header("Accept", "application/json")
    //we are asking for json as a response
    @Test
    public void test1() {
        given().
                accept("application/json").
                get("/employees").
                then().
                assertThat().statusCode(200).
                and().assertThat().contentType("application/json").
                log().all(true);
    }
    @Test
    public void test2(){
        given().accept("application/json"). // bu satir bize gelecek kodun formatini belirliyor, json seklinde aldik
                pathParam("id",100). // pathparam - to point on speficifc resource
        when().get("/employees/{id}").// burada id 100 ile degistirilecek
                 then().assertThat().statusCode(200).
                 and().assertThat().body("employee_id", is(100)).
               and().assertThat().body("department_id",is(90)).
        log().all(true);
        // query parameter= to filter results, or describe new resource :
        // Post / username=James, age=50, title=SDET
        //or to filter GET / employeename= Jack get all employees woth name Jamal

        /**
         * given path parameter is "/regions/{id}"
         * when user makes get request
         * and region id is equals to 1
         * then assert that status code is 200
         * and assert that region name is Europe
         */
    }
    @Test
    public void test3(){

        given().accept("application/json").
                pathParam("id", "1").

         when().get("/regions/{id}").

         then().assertThat().statusCode(200).

         and().assertThat().body("region_name", is("Europe"))
                .time(lessThan(2L), TimeUnit.SECONDS).
                extract().response().prettyPrint() ;// kac saniyede olacagini soyluyoruz
    }                                                       //log.body(true)
    @Test
    public void test4() {
        JsonPath json = given().
                accept("application/json").
                when().
                get("/employees").
                thenReturn().jsonPath();

        //items[employee1, employee2, employee3] | items[0] = employee1.first_name = Steven

        String nameOfFirstEmployee = json.getString("items[0].first_name");
        String nameOfLastEmployee = json.getString("items[-1].first_name"); //-1 - last index

        System.out.println("First employee name: " + nameOfFirstEmployee);
        System.out.println("Last employee name: " + nameOfLastEmployee);
        //in JSON, employee looks like object that consists of params and their values
        //we can parse that json object and store in the map.
        Map<String, ?> firstEmployee = json.get("items[0]"); // we put ? because it can be also not String
        System.out.println(firstEmployee);

        //since firstEmployee it's a map (key-value pair, we can iterate through it by using Entry. entry represent one key=value pair)
        for (Map.Entry<String, ?> entry : firstEmployee.entrySet()) {
            System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
        }
//       get and print all last names
        List<String> lastNames = json.get("items.last_name");
        for (String str : lastNames) {
            System.out.println("last name: " + str);
        }

    }
    @Test
    public void test5(){
        JsonPath json = given().
                accept("application/json").
                when().                 //pretty printle ayni, bu response verir, prettyprint string verir
                get("/countries").prettyPeek().jsonPath();

        List<HashMap<String,?>>allCountries=json.get("items");
        System.out.println(allCountries);

        for (HashMap<String,?>map:allCountries){
            System.out.println(map);
        }

    }
    @Test
    public void test6(){
        List<Integer> salaries=given().
                                        accept("application/json").
                                when().
                                        get("/employees").
                                thenReturn().jsonPath().get("items.salary");

        Collections.sort(salaries);
        Collections.reverse(salaries);
        System.out.println(salaries);
    }
    @Test
    public  void test7(){
        List<String> phoneNumbers=given().
                                            accept("application/json").
                                 when().
                                          get("/employees").
                                    thenReturn().jsonPath().get("items.phone_number");
        phoneNumbers.forEach(x-> x.replace(".", "-"));

        System.out.println(phoneNumbers);
    }
    @Test
    public void test8(){
                         given().
                                    accept("application/json").
                                    pathParam("id", 1700).
                         when().get("/locations/{id}").
                          then().assertThat().statusCode(200).
                                     assertThat().body("location_id", is(1700),
                                     "postal_code",is("98199"),
                                     "city",is("Seattle"),
                                     "state_province",is("Washington")).
                log().body(true);

    }
}