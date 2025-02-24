package org.acme.resource;

import java.util.NoSuchElementException;

import org.acme.dto.CreateCustomerDto;
import org.acme.dto.UpdateCustomerDto;
import org.acme.entity.CustomerEntity;
import org.acme.mapper.ICustomerMapper;
import org.acme.service.ICountryService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("api/customers")
@Produces("application/json")
@Consumes("application/json")
public class CustomerResource {
    
    @Inject
    ICountryService countryService;

    @Inject
    ICustomerMapper customerMapper;

    @POST
    @Transactional
    public Response create(@Valid CreateCustomerDto createCustomerDto){

        var demonym = countryService.GetDemonymCountry(createCustomerDto.countryCode());
        var customer = customerMapper.fromCreate(createCustomerDto, demonym);
        customer.persist();
        return Response.ok(customer).build();
    }

    @GET
    public Response getAll(){
        return Response.ok(CustomerEntity.listAll()).build();
    }

    @GET
    @Path("getCustomersByCountry")
    public Response GetCustomerByCountry(@QueryParam("country") String country){

        if(country == null || country.isEmpty()){
            throw new BadRequestException("Country value is not valid");
        }

        var customersByCountry = CustomerEntity.list("countryCode", country);
        return Response.ok(customersByCountry).build();
    }

    @GET   
    @Path("/getCustomerById/{id}") 
    public Response GetCustomerById(Long id){
        var customer = CustomerEntity.findById(id);
        if(customer == null){
            throw new NoSuchElementException("Customer not found");
        }

        return Response.ok(customer).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("{id}") Long id,@Valid UpdateCustomerDto updateCustomerDto){
        
        var existCustomer = CustomerEntity.findById(id);
        if(existCustomer == null){
            throw new NoSuchElementException("Customer not found");
        }

        var demonym = countryService.GetDemonymCountry(updateCustomerDto.countryCode());
        var customer = customerMapper.fromUpdate(id, updateCustomerDto, demonym);
        if(customer != null){
            customer.persist();
        }

        return Response.ok(updateCustomerDto).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id){
        boolean deleted = CustomerEntity.deleteById(id);
        if(!deleted){
            throw new NoSuchElementException("Customer not found");
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
