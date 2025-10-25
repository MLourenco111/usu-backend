package com.fiap.usu.services;

import com.fiap.usu.dtos.user.UserCreateAddressDto;
import com.fiap.usu.dtos.user.UserUpdateAddressDto;
import com.fiap.usu.entities.Address;
import com.fiap.usu.entities.User;
import com.fiap.usu.entities.UserAddress;
import com.fiap.usu.entities.UserAddressId;
import com.fiap.usu.exceptions.InvalidAddressException;
import com.fiap.usu.mappers.AddressMapper;
import com.fiap.usu.repositories.AddressRepository;
import com.fiap.usu.repositories.UserAddressRepository;
import com.fiap.usu.utils.AddressUtils;
import com.fiap.usu.utils.StringUtils;
import com.fiap.usu.validations.AddressValidationResult;
import com.fiap.usu.validations.AddressValidationService;
import com.fiap.usu.validations.ValidationMessages;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class UserAddressService {

    private final AddressValidationService addressValidationService;
    private final AddressRepository addressRepository;
    private final UserAddressRepository userAddressRepository;

    public UserAddressService(
            AddressValidationService addressValidationService,
            AddressRepository addressRepository,
            UserAddressRepository userAddressRepository
    ) {
        this.addressValidationService = addressValidationService;
        this.addressRepository = addressRepository;
        this.userAddressRepository = userAddressRepository;
    }

    @Transactional
    public void persistUserAddresses(User user, Set<UserCreateAddressDto> addressDto) {
        if (CollectionUtils.isEmpty(addressDto)) return;

        validatePrimaryAddress(addressDto);

        for (UserCreateAddressDto dto : addressDto) {
            Address address = validateAndGetAddress(dto);
            UserAddress ua = buildUserAddress(user, dto, address);
            userAddressRepository.save(ua);
            log.info("Endereço vinculado ao usuário {}: {}", user.getId(), ua);
        }
    }

    private void validatePrimaryAddress(Set<UserCreateAddressDto> addressDto) {
        long primaryCount = addressDto.stream().filter(UserCreateAddressDto::primary).count();
        if (primaryCount == 0) {
            throw new InvalidAddressException(ValidationMessages.ADDRESS_PRIMARY_REQUIRED);
        }

        if (primaryCount > 1) {
            throw new InvalidAddressException(ValidationMessages.ADDRESS_PRIMARY_ONLY_ONE);
        }
    }

    private Address validateAndGetAddress(UserCreateAddressDto dto) {
        AddressValidationResult result = addressValidationService.validateAddress(
                dto.zipCode(), dto.street(), dto.city(), dto.state(), dto.number()
        );
        if (!result.valid()) {
            throw new InvalidAddressException(result.errors());
        }

        return addressRepository.findByStreetAndNumberAndCityAndStateAndZipCode(
                StringUtils.capitalizeWords(dto.street()),
                StringUtils.safeUpper(dto.number()),
                StringUtils.capitalizeWords(dto.city()), StringUtils.safeUpper(dto.state()),
                AddressUtils.normalize(dto.zipCode())
        ).orElseGet(() -> {
            Address newAddress = AddressMapper.mapPersistenceEntity(dto);
            log.info("Criando novo endereço: {}", newAddress);
            return addressRepository.save(newAddress);
        });
    }

    @Transactional
    public void updateUserAddresses(User user, Set<UserUpdateAddressDto> addresses) {
        if (addresses != null && addresses.isEmpty()) {
            user.getAddresses().clear();
            return;
        } else if (CollectionUtils.isEmpty(addresses)) {
            return;
        }

        validatePrimaryAddressForUpdate(user, addresses);
        Map<String, UserAddress> currentUserAddresses = user.getAddresses().stream()
                .collect(Collectors.toMap(
                        ua -> generateAddressKey(ua.getAddress()),
                        ua -> ua
                ));

        Set<String> newAddressKeys = new HashSet<>();
        for (UserUpdateAddressDto dto : addresses) {
            Address address = validateAndGetAddress(dto);
            String key = generateAddressKey(address);
            newAddressKeys.add(key);

            UserAddress ua = currentUserAddresses.getOrDefault(key, buildUserAddress(user, dto, address));
            ua.setComplement(StringUtils.capitalizeWords(dto.complement()));
            ua.setAddressType(dto.addressType());
            ua.setPrimary(dto.primary());

            userAddressRepository.save(ua);
        }
        user.getAddresses().removeIf(ua -> !newAddressKeys.contains(generateAddressKey(ua.getAddress())));
    }

    private String generateAddressKey(Address address) {
        return String.join("|",
                address.getStreet().toLowerCase(),
                address.getNumber().toLowerCase(),
                address.getCity().toLowerCase(),
                address.getState().toLowerCase(),
                address.getZipCode().toLowerCase()
        );
    }

    private void validatePrimaryAddressForUpdate(User user, Set<UserUpdateAddressDto> dtos) {
        long primaryCount = user.getAddresses().stream()
                                    .filter(UserAddress::getPrimary)
                                    .filter(ua -> dtos.stream()
                                            .noneMatch(dto -> generateAddressKey(AddressMapper.mapUpdateEntity(dto)).equals(generateAddressKey(ua.getAddress())))
                                    )
                                    .count() + dtos.stream().filter(UserUpdateAddressDto::primary).count();

        if (primaryCount == 0) {
            throw new InvalidAddressException(ValidationMessages.ADDRESS_PRIMARY_REQUIRED);
        }

        if (primaryCount > 1) {
            throw new InvalidAddressException(ValidationMessages.ADDRESS_PRIMARY_ONLY_ONE);
        }
    }

    private Address validateAndGetAddress(UserUpdateAddressDto dto) {
        AddressValidationResult result = addressValidationService.validateAddress(
                dto.zipCode(), dto.street(), dto.city(), dto.state(), dto.number()
        );
        if (!result.valid()) {
            throw new InvalidAddressException(result.errors());
        }

        return addressRepository.findByStreetAndNumberAndCityAndStateAndZipCode(
                StringUtils.capitalizeWords(dto.street()),
                StringUtils.safeUpper(dto.number()),
                StringUtils.capitalizeWords(dto.city()), StringUtils.safeUpper(dto.state()),
                AddressUtils.normalize(dto.zipCode())
        ).orElseGet(() -> addressRepository.save(AddressMapper.mapUpdateEntity(dto)));
    }

    private <T> UserAddress buildUserAddress(User user, T dto, Address address) {
        UserAddress ua = new UserAddress();
        ua.setUser(user);
        ua.setAddress(address);
        ua.setAddressType((dto instanceof UserCreateAddressDto c) ? c.addressType() : ((UserUpdateAddressDto) dto).addressType());
        ua.setPrimary((dto instanceof UserCreateAddressDto c) ? c.primary() : ((UserUpdateAddressDto) dto).primary());
        ua.setComplement(StringUtils.capitalizeWords((dto instanceof UserCreateAddressDto c) ? c.complement() : ((UserUpdateAddressDto) dto).complement()));
        ua.setId(new UserAddressId(user.getId(), address.getId()));
        return ua;
    }
}
