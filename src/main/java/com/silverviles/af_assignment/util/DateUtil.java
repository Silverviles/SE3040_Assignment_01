package com.silverviles.af_assignment.util;

import com.silverviles.af_assignment.common.ExceptionCode;
import com.silverviles.af_assignment.common.ServiceException;

import java.time.LocalDate;

public class DateUtil {
    public static boolean isDateValid(String date) {
        return date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    public static LocalDate convertStringToDate(String date) throws ServiceException {
        if (!isDateValid(date)) {
            throw new ServiceException(ExceptionCode.INVALID_DATE_FORMAT);
        }
        return LocalDate.parse(date);
    }
}
