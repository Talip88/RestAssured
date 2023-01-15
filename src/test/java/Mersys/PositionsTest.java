package Mersys;
import Mersys.Model.Positions;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PositionsTest {



    Cookies cookies;

    @BeforeClass
    public void loginCampus() {

        baseURI="https://test.mersys.io/";

        Map<String,String> credential=new HashMap<>();
        credential.put("username","turkeyts");
        credential.put("password","TechnoStudy123");
        credential.put("rememberMe","true");


        cookies=

                given()
                        .contentType(ContentType.JSON)
                        .body(credential)


                        .when()
                        .post("auth/login")


                        .then()
                        .log().body()
                        // .log().cookies()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();

        System.out.println("cookies = " + cookies);

    }

    public String getRandomName()
    {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String getRandomCode()
    {
        return RandomStringUtils.randomAlphabetic(3);
    }


    String positionsId;
    String positionsFirstName;
    String positionsLastName;


    @Test
    public void addPositions() {

        positionsFirstName= getRandomName();
        positionsLastName =getRandomName();

        Positions positions=new Positions();
        positions.setFirstName(positionsFirstName);
        positions.setLastName(positionsLastName);

        positionsId=

                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(positions)
                        .when()
                        .post("school-service/api/grade-levels")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id")
        ;


    }

    @Test (dependsOnMethods = "addGradeLevel")

    public void updatePositions() {

        positionsFirstName= getRandomName();
        positionsLastName =getRandomName();

        Positions positions=new Positions();
        positions.setFirstName(positionsFirstName);
        positions.setLastName(positionsLastName);
        positions.setId(positionsId);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(positions)

                .when()
                .put("school-service/api/grade-levels")
                .then()
                .log().body()
                .statusCode(200)

                .body("name",equalTo(positionsFirstName));

    }

    @Test (dependsOnMethods = "updateGradeLevel")
    public void deletePositions()
    {
        given()
                .cookies(cookies)
                .pathParam("gradeLevelId", positionsId)
                .log().uri()

                .when()
                .delete("school-service/api/grade-levels/{gradeLevelId}")

                .then()
                .log().body()
                .statusCode(200)
        ;

    }



}

