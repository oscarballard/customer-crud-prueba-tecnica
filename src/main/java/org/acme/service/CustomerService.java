package org.acme.service;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.acme.dto.CreateCustomerDto;
import org.acme.dto.UpdateCustomerDto;
import org.acme.entity.CustomerEntity;
import org.acme.mapper.ICustomerMapper;
import org.acme.repository.CustomerRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class CustomerService {
    

    @Inject
    CustomerRepository customerRepository;

    @Inject
    ICountryService countryService;

    @Inject
    ICustomerMapper customerMapper;

    public List<CustomerEntity> listAll() {
        return customerRepository.listAll();
    }

    public Optional<CustomerEntity> getById(Long id) {
        return Optional.ofNullable(customerRepository.findById(id));
    }

    public List<CustomerEntity> getCustomersByCountryCode(String countryCode) {
        return customerRepository.findByCountryCode(countryCode);
    }

    public Response create(CreateCustomerDto createCustomerDto) {
        
        var demonym = countryService.GetDemonymCountry(createCustomerDto.countryCode());
        var customer = customerMapper.fromCreate(createCustomerDto, demonym);

        if(customer != null){
            customer.persist();
            return Response.created(URI.create("/customers/" + customer.id)).entity(customer).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity("Customer could not be created").build();
    }

    public boolean delete(Long id) {
        return customerRepository.deleteById(id);
    }

    public Response update(Long id, UpdateCustomerDto updateCustomerDto) {
        var existCustomer = customerRepository.findById(id);  // Busca el cliente
        if (existCustomer == null) {
            throw new NoSuchElementException("Customer not found");
        }

        var demonym = countryService.GetDemonymCountry(updateCustomerDto.countryCode()); // Obtener demonym
        var customer = customerMapper.fromUpdate(id, updateCustomerDto, demonym); // Mapear DTO a entidad

        if (customer != null) {
            customer.persist(); // Guardar en la base de datos
        }

        return Response.ok(updateCustomerDto).build();
    }
}
