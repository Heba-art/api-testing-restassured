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
<h1>📋 API Test Cases – ReqRes</h1>

<table>
  <thead>
    <tr>
      <th>ID</th>
      <th>Title</th>
      <th>Endpoint / Method</th>
      <th>Request (Params / Body)</th>
      <th>Validations (Assertions)</th>
      <th>Expected HTTP</th>
      <th>Notes</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><b>TC-001</b></td>
      <td>List Users – page 2</td>
      <td><code>GET /users?page=2</code></td>
      <td>Query: <code>page=2</code></td>
      <td>
        • <code>statusCode == 200</code><br>
        • <code>page == 2</code><br>
        • <code>data.size() > 0</code><br>
        • Fields: <code>id, email, first_name, last_name, avatar</code><br>
        • Email format valid, avatar URL valid
      </td>
      <td style="color:green"><b>200 OK</b></td>
      <td style="color:gray">Matches <i>testGetUsers</i> implemented</td>
    </tr>
    <tr>
      <td><b>TC-002</b></td>
      <td>Get Single User (exists)</td>
      <td><code>GET /users/2</code></td>
      <td>Path: <code>id = 2</code></td>
      <td>
        • <code>statusCode == 200</code><br>
        • <code>data.id == 2</code><br>
        • Required fields: <code>email, first_name, last_name, avatar</code><br>
        • <code>support</code> has <code>url</code> & <code>text</code>
      </td>
      <td style="color:green"><b>200 OK</b></td>
      <td style="color:gray">Positive path</td>
    </tr>
    <tr>
      <td><b>TC-003</b></td>
      <td>Get Single User (not found)</td>
      <td><code>GET /users/23</code></td>
      <td>Path: <code>id = 23</code> (non-existing)</td>
      <td>
        • <code>statusCode == 404</code><br>
        • Body empty
      </td>
      <td style="color:red"><b>404 Not Found</b></td>
      <td style="color:gray">Negative path</td>
    </tr>
    <tr>
      <td><b>TC-004</b></td>
      <td>Create User</td>
      <td><code>POST /users</code></td>
      <td>JSON: <code>{ "name": "Heba", "job": "QA" }</code></td>
      <td>
        • <code>statusCode == 201</code><br>
        • Body has <code>id, name, job, createdAt</code> (ISO-8601)
      </td>
      <td style="color:green"><b>201 Created</b></td>
      <td style="color:gray">Smoke test for POST</td>
    </tr>
    <tr>
      <td><b>TC-005</b></td>
      <td>Update User (PUT)</td>
      <td><code>PUT /users/2</code></td>
      <td>JSON: <code>{ "name": "Heba", "job": "QA Lead" }</code></td>
      <td>
        • <code>statusCode == 200</code><br>
        • Body has <code>name, job, updatedAt</code> (ISO-8601)
      </td>
      <td style="color:green"><b>200 OK</b></td>
      <td style="color:gray">Full update semantics</td>
    </tr>
    <tr>
      <td><b>TC-006</b></td>
      <td>Partial Update (PATCH)</td>
      <td><code>PATCH /users/2</code></td>
      <td>JSON: <code>{ "job": "Principal QA" }</code></td>
      <td>
        • <code>statusCode == 200</code><br>
        • Body has updated <code>job</code> + <code>updatedAt</code>
      </td>
      <td style="color:green"><b>200 OK</b></td>
      <td style="color:gray">Partial update semantics</td>
    </tr>
    <tr>
      <td><b>TC-007</b></td>
      <td>Delete User</td>
      <td><code>DELETE /users/2</code></td>
      <td>Path: <code>id = 2</code></td>
      <td>
        • <code>statusCode == 204</code><br>
        • Response body empty
      </td>
      <td style="color:green"><b>204 No Content</b></td>
      <td style="color:gray">ReqRes mock API</td>
    </tr>
    <tr>
      <td><b>TC-008</b></td>
      <td>Login – success</td>
      <td><code>POST /login</code></td>
      <td>JSON: <code>{ "email": "eve.holt@reqres.in", "password": "cityslicka" }</code></td>
      <td>
        • <code>statusCode == 200</code><br>
        • Body has <code>token</code> (non-empty)
      </td>
      <td style="color:green"><b>200 OK</b></td>
      <td style="color:gray">Valid credentials per ReqRes docs</td>
    </tr>
    <tr>
      <td><b>TC-009</b></td>
      <td>Login – missing password</td>
      <td><code>POST /login</code></td>
      <td>JSON: <code>{ "email": "peter@klaven" }</code></td>
      <td>
        • <code>statusCode == 400</code><br>
        • Body has <code>error</code> ("Missing password")
      </td>
      <td style="color:red"><b>400 Bad Request</b></td>
      <td style="color:gray">Negative path validation</td>
    </tr>
  </tbody>
</table>

# 🔍 Schema-Level Validation (Optional Future Improvement)

Extend the test to validate the structure of each user object returned in the data array:
<h3>🔍 Schema-Level Validation</h3>

<table>
  <thead>
    <tr>
      <th>Field</th>
      <th>Type</th>
      <th>Constraint</th>
      <th>Example</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>id</code></td>
      <td style="color:blue;"><b>integer</b></td>
      <td style="color:green;">Required</td>
      <td><code>2</code></td>
    </tr>
    <tr>
      <td><code>email</code></td>
      <td style="color:blue;"><b>string</b></td>
      <td style="color:green;">Required, valid email format</td>
      <td><code>janet.weaver@reqres.in</code></td>
    </tr>
    <tr>
      <td><code>first_name</code></td>
      <td style="color:blue;"><b>string</b></td>
      <td style="color:green;">Required</td>
      <td><code>Janet</code></td>
    </tr>
    <tr>
      <td><code>last_name</code></td>
      <td style="color:blue;"><b>string</b></td>
      <td style="color:green;">Required</td>
      <td><code>Weaver</code></td>
    </tr>
    <tr>
      <td><code>avatar</code></td>
      <td style="color:blue;"><b>string (URL)</b></td>
      <td style="color:green;">Required, valid URL</td>
      <td><code>https://reqres.in/img/faces/2-image.jpg</code></td>
    </tr>
  </tbody>
</table>

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