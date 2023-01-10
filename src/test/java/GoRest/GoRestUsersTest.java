package GoRest;

import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class GoRestUsersTest {

    @BeforeClass
    void Setup()
    {
        baseURI="https://gorest.co.in/public/v2/users";

    }





    @Test
    public void createUserObject()

    {
        //başlangıç işlemleri
        //token aldım
        //users JSON ı hazırladım
    int userID=
        given()

        .header("Authorization","Bearer 46f9460f6cd9e573021594db5fd1713381fefed250aaab14594c9d5892963cfc")
        .contentType(ContentType.JSON)
        .body("{\"name\":\"Talpippppp Çolapk\",\"gender\":\"male\", \"email\":\"talipppp88@gmail.com\", \"status\":\"active\"}")

                //üst taraf request özellikleridir. Hazırlık işlemleri ---POSTMAN'daki Authorization ve Request Body kısmı

                .log().uri()
                .when()  // Request in olduğu nokta-- POSTMAN deki Send Butonu

                .post(baseURI)// Response'un oluştuğu nokta... baseURI yazsan da olur "" koysan da


                //Alt taraf response sonrası  POSTMAN'daki test penceresi

                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().path("id")


                ;


    }





}
