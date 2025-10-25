package com.fiap.usu.services;

import com.fiap.usu.dtos.user.*;
import com.fiap.usu.entities.User;
import com.fiap.usu.entities.UserType;
import com.fiap.usu.exceptions.InvalidCurrentPasswordException;
import com.fiap.usu.exceptions.InvalidPasswordException;
import com.fiap.usu.exceptions.ResourceNotFoundException;
import com.fiap.usu.exceptions.UserAlreadyExistsException;
import com.fiap.usu.mappers.UserMapper;
import com.fiap.usu.repositories.UserRepository;
import com.fiap.usu.repositories.UserTypeRepository;
import com.fiap.usu.validations.SecurityValidator;
import com.fiap.usu.validations.ValidationMessages;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserPhoneService phoneService;
    private final UserAddressService userAddressService;
    private final UserTypeRepository userTypeRepository;
    private final SecurityValidator securityValidator;

    public UserService(
            UserRepository userRepository,
            UserPhoneService phoneService,
            UserAddressService userAddressService,
            UserTypeRepository userTypeRepository,
            SecurityValidator securityValidator
    ) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userRepository = userRepository;
        this.phoneService = phoneService;
        this.userAddressService = userAddressService;
        this.userTypeRepository = userTypeRepository;
        this.securityValidator = securityValidator;
    }

    @Transactional
    public Long create(UserCreateDto dto) {
        log.info("Iniciando criação do usuário: {}", dto.email());

        log.debug("Validação do usuário concluída para: {}", dto.email());

        User user = UserMapper.mapPersistenceEntity(dto);
        validadePersistUser(dto);
        Set<UserType> userTypes = getUserTypes(dto.idTypes());
        user.setTypes(userTypes);
        log.debug("Mapeamento do DTO para entidade concluído: {}", user);

        User saved = userRepository.save(user);
        log.info("Usuário salvo com ID: {}", saved.getId());

        userAddressService.persistUserAddresses(user, dto.addresses());
        phoneService.saveUserPhone(user, dto.phones());

        log.info("Criação do usuário finalizada com sucesso: {}", saved.getId());
        return saved.getId();
    }

    private void validadePersistUser(UserCreateDto dto) {
        // Apenas admin pode adicionar ADMIN e RESTAURANT_OWNER
        if (dto.idTypes().stream().anyMatch(t -> t.equals(1L) || t.equals(3L))) {
            securityValidator.validateUserAccess();
        }

        if (userRepository.existsByDocument(dto.document())) {
            throw new UserAlreadyExistsException(ValidationMessages.DOCUMENT_EXISTS);
        }

        if (userRepository.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException(ValidationMessages.EMAIL_EXISTS);
        }

        if (userRepository.existsByLogin(dto.login())) {
            throw new UserAlreadyExistsException(ValidationMessages.LOGIN_EXISTS);
        }

        if (isInvalidPassword(dto.password())) {
            throw new InvalidPasswordException();
        }

    }

    @Transactional
    @PreAuthorize("#id == principal.id or hasAnyRole('ADMIN')")
    public Long update(Long id, UserUpdateDto dto) {
        User user = userRepository.findByIdWithAssociations(id).orElseThrow(() -> new ResourceNotFoundException(ValidationMessages.USER_NOT_FOUND));
        validadeUpdateUser(user, dto);

        if (dto.idsTypes() != null) {
            Set<UserType> userTypes = getUserTypes(dto.idsTypes());
            user.getTypes().clear();
            user.getTypes().addAll(userTypes);
        }
        UserMapper.mapUpdateEntity(dto, user);
        User updated = userRepository.save(user);
        userAddressService.updateUserAddresses(user, dto.addresses());
        phoneService.updateUserPhone(user, dto.phones());
        return updated.getId();
    }

    private void validadeUpdateUser(User user, UserUpdateDto dto) {
        // Apenas admin pode adicionar ADMIN e RESTAURANT_OWNER
        if (nonNull(dto.idsTypes()) && dto.idsTypes().stream().anyMatch(t -> t.equals(1L) || t.equals(3L))) {
            securityValidator.validateUserAccess();
        }

        if (nonNull(dto.document()) && !dto.document().equals(user.getDocument())) {
            if (userRepository.existsByDocument(dto.document())) {
                throw new UserAlreadyExistsException(ValidationMessages.DOCUMENT_EXISTS);
            }
        }

        if (nonNull(dto.email()) && !dto.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.email())) {
                throw new UserAlreadyExistsException(ValidationMessages.EMAIL_EXISTS);
            }
        }

        if (nonNull(dto.login()) && !dto.login().equals(user.getLogin())) {
            if (userRepository.existsByLogin(dto.login())) {
                throw new UserAlreadyExistsException(ValidationMessages.LOGIN_EXISTS);
            }
        }

    }

    private Set<UserType> getUserTypes(Set<Long> userTypeIds) {
        List<UserType> userTypes = userTypeRepository.findAllById(userTypeIds);
        return Set.copyOf(userTypes);
    }

    @Transactional
    public void passwordUpdate(PasswordUpdateDto dto) {
        log.info("Iniciando atualização de senha para o email: {}", dto.email());
        User user = userRepository.findByEmail(dto.email()).orElseThrow(() -> {
            log.warn("Usuário não encontrado para email: {}", dto.email());
            return new ResourceNotFoundException(ValidationMessages.USER_NOT_FOUND);
        });

        securityValidator.validateUserAccess(user.getId());

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new InvalidCurrentPasswordException();
        }

        if (isInvalidPassword(dto.newPassword())) {
            log.warn("Senha atual inválida para usuário: {}", dto.email());
            throw new InvalidPasswordException();
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        log.info("Senha atualizada com sucesso para o usuário: {}", dto.email());
        userRepository.save(user);
    }

    private boolean isInvalidPassword(String password) {
        // Regex explicada:
        // (?=.*[0-9]) -> pelo menos um número
        // (?=.*[A-Z]) -> pelo menos uma letra maiúscula
        // (?=.*[@#$%^&+=!]) -> pelo menos um caractere especial
        // .{6,} -> no mínimo 6 caracteres
        String pattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,}$";
        return password == null || !password.matches(pattern);
    }

    @PreAuthorize("#id == principal.id or hasAnyRole('ADMIN','RESTAURANT_OWNER')")
    public UserDto getUserDto(Long id) {
        User user = userRepository.findByIdWithAssociations(id)
                .orElseThrow(() -> new ResourceNotFoundException(ValidationMessages.USER_NOT_FOUND));
        ;
        return UserMapper.mapUserDto(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ValidationMessages.USER_NOT_FOUND));
    }

    public User getUserByLoginOrEmail(String identifier) {
        return userRepository.findByLoginOrEmail(identifier)
                .orElseThrow(() -> new ResourceNotFoundException(ValidationMessages.USER_NOT_FOUND));
    }

    @Transactional
    @PreAuthorize("#id == principal.id or hasAnyRole('ADMIN')")
    public void delete(Long id) {
        log.info("Tentativa de deletar usuário com id={}", id);
        User user = getUser(id);
        userRepository.delete(user);
        log.info("Usuário com id={} deletado com sucesso", id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_OWNER')")
    public Page<UserPagedDto> getUsersPaged(Pageable pageable, String name) {
        return userRepository.getUsersPaged(pageable, name);
    }

}
