/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2016
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A convenient class to manipulation dates and time
 * 
 * @author Pascal Degenne - Initial contribution
 */
public class Date {

	protected GregorianCalendar gc;

	public Date() {
		gc = new GregorianCalendar();
	}

	public Date(int year, int month, int day) {
		gc = new GregorianCalendar(year, month-1, day);
	}

	public Date(int year, int month, int day, int hour, int minute, int second) {
		gc = new GregorianCalendar(year,month-1,day, hour, minute, second);
	}
	
	public static Date ymd(int year, int month, int day) {
		return new Date(year, month, day);
	}

	public static Date fromString(String format, String sdate) {
		Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			java.util.Date jud = sdf.parse(sdate);
			result = new Date();
			result.gc.setTime(jud);
		} catch (ParseException e) {
			System.out.println("Failed to create a valid Date from " + sdate
					+ " using format " + format);
		}
		return result;
	}

	public Date clone() {
		return new Date(getYear(),getMonth(),getDayOfMonth(),getHour(),getMinute(),getSecond());
	}
	
	public Date addDays(int amount) {
		gc.add(Calendar.DAY_OF_YEAR, amount);
		return this;
	}

	public Date addWeeks(int amount) {
		gc.add(Calendar.DAY_OF_YEAR, amount * 7);
		return this;
	}

	public Date addMonths(int amount) {
		gc.add(Calendar.MONTH, amount);
		return this;
	}

	public Date addYears(int amount) {
		gc.add(Calendar.YEAR, amount);
		return this;
	}

	public Date addSeconds(int amount) {
		gc.add(Calendar.SECOND, amount);
		return this;
	}

	public Date addMinutes(int amount) {
		gc.add(Calendar.MINUTE, amount);
		return this;
	}

	public Date addHours(int amount) {
		gc.add(Calendar.HOUR, amount);
		return this;
	}

	public void setDayOfWeek(int wd) {
		gc.set(Calendar.DAY_OF_WEEK, wd);
	}

	public void setDayOfMonth(int wd) {
		gc.set(Calendar.DAY_OF_MONTH, wd);
	}

	public void setDayOfYear(int wd) {
		gc.set(Calendar.DAY_OF_YEAR, wd);
	}

	public int getDayOfWeek() {
		return gc.get(Calendar.DAY_OF_WEEK);
	}

	public int getDayOfMonth() {
		return gc.get(Calendar.DAY_OF_MONTH);
	}

	public int getDayOfYear() {
		return gc.get(Calendar.DAY_OF_YEAR);
	}

	public int getMonth() {
		return gc.get(Calendar.MONTH)+1;
	}

	public void setMonth(int m) {
		gc.set(Calendar.MONTH, m-1);
	}

	public int getYear() {
		return gc.get(Calendar.YEAR);
	}

	public void setYear(int y) {
		gc.set(Calendar.YEAR, y);
	}

	public int getHour() {
		return gc.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinute() {
		return gc.get(Calendar.MINUTE);
	}

	public int getSecond() {
		return gc.get(Calendar.SECOND);
	}

	public void setHour(int h) {
		gc.set(Calendar.HOUR, h);
	}

	public int getMillisecond() {
		return gc.get(Calendar.MILLISECOND);
	}

	public long getTimeAsMilliseconds() {
		return ((getHour() * 60 + getMinute()) * 60 + getSecond()) * 1000
				+ getMillisecond();
	}

	public String msToString(long ms) {
		StringBuffer sb = new StringBuffer();
		long h = ms / 3600000;
		if (h > 0) {
			sb.append(h + "h ");
			ms = ms % 3600000;
		}
		long m = ms / 60000;
		if (m > 0) {
			sb.append(m + "min ");
			ms = ms % 60000;
		}
		long s = ms / 1000;
		if (s > 0) {
			sb.append(s + "s ");
			ms = ms % 1000;
		}
		if (ms > 0) sb.append(ms + "ms");
		return sb.toString();
	}

	public void setMinute(int m) {
		gc.set(Calendar.MINUTE, m);
	}

	public void setSecond(int s) {
		gc.set(Calendar.SECOND, s);
	}

	public void setMillisecond(int ms) {
		gc.set(Calendar.MILLISECOND, ms);
	}

	public void setTime(int hour, int minute, int second) {
		gc.set(Calendar.HOUR_OF_DAY, hour);
		gc.set(Calendar.MINUTE, minute);
		gc.set(Calendar.SECOND, second);
	}

	public boolean isBefore(Date d) {
		return this.gc.before(d.gc);
	}

	public boolean isAfter(Date d) {
		return this.gc.after(d.gc);
	}

	public String toString(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(gc.getTime());
	}

	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat();
		return sdf.format(gc.getTime());
	}
}
