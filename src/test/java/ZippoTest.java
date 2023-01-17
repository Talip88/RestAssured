import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

        @Test
        public void test(){

            given()
                    // hazırlık işlemlerini yapacağız

                    .when()

                    //linki ve metodu veriyoruz

                    .then();

                    //assertion ve verileri ele alma işlemleri





        }


    @Test
    public void statusCodeTest(){

        given()
                // hazırlık işlemlerini yapacağız

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .log().all()  // bütün responsu gösterir...
                .statusCode(200);

        //assertion ve verileri ele alma işlemleri





    }

    @Test
    public void contentTypeTest(){

        given()
                // hazırlık işlemlerini yapacağız

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON);

        //assertion ve verileri ele alma işlemleri





    }

    @Test
    public void checkCountryInResponseBody(){

        given()
                // hazırlık işlemlerini yapacağız

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
                .body("country",equalTo("United States"))


                ;

        //assertion ve verileri ele alma işlemleri





    }


    @Test
    public void checkStateInResponseBody() {

        given()
                // hazırlık işlemlerini yapacağız

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
                .body("places[0].state", equalTo("California"))


        ;
    }



    @Test
    public void bodyJsonPathTest3() {

        given()
                // hazırlık işlemlerini yapacağız
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .statusCode(200)
                .body("places.'place name'",hasItem("Dörtağaç Köyü"))  //verilen pathdeki liste bu item a sahip mi?

        ;


    }


    @Test
    public void bodyArrayHasSizeTest() {

        given()
                // hazırlık işlemlerini yapacağız

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
                .body("places", hasSize(1))  //verilen pathdeki liste size ı 1 mi?

        ;

    }

        @Test
        public void combiningTest () {

            given()

                    .when()
                    .get("http://api.zippopotam.us/us/90210")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("places", hasSize(1))
                    .body("places.state",hasItem("California"))
                    .body("places[0].'place name'", equalTo("Beverly Hills"))


            ;

        }




    @Test
    public void pathParamTest () {

        given()
                .pathParam("Country","us")
                .pathParam("ZipKod",90210)
                .log().uri()   // request linkini verir. response değil
                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipKod}")
                .then()
                .log().body()
                .statusCode(200)

        ;
    }

    @Test
    public void pathParamTest2 () {

        // 90210 dan 90213 kadar test sonuçlarında places in size nın hepsinde 1 geldiğini test ediniz.


        for (int i = 90210; i <=90213 ; i++) {

            given()
                    .pathParam("Country","us")
                    .pathParam("ZipKod",i)
                    .log().uri()
                    .when()
                    .get("http://api.zippopotam.us/{Country}/{ZipKod}")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("places", hasSize(1))
            ;

        }

    }

    @Test
    public void queryParamTest () {

            given()
                    .param("page",1)  // linke ?page=1 olarak eklenir...
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("meta.pagination.page",equalTo(1))
            ;

        }


    @Test
    public void queryParamTest2 () {

        // https://gorest.co.in/public/v1/users
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.


        for (int pageNo = 1; pageNo < 10; pageNo++) {

            given()
                    .param("page",pageNo)  // linke ?page=1 olarak eklenir...
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("meta.pagination.page",equalTo(pageNo))
            ;
        }
    }

        RequestSpecification requestSpec;
        ResponseSpecification responseSpec;

        @BeforeClass
        void Setup(){

            baseURI="https://gorest.co.in/public/v1";

            requestSpec = new RequestSpecBuilder()
                    .log(LogDetail.URI)
                    .setContentType(ContentType.JSON)
                    .build();


            responseSpec = new ResponseSpecBuilder()
                    .expectStatusCode(200)
                    .expectContentType(ContentType.JSON)
                    .log(LogDetail.BODY)
                    .build();


        }

        @Test
        public void requestResponseSpecification(){
            //https:gorest.co.in/public/v1/users?page=3

            given()
                    .param ("page",1)
                    .spec(requestSpec)
                    .when()
                    .get("/users")
                    .then()
                    .body("meta.pagination.page", equalTo(1))
                    .spec(responseSpec)
                    ;

        }


        //JSON EXRACT

        @Test
        public void extractingJsonPath()
        {
            String placeName=
            given()


                    .when()
                    .get("http://api.zippopotam.us/us/90210")


                    .then()
                    .statusCode(200)
                    .log().body()
                    .extract().path("places[0].'place name'");

                     System.out.println("placeName="+placeName);

        }


    @Test
    public void extractingJsonPathInt()


    {

        int limit=

                given()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()

                .log().body()
                .statusCode(200)

                .extract().path("meta.pagination.limit");

        System.out.println("limit="+limit);
        Assert.assertEquals(limit,10,"test sonucu");

    }

    @Test
    public void extractingJsonPathList()


    {
        List<Integer> idler=
            given()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()

                    .log().body()
                    .statusCode(200)

                    .extract().path("data.id");

        System.out.println("idler="+idler);

        Assert.assertTrue(idler.contains(3564));

    }

    @Test
    public void extractingJsonPathString()


    {
        List<String> names=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()

                        .log().body()
                        .statusCode(200)

                        .extract().path("data.name");

        System.out.println("idler="+names);

        Assert.assertTrue(names.contains("Kanak Dubashi"));

    }


    @Test
    public void extractingJsonPathResponseAll()

    {
        Response response=

                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .statusCode(200)

                        .extract().response();


       List<Integer> idler=response.path("data.id");
       List<String> isimler=response.path("data.name");
       int limit=response.path("meta.pagination.limit");

        System.out.println("idler="+idler);
        System.out.println("isimler="+isimler);
        System.out.println("limit="+limit);


        Assert.assertTrue(isimler.contains("Kanak Dubashi"));
        Assert.assertTrue(idler.contains(3564));
        Assert.assertEquals(limit,10,"test sonucu");

    }





    @Test
    public void extractingJsonPOJO() // PLAIN OLD JAVA OBJECT

    {

        Location yer=
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .extract().as(Location.class)// Location şablonuna göre

        ;

        System.out.println("yer="+yer.getPostCode());

        System.out.println("yer.getPlaces().get(0).getPlaceName()="+
                yer.getPlaces().get(0).getPlaceName());



    }



}


