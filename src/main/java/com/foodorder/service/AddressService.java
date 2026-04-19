package com.foodorder.service;

import com.foodorder.dto.AddressOptionDTO;
import com.foodorder.entity.Address;
import com.foodorder.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<AddressOptionDTO> getAddressOptions(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            return Collections.emptyList();
        }

        return addressRepository.findByCustomerIdOrderByIdDesc(customerId).stream()
                .map(AddressOptionDTO::fromEntity)
                .toList();
    }

    public String resolveCheckoutAddress(String customerId, String selectedAddressId, String typedAddress) {
        if (selectedAddressId != null && !selectedAddressId.isBlank()) {
            Address selectedAddress = addressRepository.findByIdAndCustomerId(selectedAddressId, customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Dia chi da chon khong hop le."));
            return AddressOptionDTO.fromEntity(selectedAddress).getFullAddress();
        }

        String normalizedTypedAddress = typedAddress != null ? typedAddress.trim() : "";
        if (normalizedTypedAddress.isBlank()) {
            throw new IllegalArgumentException("Vui long chon dia chi hoac nhap dia chi giao hang.");
        }
        return normalizedTypedAddress;
    }
}