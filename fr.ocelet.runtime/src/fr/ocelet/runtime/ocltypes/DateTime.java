/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2018
*
*  This software is a domain specific programming language dedicated to writing
*  spatially explicit models and performing spatial dynamics simulations.
*
*  This software is governed by the CeCILL license under French law and
*  abiding by the rules of distribution of free software.  You can  use,
*  modify and/ or redistribute the software under the terms of the CeCILL
*  license as circulated by CEA, CNRS and INRIA at the following URL
*  "http://www.cecill.info".
*  As a counterpart to the access to the source code and  rights to copy,
*  modify and redistribute granted by the license, users are provided only
*  with a limited warranty  and the software's author,  the holder of the
*  economic rights,  and the successive licensors  have only limited
*  liability.
*  The fact that you are presently reading this means that you have had
*  knowledge of the CeCILL license and that you accept its terms.
*/

package fr.ocelet.runtime.ocltypes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;;

/**
 * An immutable date and time manipulation class
 * 
 * @author Pascal Degenne - Initial contribution
 */
public final class DateTime {

	public static DateTime fromString(String format, String sdate) {
		try {
			return new DateTime(LocalDateTime.parse(sdate, DateTimeFormatter.ofPattern(format)),
					DateTimeFormatter.ofPattern(format));
		} catch (DateTimeParseException e) {
			System.out
					.println("Failed to create a valid DateTime from \"" + sdate + "\" using formating pattern: \"" + format+"\"");
			System.out.println("  creating a default DateTime with system time instead.");
		}
		return new DateTime();
	}

	public static DateTime ymd(int year, int month, int day) {
		return new DateTime(year, month, day);
	}
	
	private final LocalDateTime ldt;
	private DateTimeFormatter dtf;

	public DateTime() {
		ldt = LocalDateTime.now();
		dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	}

	public DateTime(int year, int month, int day) {
		ldt = LocalDateTime.of(year, month, day, 0, 0);
		dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	}

	public DateTime(int year, int month, int day, int hour, int minute, int second) {
		ldt = LocalDateTime.of(year, month, day, hour, minute, second);
		dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	}

	private DateTime(LocalDateTime aldt, DateTimeFormatter adft) {
		ldt = aldt;
		dtf = adft;
	}

	public DateTime addDays(int amount) {
		return new DateTime(ldt.plusDays(amount), dtf);
	}

	public DateTime addHours(int amount) {
		return new DateTime(ldt.plusHours(amount), dtf);
	}

	public DateTime addMicroseconds(int amount) {
		return new DateTime(ldt.plusNanos(amount*1000), dtf);
	}

	public DateTime addMilliseconds(int amount) {
		return new DateTime(ldt.plusNanos(amount*1000000), dtf);
	}

	public DateTime addMinutes(int amount) {
		return new DateTime(ldt.plusMinutes(amount), dtf);
	}

	public DateTime addMonths(int amount) {
		return new DateTime(ldt.plusMonths(amount), dtf);
	}

	public DateTime addNanoseconds(int amount) {
		return new DateTime(ldt.plusNanos(amount), dtf);
	}

	public DateTime addSeconds(int amount) {
		return new DateTime(ldt.plusSeconds(amount), dtf);
	}

	public DateTime addWeeks(int amount) {
		return new DateTime(ldt.plusDays(amount * 7), dtf);
	}

	public DateTime addYears(int amount) {
		return new DateTime(ldt.plusYears(amount), dtf);
	}

	public int compareTo(DateTime adt) {
		return ldt.compareTo(adt.ldt);
	}
	
	public boolean equals(DateTime adt) {
		return ldt.equals(adt.ldt);
	}
	
	
	public int getDayOfMonth() {
		return ldt.getDayOfMonth();
	}

	/**
	 * @return The day of the week : 1 is Monday, 7 is Sunday
	 */
	public int getDayOfWeek() {
		return ldt.getDayOfWeek().getValue();
	}

	public int getDayOfYear() {
		return ldt.getDayOfYear();
	}

	public int getHour() {
		return ldt.getHour();
	}

	public int getMicrosecond() {
		return ldt.getNano() / 1000;
	}

	public int getMillisecond() {
		return ldt.getNano() / 1000000;
	}

	public int getMinute() {
		return ldt.getMinute();
	}

	/**
	 * @return The month in the year : 1 for January, 12 for December
	 */
	public int getMonth() {
		return ldt.getMonth().getValue();
	}

	public int getNanosecond() {
		return ldt.getNano();
	}

	public int getSecond() {
		return ldt.getSecond();
	}

	public long getTimeAsMilliseconds() {
		return ((getHour() * 60 + getMinute()) * 60 + getSecond()) * 1000 + getMillisecond();
	}

	public int getYear() {
		return ldt.getYear();
	}

	public boolean isAfter(DateTime d) {
		return this.ldt.isAfter(d.ldt);
	}

	public boolean isBefore(DateTime d) {
		return this.ldt.isBefore(d.ldt);
	}

	public void setFormat(String format) {
		try {
			dtf = DateTimeFormatter.ofPattern(format);
		} catch (Exception e) {
			System.out.println("Unrecognized DateTime formatting pattern : " + format);
		}
	}

	public String toString() {
		return dtf.format(ldt);
	}

	public String toString(String format) {
		return DateTimeFormatter.ofPattern(format).format(ldt);
	}

	public DateTime withDayOfMonth(int wd) {
		return new DateTime(ldt.withDayOfMonth(wd), dtf);
	}

	/**
	 * @param wd
	 *            1 is Monday, 7 is Sunday
	 * @return
	 */
	public DateTime withDayOfWeek(int wd) {
		int dow = ldt.getDayOfWeek().getValue();
		int mwd = wd % 7;
		if (mwd == 0)
			mwd = 7;
		return new DateTime(ldt.plusDays(mwd - dow), dtf);
	}

	public DateTime withDayOfYear(int wd) {
		return new DateTime(ldt.withDayOfYear(wd), dtf);
	}

	public DateTime withHour(int h) {
		return new DateTime(ldt.withHour(h % 24), dtf);
	}

	public DateTime withMicrosecond(int s) {
		return new DateTime(ldt.withNano(s * 1000), dtf);
	}

	public DateTime withMillisecond(int s) {
		return new DateTime(ldt.withNano(s * 1000000), dtf);
	}

	public DateTime withMinute(int m) {
		return new DateTime(ldt.withMinute(m % 60), dtf);
	}

	/**
	 * @param m
	 *            1 is January, 12 is December
	 */
	public DateTime withMonth(int m) {
		return new DateTime(ldt.withMonth((m % 12 == 0) ? 12 : m % 12), dtf);
	}

	public DateTime withNanosecond(int s) {
		return new DateTime(ldt.withNano(s), dtf);
	}

	public DateTime withSecond(int s) {
		return new DateTime(ldt.withSecond(s % 60), dtf);
	}

	public DateTime withTime(int hour, int minute, int second) {
		return new DateTime(ldt.withHour(hour % 24).withMinute(minute % 60).withSecond(second % 60), dtf);
	}

	public DateTime withYear(int y) {
		return new DateTime(ldt.withYear(y), dtf);
	}
}
