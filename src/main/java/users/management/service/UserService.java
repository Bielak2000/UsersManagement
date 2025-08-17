package users.management.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import users.management.dto.AddressFormDTO;
import users.management.dto.ResetPasswordDTO;
import users.management.dto.UserDTO;
import users.management.dto.UserFormDTO;
import users.management.dto.UserLoginDTO;
import users.management.dto.UserUpdateFormDTO;
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

    private final String activeProfile;
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
    public User getUserById(UUID userID) {
        return userRepository.findById(userID).orElseThrow(() -> new NotFoundException(String.format("User with id %s doesn't exists.", userID)));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(String.format("User with email %s doesn't exists.", email)));
    }

    @Transactional
    public void enableUser(UUID userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new NotFoundException(String.format("User with id %s doesn't exists.", userID)));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public User create(UserFormDTO userFormDTO, Address address, Company company) {
        if (userFormDTO.password().equals(userFormDTO.confirmationPassword())) {
            if (userRepository.findByEmail(userFormDTO.email()).isEmpty()) {
                User user = new User(userFormDTO, passwordEncoder.encode(userFormDTO.password()), address, company, activeProfile.equals("mock-mail"));
                userRepository.save(user);
                return user;
            } else {
                throw new BadRequestException(String.format("User with email %s already exists.", userFormDTO.email()));
            }
        } else {
            throw new BadRequestException("Passwords are different.");
        }
    }

    @Transactional
    public void updateLastActivityUser(UUID userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new NotFoundException(String.format("User with ID %s doesn't exists.", userID)));
        user.setLastActivityAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public User update(UUID userID, UserUpdateFormDTO userUpdateFormDTO, Company company) {
        User user = userRepository.findById(userID).orElseThrow(() -> new NotFoundException(String.format("User with ID %s doesn't exists.", userID)));
        Address address = updateUserAddress(user, userUpdateFormDTO.addressFormDTO());
        user.update(userUpdateFormDTO, address, company);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserLoginDTO getUserLoginDataByUserID(UUID userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new NotFoundException(String.format("User with ID %s doesn't exists.", userID)));
        return UserLoginDTO.create(user);
    }

    @Transactional
    public void changePassword(UUID userID, users.management.dto.ChangePasswordDTO changePasswordDTO) {
        User user = userRepository.findById(userID).orElseThrow(() -> new NotFoundException(String.format("User with id %s not found.", userID)));
        if (passwordEncoder.matches(changePasswordDTO.oldPassword(), user.getPassword())) {
            if (changePasswordDTO.newPassword().equals(changePasswordDTO.confirmationPassword())) {
                user.setPassword(passwordEncoder.encode(changePasswordDTO.newPassword()));
                userRepository.save(user);
            } else {
                throw new BadRequestException("The password are not the same.");
            }
        } else {
            throw new BadRequestException("The wrong old password.");
        }
    }

    @Transactional
    public void resetPassword(UUID userID, ResetPasswordDTO resetPasswordDTO) {
        User user = userRepository.findById(userID).orElseThrow(() -> new NotFoundException(String.format("User with id %s not found.", userID)));
        if (resetPasswordDTO.password().equals(resetPasswordDTO.confirmationPassword())) {
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.password()));
            userRepository.save(user);
        } else {
            throw new BadRequestException("The password are not the same.");
        }
    }

    private Address updateUserAddress(User user, AddressFormDTO addressFormDTO) {
        if (addressFormDTO != null) {
            if (user.getAddress() == null) return new Address(addressFormDTO);
            else return user.getAddress().update(addressFormDTO);
        } else return null;
    }

}
