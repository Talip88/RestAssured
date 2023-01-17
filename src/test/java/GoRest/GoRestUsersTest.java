package GoRest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;

public class GoRestUsersTest {

//GLOBAL DEĞİŞKEN ATIYORUZ...

    int userID;
    User newUser;

// ÇALISMA SIRASI

    // Diğer create çeşitleri toplu çalışmada çalışmaması için enable false yapılacak.. sadece bir tane create çalışması için.

    //create             priority 1
    //getUserByID        priority 2                       dependsOnMethods ="createUserObjectWithObject"
    //update             priority 3                       dependsOnMethods ="createUserObjectWithObject"
    //deleteUser         priority 4                       dependsOnMethods ="createUserObjectWithObject"

    @BeforeClass
    void Setup() {
        baseURI = "https://gorest.co.in/public/v2/users"; // PROD
        //baseURI="https://test.gorest.co.in/public/v2/users"; // TEST

    }

    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String getRandomMail() {
        return RandomStringUtils.randomAlphabetic(8).toLowerCase() + "@gmail.com";

    }


    @Test(enabled = false)   //tek  create çalışması içim
    public void createUserObject() {
        userID =
                given()

                        .header("Authorization", "Bearer 46f9460f6cd9e573021594db5fd1713381fefed250aaab14594c9d5892963cfc")
                        .contentType(ContentType.JSON)
                        .body("{\"name\":\"" + getRandomName() + "\",\"gender\":\"male\", \"email\":\"" + getRandomMail() + "\", \"status\":\"active\"}")
                        .log().uri()



                        .when()
                        .post("")


                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;


    }

    @Test(enabled = false)  //tek  create çalışması içim
    public void createUserObjectWithMap() {
// CREATE USER AŞAMASI**************************************************************************************************

        Map<String, String> newUSer = new HashMap<>();
        newUSer.put("name", getRandomName());
        newUSer.put("gender", "male");
        newUSer.put("email", getRandomMail());
        newUSer.put("status", "active");

// GIVEN ILE REQUEST OZELLİKLERİ GİRİLİR******************POSTMAN'daki Authorization ve Request Body kısmı**************

        userID =
                given()

                        .header("Authorization", "Bearer 46f9460f6cd9e573021594db5fd1713381fefed250aaab14594c9d5892963cfc")
                        .contentType(ContentType.JSON)
                        //.body("{\"name\":\""+getRandomName()+"\",\"gender\":\"male\", \"email\":\""+getRandomMail()+"\", \"status\":\"active\"}")
                        .body(newUSer)

// WHEN ILE REQUEST BAŞLAR******************POSTMAN'daki SEND***********************************************************
                        .log().uri()
                        .when()
                        .post("")   // Response'un oluştuğu nokta... baseURI yazsan da olur "" koysan da bu kısma bu şekilde de yazılır. baseURI de yazılır. "" şeklinde de yazılır

// TEST PENCERESI*******************************************************************************************************
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;


    }


    @Test
    public void createUserObjectWithObject() {
// User newUser
// GLOBAL DEĞİŞKENDE

        newUser = new User();

        newUser.setName(getRandomName());
        newUser.setGender("male");
        newUser.setEmail(getRandomMail());
        newUser.setStatus("active");


        userID =
                given()

                        .header("Authorization", "Bearer 46f9460f6cd9e573021594db5fd1713381fefed250aaab14594c9d5892963cfc")
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        .log().body()


                        .when()
                        .post("")


                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        //.extract().path("id")
                        .extract().jsonPath().getInt("id"); // id yi dönüştürerek alabilmek için böyle de alabilirsin...

        // path : class veya tip dönüşümüne imkan vermeden direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.

        ;

        System.out.println("userID = " + userID);
    }


    @Test(dependsOnMethods = "createUserObjectWithObject", priority = 1)
    public void getUserByID() {
        given()
                .header("Authorization", "Bearer 46f9460f6cd9e573021594db5fd1713381fefed250aaab14594c9d5892963cfc")
                .pathParam("userId", userID)
                .log().uri()

                .when()
                .get("/{userId}")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(userID))
        ;
    }


    @Test(dependsOnMethods = "createUserObjectWithObject", priority = 2)
    public void updateUserObject() {

        //newUser.setName("ismet temur");

        Map<String, String> updateUser = new HashMap<>();
        updateUser.put("name", "ismet temur");

        given()
                .header("Authorization", "Bearer 46f9460f6cd9e573021594db5fd1713381fefed250aaab14594c9d5892963cfc")
                .pathParam("userId", userID)
                .contentType(ContentType.JSON)
                .body(updateUser)
                .log().body()
                .log().uri()

                .when()
                .put("/{userId}")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(userID))
                .body("name", equalTo("ismet temur"))
        ;
    }


    @Test(dependsOnMethods = "updateUserObject", priority = 3)
    public void deleteUserById() {

        given()

                .header("Authorization", "Bearer 46f9460f6cd9e573021594db5fd1713381fefed250aaab14594c9d5892963cfc")
                .pathParam("userId", userID)
                .log().uri()

                .when()
                .delete("/{userId}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }


    @Test(dependsOnMethods = "deleteUserById")
    public void deleteUserByIdNegative() {

        given()
                .header("Authorization", "Bearer 46f9460f6cd9e573021594db5fd1713381fefed250aaab14594c9d5892963cfc")
                .pathParam("userId", userID)
                .log().uri()

                .when()
                .delete("/{userId}")

                .then()
                .log().body()
                .statusCode(404)
        ;
    }


    @Test
    public void getUsers() {
        Response body =
                given()
                        .header("Authorization", "Bearer 46f9460f6cd9e573021594db5fd1713381fefed250aaab14594c9d5892963cfc")

                        .when()
                        .get()

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response();

        // path ve jsonpath farkları
        int idUser3path = body.path("[2].id");  // tip dönüşümü ototmatik uygun tip verilmeli
        int idUser3JsonPath = body.jsonPath().getInt("[2].id"); // tip dönüşümü kendi içinde yapılabiliyor
        System.out.println("idUser3path = " + idUser3path);
        System.out.println("idUser3JsonPath = " + idUser3JsonPath);

        User[] userlar = body.as(User[].class);  // extract.as
        System.out.println("Arrays.toString(userlar) = " + Arrays.toString(userlar));

        List<User> listUserlar = body.jsonPath().getList("", User.class);  // jsonpath ile list e dönüştürerek alabiliyorum
        System.out.println("listUserlar = " + listUserlar);

    }

    @Test
    public void getUsersV1() {
        Response body =
                given()
                        .header("Authorization", "Bearer 46f9460f6cd9e573021594db5fd1713381fefed250aaab14594c9d5892963cfc")

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response();

        // body.as(), extract.as // tüm gelen response uygun nesneler için tüm classların yapılması gerekiyor.

        List<User> dataUsers = body.jsonPath().getList("data", User.class);
        // JSONPATH bir response içindeki bir parçayı nesneye ödnüştürebiliriz.
        System.out.println("dataUsers = " + dataUsers);

        // Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
        // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ise veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.

    }









}
    class User {
        private int id;
        private String name;
        private String gender;
        private String email;
        private String status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", gender='" + gender + '\'' +
                    ", email='" + email + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }





