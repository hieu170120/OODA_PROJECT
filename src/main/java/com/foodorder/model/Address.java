package com.foodorder.model;

public class Address {
    private String addressId;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private User user;

    public Address() {
    }

    public Address(String addressId, String street, String city, String postalCode, String country, User user) {
        this.addressId = addressId;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.user = user;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
