package com.fiap.usu.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String formatInstant(Instant instant) {
        return instant != null
                ? instant.atZone(ZoneId.of("America/Sao_Paulo")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                : "-";
    }
}
