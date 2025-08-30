package com.arshaa.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	
	public static Date formatUtil(Date date)
	{
        DateFormat date_format = new SimpleDateFormat("dd-MM-yyyy");
        String date_string = date_format.format(date);
	    Date date1 = null;
		try {
			date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date_string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date1;  
	}
	
}
