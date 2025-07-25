package com.reliaquest.api;

import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.dto.SingleEmployeeResponse;
import com.reliaquest.api.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class ApiApplicationTest {

    @Value("${employee.base-url}")
    private String baseUrl;

    @Mock
    private RestTemplate restTemplate;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        RestTemplateBuilder mockBuilder = mock(RestTemplateBuilder.class);
        when(mockBuilder.build()).thenReturn(restTemplate);
        employeeService = new EmployeeService(mockBuilder);
        ReflectionTestUtils.setField(employeeService, "baseUrl", baseUrl);
    }

    @Test
    void testGetAllEmployees_returnsList() {
        List<Employee> mockEmployees = List.of(
                new Employee(UUID.randomUUID(), "Julio", 80000, 30, "Community-Services Developer", "Julio@company.com")
        );
        EmployeeResponse response = new EmployeeResponse(mockEmployees, "Successfully processed request.");
        ResponseEntity<EmployeeResponse> entity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.getForEntity(baseUrl, EmployeeResponse.class)).thenReturn(entity);

        List<Employee> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmployee_name()).isEqualTo("Julio");
    }

    @Test
    void testGetEmployeeById_returnsEmployee() {
        UUID id = UUID.randomUUID();
        Employee employee = new Employee(id, "Julio", 90000, 28, "Community-Services Developer", "Julio@company.com");
        SingleEmployeeResponse response = new SingleEmployeeResponse(employee, "Successfully processed request.");
        ResponseEntity<SingleEmployeeResponse> entity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.getForEntity(baseUrl + "/" + id, SingleEmployeeResponse.class)).thenReturn(entity);

        Employee result = employeeService.getEmployeeById(id.toString());

        assertThat(result).isNotNull();
        assertThat(result.getEmployee_name()).isEqualTo("Julio");
    }

    @Test
    void testSearchEmployeesByName_matchesCorrectly() {
        List<Employee> mockEmployees = List.of(
                new Employee(UUID.randomUUID(), "Bobbie", 60000, 27, "Chief Associate", "Bobbie@company.com"),
                new Employee(UUID.randomUUID(), "Robbie", 70000, 27, "Chief Associate", "Robbie@company.com")
        );
        EmployeeResponse response = new EmployeeResponse(mockEmployees, "Successfully processed request.");
        ResponseEntity<EmployeeResponse> entity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.getForEntity(baseUrl, EmployeeResponse.class)).thenReturn(entity);

        List<Employee> results = employeeService.searchEmployeesByName("ob");

        assertThat(results).hasSize(2);
    }

    @Test
    void testGetHighestSalary_returnsCorrectMax() {
        List<Employee> employees = List.of(
                new Employee(UUID.randomUUID(), "Julio", 3000, 30, "Community-Services Developer", "Julio@company.com"),
                new Employee(UUID.randomUUID(), "Quinn", 5000, 32, "Chief Associate", "Quinn@company.com")
        );
        EmployeeResponse response = new EmployeeResponse(employees, "Successfully processed request.");
        ResponseEntity<EmployeeResponse> entity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.getForEntity(baseUrl, EmployeeResponse.class)).thenReturn(entity);

        int result = employeeService.getHighestSalary();

        assertThat(result).isEqualTo(5000);
    }

    @Test
    void testGetTopTenHighestPaidEmployeeNames_returnsList() {
        List<Employee> employees = List.of(
                new Employee(UUID.randomUUID(), "Julio", 5000, 30, "Community-Services Developer", "Julio@company.com"),
                new Employee(UUID.randomUUID(), "Quinn", 9000, 35, "Chief Associate", "Quinn@company.com"),
                new Employee(UUID.randomUUID(), "Robbie", 8000, 28, "Community-Services Developer", "Robbie@company.com")
        );
        EmployeeResponse response = new EmployeeResponse(employees, "Successfully processed request.");
        ResponseEntity<EmployeeResponse> entity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.getForEntity(baseUrl, EmployeeResponse.class)).thenReturn(entity);

        List<String> topNames = employeeService.getTop10HighestPaidEmployeeNames();

        assertThat(topNames).containsExactly("Quinn", "Robbie", "Julio");
    }

    @Test
    void testCreateEmployee_returnsCreatedEmployee() {
        CreateEmployeeRequest request = new CreateEmployeeRequest("Ryan", 75000, 27, "Chief Associate");
        Employee created = new Employee(UUID.randomUUID(), request.getName(), request.getSalary(), request.getAge(), request.getTitle(), null);
        SingleEmployeeResponse response = new SingleEmployeeResponse(created, "Successfully processed request.");
        ResponseEntity<SingleEmployeeResponse> entity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.postForEntity(eq(baseUrl), any(HttpEntity.class), eq(SingleEmployeeResponse.class))).thenReturn(entity);

        Employee result = employeeService.createEmployee(request);

        assertThat(result).isNotNull();
        assertThat(result.getEmployee_name()).isEqualTo("Ryan");
    }

    @Test
    void testDeleteEmployeeById_performsDeleteCall() {
        UUID id = UUID.randomUUID();
        Employee employee = new Employee(id, "Julio", 60000, 30, "Chief Associate", "Julio@company.com");
        EmployeeResponse response = new EmployeeResponse(List.of(employee), "Successfully processed request.");
        ResponseEntity<EmployeeResponse> entity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.getForEntity(baseUrl, EmployeeResponse.class)).thenReturn(entity);
        when(restTemplate.exchange(eq(baseUrl), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(ResponseEntity.ok().build());

        String result = employeeService.deleteEmployeeById(id.toString());

        assertThat(result).isEqualTo(id.toString());
        verify(restTemplate, times(1)).exchange(eq(baseUrl), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Void.class));
    }
}
