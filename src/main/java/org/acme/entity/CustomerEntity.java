package org.acme.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
@Entity()
@Table(name = "customers")
public class CustomerEntity extends PanacheEntity {

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(nullable = true)
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = true)
    private String secondLastName;

    @NotBlank(message = "Email is required")
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank(message = "Address is required")
    @Column(name = "address", nullable = false)
    private String address;

    @NotBlank(message = "Phone is required")
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotBlank(message = "Country code is required")
    @Pattern(regexp = "^[A-Z]{2,3}$", message = "The country code must be a 2 or 3 letter ISO 3166 code")
    private String countryCode;

    @Column(nullable = true)
    private String demonym;


    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDemonym() {
        return demonym;
    }

    public void setDemonym(String demonym) {
        this.demonym = demonym;
    }
}
