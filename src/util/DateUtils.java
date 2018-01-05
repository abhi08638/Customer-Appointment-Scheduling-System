/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.time.DayOfWeek;
import main.ApplicationConfig;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static util.Helper.getLabel;

/**
 *
 * @author Abhi
 */
public class DateUtils {

    public static ZonedDateTime convertToLocalTime(Instant pit) {
        ZonedDateTime currDateAndTime = ZonedDateTime.ofInstant(pit, ApplicationConfig.getDefZoneId());
        return currDateAndTime;
    }

    public static String getFormattedDate(Instant pit) {
        String formattedDate = convertToLocalTime(pit).format(DateTimeFormatter.ofPattern(getLabel(Constants.Labels.DATE_FORMAT)));
        return formattedDate;
    }

    public static String getFormattedTime(Instant pit) {
        LocalTime time = convertToLocalTime(pit).toLocalTime();
        String ampm = Constants.AM;
        String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm "));
        if (getLabel(Constants.Labels.TIME_FORMAT).equals("12")) {
            if (time.getHour() > 12) {
                ampm = Constants.PM;
                time = time.minusHours(12);
            } else if (time.getHour() == 0) {
                time = time.plusHours(12);
            } else if (time.getHour() == 12) {
                ampm = Constants.PM;
            }
            formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm ")) + ampm;
        }
        return formattedTime;
    }

    private static String convertTo24Hours(String userTime) throws Exception {
        userTime = userTime.toUpperCase();
        if (getLabel(Constants.Labels.TIME_FORMAT).equals("12")
                || userTime.contains(Constants.AM) || userTime.contains(Constants.PM)) {
            if (userTime.contains(Constants.PM)) {
                userTime = userTime.substring(0, userTime.indexOf(Constants.PM));
                int hour = Integer.parseInt(userTime.substring(0, userTime.indexOf(":")));
                userTime = userTime.substring(userTime.indexOf(":"), userTime.length());
                if (hour != 12) {
                    hour += 12;
                }
                userTime = Integer.toString(hour) + userTime;
            } else {
                userTime = userTime.substring(0, userTime.indexOf(Constants.AM));
                int hour = Integer.parseInt(userTime.substring(0, userTime.indexOf(":")));
                userTime = userTime.substring(userTime.indexOf(":"), userTime.length());
                if (hour == 12) {
                    hour -= 12;
                }
                if (Integer.toString(hour).length() == 1) {
                    userTime = "0" + Integer.toString(hour) + userTime;
                } else {
                    userTime = Integer.toString(hour) + userTime;
                }
            }
        } else {
            int hour = Integer.parseInt(userTime.substring(0, userTime.indexOf(":")));
            userTime = userTime.substring(userTime.indexOf(":"), userTime.length());
            if (Integer.toString(hour).length() == 1) {
                userTime = "0" + Integer.toString(hour) + userTime;
            } else {
                userTime = Integer.toString(hour) + userTime;
            }
        }
        return userTime.trim();
    }

    public static Instant toDataObjectInstant(String userDate, String userTime) throws Exception {
        try {
            LocalDate ld = convertDateToUTC(userDate);
            userTime = convertTo24Hours(userTime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime lt = LocalTime.parse(userTime, formatter);
            LocalDateTime ldt = LocalDateTime.of(ld, lt);
            return LocalDateTime.of(ld, lt).toInstant(OffsetDateTime.now().getOffset());
        } catch (Exception e) {
            Helper.throwException(Helper.getLabel(Constants.Error.PREFIX_CONVERT) + userTime + Helper.getLabel(Constants.Error.SUFFIX_UTC_TIME));
        }
        return null;
    }

    public static LocalDate convertDateToUTC(String userDate) throws Exception {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Helper.getLabel(Constants.Labels.DATE_FORMAT));
            LocalDate ld = LocalDate.parse(userDate, formatter);
            return ld;
        } catch (Exception e) {
            Helper.throwException(Helper.getLabel(Constants.Error.PREFIX_CONVERT) + userDate + Helper.getLabel(Constants.Error.SUFFIX_UTC_DATE)
                    + ": " + Helper.getLabel(Constants.Labels.DATE_FORMAT));
        }
        return null;
    }

    public static LocalDateTime toLocalDateTime(Instant i) {
        //used to convert the dataobject instant to localdatetime to save in the db
        //from a to stmt
        return LocalDateTime.ofInstant(i, ZoneOffset.UTC);
    }

    public static Instant toInstant(LocalDateTime ldt) {
        //used to convert localdatetime from resultset into utc instant for dataobject
        //from rs to a
        return ldt.toInstant(ZoneOffset.UTC);
    }

    public static LocalDateTime getEndOfMonth() {
        LocalDate ld = LocalDate.now();
        ld = ld.with(lastDayOfMonth());
        LocalDateTime ldt = LocalDateTime.of(ld, LocalTime.MAX);
        return ldt;
    }

    public static LocalDateTime getEndOfWeek() {
        LocalDate ld = LocalDate.now();
        ld = ld.with(DayOfWeek.SATURDAY);
        LocalDateTime ldt = LocalDateTime.of(ld, LocalTime.MAX);
        return ldt;
    }

}
