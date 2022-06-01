package valentin.marianov.employees.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * Utility class holding methods for converting string dates of three different
 * formats to a Date object and computing the overlap in days for two date
 * ranges.
 * 
 * @author Valentin
 */
public class DateUtility {

	private static Logger logger = Logger.getLogger("employees-logger");

	/**
	 * Converts the follwing date strings into {@link Date}:
	 * <ul>
	 * <li><b>MM/dd/yyyy
	 * <li>yyyyMMdd
	 * <li>yyyy-MM-dd </b>
	 * </ul>
	 *
	 * @param strDate - the date as a string
	 * @return Date object
	 * 
	 * @author Valentin
	 */
	public static Date convertStringToDate(final String strDate)
			throws IllegalArgumentException, IllegalFieldValueException {

		DateTimeFormatter formatter = null;

		if (strDate == null || strDate.isEmpty()) {
			return null;
		} else if (strDate.equalsIgnoreCase("NULL")) {
			return new Date();
		} else {
			if (formatter == null) {
				formatter = new DateTimeFormatterBuilder()
						.appendOptional(DateTimeFormat.forPattern("MM/dd/yyyy").getParser())
						.appendOptional(DateTimeFormat.forPattern("yyyyMMdd").getParser())
						.appendOptional(DateTimeFormat.forPattern("yyyy-MM-dd").getParser()).toFormatter();
			}

			LocalDateTime dateTime = LocalDateTime.parse(strDate, formatter);
			return dateTime.toDate();
		}
	}

	/**
	 * Takes two date ranges for two employees having worked on the same project and
	 * checks if they have worked together, i.e. there must be an overlap of atleast
	 * one day between the two date ranges.
	 * <p>
	 * For an overlap (X, Y) with Y >= X the end result in the difference between
	 * the dates, i.e. (Y - X) + 1. With other words the period of overlap includes
	 * also the end date.
	 * 
	 * @param e1StartDate - start day working on project 'x' for employee 1
	 * @param e1EndDate   - end day working on project 'x' for employee 1
	 * @param e2StartDate - start day working on project 'x' for employee 2
	 * @param e2EndDate   - end day working on project 'x' for employee 2
	 * @return the overlap in days (including the end date)
	 * 
	 * @author Valentin
	 */
	public static int computeOverlapOfDateRages(Date e1StartDate, Date e1EndDate, Date e2StartDate, Date e2EndDate) {

		//@formatter:off
		/*
		 * Check if there is an overlap between two date ranges ( A_s, A_e ) and ( B_s, B_e ).
		 * 
		 * An overlap exist only in the case when the end of the first interval A_e has a value
		 * greater or equal to the start of the second interval B_s and the start of the first
		 * interval A_s is before the end of second interval B_e.
		 * 
		 * Using boolean algebra this results to ( A_e >= B_s ) && ( A_s <= B_e ) and the output 
		 * will always be true regardless of the fact if interval A being before, at the same time
		 * or at later point in the future compared to interval B (if of course A and B overlap).
		 */
		//@formatter:on
		if ((e1EndDate.getTime() >= e2StartDate.getTime() && e1StartDate.getTime() <= e2EndDate.getTime())) {

			DateTime e1StartDt = new DateTime(e1StartDate);
			DateTime e1EndDt = new DateTime(e1EndDate);
			DateTime e2StartDt = new DateTime(e2StartDate);
			DateTime e2EndDt = new DateTime(e2EndDate);

			/*
			 * Swap positions of start and end date, otherwise results will always be
			 * negative. The TimeUnit class does not work due to DayLight Saving Time
			 * anomaly.
			 * 
			 * Compute all possible ranges since an overlap exists. The minimum value should
			 * represent the actual overlap in days between both periods (excluding the end
			 * date).
			 */
			int range1 = Days.daysBetween(e1StartDt, e1EndDt).getDays();
			int range2 = Days.daysBetween(e2StartDt, e1EndDt).getDays();
			int range3 = Days.daysBetween(e2StartDt, e2EndDt).getDays();
			int range4 = Days.daysBetween(e1StartDt, e2EndDt).getDays();

			List<Integer> ranges = new ArrayList<>();

			ranges.add(range1);
			ranges.add(range2);
			ranges.add(range3);
			ranges.add(range4);

			// get the smallest value, this is the actual overlap (without the end date)
			int rangeOverlap = ranges.get(ranges.indexOf(Collections.min(ranges))).intValue();

			// count the end date as well
			rangeOverlap += 1;
			logger.info("Range overlap in days INCLUDING END DATE: " + rangeOverlap);

			return rangeOverlap;

		} else {
			// no overlap
			return 0;
		}

	}
}
