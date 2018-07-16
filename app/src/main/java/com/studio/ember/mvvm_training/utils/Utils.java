package com.studio.ember.mvvm_training.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class Utils {

    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public static long getDateMiliseconds(){
        return new GregorianCalendar().getTimeInMillis();
    }

    public static String getDateFormated(long dateMilliseconds){
        return format.format(new Date(dateMilliseconds));
    }
}
