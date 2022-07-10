package com.example.projectservicev.utils;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Date;

public class DateTimeUtils {

    public static Date getDateNow(){

        long miliseconds = System.currentTimeMillis();
        return new Date(miliseconds);
    }
}
