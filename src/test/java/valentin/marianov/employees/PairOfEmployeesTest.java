package valentin.marianov.employees;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import valentin.marianov.employees.employee.EmployeePairs;
import valentin.marianov.employees.employee.EmployeeProcessing;

/**
 * Test class for testing different possible scenarions, e.g.
 * selection of an empty file, a file with only one employee,
 * syntax errors and others.
 * 
 * @author Valentin
 */
public class PairOfEmployeesTest {

	Path path;
	File file;

	@Test
	public void testEmptyFile() {

		HashMap<Integer, EmployeePairs> employeePairs = EmployeeProcessing
				.findAllEmployeePairs(getFile("empty-file.txt"), true);

		String[] longestTogetherWorkingPair = null;

		if (employeePairs.size() > 0) {
			longestTogetherWorkingPair = EmployeeProcessing.findLongestWorkingEmployeePair(employeePairs);
		}

		assertEquals(0, employeePairs.size());
		assertEquals(null, longestTogetherWorkingPair);
	}

	@Test
	public void testFirstLineError() {

		HashMap<Integer, EmployeePairs> employeePairs = EmployeeProcessing
				.findAllEmployeePairs(getFile("first-line-error.txt"), true);

		String[] longestTogetherWorkingPair = null;

		if (employeePairs.size() > 0) {
			longestTogetherWorkingPair = EmployeeProcessing.findLongestWorkingEmployeePair(employeePairs);
		}

		assertEquals(0, employeePairs.size());
		assertEquals(null, longestTogetherWorkingPair);
	}

	@Test
	public void testNoOverlap() {

		HashMap<Integer, EmployeePairs> employeePairs = EmployeeProcessing
				.findAllEmployeePairs(getFile("no-overlap.txt"), true);

		String[] longestTogetherWorkingPair = null;

		if (employeePairs.size() > 0) {
			longestTogetherWorkingPair = EmployeeProcessing.findLongestWorkingEmployeePair(employeePairs);
		}

		assertEquals(0, employeePairs.size());
		assertEquals(null, longestTogetherWorkingPair);
	}

	@Test
	public void testSingleEmployee() {

		HashMap<Integer, EmployeePairs> employeePairs = EmployeeProcessing
				.findAllEmployeePairs(getFile("single-employee.txt"), true);

		String[] longestTogetherWorkingPair = null;

		if (employeePairs.size() > 0) {
			longestTogetherWorkingPair = EmployeeProcessing.findLongestWorkingEmployeePair(employeePairs);
		}

		assertEquals(0, employeePairs.size());
		assertEquals(null, longestTogetherWorkingPair);
	}

	@Test
	public void testUnsupportedDateFormat() {

		HashMap<Integer, EmployeePairs> employeePairs = EmployeeProcessing
				.findAllEmployeePairs(getFile("unsupported-date-format.txt"), true);
		
		String[] longestTogetherWorkingPair = null;

		if (employeePairs.size() > 0) {
			longestTogetherWorkingPair = EmployeeProcessing.findLongestWorkingEmployeePair(employeePairs);
		}

		assertEquals(0, employeePairs.size());
		assertEquals(null, longestTogetherWorkingPair);
	}

	@Test
	public void testInsufficientNumberOfValues() {

		HashMap<Integer, EmployeePairs> employeePairs = EmployeeProcessing
				.findAllEmployeePairs(getFile("employee-insufficient-number-values.txt"), true);

		String[] longestTogetherWorkingPair = null;

		if (employeePairs.size() > 0) {
			longestTogetherWorkingPair = EmployeeProcessing.findLongestWorkingEmployeePair(employeePairs);
		}

		assertEquals(0, employeePairs.size());
		assertEquals(null, longestTogetherWorkingPair);
	}

	@Test
	public void testEmployeesWithSingleOverlap() {

		HashMap<Integer, EmployeePairs> employeePairs = EmployeeProcessing
				.findAllEmployeePairs(getFile("employees-with-overlap.txt"), true);

		String[] longestTogetherWorkingPair = null;

		if (employeePairs.size() > 0) {
			longestTogetherWorkingPair = EmployeeProcessing.findLongestWorkingEmployeePair(employeePairs);
		}

		assertNotEquals(0, employeePairs.size());
		assertEquals(4, longestTogetherWorkingPair.length);
		assertEquals("18", longestTogetherWorkingPair[0]);
		assertEquals("19", longestTogetherWorkingPair[1]);
		assertEquals("69 55 ", longestTogetherWorkingPair[2]);
		assertEquals("14", longestTogetherWorkingPair[3]);

	}

	@Test
	public void testEmployeesWithOverlapsAndDifferentDateFormats() {

		HashMap<Integer, EmployeePairs> employeePairs = EmployeeProcessing
				.findAllEmployeePairs(getFile("employees-with-overlaps-and-different-date-formats.txt"), true);

		String[] longestTogetherWorkingPair = null;

		if (employeePairs.size() > 0) {
			longestTogetherWorkingPair = EmployeeProcessing.findLongestWorkingEmployeePair(employeePairs);
		}

		assertNotEquals(0, employeePairs.size());
		assertEquals(4, longestTogetherWorkingPair.length);
		assertEquals("1", longestTogetherWorkingPair[0]);
		assertEquals("4", longestTogetherWorkingPair[1]);
		assertEquals("19 11 ", longestTogetherWorkingPair[2]);
		assertEquals("427", longestTogetherWorkingPair[3]);

	}

	@Test
	public void testEmployeesWithOverlaps() {

		HashMap<Integer, EmployeePairs> employeePairs = EmployeeProcessing
				.findAllEmployeePairs(getFile("employees-with-multiple-overlaps.txt"), true);

		String[] longestTogetherWorkingPair = null;

		if (employeePairs.size() > 0) {
			longestTogetherWorkingPair = EmployeeProcessing.findLongestWorkingEmployeePair(employeePairs);
		}

		assertNotEquals(0, employeePairs.size());
		assertEquals(4, longestTogetherWorkingPair.length);
		assertEquals("1", longestTogetherWorkingPair[0]);
		assertEquals("2", longestTogetherWorkingPair[1]);
		assertEquals("1 3 ", longestTogetherWorkingPair[2]);
		assertEquals("1633", longestTogetherWorkingPair[3]);

	}

	private File getFile(String fileName) {

		String customImageOne = "./test-files/" + fileName;
		File file = new File(customImageOne);

		return file;
	}
}
