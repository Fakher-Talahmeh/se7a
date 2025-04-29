package org.health.se7a.util;

import java.util.Date;

public class DateUtil {

    final static Integer SEC_IN_MIN = 60;
    final static Integer MS_IN_SEC = 1000;
    final static Integer MIN_IN_HOUR = 60;
    final static Integer HOUR_IN_DAY = 24;

    private DateUtil() {
    }

    public static Date now() {
        return new Date(System.currentTimeMillis());
    }

    public static Date expAt(final Long expirationInMs) {
        return new Date(System.currentTimeMillis() + expirationInMs);
    }

    public static Long convertMinToMs(final Long minutes) {
        return minutes * SEC_IN_MIN * MS_IN_SEC;
    }

    public static Long convertDaysToMs(final Long days) {
        return days * HOUR_IN_DAY * MIN_IN_HOUR * SEC_IN_MIN * MS_IN_SEC;
    }
}
