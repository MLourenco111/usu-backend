package com.fiap.usu.utils;

public class StringUtils {
    private StringUtils() {
    }

    public static String safeLower(String str) {
        return str == null ? null : str.toLowerCase();
    }

    public static String safeUpper(String str) {
        return str == null ? null : str.toUpperCase();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isBlank();
    }

    public static String capitalizeWords(String str) {
        if (str == null || str.isBlank()) {
            return str;
        }
        String[] words = str.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    sb.append(word.substring(1));
                }
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

}
