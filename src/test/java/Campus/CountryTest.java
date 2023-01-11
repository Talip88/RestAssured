package Campus;

import Campus.Model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountryTest {

    Cookies cookies; // Cookies global değişken oldu ve böylece tüm testlerde geçerli olacak

    String countryID;
    String countryName;
    String countryCode;



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
        .statusCode(200)

                .extract().response().getDetailedCookies();
;
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
    @Test
    public void createCountry()

    {

        Country country=new Country();
        country.setName(getRandomName());
        country.setCode(getRandomCode());


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .post("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)



                ;




    }


    @Test
    public void createCountryNegative()
    {

    }







}