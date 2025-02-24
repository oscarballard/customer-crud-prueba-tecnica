package org.acme.mapper;

import org.acme.dto.CreateCustomerDto;
import org.acme.dto.UpdateCustomerDto;
import org.acme.entity.CustomerEntity;

public interface ICustomerMapper {
    CustomerEntity fromCreate(CreateCustomerDto createCustomerDto, String demonym);
    CustomerEntity fromUpdate(Long id,UpdateCustomerDto updateCustomerDto, String demonym);
}
