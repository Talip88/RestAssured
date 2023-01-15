package Mersys;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*;

public class GradeLevelTest {

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


    String gradeLevelId;
    String gradeLevelFirstName;
    String gradeLevelLastName;


    @Test
    public void addGradeLevel() {

        gradeLevelFirstName= getRandomName();
        gradeLevelLastName =getRandomName();

      GradeLevel gradeLevel=new GradeLevel();
      gradeLevel.setFirstName(gradeLevelFirstName);
      gradeLevel.setLastName(gradeLevelLastName);

      gradeLevelId=

              given()
                      .cookies(cookies)
                      .contentType(ContentType.JSON)
                      .body(gradeLevel)
                      .when()
                      .post("school-service/api/grade-levels")
                      .then()
                      .log().body()
                      .statusCode(201)
                      .extract().jsonPath().getString("id")
              ;


    }

    @Test (dependsOnMethods = "addGradeLevel")

    public void updateGradeLevel() {

        gradeLevelFirstName= getRandomName();
        gradeLevelLastName =getRandomName();


        GradeLevel gradeLevel=new GradeLevel();

        gradeLevel.setFirstName(gradeLevelFirstName);
        gradeLevel.setLastName(gradeLevelLastName);
        gradeLevel.setId(gradeLevelId);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(gradeLevel)

                .when()
                .put("school-service/api/grade-levels")
                .then()
                .log().body()
                .statusCode(200)

                .body("name",equalTo(gradeLevelFirstName));

    }

    @Test (dependsOnMethods = "updateGradeLevel")
    public void deleteGradeLevel()
    {
        given()
                .cookies(cookies)
                .pathParam("gradeLevelId", gradeLevelId)
                .log().uri()

                .when()
                .delete("school-service/api/grade-levels/{gradeLevelId}")

                .then()
                .log().body()
                .statusCode(200)
                ;

    }



    }



