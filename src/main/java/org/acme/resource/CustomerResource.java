package org.acme.resource;

import java.util.List;
import org.acme.dto.CreateCustomerDto;
import org.acme.dto.UpdateCustomerDto;
import org.acme.entity.CustomerEntity;
import org.acme.mapper.ICustomerMapper;
import org.acme.service.CustomerService;
import org.acme.service.ICountryService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("api/customers")
@Produces("application/json")
@Consumes("application/json")
public class CustomerResource {
    
    @Inject
    ICountryService countryService;

    @Inject
    ICustomerMapper customerMapper;

    @Inject 
    CustomerService customerService;


    @Operation(summary = "Create a new customer", 
           description = "Adds a new customer")
    @POST
    @Transactional
    public Response create(@Valid CreateCustomerDto createCustomerDto){
        return customerService.create(createCustomerDto);
    }

    @Operation(summary = "Get all customers", 
    description = "Retrieves a list of all registered customers.")
    @GET
    public Response getAll(){
        return Response.ok(customerService.listAll()).build();
    }

    @Operation(summary = "Get customers by country", 
           description = "Retrieves customers filtered by country.")
    @GET
    @Path("getCustomersByCountry/{countryCode}")
    public Response getCustomersByCountryCode(@PathParam("countryCode") String countryCode) {
        List<CustomerEntity> customers = customerService.getCustomersByCountryCode(countryCode);
        if (customers.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No customers found for country code: " + countryCode)
                    .build();
        }
        return Response.ok(customers).build();
    }

    @Operation(summary = "Get customer by ID", 
           description = "Fetches customer details by ID. Returns 404 if not found.")
    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") Long id) {
        var customer = customerService.getById(id);
        
        if (customer.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Customer not found\"}")
                    .build();
        }
        
        return Response.ok(customer).build();
    }


    @Operation(summary = "Update a customer", 
           description = "Updates an existing customer by ID. Returns 404 if not found.")
    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id,@Valid UpdateCustomerDto updateCustomerDto) {
        return customerService.update(id, updateCustomerDto);
    }


    @Operation(summary = "Delete a customer", 
           description = "Removes a customer by ID. Returns 404 if not found.")
    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = customerService.delete(id);
        if (deleted) {
            return Response.noContent().build(); // 204 si se eliminó correctamente
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
    }

    
}
