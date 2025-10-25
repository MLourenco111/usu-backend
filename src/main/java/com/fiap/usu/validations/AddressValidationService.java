package com.fiap.usu.validations;

import com.fiap.usu.dtos.ViaCepResponse;
import com.fiap.usu.utils.AddressUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressValidationService {

    private final RestTemplate restTemplate;
    private final String urlValidador = "https://viacep.com.br/ws/{cep}/json/";

    public AddressValidationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AddressValidationResult validateAddress(String cep, String street, String city, String state, String number) {
        List<String> errors = new ArrayList<>();

        // valida CEP + endereço
        ViaCepResponse response = null;
        try {
            response = restTemplate.getForObject(urlValidador, ViaCepResponse.class, cep);
        } catch (Exception e) {
            errors.add(ValidationMessages.ADDRESS_CEP_INVALID);
        }

        if (response == null || response.error() != null) {
            errors.add(ValidationMessages.ADDRESS_CEP_INVALID);
        } else {
            if (!AddressUtils.normalize(response.street()).equals(AddressUtils.normalize(street))) {
                errors.add(ValidationMessages.ADDRESS_STREET_INVALID);
            }
            if (!AddressUtils.normalize(response.city()).equals(AddressUtils.normalize(city))) {
                errors.add(ValidationMessages.ADDRESS_CITY_INVALID);
            }
            if (!AddressUtils.normalize(response.state()).equals(AddressUtils.normalize(state))) {
                errors.add(ValidationMessages.ADDRESS_STATE_INVALID);
            }
        }

        if (!AddressUtils.validateNumber(number)) {
            errors.add(ValidationMessages.ADDRESS_NUMBER_INVALID);
        }

        return errors.isEmpty() ? AddressValidationResult.ok() : AddressValidationResult.fail(errors);
    }

}
