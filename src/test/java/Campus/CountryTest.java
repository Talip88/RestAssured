package Campus;

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

public class CountryTest {

    Cookies cookies; // Cookies global değişken oldu ve böylece tüm testlerde geçerli olacak

    @BeforeClass
    public void loginCampus()
    {

        baseURI="https://test.mersys.io/";
        //diğer testler çalışmadan önce login olup cookies in alınması gerekiyor.
        //bu yüzden beforeClass anotation ı eklendi...

// COOKİES ALMA

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


//******************************RANDOM ISIM VE CODE URETMEK ICIN*******************************************************
public String getRandomName()
{
    return RandomStringUtils.randomAlphabetic(8);
}

public String getRandomCode()
{
    return RandomStringUtils.randomAlphabetic(3);
}

//**********************************************************************************************************************


    String countryID;
    String countryName;
    String countryCode;


    @Test
    public void createCountry()

    {
        countryName= getRandomName();
        countryCode=getRandomCode();

        Country country=new Country();
        country.setName(countryName);
        country.setCode(countryCode);

        countryID=
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .post("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id")


                ;




    }


    @Test (dependsOnMethods = "createCountry")
    public void createCountryNegative()
    {

        Country country=new Country();
        country.setName(countryName);
        country.setCode(countryCode);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)

                .when()
                .post("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("The Country with Name \""+countryName+"\" already exists."))
                .body("message", containsString("already exist"))
        ;
    }


    @Test(dependsOnMethods = "createCountry")
    public void updateCountry()
    {
        countryName = getRandomName();
        countryCode=getRandomCode();

        Country country=new Country();
        country.setName(countryName);
        country.setCode(countryCode);
        country.setId(countryID);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)

                .when()
                .put("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(countryName))
        ;
    }



    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountryById()
    {
        given()
                .cookies(cookies)
                .pathParam("countryID", countryID)
                .log().uri()

                .when()
                .delete("school-service/api/countries/{countryID}")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }



    @Test(dependsOnMethods = "deleteCountryById")
    public void deleteCountryByIdNegative()
    {
        given()
                .cookies(cookies)
                .pathParam("countryID", countryID)
                .log().uri()
                .when()
                .delete("school-service/api/countries/{countryID}")

                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Country not found"));

        ;
    }

    @Test(dependsOnMethods = "deleteCountryById")
    public void updateCountryNegative()
    {
        countryName = getRandomName();
        countryCode=getRandomCode();

        Country country=new Country();
        country.setId(countryID);
        country.setName(countryName);
        country.setCode(countryCode);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)

                .when()
                .put("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Country not found"))
        ;
    }






}




