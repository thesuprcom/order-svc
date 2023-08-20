package com.supr.orderservice.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.supr.orderservice.utils.Constants.DATE_FORMAT;
import static com.supr.orderservice.utils.Constants.DATE_OBJECT_TIMESTAMP_FORMAT;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

  public static final String UTC_TIMEZONE = "UTC";
  public static final String DUBAI_TIMEZONE = "Asia/Dubai";
  public static final String UAE = "UAE";
  public static final String MYSQL_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS";

  public static Date getPastUtcDate(Duration duration) {
    return Date.from(Instant.now().minus(duration));
  }

  public static Date getDateTimeInUTC(long dateTime) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_OBJECT_TIMESTAMP_FORMAT);
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    // Using Deprecated method, just to unblock finance for now. Will take a tech dept to modify this soon.
    return new Date(sdf.format(new Date(dateTime)));
  }

  public static Date getDateTimeInUTC(Date dateTime) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_OBJECT_TIMESTAMP_FORMAT);
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    // Using Deprecated method, just to unblock finance for now. Will take a tech dept to modify this soon.
    return new Date(sdf.format(dateTime));
  }

  public static LocalDate getLocalDate(Date date) {
    return new java.sql.Date(date.getTime()).toLocalDate();
  }

  @SneakyThrows
  public static String getZonalDatetime(long dateTime, String countryCode) {
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    Date date = new Date(dateTime);
    return getZonedTime(countryCode, formatter, date);
  }

  private static String getZonedTime(String countryCode, SimpleDateFormat formatter, Date date) {
    switch (countryCode) {
      case UAE:
        TimeZone tzInUAE = TimeZone.getTimeZone(DUBAI_TIMEZONE);
        formatter.setTimeZone(tzInUAE);
        return formatter.format(date);
      default:
        TimeZone tzInUTC = TimeZone.getTimeZone(UTC_TIMEZONE);
        formatter.setTimeZone(tzInUTC);
        return formatter.format(date);
    }
  }

  public static LocalDate getZonedLocalDate(Date date, ZoneId zoneId) {
    return date.toInstant().atZone(zoneId).toLocalDate();
  }

  @SneakyThrows
  public static String getZonalDatetime(Date dateTime, String countryCode) {
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    return getZonedTime(countryCode, formatter, dateTime);
  }

  public static Timestamp getCurrentDateTimeUTC() {
    SimpleDateFormat sdf = new SimpleDateFormat(MYSQL_TIMESTAMP_FORMAT);
    sdf.setTimeZone(TimeZone.getTimeZone(UTC_TIMEZONE));
    String utcTime = sdf.format(new Date());
    return Timestamp.valueOf(utcTime);
  }

  public static String getUserMessage(String slotDate, String slotTime) {
    LocalDate currentDate = LocalDate.parse(slotDate);
    int day = currentDate.getDayOfMonth();
    Month month = currentDate.getMonth();
    return month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + day + ", " + slotTime;
  }

  @SneakyThrows
  public static Timestamp getScheduledTimestamp(String dateString){
    SimpleDateFormat dateFormat = new SimpleDateFormat(MYSQL_TIMESTAMP_FORMAT);
    Date parsedDate = dateFormat.parse(dateString);
    return  new Timestamp(parsedDate.getTime());
  }

}
