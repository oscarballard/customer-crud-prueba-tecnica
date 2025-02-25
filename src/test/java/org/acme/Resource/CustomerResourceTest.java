package org.acme.Resource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.acme.dto.CreateCustomerDto;
import org.acme.dto.UpdateCustomerDto;
import org.acme.entity.CustomerEntity;
import org.acme.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class CustomerResourceTest {

    @InjectMock
    CustomerService customerService;

    @Test
    void testCreateCustomer_Success() {
        CreateCustomerDto dto = new CreateCustomerDto(
                "John", "Doe", "Smith", "Anderson",
                "john.doe@example.com", "123 Main St", "+123456789", "DOM"
        );

        Response mockResponse = Response.status(Response.Status.CREATED).build();
        when(customerService.create(any(CreateCustomerDto.class))).thenReturn(mockResponse);

        given()
            .contentType("application/json")
            .body(dto)
            .when().post("/api/customers")
            .then()
            .statusCode(201);
    }

    @Test
    void testCreateCustomer_InvalidEmail() {
        CreateCustomerDto dto = new CreateCustomerDto(
                "John", "Doe", "Smith", "Anderson",
                "invalid-email", "123 Main St", "+123456789", "US"
        );
    
        given()
                .contentType("application/json")
                .body(dto)
                .when().post("/api/customers")
                .then()
                .statusCode(400)
                .body("error", equalTo("Email must be in a valid format"));
    }

    @Test
    void testCreateCustomer_BlankFields() {
        CreateCustomerDto dto = new CreateCustomerDto(
                "", "", "", "", "", "", "", ""
        );

        given()
                .contentType("application/json")
                .body(dto)
                .when().post("/api/customers")
                .then()
                .statusCode(400)
                .body("error", anyOf(
                    equalTo("First name cannot be empty"),
                    equalTo("Last name cannot be empty"),
                    equalTo("Email cannot be empty"),
                    equalTo("Address cannot be empty"),
                    equalTo("Phone number cannot be empty"),
                    equalTo("Country code cannot be empty")
            ));
    }

    @Test
    void testCreateCustomer_InvalidCountryCode() {
        CreateCustomerDto dto = new CreateCustomerDto(
                "John", "Doe", "Smith", "Anderson",
                "john.doe@example.com", "123 Main St", "+123456789", "USA1"
        );

        given()
                .contentType("application/json")
                .body(dto)
                .when().post("/api/customers")
                .then()
                .statusCode(400)
                .body("error", equalTo("Country code must be an ISO 3166 code with 2 or 3 uppercase letters"));
    }

    @Test
    void testGetAllCustomers() {
        List<CustomerEntity> customers = List.of(new CustomerEntity());
        when(customerService.listAll()).thenReturn(customers);

        given()
                .when().get("/api/customers")
                .then()
                .statusCode(200)
                .body("$", hasSize(1));
    }

    @Test
    void testGetCustomerById_Found() {
        CustomerEntity customer = new CustomerEntity();
        when(customerService.getById(1L)).thenReturn(Optional.of(customer));

        given()
                .when().get("/api/customers/1")
                .then()
                .statusCode(200);
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerService.getById(999L)).thenReturn(Optional.empty());

        given()
                .when().get("/api/customers/999")
                .then()
                .statusCode(404)
                .body("error", equalTo("Customer not found"));
    }

    @Test
    void testUpdateCustomer_Success() {
        UpdateCustomerDto dto = new UpdateCustomerDto(
                "jane.doe@example.com", "456 Elm St", "+987654321", "CA"
        );

        Response mockResponse = Response.ok().build();
        when(customerService.update(any(Long.class), any(UpdateCustomerDto.class))).thenReturn(mockResponse);

        given()
                .contentType("application/json")
                .body(dto)
                .when().put("/api/customers/1")
                .then()
                .statusCode(200);
    }

    @Test
    void testUpdateCustomer_InvalidData() {
        UpdateCustomerDto dto = new UpdateCustomerDto(
                "invalid-email", "", "", "INVALID"
        );

        given()
                .contentType("application/json")
                .body(dto)
                .when().put("/api/customers/1")
                .then()
                .statusCode(400)
                .body("error", anyOf(
                    equalTo("Email must be in a valid format"),
                    equalTo("Address cannot be empty"),
                    equalTo("Phone number cannot be empty"),
                    equalTo("Country code must be an ISO 3166 code with 2 or 3 uppercase letters")
            ));
    }

    @Test
    void testDeleteCustomer_Success() {
        when(customerService.delete(1L)).thenReturn(true);

        given()
                .when().delete("/api/customers/1")
                .then()
                .statusCode(204);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(customerService.delete(999L)).thenReturn(false);

        given()
                .when().delete("/api/customers/999")
                .then()
                .statusCode(404)
                .body(equalTo("Customer not found"));
    }
}
