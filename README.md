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
# API Tests (RestAssured + TestNG) — Local & Deterministic
## Why WireMock?
Public sandbox (reqres.in) sometimes returns 401/403 (“Missing/Invalid API key”) from upstream gateways. To keep tests stable and deterministic, we mock the API locally with WireMock and stub the exact responses we expect.
Note: When running against the real API, tests contain a small helper that passes 401/403 gateway blocks instead of failing, since that’s infrastructure—not logic.

## How it works (brief)
Each test method starts a WireMockServer on a dynamic port, registers stubs for the endpoints above, and points RestAssured to http://localhost:<port>/api.
After each test, the server is stopped.
If you want to run against the real API, switch the baseUri back to https://reqres.in and optionally keep the skipIfGatewayBlocked(...) helper enabled to avoid false failures.

# 📋 API Test Cases – ReqRes

<table border="1" cellpadding="6" cellspacing="0" width="100%">
<thead style="background:#f3f4f6;">
<tr>
<th>ID</th>
<th>Title</th>
<th>Endpoint / Method</th>
<th>Request</th>
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
• <code>statusCode == 200</code><br/>
• <code>page == 2</code><br/>
• <code>data.size() > 0</code><br/>
• Fields: <code>id, email, first_name, last_name, avatar</code>
</td>
<td>🟢 <b>200 OK</b></td>
<td>Matches <code>testGetUsers</code> implemented.</td>
</tr>
<tr>
<td><b>TC-002</b></td>
<td>Get Single User (exists)</td>
<td><code>GET /users/2</code></td>
<td>Path: <code>id = 2</code></td>
<td>
• <code>statusCode == 200</code><br/>
• <code>data.id == 2</code><br/>
• Fields exist: <code>email, first_name, last_name, avatar</code><br/>
• <code>support</code> contains <code>url, text</code>
</td>
<td>🟢 <b>200 OK</b></td>
<td>Positive path.</td>
</tr>
<tr>
<td><b>TC-003</b></td>
<td>Register – missing password</td>
<td><code>POST /register</code></td>
<td>Body: <code>{"email":"eve.holt@reqres.in"}</code></td>
<td>
• <code>statusCode == 400</code><br/>
• Body has <code>error</code> ("Missing password")
</td>
<td>🟠 <b>400 Bad Request</b></td>
<td>Negative path validation.</td>
</tr>
<tr>
<td><b>TC-004</b></td>
<td>Register – success</td>
<td><code>POST /register</code></td>
<td>Body: <code>{ "email": "eve.holt@reqres.in", "password": "pistol" }</code></td>
<td>
• <code>statusCode == 200</code><br/>
• Body has <code>id</code> (non-empty)<br/>
• Body has <code>token</code> (non-empty)
</td>
<td>🟢 <b>200 OK</b></td>
<td>Positive path for registration.</td>
</tr>
<tr>
<td><b>TC-005</b></td>
<td>Create User</td>
<td><code>POST /users</code></td>
<td><code>{ "name": "Heba", "job": "QA" }</code></td>
<td>
• <code>statusCode == 201</code><br/>
• Body has <code>id, name, job, createdAt</code>
</td>
<td>🟢 <b>201 Created</b></td>
<td>Smoke test for POST.</td>
</tr>
<tr>
<td><b>TC-006</b></td>
<td>Update User (PUT)</td>
<td><code>PUT /users/2</code></td>
<td><code>{ "name": "Heba", "job": "QA Lead" }</code></td>
<td>
• <code>statusCode == 200</code><br/>
• Body has <code>name, job, updatedAt</code>
</td>
<td>🟢 <b>200 OK</b></td>
<td>Full update semantics.</td>
</tr>
<tr>
<td><b>TC-007</b></td>
<td>Partial Update (PATCH)</td>
<td><code>PATCH /users/2</code></td>
<td><code>{ "job": "Principal QA" }</code></td>
<td>
• <code>statusCode == 200</code><br/>
• Body has updated <code>job</code> and <code>updatedAt</code>
</td>
<td>🟢 <b>200 OK</b></td>
<td>Partial update semantics.</td>
</tr>
<tr>
<td><b>TC-008</b></td>
<td>Delete User</td>
<td><code>DELETE /users/2</code></td>
<td>Path: <code>id = 2</code></td>
<td>
• <code>statusCode == 204</code><br/>
• Response body empty
</td>
<td>🟢 <b>204 No Content</b></td>
<td>ReqRes mock API.</td>
</tr>
<tr>
<td><b>TC-009</b></td>
<td>Login – success</td>
<td><code>POST /login</code></td>
<td><code>{ "email": "eve.holt@reqres.in", "password": "cityslicka" }</code></td>
<td>
• <code>statusCode == 200</code><br/>
• Body has <code>token</code> (non-empty)
</td>
<td>🟢 <b>200 OK</b></td>
<td>Valid credentials.</td>
</tr>
<tr>
<td><b>TC-010</b></td>
<td>Login – missing password</td>
<td><code>POST /login</code></td>
<td><code>{ "email": "peter@klaven" }</code></td>
<td>
• <code>statusCode == 400</code><br/>
• Body has <code>error</code> ("Missing password")
</td>
<td>🟠 <b>400 Bad Request</b></td>
<td>Negative path validation.</td>
</tr>
<tr>
  <td><b>TC-010</b></td>
  <td>Login – missing password</td>
  <td><code>POST /login</code></td>
  <td>Body: <code>{"email":"peter@klaven"}</code></td>
  <td>
    • <code>statusCode == 400</code><br/>
    • <code>error == "Missing password"</code>
  </td>
  <td>🟠 400 Bad Request</td>
  <td>Negative path validation.</td>
</tr>

</tbody>
</table>

<h3>🔎 Schema-Level Validation</h3>
<p>Validate the structure of each user object returned in <code>data</code>:</p>

<table border="1" cellpadding="6" cellspacing="0" width="100%">
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
      <td>integer</td>
      <td>✅ Required</td>
      <td><code>2</code></td>
    </tr>
    <tr>
      <td><code>email</code></td>
      <td>string</td>
      <td>✅ Required, valid email format</td>
      <td><code>janet.weaver@reqres.in</code></td>
    </tr>
    <tr>
      <td><code>first_name</code></td>
      <td>string</td>
      <td>✅ Required</td>
      <td><code>Janet</code></td>
    </tr>
    <tr>
      <td><code>last_name</code></td>
      <td>string</td>
      <td>✅ Required</td>
      <td><code>Weaver</code></td>
    </tr>
    <tr>
      <td><code>avatar</code></td>
      <td>string (URL)</td>
      <td>✅ Required, valid URL</td>
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