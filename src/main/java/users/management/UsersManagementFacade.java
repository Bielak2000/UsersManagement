package users.management;

import lombok.AllArgsConstructor;
import users.management.dto.AddressFormDTO;
import users.management.dto.ChangePasswordDTO;
import users.management.dto.CompanyDTO;
import users.management.dto.CompanyFormDTO;
import users.management.dto.ResetPasswordDTO;
import users.management.dto.UserDTO;
import users.management.dto.UserFormDTO;
import users.management.dto.UserLoginDTO;
import users.management.dto.UserSettingsFormDTO;
import users.management.dto.UserUpdateFormDTO;
import users.management.entity.Address;
import users.management.entity.Company;
import users.management.entity.User;
import users.management.exception.UnauthorizedException;
import users.management.service.AddressService;
import users.management.service.CompanyService;
import users.management.service.EmailSenderService;
import users.management.service.UserService;
import users.management.service.UserSettingsService;
import users.management.service.VerificationTokenService;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class UsersManagementFacade {

    private final CompanyService companyService;
    private final AddressService addressService;
    private final UserService userService;
    private final UserSettingsService userSettingsService;
    private final VerificationTokenService verificationTokenService;
    private final EmailSenderService emailSenderService;

    public void createCompany(CompanyFormDTO companyFormDTO) {
        Address address = addressService.create(companyFormDTO.addressFormDTO());
        companyService.create(companyFormDTO, address);
    }

    public User createUser(UserFormDTO userFormDTO) {
        Address address = createAddressForUser(userFormDTO.addressFormDTO());
        Company company = userFormDTO.companyID() != null ? companyService.getById(userFormDTO.companyID()) : null;
        User user = userService.create(userFormDTO, address, company);
        String verificationToken = verificationTokenService.create(user);
        emailSenderService.verificationAccount(user.getEmail(), String.format("%s %s", user.getName(), user.getSurname()), verificationToken, user.getId());
        return user;
    }

    public void updateCompany(UUID companyID, CompanyFormDTO companyFormDTO) {
        companyService.update(companyID, companyFormDTO);
    }

    public User updateUser(UUID userID, UserUpdateFormDTO userUpdateFormDTO) {
        Company company = userUpdateFormDTO.companyID() != null ? companyService.getById(userUpdateFormDTO.companyID()) : null;
        return userService.update(userID, userUpdateFormDTO, company);
    }

    public void updateLastActivityUser(UUID userID) {
        userService.updateLastActivityUser(userID);
    }

    public void updateUserSettings(UUID userID, UserSettingsFormDTO userSettingsFormDTO) {
        User user = userService.getUserById(userID);
        userSettingsService.updateUserSettings(user, userSettingsFormDTO);
    }

    public List<CompanyDTO> getCompanies() {
        return companyService.getCompanies();
    }

    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    public List<UserDTO> getUsersByCompanyId(UUID companyId) {
        return userService.getUsersByCompanyId(companyId);
    }

    public UserLoginDTO getUserLoginData(UUID userID) {
        return userService.getUserLoginDataByUserID(userID);
    }

    public void changeUserPassword(UUID userID, ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(userID, changePasswordDTO);
    }

    public void activateUser(UUID userID, String token) {
        boolean validToken = verificationTokenService.isValidToken(token, userID);
        if (validToken) {
            userService.enableUser(userID);
        } else {
            throw new UnauthorizedException(String.format("The token %s for user %s has expired", token, userID));
        }
    }

    public void sendResetUserPasswordEmail(UUID userID) {
        User user = userService.getUserById(userID);
        String verificationToken = verificationTokenService.create(user);
        emailSenderService.resetPassword(user.getEmail(), String.format("%s %s", user.getName(), user.getSurname()), verificationToken, user.getId());
    }

    public void resetUserPassword(UUID userID, ResetPasswordDTO resetPasswordDTO) {
        boolean validToken = verificationTokenService.isValidToken(resetPasswordDTO.token(), userID);
        if (validToken) {
            userService.resetPassword(userID, resetPasswordDTO);
        } else {
            throw new UnauthorizedException(String.format("The token %s for user %s to reset password has expired", resetPasswordDTO.token(), userID));
        }
    }

    public User getUserById(UUID userID) {
        return userService.getUserById(userID);
    }

    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    private Address createAddressForUser(AddressFormDTO addressFormDTO) {
        if (addressFormDTO != null) {
            if (addressFormDTO.addressID() != null) return addressService.getById(addressFormDTO.addressID());
            else return new Address(addressFormDTO);
        } else return null;
    }

}
