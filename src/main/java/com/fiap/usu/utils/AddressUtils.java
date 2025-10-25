package com.fiap.usu.utils;

import java.text.Normalizer;

public class AddressUtils {
    private AddressUtils() {
    }

    /**
     * Normaliza uma string de endereço para comparação:
     * - remove espaços extras
     * - transforma em maiúsculas
     * - remove acentos
     * - remove caracteres especiais
     */
    public static String normalize(String input) {
        if (input == null) return null;

        String normalized = input.trim().toUpperCase();
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        normalized = normalized.replaceAll("[^A-Z0-9 ]", "");

        return normalized;
    }

    public static boolean validateNumber(String number) {
        if (number == null || number.isBlank())
            return false;
        return number.equalsIgnoreCase("S/N") || number.matches("\\d+");
    }
}
