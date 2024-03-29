package com.automation.tests.day7;
import com.automation.pojos.Spartan;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import com.automation.pojos.Job;
import com.automation.pojos.Location;
import com.automation.utilities.ConfigurationReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
public class SpartansTestsDay7 {
    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("spartan.uri");
    }
    //add new Spartan from the external JSON file
    @Test
    @DisplayName("Add new user by using external JSON file")
    public void test1(){
        File file = new File(System.getProperty("user.dir")+"/spartan.json");
        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(file).
                when().
                post("/spartans").prettyPeek().
                then().assertThat().
                statusCode(201).
                body("success", is("A Spartan is Born!"));
    }
    @Test
    @DisplayName("Add new user by using map")
    public void test2(){
        Map<String, Object> spartan = new HashMap<>();
        spartan.put("phone", 12999999117L);
        spartan.put("gender", "Male");
        spartan.put("name", "John Deer");
        // you must specify content type, whenever you POST
        //contentType(ContentType.JSON)
        assertEquals("Male", spartan.get("gender"));
        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(spartan).
                when().
                post("/spartans").prettyPeek().
                then().assertThat().
                statusCode(201).
                body("success", is("A Spartan is Born!")).
                body("data.name", is("John Deer")).
                body("data.gender", is("Male"));
        //in the response, we have spartan object inside data variable
        //to get properties we need to specify name of that object data
        //put . and parameter that we want to read
        // data.id , data.gender, data.name
        // success - property, string variable
        // data - object that represents spartan
    }
    @Test
    @DisplayName("update spartan, only name PATCH")
    public void test3(){
        Map<String, Object> update = new HashMap<>();
        update.put("name", "Lynda");
        update.put("gender", "Female");
        given().
                contentType(ContentType.JSON).
                body(update).
                pathParam("id", 904).
                when().
                patch("/spartans/{id}").prettyPeek().
                then().assertThat().
                statusCode(204);
        //since response doesn't contain body, after PATCH request,
        //we don't need  accept(ContentType.JSON).
        // PUT - all parameters
        // PATCH - 1+ parameters
    }
}
/*
CRUD
					In SQL
	C - create | INSERT
	R - read   | SELECT
	U - update | UPDATE
	D - delete | DELETE
	In API, there are HTTP verbs fro CRUD operations:
	POST (CREATE) - to create something, and then you should get 201 status code, if no - it's a bug, or you failed to create something because your request was not correct.
	POST: baseUrl/spartans
	we don't specify id, because it's auto generated value.
	{
	  "name": "Galatasaray",
	  "gender": "Male",
	  "phone": 12023615117
	}
	Whenever you are getting status code 4XX - that means problem on your side. Either URL is wrong, or you don't have a permission, or your request was formed in the wrong way.
	GET (READ) - to read the data from web service. You should get 200 status code (means OK) if your request went well.
	PUT (UPDATE) - to update existing record. You can get 200 (OK) or 204 (no content),  if your request went well. In case of PUT, you have completely replace existing record:
Let's say we have this user...
	{
	  "id": 150
	  "name": "Galatasaray",
	  "gender": "Male",
	  "phone": 12023615117
	}
To update him, we need to perform PUT request.
Body of your PUT request, should contain all attributes, except id:
	PUT: baseUrl/spartans/:id    | id - is a path parameter
	PUT: baseUrl/spartans/150
	Body:
	{
	  "name": "New Name",
	  "gender": "Male",
	  "phone": 12023615117
	}
We specify id as path parameter.
Because web service, must know which record to update.
If you gonna miss at least one parameter, It will not work.
	DELETE (DELETE) - to delete some record.
	DELETE: baseUrl/spartans/:id | id - is a path parameter
	DELETE baseUrl/spartans/150
	As a response, you should get either 200 (OK), or 204 (NO CONTENT). Otherwise, either your request is not correct (4XX status code) or something wrong with a system.
204 (NO CONTENT) - means no JSON/XML/etc. body in the response.
If you added something over API into web service --> data will be stored in the data base, UI.
If you deleted something via API from web service --> data will be gone from data base, UI.
PATCH (UPDATE) - partial update of existing record. In case of PUT, we have to completely replace existing record. But, in case of PATCH, you can update one or more properties of existing record. In case of success, you should get 200 (OK) or 204 (NO CONTENT). If you are getting 4XX status - problem in your request, 5XX - problem in the server, could be the bug.
Let's say we have this user...
	{
	  "id": 150
	  "name": "Galatasaray",
	  "gender": "Male",
	  "phone": 12023615117
	}
To update him, we need to perform PATCH request.
Body of your PATCH request, can contain 1 or mote attributes, except id:
	PATCH: baseUrl/spartans/:id    | id - is a path parameter
	PATCH: baseUrl/spartans/150
	Body:
	{
	  "name": "New Name",
	}
	OR
	{
	  "phone": 45464654654
	}
	OR like this....
	{
	  "name": "Jasmine",
	  "gender": "Female",
	}
As you can see, GET and DELETE requests performs without body, but POST, PUT, PATCH - with body.
Also, in case of DELETE, you might get 405 status code (NOT ALLOWED) that means you cannot delete something. Also, whenever you PUT, POST, PATCH, don't forget to specify content type. If you are sending JSON file, provide content type as JSON. Otherwise, system might confuse your JSON with plain text, because they look alike.
Payload = body.
Keep in mind that not every web service supports certain operations. For example: PATCH. So to understand what your web service can do, what it requires to perform request (end point, API key, authentication, cookies, proxy, etc) you should read API doc (like Swagger).
All web applications have API. Because, there is no way to deliver data from business logic component to UI. Also, Modern application are not monolith. Now days, applications are build of micro services. Micro service it's a small web service, that performs certain operations for web application, but not all. It would would be to difficult to update, manage, deploy web application that would be single unit. Instead, developers use micro services. An communication among micro services happens via API.
New ORDS url: http://ec2-3-81-4-150.compute-1.amazonaws.com:1000/ords/hr
Create ORDS_QA environment in Postman
variable name  |  value
baseUrl		   | http://ec2-3-81-4-150.compute-1.amazonaws.com:1000/ords/hr
You can use yours too...
just replace host name: ec2-3-81-4-150.compute-1.amazonaws.com
With RestAssured, how we can POST new spartan?
1. Create POJO and insert it into body of request:
		Spartan spartan = new Spartan();
        spartan.setGender("Male");//Male or Female
        spartan.setName("Mister Twister");
        spartan.setPhone(5712134235L); //at least 10 digits
Response response = given().
                         accept(ContentType.JSON).		 <--- what data type we want as payload
                         contentType(ContentType.JSON). <--- to specify what data we are sending
                         body(spartan).			        <- this is a body of request
                      post("/spartans").prettyPeek();     <--- POST request with path
RestAssured will use Gson library, to automatically serialize our POJO into JSON.
2. Read external JSON file and insert it into body of request:
File file = new File("src/resources/spartan.json");
Response response = given().
                         accept(ContentType.JSON).		 <--- what data type we want as payload
                         contentType(ContentType.JSON). <--- to specify what data we are sending
                         body(file). <- this is a body of request
                      post("/spartans").prettyPeek();     <--- POST request with path
3. Create map and insert it into body of request:
? - works for parsing response.
		Map<String, Object> spartan = new HashMap<>();
        spartan.put("gender", "Male");//Male or Female
        spartan.put("name", Mister Twister");
        spartan.put("phone", 5712134235L); //at least 10 digits
Object - super class for all classes in java. Long and String by default extend Object class.
Response response = given().
                         accept(ContentType.JSON).		 <--- what data type we want as payload
                         contentType(ContentType.JSON). <--- to specify what data we are sending
                         body(spartan).			        <- this is a body of request
                      post("/spartans").prettyPeek();     <--- POST request with path
To UPDATE existing one
Response response = given().
                          accept(ContentType.JSON).		 <--- what data type we want as payload
                          contentType(ContentType.JSON). <--- to specify what data we are sending
                          body(spartan).				 <--- this is a body of request
                          pathParam("id", 380).			 <--- which spartan to update
                      put("/spartans/{id}").prettyPeek();<--- PUT request with path
/**
 * Class {@code Object} is the root of the class hierarchy.
 * Every class has {@code Object} as a superclass. All objects,
 * including arrays, implement the methods of this class.
Preschool POSTMAN collection: https://www.getpostman.com/collections/86e64b62e4b310785969
Break till 3:15
Student{
	admissionNo='123456',
	batch=11,
	birthDate='01/06/1990',
	company=Company{
			address=Address{
						addressId=2633,
						city='McLean',
						state='Virginia',
						street='7925 Jones Branch Dr #3300',
						zipCode=7925
						},
					companyId=2633,
					companyName='02/02/2020',
					startDate='Cybertek',
					title='SDET'
					},
				contact=Contact{
						contactId=2633,
						emailAddress='sdet@email.com',
						phone='454-323-2341',
						permanentAddress='7925 Jones Branch Dr #3300'
					},
	firstName='Guys do delete me  ',
	gender='Male',
	joinDate='11/26/2017',
	lastName='Joker',
	major='MBA',
	password='AP-45',
	section='1111',
	studentId=2633,
	subject='Math'
}
API key required by web service, to know who is using it. Thus, strangers cannot use the web service. To get an API key, you have to register first and that you will receive a key.
Insert that key as a query parameter with every request:
https://www.potterapi.com/v1/characters?key=$2a$10$uz3xGNC5vvHzxUzLU5gkW.yngiL6mstfGlwJ6EbqfyDYFAfiJJkvu
 */