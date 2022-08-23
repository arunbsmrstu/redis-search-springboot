package com.biswas.arun.redis.redisapi.common.utils;

import java.text.SimpleDateFormat;

public class Defs {
    public static final int DEFAULT_LIMIT=10;
    public static final Integer YES=1;
    public static final Integer No=0;
    public static final String DATE_FORMAT = "dd-MM-yyyy"; //dd-MM-yyyy
    public static final String DATE_FORMAT_2 = "yyyy-MM-ddTHH:mm:ssZ";
    public static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static String DATE_TIME_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String OPERATION_SUCCESSFUL = "Successful";
    public static final String OPERATION_FAILED = "FAILED";
    public static SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final boolean WRITE_SQL = true;
    public static SimpleDateFormat viewDate = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat viewDateANDTime= new SimpleDateFormat("dd/MM/yyyy hh:mm a");
    public static final String ES_INSTANT_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static final String YearWise = "yyyy";
    public static final String MonthWise = "yyyy-Month";
    public static final String ES_DATE_FORMAT = "yyyy-MM-dd";
    public static final String OLD_DATE_FORMAT = "MM/dd/yyyy";
}
