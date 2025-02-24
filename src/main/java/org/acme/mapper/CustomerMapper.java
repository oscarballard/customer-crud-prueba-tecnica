package org.acme.mapper;

import org.acme.dto.CreateCustomerDto;
import org.acme.dto.UpdateCustomerDto;
import org.acme.entity.CustomerEntity;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class CustomerMapper implements ICustomerMapper {
    
    @Override
    public CustomerEntity fromCreate(CreateCustomerDto createCustomerDto, String demonym){

        var customerEntity = new CustomerEntity();
        customerEntity.setFirstName(createCustomerDto.firstName());
        customerEntity.setMiddleName(createCustomerDto.firstName());
        customerEntity.setLastName(createCustomerDto.lastName());
        customerEntity.setSecondLastName(createCustomerDto.secondLastName());        


        customerEntity.setEmail(createCustomerDto.email());
        customerEntity.setAddress(createCustomerDto.address());
        customerEntity.setPhone(createCustomerDto.phone());
        customerEntity.setCountryCode(createCustomerDto.countryCode());
        customerEntity.setDemonym(demonym);

        return customerEntity;
    }

    @Override
    public CustomerEntity fromUpdate(Long id, UpdateCustomerDto updateCustomerDto, String demonym){
        var entity = CustomerEntity.findById(id);
        var customer = CustomerEntity.class.cast(entity);
        if(customer == null) {
            return null;
        }
        
        customer.setEmail(updateCustomerDto.email());
        customer.setAddress(updateCustomerDto.address());
        customer.setPhone(updateCustomerDto.phone());
        customer.setCountryCode(updateCustomerDto.countryCode());
        customer.setDemonym(demonym);

        return customer;
    }
}
