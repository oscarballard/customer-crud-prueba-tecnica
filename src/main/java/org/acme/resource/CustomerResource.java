package org.acme.resource;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.acme.dto.CreateCustomerDto;
import org.acme.dto.UpdateCustomerDto;
import org.acme.entity.CustomerEntity;
import org.acme.mapper.ICustomerMapper;
import org.acme.service.CustomerService;
import org.acme.service.ICountryService;

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

    @POST
    @Transactional
    public Response create(@Valid CreateCustomerDto createCustomerDto){
        return customerService.create(createCustomerDto);
    }

    @GET
    public Response getAll(){
        return Response.ok(customerService.listAll()).build();
    }

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

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id,@Valid UpdateCustomerDto updateCustomerDto) {
        return customerService.update(id, updateCustomerDto);
    }

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
