package com.foodorder.dto;

import com.foodorder.entity.Address;

public class AddressOptionDTO {
    private String id;
    private String fullAddress;

    public AddressOptionDTO() {
    }

    public static AddressOptionDTO fromEntity(Address address) {
        AddressOptionDTO dto = new AddressOptionDTO();
        dto.setId(address.getId());
        dto.setFullAddress(buildFullAddress(address));
        return dto;
    }

    private static String buildFullAddress(Address address) {
        String street = address.getStreet() != null ? address.getStreet().trim() : "";
        String ward = address.getWard() != null ? address.getWard().trim() : "";
        String city = address.getCity() != null ? address.getCity().trim() : "";

        StringBuilder sb = new StringBuilder();
        if (!street.isEmpty()) {
            sb.append(street);
        }
        if (!ward.isEmpty()) {
            if (!sb.isEmpty()) {
                sb.append(", ");
            }
            sb.append(ward);
        }
        if (!city.isEmpty()) {
            if (!sb.isEmpty()) {
                sb.append(", ");
            }
            sb.append(city);
        }
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}