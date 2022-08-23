package com.biswas.arun.redis.redisapi.common.enums;

public enum ErrorCode {
    GENERAL_ERROR,
    REQUIRED_PARAMETER_MISSING,
    REQUIRED_PARAMETER_INVALID,
    DUPLICATE_ENTRY,
    AUTHENTICATION_ERROR,
    SERVICE_ERROR,
    UNSPECIFIED_ERROR,
    SERVICE_NOT_IMPLEMENTED,
    DATA_NOT_FOUND;

    public static ErrorCode getValue(int index) {
        try {
            return values()[index];
        } catch (Exception ex) {
            return GENERAL_ERROR;
        }
    }
}
