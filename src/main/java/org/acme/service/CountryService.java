package org.acme.service;

import java.util.NoSuchElementException;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class CountryService implements ICountryService {

    @Inject 
    @RestClient
    ICountryApiClient countryApiClient;

    @Override 
    public String GetDemonymCountry(String countryCode) {
        try {
            String countryResponse = countryApiClient.getCountryByCode(countryCode);

            if(countryResponse == null || countryResponse.isEmpty()) {
                throw new NoSuchElementException("The country with the following code was not found: " + countryCode);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(countryResponse);
            JsonNode demonymsNode = rootNode.get(0).get("demonyms");
            String englishDemomyn = demonymsNode.get("eng").get("f").asText();
            return englishDemomyn;
        } catch (Exception e) {
            return "Country not found";
        }
    }
}
