# Implement this API

#### In this assessment you will be tasked with filling out the functionality of different methods that will be listed further down.

These methods will require some level of API interactions with Mock Employee API at http://localhost:8112/api/v1/employee.

Please keep the following in mind when doing this assessment:
* clean coding practices
* test driven development
* logging
* scalability

### Endpoints to implement

_See `com.reliaquest.api.controller.IEmployeeController` for details._

getAllEmployees()

    output - list of employees
    description - this should return all employees

getEmployeesByNameSearch(...)

    path input - name fragment
    output - list of employees
    description - this should return all employees whose name contains or matches the string input provided

getEmployeeById(...)

    path input - employee ID
    output - employee
    description - this should return a single employee

getHighestSalaryOfEmployees()

    output - integer of the highest salary
    description - this should return a single integer indicating the highest salary of amongst all employees

getTop10HighestEarningEmployeeNames()

    output - list of employees
    description - this should return a list of the top 10 employees based off of their salaries

createEmployee(...)

    body input - attributes necessary to create an employee
    output - employee
    description - this should return a single employee, if created, otherwise error

deleteEmployeeById(...)

    path input - employee ID
    output - name of the employee
    description - this should delete the employee with specified id given, otherwise error

### Testing
Please include proper integration and/or unit tests.

# Employee API Implementation

This project is a Java Spring Boot implementation of an Employee API controller that interacts with a mock server (`http://localhost:8112/api/v1/employee`). It fulfills the contract defined by the `IEmployeeController` interface.

---

### Features Implemented

- `GET /` – Get all employees
- `GET /search/{searchString}` – Search employees by name
- `GET /{id}` – Get employee by ID
- `GET /highestSalary` – Get highest salary among employees
- `GET /topTenHighestEarningEmployeeNames` – Get top 10 highest-paid employees
- `POST /` – Create a new employee
- `DELETE /{id}` – Delete employee by ID (sends body with name)

---

### Technologies Used

- Java 17+
- Spring Boot
- RestTemplate (for external API communication)
- JUnit 5, Mockito (for testing)
- Lombok (for DTOs and logging)
- SLF4J (for structured logging)

### Notes & Assumptions

- The `DELETE` endpoint of the mock server requires a JSON body with the employee name.  
  Since `RestTemplate.delete()` does not support a body,`RestTemplate.exchange(...)` with `HttpMethod.DELETE` is used.
- Unit testing is prioritized over integration testing to ensure speed and reliability. All core logic is tested independently of the server implementation.
