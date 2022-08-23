package com.biswas.arun.redis.redisapi.common.enums;

public enum SortOrderEnum {
    ASC(1),
    DESC(2);

    private final int order;

    private SortOrderEnum(int order) {
        this.order = order;
    }

    public int getSortOrder() {
        return this.order;
    }
}
