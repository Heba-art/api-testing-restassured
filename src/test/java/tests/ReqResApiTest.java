package tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.*;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class ReqResApiTest {

    private static WireMockServer wm;

    // ---------- WireMock lifecycle ----------
    @BeforeClass
    public void startWireMock() {
        wm = new WireMockServer(wireMockConfig().dynamicPort());
        wm.start();

        // ------------------ STUBS ------------------

        // TC-001: GET /api/users?page=2 -> 200 + non-empty list
        wm.stubFor(get(urlPathEqualTo("/api/users"))
                .withQueryParam("page",
                        com.github.tomakehurst.wiremock.client.WireMock.equalTo("2"))
                .willReturn(okJson("""
                {
                  "page": 2,
                  "data": [
                    {
                      "id": 7,
                      "email": "george.bluth@reqres.in",
                      "first_name": "George",
                      "last_name": "Bluth",
                      "avatar": "https://example.com/7.png"
                    }
                  ]
                }
                """)));

        // TC-002: GET /api/users/2 -> 200 + data + support
        wm.stubFor(get(urlPathEqualTo("/api/users/2"))
                .willReturn(okJson("""
                {
                  "data": {
                    "id": 2,
                    "email": "janet.weaver@reqres.in",
                    "first_name": "Janet",
                    "last_name": "Weaver",
                    "avatar": "https://example.com/2.png"
                  },
                  "support": {
                    "url": "https://reqres.in/#support-heading",
                    "text": "To keep ReqRes free, contributions are appreciated!"
                  }
                }
                """)));

        // TC-003: POST /api/register (email only) -> 400
        wm.stubFor(post(urlEqualTo("/api/register"))
                .withRequestBody(equalToJson("{\"email\":\"eve.holt@reqres.in\"}", true, true))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type","application/json")
                        .withBody("{\"error\":\"Missing password\"}")));

        // TC-004: POST /api/register (email+password) -> 200
        wm.stubFor(post(urlEqualTo("/api/register"))
                .withRequestBody(equalToJson(
                        "{\"email\":\"eve.holt@reqres.in\",\"password\":\"pistol\"}", true, true))
                .willReturn(okJson("{\"id\":4,\"token\":\"QpwL5tke4Pnpja7X4\"}")));

        // TC-005: POST /api/users -> 201 (create)
        wm.stubFor(post(urlEqualTo("/api/users"))
                .withRequestBody(equalToJson("{\"name\":\"Heba\",\"job\":\"QA\"}", true, true))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type","application/json")
                        .withBody("""
                        {
                          "id": "101",
                          "name": "Heba",
                          "job": "QA",
                          "createdAt": "2025-09-02T10:00:00.000Z"
                        }
                        """)));

        // TC-006: PUT /api/users/2 -> 200 (full update)
        wm.stubFor(put(urlEqualTo("/api/users/2"))
                .withRequestBody(equalToJson("{\"name\":\"Heba\",\"job\":\"QA Lead\"}", true, true))
                .willReturn(okJson("""
                {
                  "name": "Heba",
                  "job": "QA Lead",
                  "updatedAt": "2025-09-02T10:02:00.000Z"
                }
                """)));

        // TC-007: PATCH /api/users/2 -> 200 (partial update)
        wm.stubFor(patch(urlEqualTo("/api/users/2"))
                .withRequestBody(equalToJson("{\"job\":\"Principal QA\"}", true, true))
                .willReturn(okJson("""
                {
                  "job": "Principal QA",
                  "updatedAt": "2025-09-02T10:03:00.000Z"
                }
                """)));

        // TC-008: DELETE /api/users/2 -> 204 (no content)
        wm.stubFor(delete(urlEqualTo("/api/users/2"))
                .willReturn(aResponse().withStatus(204)));

        // TC-009: POST /api/login (success) -> 200 {token}
        wm.stubFor(post(urlEqualTo("/api/login"))
                .withRequestBody(equalToJson(
                        "{\"email\":\"eve.holt@reqres.in\",\"password\":\"cityslicka\"}", true, true))
                .willReturn(okJson("{\"token\":\"QpwL5tke4Pnpja7X4\"}")));

        // TC-010: POST /api/login (missing password) -> 400 {error}
        wm.stubFor(post(urlEqualTo("/api/login"))
                .withRequestBody(equalToJson("{\"email\":\"peter@klaven\"}", true, true))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type","application/json")
                        .withBody("{\"error\":\"Missing password\"}")));
    }

    @AfterClass
    public void stopWireMock() {
        if (wm != null) wm.stop();
    }

    // ---------- RestAssured setup ----------
    @BeforeMethod
    public void setupRestAssured() {
        RestAssured.reset();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI  = "http://localhost";
        RestAssured.port     = wm.port();
        RestAssured.basePath = ""; // stubs already start with /api
    }

    // ------------------ TESTS ------------------

    @Test(description = "TC-001 | List Users – page 2: GET /api/users?page=2 -> 200 & non-empty list")
    public void TC001_testGetUsers_Page2() {
        given()
                .accept(ContentType.JSON)
                .queryParam("page", 2)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data.size()", greaterThan(0))
                .body("data[0].id", notNullValue())
                .body("data[0].email", not(emptyOrNullString()));
    }

    @Test(description = "TC-002 | Get Single User (exists): GET /api/users/2 -> 200 + user fields + support")
    public void testGetSingleUser_Exists() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", hasKey("support"))
                .body("data.id", equalTo(2))
                .body("data.email", not(emptyOrNullString()))
                .body("data.first_name", not(emptyOrNullString()))
                .body("data.last_name", not(emptyOrNullString()))
                .body("data.avatar", startsWith("https://"))
                .body("support.url", not(emptyOrNullString()))
                .body("support.text", not(emptyOrNullString()));
    }

    @Test(description = "TC-003 | Register – missing password: POST /api/register (email only) -> 400 + error")
    public void testRegister_MissingPassword() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"eve.holt@reqres.in\"}")
                .when()
                .post("/api/register")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test(description = "TC-004 | Register – success: POST /api/register -> 200 + id & token")
    public void testRegister_Success() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"eve.holt@reqres.in\",\"password\":\"pistol\"}")
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .body("$", hasKey("id"))
                .body("$", hasKey("token"))
                .body("id", not(emptyOrNullString()))
                .body("token", not(emptyOrNullString()));
    }

    @Test(description = "TC-005 | Create User: POST /api/users -> 201 + id/name/job/createdAt")
    public void testCreateUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Heba\",\"job\":\"QA\"}")
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("$", hasKey("id"))
                .body("name", equalTo("Heba"))
                .body("job", equalTo("QA"))
                .body("createdAt", not(emptyOrNullString()));
    }

    @Test(description = "TC-006 | Update User (PUT): PUT /api/users/2 -> 200 + name/job/updatedAt")
    public void testUpdateUser_Put() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Heba\",\"job\":\"QA Lead\"}")
                .when()
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("Heba"))
                .body("job", equalTo("QA Lead"))
                .body("updatedAt", not(emptyOrNullString()));
    }

    @Test(description = "TC-007 | Partial Update (PATCH): PATCH /api/users/2 -> 200 + updated job & updatedAt")
    public void testPartialUpdate_Patch() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"job\":\"Principal QA\"}")
                .when()
                .patch("/api/users/2")
                .then()
                .statusCode(200)
                .body("job", equalTo("Principal QA"))
                .body("updatedAt", not(emptyOrNullString()));
    }

    @Test(description = "TC-008 | Delete User: DELETE /api/users/2 -> 204 + empty body")
    public void testDeleteUser() {
        Response res =
                given()
                        .when()
                        .delete("/api/users/2")
                        .then()
                        .statusCode(204)
                        .extract().response();

        assertTrue(res.asString().isEmpty(), "Body should be empty for 204 No Content");
    }

    @Test(description = "TC-009 | Login – success: POST /api/login -> 200 + token")
    public void testLogin_Success() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"eve.holt@reqres.in\",\"password\":\"cityslicka\"}")
                .when()
                .post("/api/login")
                .then()
                .statusCode(200)
                .body("token", not(emptyOrNullString()));
    }

    @Test(description = "TC-010 | Login – missing password: POST /api/login -> 400 + error")
    public void testLogin_MissingPassword() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"peter@klaven\"}")
                .when()
                .post("/api/login")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }
}
