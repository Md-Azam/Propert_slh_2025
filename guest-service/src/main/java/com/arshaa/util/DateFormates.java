package com.arshaa.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateFormates {
	public static final ZoneId DEFAULT_TIME_ZONE = ZoneId.systemDefault();
	public static Instant dateToInstant(Date date) {
		return date.toInstant();
	}
	public static Date instantToDate(Instant instant) {
		return Date.from(instant);
	}
	public static LocalDate dateToLocalDate(Date date) {
		return dateToInstant(date).atZone(DEFAULT_TIME_ZONE).toLocalDate();
	}
	public static java.sql.Date utilToSql(Date date) {
		return new java.sql.Date(date.getTime());
	}
	public static ZonedDateTime sqlToZoned(java.sql.Date date)
	{
		LocalDate ld = date.toLocalDate();
		return ld.atStartOfDay( DEFAULT_TIME_ZONE );
	}
	public static long zonedDateTimeDifference(ZonedDateTime d1, ZonedDateTime d2, ChronoUnit unit){
	    return unit.between(d1, d2);
	}
	public static long lastDayOfMonth(ZonedDateTime d1)
	{
		LocalDate localDateTime = d1.toLocalDate();
		return localDateTime.getMonth().length(localDateTime.isLeapYear());
	}
	public static java.sql.Date ZonedToSql(ZonedDateTime d1)
	{
		LocalDate localDate = d1.toLocalDate();
        return java.sql.Date.valueOf(localDate);
	}
	
}
