package tests;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ReqResApiTest {

    @Test
    public void testGetUsers() {
        baseURI = "https://reqres.in/api";

        given()
                .when()
                .get("/users?page=2")
                .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data.size()", greaterThan(0));
    }
}
