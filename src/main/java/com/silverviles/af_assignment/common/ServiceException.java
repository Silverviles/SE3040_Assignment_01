package com.silverviles.af_assignment.common;

public class ServiceException extends Exception {
    public ServiceException(ExceptionCode errorCode) {
        super(errorCode.getExceptionCode());
    }
}
