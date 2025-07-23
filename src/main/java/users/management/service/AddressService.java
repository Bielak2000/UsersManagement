package users.management.service;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import users.management.dto.AddressFormDTO;
import users.management.entity.Address;
import users.management.exception.NotFoundException;
import users.management.repository.AddressRepository;

import java.util.UUID;

@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public Address getById(UUID addressID) {
        return addressRepository.findById(addressID).orElseThrow(() -> new NotFoundException(String.format("Address with id %s not found", addressID)));
    }

    @Transactional
    public Address create(AddressFormDTO addressFormDTO) {
        Address address = new Address(addressFormDTO);
        addressRepository.save(address);
        return address;
    }

}
