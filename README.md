# API Testing with RestAssured + TestNG

![Java](https://img.shields.io/badge/Java-23-blue?logo=java)
![Maven](https://img.shields.io/badge/Maven-Build-orange?logo=apachemaven)
![TestNG](https://img.shields.io/badge/TestNG-Framework-brightgreen)
![RestAssured](https://img.shields.io/badge/RestAssured-API--Testing-yellow)
![License: MIT](https://img.shields.io/badge/License-MIT-green)

This project demonstrates **API Testing** using [RestAssured](https://rest-assured.io/) with **TestNG** on the public [ReqRes API](https://reqres.in/).

---

## 📌 Project Overview
- Automated API tests with **Java + RestAssured**.
- Used **TestNG** for test execution and reporting.
- Target API: [ReqRes](https://reqres.in/) (free API for testing and learning).
- Assertions for:
    - Status codes
    - Response body validation
    - Data size checks

---

## ⚙️ Tech Stack
- **Java 23** (JDK 23)
- **Maven** (for dependencies & build)
- **RestAssured** (API automation)
- **TestNG** (test framework)
- **Hamcrest** (matchers for assertions)

---

## 📂 Project Structure
```bash
api-testing-restassured
├── src
│   ├── main
│   │   └── java
│   └── test
│       └── java
│           └── tests
│               └── ReqResApiTest.java
└── pom.xml
```
# 📋 API Test Cases – ReqRes

| ID      | Title                     | Endpoint / Method        | Request (Params / Body)                              | Validations (Assertions)                                                                                                      | Expected HTTP | Notes                                |
|---------|---------------------------|--------------------------|------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------|---------------|--------------------------------------|
| TC-001  | List Users – page 2       | `GET /users?page=2`      | Query: `page=2`                                      | • `statusCode == 200` <br> • `page == 2` <br> • `data.size() > 0` <br> • Fields: `id, email, first_name, last_name, avatar` <br> • Email format valid, avatar URL valid | **200 OK**    | Matches `testGetUsers` implemented. |
| TC-002  | Get Single User (exists)  | `GET /users/2`           | Path: `id = 2`                                       | • `statusCode == 200` <br> • `data.id == 2` <br> • Required fields: `email, first_name, last_name, avatar` <br> • `support` has `url` & `text`                         | **200 OK**    | Positive path                        |
| TC-003  | Get Single User (not found)| `GET /users/23`          | Path: `id = 23` (non-existing)                       | • `statusCode == 404` <br> • Body empty                                                                                        | **404 Not Found** | Negative path                        |
| TC-004  | Create User               | `POST /users`            | JSON: `{ "name": "Heba", "job": "QA" }`              | • `statusCode == 201` <br> • Body has `id`, `name`, `job`, `createdAt` (ISO-8601)                                             | **201 Created** | Smoke test for POST                  |
| TC-005  | Update User (PUT)         | `PUT /users/2`           | JSON: `{ "name": "Heba", "job": "QA Lead" }`         | • `statusCode == 200` <br> • Body has `name`, `job`, `updatedAt` (ISO-8601)                                                    | **200 OK**    | Full update semantics                |
| TC-006  | Partial Update (PATCH)    | `PATCH /users/2`         | JSON: `{ "job": "Principal QA" }`                    | • `statusCode == 200` <br> • Body has updated `job` + `updatedAt`                                                              | **200 OK**    | Partial update semantics             |
| TC-007  | Delete User               | `DELETE /users/2`        | Path: `id = 2`                                       | • `statusCode == 204` <br> • Response body empty                                                                               | **204 No Content** | ReqRes mock API                      |
| TC-008  | Login – success           | `POST /login`            | JSON: `{ "email": "eve.holt@reqres.in", "password": "cityslicka" }` | • `statusCode == 200` <br> • Body has `token` (non-empty)                                                                     | **200 OK**    | Valid credentials per ReqRes docs    |
| TC-009  | Login – missing password  | `POST /login`            | JSON: `{ "email": "peter@klaven" }`                  | • `statusCode == 400` <br> • Body has `error` ("Missing password")                                                            | **400 Bad Request** | Negative path validation             |

# 🔍 Schema-Level Validation (Optional Future Improvement)

Extend the test to validate the structure of each user object returned in the data array:

| Field       | Type         | Constraint                  | Example                                  |
|-------------|-------------|-----------------------------|------------------------------------------|
| `id`        | integer      | Required                    | `2`                                      |
| `email`     | string       | Required, valid email format| `janet.weaver@reqres.in`                 |
| `first_name`| string       | Required                    | `Janet`                                  |
| `last_name` | string       | Required                    | `Weaver`                                 |
| `avatar`    | string (URL) | Required, valid URL         | `https://reqres.in/img/faces/2-image.jpg`|

## ▶️ How to Run
1. Clone the repo:

```bash
   git clone https://github.com/Heba-art/api-testing-restassured.git
```
2. Navigate to the project folder:
```bash 
  cd api-testing-restassured
```
3. Run the tests with Maven:
```bash
  mvn test
```
📊 Test Reports
Default TestNG reports will be generated under:
```bash
  target/surefire-reports
```
📜 License

This project is licensed under the MIT License