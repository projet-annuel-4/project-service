package fr.esgi.projectservice.utils;

import java.util.Date;

public class DateTimeUtils {

    public static Date getDateNow() {

        long miliseconds = System.currentTimeMillis();
        return new Date(miliseconds);
    }
}
