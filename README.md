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
<h3>Test Case Details: <code>testGetUsers</code></h3>
<p>
  This test validates the <code>GET /users?page=2</code> endpoint of the ReqRes API.
  It ensures both functional correctness and data integrity of the API response.
</p>

<table border="1" cellpadding="6" cellspacing="0" width="100%">
  <thead>
    <tr>
      <th>#</th>
      <th>Step</th>
      <th>Assertion / Expected</th>
      <th>Why it matters</th>
      <th>Example</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>1</td>
      <td><strong>Send Request</strong><br>
          Execute HTTP <code>GET</code> to <code>/users?page=2</code>.
      </td>
      <td>Request is processed successfully by the server.</td>
      <td>Initiates retrieval of a paginated users list.</td>
      <td><code>GET https://reqres.in/api/users?page=2</code></td>
    </tr>
    <tr>
      <td>2</td>
      <td><strong>Status Code Validation</strong></td>
      <td><code>status = 200 (OK)</code></td>
      <td>Confirms the endpoint is reachable and handled correctly.</td>
      <td><code>HTTP/1.1 200 OK</code></td>
    </tr>
    <tr>
      <td>3</td>
      <td><strong>Pagination Check</strong></td>
      <td>Response body contains <code>"page": 2</code></td>
      <td>Verifies the API returned the requested page (important for UIs using paging).</td>
      <td><code>{ "page": 2, "per_page": 6, ... }</code></td>
    </tr>
    <tr>
      <td>4</td>
      <td><strong>Data Integrity</strong></td>
      <td><code>data</code> array is not empty (<code>data.length &gt; 0</code>)</td>
      <td>Ensures actual user records are returned, not an empty payload.</td>
      <td><code>"data": [{ "id": 7, "email": "...", ... }]</code></td>
    </tr>
  </tbody>
</table>

<h4>Schema-Level Validation (Optional Future Improvement)</h4>
<p>Extend the test to validate the structure of each user object:</p>

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
      <td>Required</td>
      <td><code>2</code></td>
    </tr>
    <tr>
      <td><code>email</code></td>
      <td>string</td>
      <td>Required, valid email format</td>
      <td><code>janet.weaver@reqres.in</code></td>
    </tr>
    <tr>
      <td><code>first_name</code></td>
      <td>string</td>
      <td>Required</td>
      <td><code>Janet</code></td>
    </tr>
    <tr>
      <td><code>last_name</code></td>
      <td>string</td>
      <td>Required</td>
      <td><code>Weaver</code></td>
    </tr>
    <tr>
      <td><code>avatar</code></td>
      <td>string (URL)</td>
      <td>Required, valid URL</td>
      <td><code>https://reqres.in/img/faces/2-image.jpg</code></td>
    </tr>
  </tbody>
</table>
## ▶️ How to Run 1. Clone the repo:

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