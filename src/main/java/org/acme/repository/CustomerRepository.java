package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

import org.acme.entity.CustomerEntity;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<CustomerEntity> {

    public List<CustomerEntity> findByCountryCode(String countryCode) {
        return list("countryCode", countryCode); // Método correcto en PanacheRepository
    }

    public boolean deleteById(Long id) {
        return delete("id", id) > 0; // Método correcto en PanacheRepository
    }
}