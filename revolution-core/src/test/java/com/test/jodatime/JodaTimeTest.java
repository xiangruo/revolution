package com.test.jodatime;

import java.util.Date;
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.GJChronology;
import org.junit.Test;

public class JodaTimeTest {

	/*
	 * java.util.Date to a DateTime
	 */
	@Test
	public void test1() {
		java.util.Date juDate = new Date();
		DateTime dt = new DateTime(juDate);
	}

	@Test
	public void test2() {
		DateTime dt = new DateTime();
		int month = dt.getMonthOfYear(); // where January is 1 and December is
		int year = dt.getYear();
	}

	@Test
	public void test3() {
		DateTime dt = new DateTime();
		DateTime year2000 = dt.withYear(2000);
		DateTime twoHoursLater = dt.plusHours(2);// add 2 hours
	}

	@Test
	public void test4() {
		DateTime dt = new DateTime();
		String monthName = dt.monthOfYear().getAsText();
		System.out.println(monthName);
		String frenchShortName = dt.monthOfYear().getAsShortText(Locale.FRENCH);
		System.out.println(frenchShortName);
		boolean isLeapYear = dt.year().isLeap();
		System.out.println(isLeapYear);
		DateTime rounded = dt.dayOfMonth().roundFloorCopy();
		System.out.println(rounded);
	}

	@Test
	public void test5() {
		Chronology coptic = CopticChronology.getInstance();
		DateTimeZone zone = DateTimeZone.forID("Asia/Tokyo");
		Chronology gregorianJuian = GJChronology.getInstance(zone);
		System.out.println(gregorianJuian.getZone());
	}

}
