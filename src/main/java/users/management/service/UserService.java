package users.management.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import users.management.dto.AddressFormDTO;
import users.management.dto.UserDTO;
import users.management.dto.UserFormDTO;
import users.management.entity.Address;
import users.management.entity.Company;
import users.management.entity.User;
import users.management.exception.BadRequestException;
import users.management.exception.NotFoundException;
import users.management.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream().map(UserDTO::create).toList();
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByCompanyId(UUID companyId) {
        return userRepository.findAllByCompanyId(companyId).stream().map(UserDTO::create).toList();
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(String.format("User with email %s doesn't exists.", email)));
    }

    @Transactional
    public void create(UserFormDTO userFormDTO, Address address, Company company) {
        if (userFormDTO.password().equals(userFormDTO.confirmationPassword())) {
            if (userRepository.findByEmail(userFormDTO.email()).isEmpty()) {
                User user = new User(userFormDTO, passwordEncoder.encode(userFormDTO.password()), address, company);
                userRepository.save(user);
            } else {
                throw new BadRequestException(String.format("User with email %s already exists.", userFormDTO.email()));
            }

        } else {
            throw new BadRequestException("Passwords are different.");
        }
    }

    @Transactional
    public void updateLastActivityUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(String.format("User with email %s doesn't exists.", email)));
        user.setLastActivityAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void update(String email, UserFormDTO userFormDTO, Company company) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(String.format("User with email %s doesn't exists.", email)));
        Address address = updateUserAddress(user, userFormDTO.addressFormDTO());
        user.update(userFormDTO, address, company);
        userRepository.save(user);
    }

    private Address updateUserAddress(User user, AddressFormDTO addressFormDTO) {
        if (addressFormDTO != null) {
            if (user.getAddress() == null) return new Address(addressFormDTO);
            else return user.getAddress().update(addressFormDTO);
        } else return null;
    }

}
