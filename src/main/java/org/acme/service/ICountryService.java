package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface ICountryService {

    public String GetDemonymCountry(String country);
}
