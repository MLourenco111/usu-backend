package com.fiap.usu.services;

import com.fiap.usu.dtos.user.UserCreatePhoneDto;
import com.fiap.usu.dtos.user.UserUpdatePhoneDto;
import com.fiap.usu.entities.Phone;
import com.fiap.usu.entities.User;
import com.fiap.usu.repositories.PhoneRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserPhoneService {

    private final PhoneRepository phoneRepository;

    public UserPhoneService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Transactional
    public void saveUserPhone(User user, Set<UserCreatePhoneDto> phonesDto) {
        for (UserCreatePhoneDto dto : phonesDto) {
            String normalizedNumber = normalize(dto.number());
            log.info("Processando telefone '{}' para o usuário: {}", normalizedNumber, user.getId());

            phoneRepository.findByUserAndNumber(user.getId(), normalizedNumber).orElseGet(() -> {
                log.info("Telefone não encontrado, criando novo: {}", normalizedNumber);
                Phone phone = new Phone();
                phone.setUser(user);
                phone.setNumber(normalizedNumber);
                phone.setType(dto.type());
                Phone saved = phoneRepository.save(phone);
                log.info("Telefone salvo: {} para o usuário: {}", saved.getNumber(), user.getId());
                return saved;
            });
        }
    }

    @Transactional
    public void updateUserPhone(User user, Set<UserUpdatePhoneDto> phonesDto) {
        if (phonesDto != null && phonesDto.isEmpty()) {
            user.getPhones().clear();
            return;
        } else if (CollectionUtils.isEmpty(phonesDto)) {
            return;
        }

        Map<Long, Phone> existingPhones = user.getPhones().stream()
                .collect(Collectors.toMap(Phone::getId, Function.identity()));

        Set<Phone> updatedPhones = new HashSet<>();
        for (UserUpdatePhoneDto dto : phonesDto) {
            Phone phone;
            if (dto.id() != null && existingPhones.containsKey(dto.id())) {
                phone = existingPhones.get(dto.id());
            } else {
                phone = new Phone();
                phone.setUser(user);
            }
            phone.setNumber(normalize(dto.number()));
            phone.setType(dto.type());

            updatedPhones.add(phone);
        }
        user.getPhones().clear();
        user.getPhones().addAll(updatedPhones);
    }

    // Remove espaços, traços, parênteses e outros caracteres não numéricos
    private String normalize(String number) {
        if (number == null)
            return "";
        return number.replaceAll("[^0-9SNsn]", "");
    }

    /*
     * TODO criar validacao a partir da localizacao do usuario
     */

}