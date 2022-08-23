package com.biswas.arun.redis.redisapi.common.utils;

import com.biswas.arun.redis.redisapi.common.annotation.ToUpper;
import com.biswas.arun.redis.redisapi.common.enums.ErrorCode;
import com.biswas.arun.redis.redisapi.common.payload.ServiceError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {
    public static final Integer RECORD_STATUS_ONE = 1;
    public static final String OPERATION_SUCESS = "SUCCESS";
    public static final String OPERATION_FAILED = "FAILED";
    public static final String INVALID_CODE = "INVALID_CODE";
    public static final String TIME_OUT = "TIME_OUT";
    public static final Integer DEFAULT_LIMIT = 100;

    public static void copyBean(final Object from, final Object to) {
        Map<String, Field> fromFields = analyze(from);
        Map<String, Field> toFields = analyze(to);
        fromFields.keySet().retainAll(toFields.keySet());
        for (Map.Entry<String, Field> fromFieldEntry : fromFields.entrySet()) {
            final String name = fromFieldEntry.getKey();
            final Field sourceField = fromFieldEntry.getValue();
            final Field targetField = toFields.get(name);
            if (targetField.getType().isAssignableFrom(sourceField.getType())) {
                sourceField.setAccessible(true);
                if (Modifier.isFinal(targetField.getModifiers())) {
                    continue;
                }
                targetField.setAccessible(true);
                try {
                    Object ob = sourceField.getType().equals(String.class)
                            && sourceField.isAnnotationPresent(ToUpper.class)
                            && sourceField.get(from) != null
                            && ((String) sourceField.get(from)).trim() != "" ?
                            ((String) sourceField.get(from)).trim().toUpperCase() : sourceField.get(from);
                    targetField.set(to, ob);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Can't access field!");
                }
            }
        }
    }

    private static Map<String, Field> analyze(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        Map<String, Field> map = new TreeMap<String, Field>();
        Class<?> current = object.getClass();
        while (current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    if (!map.containsKey(field.getName())) {
                        map.put(field.getName(), field);
                    }
                }
            }
            current = current.getSuperclass();
        }

        return map;
    }

    public static Integer getIntegerFromObject(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Integer) {
            return ((Integer) object).intValue();
        } else if (object instanceof Short) {
            return ((Short) object).intValue();
        } else if (object instanceof Long) {
            return ((Long) object).intValue();
        } else if (object instanceof BigDecimal) {
            return ((BigDecimal) object).intValue();
        } else if (object instanceof BigInteger) {
            return ((BigInteger) object).intValue();
        } else if (object instanceof Byte) {
            return new Integer((Byte) object);
        }else if (object instanceof Double) {
            return ((Double) object).intValue();
        } else if(object instanceof String) {
            try {
                return Integer.parseInt((String) object);
            } catch (Throwable t) {
                return 0;
            }
        } else {
            return 0;
        }
    }


    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    /**
     * @return Timestamp object of current date and time
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static long getElapsedTimeFromNow(Timestamp fromTime) {
        return System.currentTimeMillis() - fromTime.getTime();
        //Timestamp currentTimestamp = getCurrentTimestamp();
        //return currentTimestamp.getTime() - fromTime.getTime();
    }

    public static String getNewUuid() {
        return UUID.randomUUID().toString();
    }


    public static String getFormattedDate(String format, Timestamp t) {
        if (t == null) {
            return "";
        }

        if (isEmpty(format)) {
            format = "dd/MM/yyyy";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(t.getTime()));
    }

    public static Timestamp getStringToTimestamp(String stringDate) {
        Timestamp dateAsTimestamp = null;
        try {
            DateFormat formatter;
            Date date;
            formatter = new SimpleDateFormat(Defs.DATE_TIME_FORMAT_STR);
            date = formatter.parse(stringDate);
            dateAsTimestamp = new Timestamp(date.getTime());
        } catch (Exception x) {
            x.printStackTrace();
        }
        return dateAsTimestamp;
    }

    public static String getDateAsString(Date givenDate) {

        String dateAsString = "";
        try {
            SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("dd/MM/yyyy");
            StringBuilder nowMMDDYYYY = new StringBuilder(dateformatMMDDYYYY.format(givenDate));
            dateAsString = nowMMDDYYYY.toString();
        } catch (Exception x) {
            x.printStackTrace();
        }
        return dateAsString;
    }


    public static String getDateToString(Date date) {
        if (date == null) {
            return "00-00-0000";
        }

        return (new SimpleDateFormat(Defs.DATE_FORMAT)).format(date);
    }

    public static String getDateStringToString(String date) {
        if (date == null) {
            return "00-00-0000";
        }

        return (new SimpleDateFormat(Defs.DATE_FORMAT_2)).format(date);
    }

    public static String getDateToFormatString(Date date, String pattern) {
        if (date == null || pattern == null) {
            return "00-00=-0000";
        }

        return (new SimpleDateFormat(pattern)).format(date);
    }

    public static boolean isLeapYear(int y) {
        if (y % 400 == 0) {
            return true;
        }
        if (y % 100 == 0) {
            return false;
        }
        return y % 4 == 0;
    }

    public static String getTimestampToString(Timestamp ts) {
        if (ts == null) {
            return "00-00-0000";
        }

        return (new SimpleDateFormat(Defs.DATE_FORMAT)).format(new Date(ts.getTime()));
    }

    public static boolean isDateEqual(Date d1, Date d2) {
        if (d1 == null && d2 == null) {
            return true;
        }
        if (d1 == null || d2 == null) {
            return false;
        }

        return getDateToString(d1).equalsIgnoreCase(getDateToString(d2));
    }

    public static boolean isDateEqual(Date d, Timestamp t) {
        if (d == null && t == null) {
            return true;
        }
        if (d == null || t == null) {
            return false;
        }

        return getDateToString(d).equalsIgnoreCase(getTimestampToString(t));
    }

    public static boolean isDateEqual(Timestamp t, Date d) {
        return isDateEqual(d, t);
    }

    public static boolean isDateEqual(Timestamp t1, Timestamp t2) {
        return getTimestampToString(t1).equalsIgnoreCase(getTimestampToString(t2));
    }

    public static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isInList(String s, List<String> args, boolean ignoreCase) {
        if (s == null) {
            return false;
        }

        if (Utils.isEmptyStringList(args)) {
            return false;
        }

        for (String arg : args) {
            if (arg == null) {
                continue;
            }

            if (ignoreCase && arg.equalsIgnoreCase(s)) {
                return true;
            } else if (arg.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInList(String s, Object... args) {
        if (s == null) {
            return false;
        }

        if (args.length < 1) {
            return false;
        }

        for (int i = 0; i < args.length; i++) {

            System.out.println(args[i]);
            if (args[i] == null) {
                continue;
            }

            if (args[i].toString().equals(s)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInList(Integer l, Integer... args) {
        if (l == null || l == 0L) {
            return false;
        }

        if (args.length < 1) {
            return false;
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i] == null || ((Integer) args[i] == 0L)) {
                continue;
            }

            if (((Integer) args[i]).equals(l)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInListIgnoreCase(String s, Object... args) {
        if (s == null) {
            return false;
        }
        if (args.length < 1) {
            return false;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                continue;
            }

            if (args[i].toString().equalsIgnoreCase(s)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInList(Long s, Long... args) {
        if (s == null) {
            return false;
        }
        if (args.length < 1) {
            return false;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                continue;
            }
            if (args[i].longValue() == s.longValue()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInList(Short s, Short... args) {
        if (s == null) {
            return false;
        }
        if (args.length < 1) {
            return false;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                continue;
            }
            if (args[i].shortValue() == s.shortValue()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmpty(String s) {
        return ((s == null) || s.isEmpty());
    }

    public static boolean isEmpty(List list) {
        return ((list == null) || list.isEmpty());
    }

    public static boolean isNull(Short value) {

        if (value == null) {
            return true;
        }

        if (value <= 0) {
            return true;
        }

        return false;
    }

    public static boolean isNull(Float value) {

        if (value == null) {
            return true;
        }

//        if (value <= 0) {
//            return true;
//        }
        return false;
    }

    public static boolean isNull(BigInteger value) {

        if (value == null) {
            return true;
        }

//        if (value.toString().equals("0")) {
//            return true;
//        }
        return false;
    }

    public static boolean isNull(Long value) {
        if (value == null) {
            return true;
        }

        return false;
    }

    public static boolean isNull(byte[] byteData) {
        if (byteData == null || byteData.length == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNull(Integer integer) {
        if (integer == null) {
            return true;
        }
        return false;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNull(BigDecimal number) {
        if (number == null) {
            return true;
        }
        return false;
    }

    public static Long getLong(Object s) {
        if (s == null || s.toString().length() < 1) {
            return null;
        }

        try {
            Long l = Long.parseLong(s.toString());
            return l;
        } catch (Exception e) {
            return null;
        }
    }

    public static long differenceBetweenDatesInYear(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        long year = Math.abs(date2.getTime() / 1000 - date1.getTime() / 1000) / (365 * 24 * 60 * 60);
        return year;
    }

    public static long differenceBetweenDatesInYear(Date date) {
        long presentDate = new Date().getTime();
        long pastDate = date.getTime();
        long year = (presentDate / 1000 - pastDate / 1000) / (365 * 24 * 60 * 60);
        return year;
    }

    /**
     * add provided days with provided date
     */
    public static Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    /**
     * add provided days with provided date
     */
    public static Date addYears(Date date, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, years);
        return c.getTime();
    }

    /**
     * find the difference between two dates
     */
    public static boolean dateDifference(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            return false;
        }

        long diffInDays = (endDate.getTime() - startDate.getTime());
        diffInDays /= (1000 * 60 * 60 * 24);
        diffInDays /= (30 * 12);
        System.out.println(diffInDays);

        if (diffInDays < 18) {
            return false;
        }
        return true;
    }

    public static int getAge(Date dateOfBirth) {
        if (dateOfBirth == null) {
            return 0;
        }

        Date now = new Date();

        if (dateOfBirth.after(now)) {
            return 0;
        }

        int year = (int) ((now.getTime() / 1000 - dateOfBirth.getTime() / 1000) / (60 * 60 * 24 * 365));

        return year;
    }

    public static int checkAge(Date dateOfBirth, Date date) {
        if (dateOfBirth == null) {
            return 0;
        }

        if (date == null) {
            date = new Date();
        }

        if (dateWithoutTime(dateOfBirth).after(dateWithoutTime(date))) {
            return 0;
        }

        int year = (int) ((date.getTime() / 1000 - dateOfBirth.getTime() / 1000) / (60 * 60 * 24 * 365));

        return year;
    }

    public static int getDifferenceInDays(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }

        int year = (int) ((date1.getTime() / 1000 - date2.getTime() / 1000) / (60 * 60 * 24));

        return year;
    }

    public static int getAbsoluteDifferenceInDays(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }

        int year = (int) ((date1.getTime() / 1000 - date2.getTime() / 1000) / (60 * 60 * 24));

        return Math.abs(year);
    }

    public static boolean compareLong(Long value1, Long value2) {
        if (value1 == null && value2 == null) {
            return true;
        }
        if (value1 == null || value2 == null) {
            return false;
        }
        if (value1.compareTo(value2) == 0) {
            return true;
        }
        return false;
    }

    public static String buildAgeComparQuery(String colName, String compare, Long age) {
        if (Utils.isEmpty(colName) || Utils.isEmpty(compare) || age == null || age.equals(0L)) {
            return "";
        }

        return " trunc((months_between(sysdate, " + colName + "))/12) " + compare + " " + age + " AND ";
    }

    public static String buildDateCompareQuery(String colName, String compare, Date date) {
        if (!Utils.isEmpty(colName) && !Utils.isEmpty(compare) && date != null) {
            return " TRUNC(" + colName + ") >= TO_DATE('" + getDateToString(date) + "','yyyy-mm-dd') AND ";
        }

        return "";
    }

    public static String buildDatesCompareQuery(String colName, String compare, Date date) {
        if (!Utils.isEmpty(colName) && !Utils.isEmpty(compare) && date != null) {
            return " TRUNC(" + colName + ") " + compare + " TO_DATE('" + getDateToString(date) + "','yyyy-mm-dd') AND ";
        }

        return "";
    }

    public static String buildEqualQuery(String colName, String value) {
        if (Utils.isEmpty(colName) || Utils.isEmpty(value)) {
            return "";
        }

        return " AND UPPER(" + colName + ") = '" + value.toUpperCase() + "' ";
    }

    public static String buildEqualQuery(String colName, Long value) {
        if (Utils.isEmpty(colName) || value == null || value.equals(0L)) {
            return "";
        }

        return " AND " + colName + " = " + value + " ";
    }

    public static String buildEqualQuery(String colName, Integer value) {
        if (Utils.isEmpty(colName) || value == null || value.equals(0)) {
            return "";
        }

        return " AND " + colName + " = " + value + " ";
    }

    public static String buildLikeQuery(String colName, String value) {
        if (!Utils.isEmpty(colName) && !Utils.isEmpty(value)) {
            value = value.replaceAll("'", "");
            return " UPPER(" + colName + ") LIKE '%" + value.toUpperCase() + "%' AND ";
        }

        return "";
    }

    public static String buildMultiValueQuery(String colName, List<String> values) {
        if (isEmpty(colName) || values == null || values.size() == 0) {
            return "";
        }

        String query = "";
        boolean flag = false;
        for (String value : values) {
            value = value.replaceAll("'", "");
            if (!Utils.isEmpty(value)) {
                if (flag) {
                    query += ",";
                }
                flag = true;
                query += "'" + value.toUpperCase() + "'";
            }
        }
        if (Utils.isEmpty(query)) {
            return "";
        }

        return " UPPER(" + colName + ") IN(" + query + ") AND ";
    }

    public static String buildCompareQuery(String colName, String compare, Long value) {
        if (Utils.isEmpty(colName) || value == null || value.equals(0L) || Utils.isEmpty(compare)) {
            return "";
        }

        return " " + colName + " " + compare + " " + value.toString() + " AND ";
    }

    public static Object compareDates(Date date1, Date date2) {
        try {
            return dateWithoutTime(date1).compareTo(dateWithoutTime(date2));
        } catch (Exception e) {
            return null;
        }
    }

    public static Object compareWithCurrentDate(Date date) {
        try {
            return dateWithoutTime(date).compareTo(dateWithoutTime(new Date()));
        } catch (Exception e) {
            return null;
        }
    }

    public static Date dateWithoutTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static int checkDobEligibility(Date dateOfBirth, Date date) {
        if (dateOfBirth == null) {
            return 0;
        }

        if (date == null) {
            date = new Date();
        }

        try {
            Calendar dob = Calendar.getInstance();
            dob.setTime(dateWithoutTime(dateOfBirth));
            Calendar today = Calendar.getInstance();
            today.setTime(dateWithoutTime(date));

            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

            if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
                age--;
            } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
                age--;
            }
            return age;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static Timestamp getTimestampFromDate(Date date) {
        if (date == null) {
            return getCurrentTimestamp();
        }
        return new Timestamp(date.getTime());
    }

    public static boolean indexValueExists(Object[] objecta, int index) {
        if (objecta == null) {
            return false;
        }
        return objecta.length >= (index + 1) && objecta[index] != null;
    }

    public static String listToStringOfIds(List<Integer> list) {
        if (list == null || list.size() < 1) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i));
            result.append(",");
        }
        return result.substring(0, result.length() - 1);
    }

    public static String listToStringOfLongs(List<Long> list) {
        if (list == null || list.size() < 1) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i));
            result.append(",");
        }
        return result.substring(0, result.length() - 1);
    }

    public static String listToStringOfCodes(List<String> list) {
        if (list == null || list.size() < 1) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i));
            result.append(",");
        }
        return result.substring(0, result.length() - 1);
    }

    public static Timestamp dateToTimeStamp(Date date) {
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }

    public static Date getDateParam(Date date, boolean maxTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (maxTime) {
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
        return calendar.getTime();
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(date);
    }

    public static String formatDateAndTime(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh.mm.ss aa");
        return df.format(date);
    }

    public static Integer getKeyFromMap(String val, HashMap<Integer, String> HASH_MAP) {

        if (val == null || HASH_MAP == null || HASH_MAP.size() == 0) {
            return 0;
        }

        for (Map.Entry<Integer, String> e : HASH_MAP.entrySet()) {
            if (e == null) {
                continue;
            }

            if (e.getValue().equalsIgnoreCase(val.trim())) {

                return e.getKey();
            }
        }

        return 0;
    }

    public static String buildOrderByQuery(String columnName, Integer order) {
        if (isEmpty(columnName) || order == null) {
            return "";
        }

        String orderBy = "DESC";
        if (order.intValue() > 0) {
            orderBy = "ASC";
        }

        return " ORDER BY " + columnName + " " + orderBy;
    }

    public static String buildOrderByQuery(String columnName, Integer order, boolean isOrderBy) {
        if (isEmpty(columnName) || order == null) {
            return "";
        }

        String orderBy = "DESC";
        if (order.intValue() > 0) {
            orderBy = "ASC";
        }
        if (isOrderBy) {
            return " ORDER BY (" + columnName + ") " + orderBy;
        } else {
            return columnName + " " + orderBy;
        }
    }

    public static String getBarCode(List<BigDecimal> idList, String code, String source, String destination) {
        BigDecimal id = null;
        if (idList == null || idList.size() < 0) {
            return "";
        } else {
            id = idList.get(0);
        }
        String barcode = (code == null ? "" : code);
        if (!isEmpty(source)) {
            if (source.length() < 3) {
                source = "0" + source;
            }
            barcode += source;
        }
        if (!isEmpty(destination)) {
            if (destination.length() < 3) {
                destination = "0" + destination;
            }
            barcode += destination;
        }
        Calendar calendar = Calendar.getInstance();
        int day, month, year;
        calendar.setTime(new Date());
        day = calendar.get(Calendar.DATE);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        if (day < 10) {
            barcode += "0" + day;
        } else {
            barcode += day;
        }
        month += 1;
        if (month < 10) {
            barcode += "0" + month;
        } else {
            barcode += month;
        }

        barcode += year;

        barcode = padBarcode(barcode, id);

        return barcode;
    }

    private static String padBarcode(String barcode, BigDecimal id) {
        if (Utils.isNull(id)) {
            return barcode;
        }
        BigDecimal bd = id.stripTrailingZeros();
        int idLength = bd.precision() - bd.scale();
        String pad = idLength == 1 ? "00" : idLength == 2 ? "0" : "";
        barcode += pad + id;

        return barcode;
    }

    public static boolean isEmptyObjectList(List<Object> objectList) {
        if (objectList == null || objectList.size() <= 0) {
            return true;
        }

        int sizeOfList = objectList.size();
        int countEmpty = 0;

        for (Object object : objectList) {
            if (object == null) {
                countEmpty++;
            }
        }

        if (countEmpty == sizeOfList) {
            return true;
        }

        return false;
    }

    public static boolean isEmptyStringList(List<String> stringList) {
        if (stringList == null || stringList.size() <= 0) {
            return true;
        }

        int sizeOfList = stringList.size();
        int countEmpty = 0;

        for (String str : stringList) {
            if (str == null) {
                countEmpty++;
            }
        }

        if (countEmpty == sizeOfList) {
            return true;
        }

        return false;
    }

    public static boolean isEmptyLongList(List<Long> intList) {
        if (intList == null || intList.size() <= 0) {
            return true;
        }

        int sizeOfList = intList.size();
        int countEmpty = 0;

        for (Long intVal : intList) {
            if (intVal == null) {
                countEmpty++;
            }
        }

        if (countEmpty == sizeOfList) {
            return true;
        }

        return false;
    }

    public static Object[] buildInQuery(String columnName, List<Object> valueList, List<Object> objectList) {
        if (columnName == null || isEmptyObjectList(valueList)) {
            return null;
        }
        if (objectList == null) {
            objectList = new ArrayList<Object>();
        }
        String query = " AND " + columnName + " IN (";
        for (int i = 0; i < valueList.size(); i++) {
            query += "?";
            if (i != valueList.size() - 1) {
                query += ",";
            }
            objectList.add(valueList.get(i));
        }
        query += ") ";

        Object obj[] = new Object[2];
        obj[0] = query;
        obj[1] = objectList;

        return obj;

    }

    public static Object[] buildNotInQuery(String columnName, List<Object> valueList, List<Object> objectList) {
        if (columnName == null || isEmptyObjectList(valueList)) {
            return null;
        }

        String query = " AND " + columnName + " NOT IN(";
        for (int i = 0; i < valueList.size(); i++) {
            query += "?";
            if (i != valueList.size() - 1) {
                query += ",";
            }
            objectList.add(valueList.get(i));
        }
        query += ")";

        Object obj[] = new Object[2];
        obj[0] = query;
        obj[1] = objectList;

        return obj;

    }

    public static Object[] buildInQuery(List<String> valueList, List<Object> objectList) {

        if (isEmptyStringList(valueList)) {
            return null;
        }

        String query = "";
        for (int i = 0; i < valueList.size(); i++) {
            if (!isEmpty(valueList.get(i))) {
                query += "?";
                if (i != valueList.size() - 1) {
                    query += ",";
                }
                objectList.add(valueList.get(i));
            }

        }

        Object obj[] = new Object[2];
        obj[0] = query;
        obj[1] = objectList;

        return obj;
    }

    public static Object[] buildInQueryOfLong(List<Long> valueList, List<Object> objectList) {

        if (isEmptyLongList(valueList)) {
            return null;
        }

        String query = "";
        for (int i = 0; i < valueList.size(); i++) {
            if (!isNull(valueList.get(i))) {
                query += "?";
                if (i != valueList.size() - 1) {
                    query += ",";
                }
                objectList.add(valueList.get(i));
            }

        }

        Object obj[] = new Object[2];
        obj[0] = query;
        obj[1] = objectList;

        return obj;
    }

    public static Object[] buildInQuery(List<String> valueList, List<Object> objectList, boolean isUpper) {

        if (isEmptyStringList(valueList)) {
            return null;
        }

        String query = "";
        for (int i = 0; i < valueList.size(); i++) {
            if (!isEmpty(valueList.get(i))) {
                query += "?";
                if (i != valueList.size() - 1) {
                    query += ",";
                }
                objectList.add(valueList.get(i).trim().toUpperCase());
            }

        }

        Object obj[] = new Object[2];
        obj[0] = query;
        obj[1] = objectList;

        return obj;

    }

    public static Long getLong(Integer value) {
        if (isNull(value)) {
            return 0L;
        }
        try {
            return new Long(value);
        } catch (Throwable t) {
            return 0L;
        }
    }

    public static List<Object> getIdListInt(List<Integer> values) {
        if (values == null || values.size() <= 0) {
            return null;
        }

        List<Object> valueList = new ArrayList<Object>();
        for (Integer id : values) {
            if (isNull(id)) {
                continue;
            }
            valueList.add(id);
        }
        return valueList;
    }

    public static List<Object> getIdList(List<Long> values) {
        if (values == null || values.size() <= 0) {
            return null;
        }

        List<Object> valueList = new ArrayList<Object>();
        for (Long id : values) {
            if (isNull(id)) {
                continue;
            }
            valueList.add(id);
        }
        return valueList;
    }

    public static List<Long> getIdList(List<BigDecimal> values, boolean isLong) {
        if (values == null || values.size() <= 0) {
            return null;
        }

        List<Long> valueList = new ArrayList<Long>();
        for (BigDecimal id : values) {
            if (isNull(id)) {
                continue;
            }
            valueList.add(id.longValue());
        }
        return valueList;
    }

    public static String toUpper(String value, boolean isTrim) {
        if (isEmpty(value)) {
            return null;
        }

        if (isTrim) {
            return value.trim().toUpperCase();
        }

        return value.toUpperCase();
    }

    public static String convertIdNameMapToSqlString(HashMap<Long, String> map, String column) {

        if (Utils.isEmpty(column) || map == null || map.size() <= 0) {
            return null;
        }

        String str = "";
        try {
            for (Long key : map.keySet()) {
                if (isNull(key) || isEmpty(map.get(key))) {
                    continue;
                }

                str += " when " + column + " = " + key + " then '" + map.get(key) + "' ";
            }
        } catch (Throwable t) {
            System.err.print(t);
            return null;
        }
        if (!Utils.isEmpty(str)) {
            return " case " + str + " end ";
        }

        return null;
    }

    /**
     * if anyone is null, then not equal.
     *
     * @param first
     * @param second
     * @return
     */
    public static boolean isEqual(String first, String second, boolean ignoreCase) {
        if (isEmpty(first) && isEmpty(second)) {
            return true;
        }
        if (isEmpty(first) || isEmpty(second)) {
            return false;
        }
        if (ignoreCase) {
            return first.equalsIgnoreCase(second);
        }
        return first.equals(second);
    }

    public static boolean isEqual(Integer first, Integer second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }

        return (first.intValue() == second.intValue());
    }

    public static boolean isEqual(byte[] first, byte[] second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }

        return isEqual(md5(first), md5(second), false);
    }

    public static String md5(byte[] arr) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(arr);
            String md5Str = new BigInteger(digest.digest()).toString(16);
            return md5Str;
        } catch (NoSuchAlgorithmException | NullPointerException e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static boolean isEqual(Long first, Long second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }

        return (first.intValue() == second.intValue());
    }

    public static boolean isEqual(Short first, Short second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }

        return (first.shortValue() == second.shortValue());
    }

    public static boolean isEqual(BigDecimal first, BigDecimal second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }

        return (first.compareTo(second) == 0 ? true : false);
    }

    public static boolean isEqual(BigInteger first, BigInteger second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }

        return (first.compareTo(second) == 0 ? true : false);
    }

    public static Integer getInteger(Long value) {
        if (value == null) {
            return null;
        }
        try {
            return value.intValue();
        } catch (Exception e) {

        }

        return 0;
    }


    public static Integer getInteger(Object obj){
        if(obj instanceof String){
            try{
                String str= (String) obj;
                Integer no=Integer.parseInt(str);
                return no;
            }catch (Exception e){
                return null;
            }
        }else if(obj instanceof Long){
            Long data= (Long) obj;
            return data.intValue();
        }
        else if(obj instanceof BigInteger){
            BigInteger data= (BigInteger) obj;
            return data.intValue();
        }
        return null;
    }

    public static boolean isStringListEmpty(List<String> list) {
        if (list == null || list.size() <= 0) {
            return true;
        }
        for (String string : list) {
            if (!isEmpty(string)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isLongListEmpty(List<Long> list) {
        if (list == null || list.size() <= 0) {
            return true;
        }
        for (Long l : list) {
            if (l != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean isBigDecimalListEmpty(List<Long> list) {
        if (list == null || list.size() <= 0) {
            return true;
        }
        for (Long l : list) {
            if (l != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean isObjectListEmpty(List<Object> list) {
        if (list == null || list.size() <= 0) {
            return true;
        }
        for (Object l : list) {
            if (l != null) {
                return false;
            }
        }
        return true;
    }

    public static List<String> getListFromString(String value) {
        List<String> list = new ArrayList<String>();
        if (isEmpty(value)) {
            return list;
        }

        StringTokenizer st = new StringTokenizer(value, ",");
        while (st.hasMoreTokens()) {
            String str = st.nextToken();
            if (!isEmpty(str)) {
                list.add(str);
            }
        }

        return list;
    }

    public static Long getLongFromObject(Object object) {
        Long l = null;
        if (object != null) {
            l = ((BigDecimal) object).longValue();
        }

        return l;
    }

    public static String getMobileNumberFormat(Integer mobile) {
        if (Utils.isNull(mobile)) {
            return "";
        }

        return String.format("%011d", mobile);
    }

//    public static boolean isInList(int num, IntStream intStream) {
//        if (intStream == null || intStream.count() <= 0L) {
//            return false;
//        }
//        OptionalInt result = intStream.filter(value -> value == num).findFirst();
//        if (result.isPresent()) {
//            return true;
//        }
//
//        return false;
//    }
    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    public static List<Long> uniqueIntegerList(List<Long> list) {
        if (list == null) {
            return new ArrayList<Long>();
        }

        HashSet<Long> hashSet = new HashSet<Long>();
        hashSet.addAll(list);
        list.clear();
        list.addAll(hashSet);
        return list;
    }

    public static String[] generateKey(int numberOfKey, boolean isIncludeSign) {
        String[] keys = new String[numberOfKey];
        try {
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            for (int key = 0; key < numberOfKey; key++) {
                String license = "";
                String randomNum = new Integer(prng.nextInt()).toString();
                MessageDigest sha = MessageDigest.getInstance("SHA-1");
                byte[] result = sha.digest(randomNum.getBytes());
                StringBuilder resultbuilder = new StringBuilder();

                char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
                for (int idx = 0; idx < result.length; ++idx) {
                    byte b = result[idx];
                    resultbuilder.append(digits[(b & 0xf0) >> 4]);
                    resultbuilder.append(digits[b & 0x0f]);
                }

                if (isIncludeSign) {
                    if (resultbuilder.length() > 0) {
                        for (int i = 0; i < resultbuilder.length(); i++) {
                            license += resultbuilder.charAt(i);
                            if (i != 0 && i % 10 == 0) {
                                license += "-";
                            }
                        }
                        license = license.toUpperCase();
                    }
                } else {
                    license = resultbuilder.toString().toUpperCase();
                }
                keys[key] = license;
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            keys = null;
        }
        return keys;
    }

    public static boolean isEntity(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(String.class);
        ret.add(Date.class);

        return ret;
    }

    /*
        public static String prettyPrintJsonString(String string) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(string);
            String prettyJsonString = gson.toJson(je);

            return prettyJsonString;
        }
     */
    public static Object getInstance(Class<?> clazz) {
        try {
            //Class<?> clazz = Class.forName(className);
            Constructor<?> ctor = clazz.getConstructor();
            Object object = ctor.newInstance(new Object[]{});

            return object;
        } catch (Throwable t) {
            System.err.println(t.getMessage());
        }

        return null;
    }

    public static Class<?> getGenericClassFromCollection(Field field) {
        ParameterizedType listType = (ParameterizedType) field.getGenericType();
        Class<?> clazz = (Class<?>) listType.getActualTypeArguments()[0];

        return clazz;
    }

    public static String getJSON(Object o) throws Throwable {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(o);
        } catch (Throwable e) {
            throw e;
        }
    }

    public static Date getDateFromString(String date, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.parse(date);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }

    public static String dateToString(Date date, String format) {
        if (date == null) {
            date = new Date();
        }
        if (format == null) {
            format = "yyyy-MM-dd";
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(date);
        } catch (Throwable t) {
            System.err.println(t.getMessage());
            return null;
        }
    }

    public static byte[] getObjectToByte(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static Object getByteToObject(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    public static Integer getCurrentUserFromContext(Object context) {
        return 1;
    }

    public static int getListSize(List list) {
        if (Utils.isEmpty(list)) {
            return 0;
        }

        return list.size();
    }

    public static ServiceError throwableError(Throwable t) {
        return new ServiceError(ErrorCode.SERVICE_ERROR.ordinal(), t != null && t.getMessage() != null
                ? t.getMessage() : "Please contact with system administrator for further information about error");
    }

    public static boolean isValidDirectory(String directory) {
        File file = new File(directory);
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }

        return true;
    }

    public static String escapeString(String value) {
        if (Utils.isEmpty(value)) {
            return value;
        }
        value = StringEscapeUtils.escapeJson(value);
//        value = StringEscapeUtils.escapeHtml3(value);
//        value = StringEscapeUtils.escapeEcmaScript(value);
        return value;
    }


    public static boolean isEqualObjects(Object o, Object o1, Class<?> type) {
        if (o == null && o1 == null) {
            return true;
        }
        if (o == null || o1 == null) {
            return false;
        }
        if (type.equals(Date.class)) {
            return isDateEqual((Date) o, (Date) o1);
        } else if (type == byte[].class) {
            return isEqual((byte[]) o, (byte[]) o1);
        } else {
            return isEqual(String.valueOf(o), String.valueOf(o1), true);
        }
    }

    public static <T> T toUpperCase(T t, boolean escape) {
        if (t == null) {
            return t;
        }
        try {
            for (Field field : t.getClass().getDeclaredFields()) {
                if (field.getType().equals(String.class)) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    if (field.isAnnotationPresent(ToUpper.class) && field.get(t) != null && ((String) field.get(t)).trim() != "") {
                        String val = ((String) field.get(t)).trim().toUpperCase();
                        field.set(t, escape ? Utils.escapeString(val) : val);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    public static Date getDateFromGregorian(XMLGregorianCalendar calendar) {
        try {
            return calendar.toGregorianCalendar().getTime();
        } catch (Exception ex) {
        }
        return null;
    }

    public static XMLGregorianCalendar getGregorianFromDate(Date date) {
        XMLGregorianCalendar xmlDate = null;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);

        try {
            xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlDate;
    }

    public static int getCurrentYear() {
        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int year = now.get(Calendar.YEAR);
        return year;
    }

    public static int getCurrentMonth() {
        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int year = now.get(Calendar.MONTH);
        return year;
    }


    public static String getStackTrace(Throwable t) {
        if (t == null) {
            return "Error Message Not Found";
        }
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static String getDOBFromAge(Integer age, String pattern) {
        if (Utils.isNull(age) || Utils.isNull(pattern)) {
            return null;
        }

        LocalDate now = LocalDate.now();
        LocalDate dob = now.minusYears(age);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

            return dob.format(formatter);

        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static boolean isOk(Integer value) {
        return !(value == null || value.intValue() <= 0);
    }
    public static boolean isOk(Date value) {
        return !(value == null);
    }

    public static boolean isOk(Short value) {
        return !(value == null || value <= 0);
    }

    public static boolean isOk(Long value) {
        return !(value == null || value.longValue() <= 0);
    }

    public static boolean isOk(BigInteger value) {
        return !(value == null || value.intValue() <= 0);
    }

    public static boolean isOk(String str) {
        return !(str == null || str.isEmpty());
    }

    public static Date getStringToDate(String date, String format) {
        if (date == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(format);

        try {
            return df.parse(date.trim());
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }


    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }

        DateFormat df = new SimpleDateFormat(Defs.DATE_FORMAT);
        return df.format(date);
    }

    public static String getReportStringFromDate(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date getStringToDate(String date) {
        if (date == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(Defs.DATE_FORMAT);

        try {
            return df.parse(date);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public static final String[] getValueFromRequest(String paramName, HttpServletRequest req) {
        if (isOk(paramName) && req != null) {
            return req.getParameterValues(paramName.trim());
        }
        return null;
    }

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR" };

    public static String getClientIpAddress(HttpServletRequest request) {
        if (request == null) return "No IP Found";
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    public static String withLargeIntegers(double value) {
        NumberFormat df = NumberFormat.getInstance();
        df.setGroupingUsed(true);
        return df.format(value);
    }

    public static String dateStringFromTimeZone(Date date, String userTimeZone) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            dateFormat.setTimeZone(TimeZone.getTimeZone(userTimeZone));
            return dateFormat.format(date);
        } catch (Throwable t) {
            return "";
        }
    }


    public static boolean isInListList(List<Integer> ops,Integer ...values) {
        if(ops == null || ops.size() ==0) {
            return false;
        }
        if(values == null || values.length == 0) {
            return false;
        }

        for (Integer op : ops) {
            if(!isOk(op)) {
                continue;
            }
            for (Integer val : values) {
                if(!isOk(val)) {
                    continue;
                }
                if(val.equals(op)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static final boolean isInList(String value,String... values) {
        if(!isOk(value) || values == null || values.length <=0) {
            return false;
        }
        for (String val : values) {
            if(isOk(val) && val.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean byteExists(byte[] value) {
        return !(value == null || value.length == 0);
    }

    public static final String getMd5(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(value.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static boolean isOk(Enum value) {
        return !(value == null);
    }

    public static boolean isOk(Boolean value) {
        return !(value == null || value.booleanValue() == false);
    }

    public static boolean isOk(LocalDate date) {
        return !(date == null);
    }

    public static boolean isOk(Instant date) {
        return !(date == null);
    }

    public static String getInstantAsStringFormat(Instant instant) {
        String str = "";
        if (instant != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Defs.ES_INSTANT_FORMAT)
                        .withZone(ZoneId.systemDefault());
                str = formatter.format(instant);

            } catch (Exception x) {
                x.printStackTrace();
            }
        }
        return str;
    }

    public static String getStringFromInstant(Instant instant) {
        if(instant == null) {
            return null;
        }
        try {
            Date myDate = Date.from(instant);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return formatter.format(myDate);
        } catch (Throwable t) {
            t.printStackTrace();
            return "";
        }
    }

    public static String getStringFromInstant(Instant instant, String format) {
        if(instant == null) {
            return null;
        }
        try {
            Date myDate = Date.from(instant);
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(myDate);
        } catch (Throwable t) {
            t.printStackTrace();
            return "";
        }
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        if (dateToConvert != null) {
            return dateToConvert.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
        return null;
    }

    public static LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDate getLocalDateFromStringFormat(String date) {
        if (!isOk(date)) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Defs.ES_DATE_FORMAT);
            return LocalDate.parse(date, formatter);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }


    public static Instant getInstantFromStringFormat(String date) {
        if (!isOk(date)) {
            return null;
        }
        try {
            return LocalDateTime.parse( // Parse as an indeterminate `LocalDate`, devoid of time zone or offset-from-UTC. NOT a moment, NOT a point on the timeline.
                    date, // This input uses a poor choice of format. Whenever possible, use standard ISO 8601 formats when exchanging date-time values as text. Conveniently, the java.time classes use the standard formats by default when parsing/generating strings.
                    DateTimeFormatter.ofPattern(Defs.ES_INSTANT_FORMAT, Locale.US) // Use single-character `M` & `d` when the number lacks a leading padded zero for single-digit values.
            ) // Returns a `LocalDateTime` object.
                    .atZone( // Apply a zone to that unzoned `LocalDateTime`, giving it meaning, determining a point on the timeline.
                            ZoneId.systemDefault() // Always specify a proper time zone with `Contintent/Region` format, never a 3-4 letter pseudo-zone such as `PST`, `CST`, or `IST`.
                    ) // Returns a `ZonedDateTime`. `toString` → 2018-05-12T16:30-04:00[America/Toronto].
                    .toInstant();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    public static String getDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getDateOnly(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static Date datefromString(String date) throws ParseException {
        final String NEW_FORMAT = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(Defs.OLD_DATE_FORMAT);
        Date d = sdf.parse(date);
        sdf.applyPattern(NEW_FORMAT);
        sdf.format(d);
        return d;
    }

    public static final LocalDate getLocalDate(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof java.sql.Date) {
            return ((java.sql.Date) object).toLocalDate();
        }
        if (object instanceof Date) {
            return new java.sql.Date(((Date) object).getTime()).toLocalDate();
        }
        if (object instanceof String) {
            return getLocalDateFromStringFormat((String) object);
        }
        return null;
    }

    public static Instant getInstant(Object object) {
        if(object instanceof Instant) {
            return (Instant) object;
        }
        LocalDate date = getLocalDate(object);
        if(object != null) {
            return date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        return null;
    }

    public static final boolean getBooleanFromObject(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof Boolean) {
            return ((Boolean) object).booleanValue();
        }
        if (object instanceof Integer) {
            if (Utils.getIntegerFromObject(object) == 1) {
                return true;
            } else {
                return false;
            }
        }
        if (object instanceof Long) {
            if (((Long) object).intValue() == 1) {
                return true;
            } else {
                return false;
            }
        }
        if (object instanceof Short) {
            if (((Short) object).intValue() == 1) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    private static Date getValidDate(String date) {

        Date mydate = null;
        if (isValidDateFormat(date)) {
            /*
             * d -> Day of month
             * M -> Month of year
             * y -> Year
             */
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            /*
             * By default setLenient() is true. We should make it false for
             * strict date validations.
             *
             * If setLenient() is true - It accepts all dates. If setLenient()
             * is false - It accepts only valid dates.
             */
            dateFormat.setLenient(false);
            try {
                mydate = dateFormat.parse(date);
            } catch (ParseException e) {
                mydate = null;
            }
        }
        return mydate;
    }

    private static boolean isValidDateFormat(String date) {

        /*
         * Regular Expression that matches String with format dd/MM/yyyy.
         * dd -> 01-31
         * MM -> 01-12
         * yyyy -> 4 digit number
         */
        String pattern = "(0?[1-9]|[12][0-9]|3[01])\\/(0?[1-9]|1[0-2])\\/([0-9]{4})";
        boolean result = false;
        if (date.matches(pattern)) {
            result = true;
        }
        return result;
    }

    public static String getDayName(int dayofWeek) {

        String dayName = null;
        switch (dayofWeek) {
            case 1:
                dayName = "SUNDAY";
                break;
            case 2:
                dayName = "MONDAY";
                break;
            case 3:
                dayName = "TUESDAY";
                break;
            case 4:
                dayName = "WEDNESDAY";
                break;
            case 5:
                dayName = "THURSDAY";
                break;
            case 6:
                dayName = "FRIDAY";
                break;
            case 7:
                dayName = "SATURDAY";
                break;
        }
        return dayName;
    }

    public static int getDayNumber(String dayofWeek) {

        int dayName = 0;
        switch (dayofWeek) {
            case "SUNDAY":
                dayName = 1;
                break;
            case "MONDAY":
                dayName = 2;
                break;
            case "TUESDAY":
                dayName = 3;
                break;
            case "WEDNESDAY":
                dayName = 4;
                break;
            case "THURSDAY":
                dayName = 5;
                break;
            case "FRIDAY":
                dayName = 6;
                break;
            case "SATURDAY":
                dayName = 7;
                break;
        }
        return dayName;
    }

    public static Date dateFromLocalDate(LocalDate ldate) {
        return Date.from(ldate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static boolean different(String oldValue, String newValue) {
        if (isOk(newValue) && isOk(oldValue)) {
            return !newValue.equalsIgnoreCase(oldValue);
        } else if (isOk(newValue) || isOk(oldValue)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean different(LocalDate oldValue, LocalDate newValue) {
        if (isOk(newValue) && isOk(oldValue)) {
            return newValue.compareTo(oldValue) != 0;
        } else if (isOk(newValue) || isOk(oldValue)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean different(Instant oldValue, Instant newValue) {
        if (isOk(newValue) && isOk(oldValue)) {
            return newValue.compareTo(oldValue) != 0;
        } else if (isOk(newValue) || isOk(oldValue)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean different(Enum oldValue, Enum newValue) {
        if (isOk(newValue) && isOk(oldValue)) {
            return !newValue.name().equals(oldValue.name());
        } else if (isOk(newValue) || isOk(oldValue)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean different(Integer oldValue, Integer newValue) {
        if (isOk(newValue) && isOk(oldValue)) {
            return oldValue.intValue() != newValue.intValue();
        } else if (isOk(newValue) || isOk(oldValue)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean different(Object oldValue, Object currentValue) {
        if (isOk(oldValue) && isOk(currentValue)) {
            if (oldValue instanceof Instant) {
                return different((Instant) oldValue, (Instant) currentValue);
            } else if (oldValue instanceof String) {
                return different((String) oldValue, (String) currentValue);
            } else if (oldValue instanceof Integer) {
                return different((Integer) oldValue, (Integer) currentValue);
            } else if (oldValue instanceof LocalDate) {
                return different((LocalDate) oldValue, (LocalDate) currentValue);
            } else if (oldValue instanceof Enum) {
                return different((Enum) oldValue, (Enum) currentValue);
            } else {
                return false;
            }
        } else if (isOk(oldValue) || isOk(currentValue)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isOk(Object value) {
        return !(value == null);
    }

    public static boolean isOk(List value) {
        return !(value == null || value.size()==0);
    }

    public static byte[] base64ToByte(String data){
        String base64=data.split(",")[1] ;
        return Base64.decodeBase64(base64);
    }

    public static boolean isOk(byte[] value) {
        return !(value == null);
    }

    public static String dbIdToLdap(Integer id) {
        return Integer.toOctalString(880 + id);
    }

    public static Integer LdapIdToDb(String id) {
        return Integer.parseInt(id, 8) - 880;
    }

    public static String exception(Throwable t) {
        if (t != null) {
            t.printStackTrace();
            return t.getMessage();
        } else {
            return null;
        }
    }

    public static String javaObjectToJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }


    public static String getLocalDateAsString(LocalDate date) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Defs.ES_DATE_FORMAT);
        return date.format(formatter);
    }

    public static boolean valueExists(Object[] objecta, int index) {
        if (objecta == null) {
            return false;
        }
        return objecta.length >= (index + 1) && objecta[index] != null;
    }

    public static final String listToString(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        ids.stream().filter(id -> isOk(id)).forEach(id -> {
            sb.append(id);
            sb.append(',');
        });
        if(sb.length() >1) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String stringListToString(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        values.stream().filter(id -> isOk(id)).forEach(id -> {
            sb.append(id);
            sb.append(',');
        });
        if(sb.length() >1) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public static final String listToString(Integer... ids) {
        if (ids == null || ids.length <=0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Integer id : ids) {
            if(!isOk(id)) {
                continue;
            }
            sb.append(id);
            sb.append(',');
        }
        if(sb.length() >1) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String datetoReportString(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return "";
        }
    }

    public static Instant getInstantFromDate(Date date) {
        if (date == null)
            return null;
        return date.toInstant();
    }

    public static boolean isValidEmail(String value) {
        final String regex = "^([a-zA-Z0-9]+[._-]?)+@([a-zA-Z0-9]+[._-])+([a-zA-Z0-9]{2,6})$";
        final Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }

    public static String getYearMonth() {
        DateFormat df = new SimpleDateFormat("yyMM");
        return df.format(new Date());
    }

    public static <T> void getJavaObject(String jsonValue, T entity) {
        if(!isOk(jsonValue)) {
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule());
            Object value = mapper.readValue(jsonValue, entity.getClass());
            copyBean(value, entity);
        } catch (Throwable t) {
            t.printStackTrace();
            return;
        }
    }

    public static boolean validNameEn(String nameEn) {
        return (isOk(nameEn) && nameEn.matches("^([a-zA-Z]+[-.]?[ ]?)+$"));
    }

    public static boolean validGender(String gender) {
        return (isOk(gender) && gender.matches("^[MFO]$"));
    }

    public static boolean validAccountName(String nameEn) {
        return (isOk(nameEn) && nameEn.matches("^([a-zA-Z]+[.]?[ ]?)+$"));
    }

    public static boolean isDigitOnly(String digit) {
        return (isOk(digit) && digit.matches("[0-9]+"));
    }

    public static boolean validAccountNo(String digit) {
        return (isOk(digit) && digit.matches("[0-9]+") && (digit.length() >= 11 && digit.length() <= 17));
    }

    public static boolean isValidNid(String digit) {
        return (isOk(digit) && digit.matches("[0-9]+") && (digit.length() == 10 || digit.length() == 17));
    }

    public static boolean isValidMobile(String digit) {
        return (isOk(digit) && digit.matches("^\\+?(88)?01[3456789][0-9]{8}$"));
    }

    public static boolean isValidBerthCert(String digit) {
        return (isOk(digit) && digit.matches("[0-9]+") && (digit.length() >= 10 && digit.length() <= 17));
    }

    public static boolean isValidRealNumber(String digit) {
        return (isOk(digit) && digit.matches("[0-9]+(\\.[0-9][0-9]?)?"));
    }

    public static boolean validNameBn(String nameBn) {

        final String regex = "[\\u0995-\\u09B9\\u09CE\\u09DC-\\u09DF]";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(nameBn);

        while (matcher.find()) {
            return true;
        }
        return false;
    }

    public static boolean isNullZero(Integer integer) {
        if (integer == null) {
            return true;
        }
        if (integer.intValue() == 0) {
            return true;
        }
        return false;
    }

//    public static byte[] getWSQByte(byte[] inputImage,Boolean isInverted) {
//        BufferedImage img =null;
//        try {
//            if(isInverted!=null && isInverted){
//                inputImage=getInvertImage(inputImage);
//            }
//            img = ImageIO.read(new ByteArrayInputStream(inputImage));
//            return WSQEncoder.encode(img);
//        } catch (Throwable t) {
//            t.printStackTrace();
//            return null;
//        }
//    }

    public static byte[] getInvertImage(byte[] inputImage) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(inputImage));
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int p = img.getRGB(x, y);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;
                    //subtract RGB from 255
                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;
                    //set new RGB value
                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    img.setRGB(x, y, p);
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }


    public static boolean isAllFieldNull(Object object) {
        try {
            for (Field f : object.getClass().getDeclaredFields())
            {
                f.setAccessible(true);
                if (f.get(object) != null)
                    return false;
            }
            return true;
        }catch (Exception ex){
            return true;
        }
    }

    public static byte[] getBMPImage(BufferedImage img) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        baos.flush();
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
    }
}
