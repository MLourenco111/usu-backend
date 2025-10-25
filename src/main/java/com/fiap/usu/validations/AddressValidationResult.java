package com.fiap.usu.validations;

import java.util.ArrayList;
import java.util.List;

public record AddressValidationResult(boolean valid, List<String> errors) {

    public static AddressValidationResult ok() {
        return new AddressValidationResult(true, new ArrayList<>());
    }

    public static AddressValidationResult fail(List<String> errors) {
        return new AddressValidationResult(false, errors);
    }

}
