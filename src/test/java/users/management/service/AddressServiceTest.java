package users.management.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import users.management.dto.AddressFormDTO;
import users.management.entity.Address;
import users.management.exception.NotFoundException;
import users.management.repository.AddressRepository;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    AddressService addressService;

    private static final AddressFormDTO ADDRESS_FROM_DTO = new AddressFormDTO(UUID.randomUUID(), "town", "postal-code", "215A", null, "city", "street");
    private static final Address ADDRESS = new Address(ADDRESS_FROM_DTO);

    @Test
    void getAddressByIdShouldReturn() {
        // when
        when(addressRepository.findById(any())).thenReturn(Optional.of(ADDRESS));

        // then
        Assertions.assertEquals(addressService.getById(UUID.randomUUID()).getId(), ADDRESS.getId());
    }

    @Test
    void getAddressByIdShouldThrowNotFoundException() {
        // when
        when(addressRepository.findById(any())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(NotFoundException.class, () -> addressService.getById(UUID.randomUUID()));
    }

    @Test
    void shouldCreateAddress() {
        // when
        when(addressRepository.save(any(Address.class))).thenReturn(ADDRESS);

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> addressService.create(ADDRESS_FROM_DTO)),
                () -> Assertions.assertEquals(ADDRESS.getCity(), addressService.create(ADDRESS_FROM_DTO).getCity()),
                () -> Assertions.assertEquals(ADDRESS.getTown(), addressService.create(ADDRESS_FROM_DTO).getTown()),
                () -> Assertions.assertEquals(ADDRESS.getHouseNumber(), addressService.create(ADDRESS_FROM_DTO).getHouseNumber())
        );
    }

}
