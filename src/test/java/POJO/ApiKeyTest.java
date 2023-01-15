package POJO;
import Campus.Model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.math3.random.RandomGenerator;
import org.testng.annotations.*;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class ApiKeyTest {

    @Test
    void apiKeyTest()

    {
        given()

                .header("x-api-key","GwMco9Tpstd5vbzBzlzW9I7hr6E1D7w2zEIrhOra")


                .when()

                .get("https://l9njuzrhf3.execute-api.eu-west-1.amazonaws.com/prod/user")


                .then()

                .log().body()

                .statusCode(200)



                ;
    }

}
