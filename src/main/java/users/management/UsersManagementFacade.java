package users.management;

import lombok.AllArgsConstructor;
import users.management.dto.AddressFormDTO;
import users.management.dto.CompanyDTO;
import users.management.dto.CompanyFormDTO;
import users.management.dto.UserDTO;
import users.management.dto.UserFormDTO;
import users.management.dto.UserSettingsFormDTO;
import users.management.entity.Address;
import users.management.entity.Company;
import users.management.entity.User;
import users.management.service.AddressService;
import users.management.service.CompanyService;
import users.management.service.UserService;
import users.management.service.UserSettingsService;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class UsersManagementFacade {

    private CompanyService companyService;
    private AddressService addressService;
    private UserService userService;
    private UserSettingsService userSettingsService;

    public void createCompany(CompanyFormDTO companyFormDTO) {
        Address address = addressService.create(companyFormDTO.addressFormDTO());
        companyService.create(companyFormDTO, address);
    }

    public void createUser(UserFormDTO userFormDTO) {
        Address address = createAddressForUser(userFormDTO.addressFormDTO());
        Company company = userFormDTO.companyID() != null ? companyService.getById(userFormDTO.companyID()) : null;
        userService.create(userFormDTO, address, company);
    }

    public void updateCompany(UUID companyID, CompanyFormDTO companyFormDTO) {
        companyService.update(companyID, companyFormDTO);
    }

    public void updateUser(String email, UserFormDTO userFormDTO) {
        Company company = userFormDTO.companyID() != null ? companyService.getById(userFormDTO.companyID()) : null;
        userService.update(email, userFormDTO, company);
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

    public void updateLastActivityUser(String email) {
        userService.updateLastActivityUser(email);
    }

    public void updateUserSettings(String email, UserSettingsFormDTO userSettingsFormDTO) {
        User user = userService.getUserByEmail(email);
        userSettingsService.updateUserSettings(user, userSettingsFormDTO);
    }

    private Address createAddressForUser(AddressFormDTO addressFormDTO) {
        if (addressFormDTO != null) {
            if (addressFormDTO.addressID() != null) return addressService.getById(addressFormDTO.addressID());
            else return new Address(addressFormDTO);
        } else return null;
    }

}
