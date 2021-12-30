package com.luxuryadmin.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static void main(String[] args) {
        System.out.println(com.luxuryadmin.common.utils.DateUtil.format(getTodayZero()));
    }

    public static Date getTodayZero() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }
}
