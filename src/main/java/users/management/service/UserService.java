package users.management.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Optional;
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

    @Transactional
    public void createUser(UserFormDTO userFormDTO, Address address, Company company) {
        if (userFormDTO.password().equals(userFormDTO.confirmationPassword())) {
            if (userRepository.findByEmail(userFormDTO.email()).isEmpty()) {
                User user = new User(userFormDTO, passwordEncoder.encode(userFormDTO.password()), address, company);
                userRepository.save(user);
            } else {
                throw new BadRequestException(String.format("User with email %s just exists.", userFormDTO.email()));
            }

        } else {
            throw new BadRequestException("Passwords are different.");
        }
    }

    @Transactional
    public void updateLastActivityUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setLastActivityAt(LocalDateTime.now());
            userRepository.save(user.get());
        } else {
            throw new BadRequestException(String.format("User with email %s just exists.", email));
        }
    }

    @Transactional
    public void updateUser(String email, UserFormDTO userFormDTO, Company company) throws BadRequestException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Address address = userFormDTO.addressFormDTO() != null ? user.getAddress().update(userFormDTO.addressFormDTO()) : null;
            user.update(userFormDTO, address, company);
            userRepository.save(user);
        } else {
            throw new NotFoundException(String.format("User with email %s address already exists.", email));
        }
    }

}
