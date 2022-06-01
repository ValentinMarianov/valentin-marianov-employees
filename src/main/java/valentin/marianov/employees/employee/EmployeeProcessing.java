package valentin.marianov.employees.employee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.joda.time.IllegalFieldValueException;

import valentin.marianov.employees.utility.DateUtility;

/**
 * Holds different methods used to process the employee data and find the pair
 * with the longest period of work on common projects.
 * 
 * @author Valentin
 *
 */
public class EmployeeProcessing {

	private static Logger logger = Logger.getLogger("employees-logger");

	/**
	 * Finds and store all employees and their corresponding projects along with the
	 * start and end dates for every single project.
	 * <p>
	 * Following, iterating through every employee and his/her projects the method
	 * searches for potential employee pairs by checking if two employees have
	 * worked on a common project and then computing if an overlap between their
	 * date ranges exists.
	 * 
	 * @param file      - list with csv separated values in the form EmployeeID,
	 *                  ProjectID, Start Date, EndDate
	 * @param isTesting - set to true when testing in order to disable pop-up
	 *                  messages
	 * 
	 * @return - pairs of emloyees, who have worked together in a project
	 * 
	 * @author Valentin
	 */
	public static HashMap<Integer, EmployeePairs> findAllEmployeePairs(final File file, final boolean isTesting) {

		List<Employee> employees = processEmployeesAndTheirProjects(file, isTesting);
		HashMap<Integer, EmployeePairs> employeePairs = new HashMap<>();

		switch (employees.size()) {

		case 0:
			logger.info("No employees available. Therefore no pontential pairs can be formed.");
			return new HashMap<Integer, EmployeePairs>();
		case 1:
			logger.info("Not enough employees available to form potential pairs.");
			if (!isTesting) {
				JOptionPane.showMessageDialog(null,
						"Insufficient number of employees. Therefore no potential pairs can be formed.");
			}
			return new HashMap<Integer, EmployeePairs>();
		default:
			employeePairs = findPairsWithCommonWorkingPeriod(employees, employeePairs);
		}
		logger.info("EMPLOYEE PAIRS FOUND:");

		for (Entry<Integer, EmployeePairs> employeePair : employeePairs.entrySet()) {
			logger.info("Employee ID #1: " + employeePair.getValue().getEmployee1ID() + " \n Employee ID #2: "
					+ employeePair.getValue().getEmployee2ID().get(0) + " \n Projects ID: "
					+ employeePair.getValue().getProjectID().get(0) + " \n DAYS WORKED: "
					+ employeePair.getValue().getDaysWorkedTogetherOnCommonProject().get(0));
		}

		if (employeePairs.size() == 0) {
			if (!isTesting) {
				JOptionPane.showMessageDialog(null,
						"Did not find any pair of employees working on the same project for the given periods.");
			}
		}

		return employeePairs;
	}

	/**
	 * Iterates over all pairs and finds pairs with common working period on one or
	 * more projects.
	 * 
	 * @param employees
	 * @param employeePairs
	 * @return
	 */
	private static HashMap<Integer, EmployeePairs> findPairsWithCommonWorkingPeriod(final List<Employee> employees,
			final HashMap<Integer, EmployeePairs> employeePairs) {

		// iterate over every single employee
		for (int i = 0; i < employees.size(); i++) {

			// get all projects of the first employee
			Employee empl1 = employees.get(i);
			List<Integer> e1Projects = empl1.getProjects();

			// iterate over all remaining employees
			for (int j = 0; j < employees.size(); j++) {

				// get all projects of the second employee
				Employee empl2 = employees.get(j);
				List<Integer> e2Projects = empl2.getProjects();

				// in case its the same employee skip the remaining
				if (empl1.getEmployeeId() == empl2.getEmployeeId()) {
					continue;
				}

				// check each project of the first employee
				for (int k = 0; k < e1Projects.size(); k++) {

					// against each project of all remaining employees
					for (int l = 0; l < e2Projects.size(); l++) {

						// both employees have worked on a given project
						if (e1Projects.get(k) == e2Projects.get(l)) {

							// check if there is an overlap between their working periods on this particular
							// project
							int overlapInDays = DateUtility.computeOverlapOfDateRages(empl1.getStartDate(k),
									empl1.getEndDate(k), empl2.getStartDate(l), empl2.getEndDate(l));

							// both employees have worked at least one day together on the same project
							if (overlapInDays >= 1) {

								logger.info("Employee pair is: " + empl1.getEmployeeId() + ", " + empl2.getEmployeeId()
										+ ", " + e1Projects.get(k) + ", " + overlapInDays);

								EmployeePairs pairsOfEmpl1 = employeePairs.get(empl1.getEmployeeId());

								if (pairsOfEmpl1 != null) {

									// get the employees with which employee 1 has worked on common projects (found
									// up to this point)
									List<Integer> employee2List = pairsOfEmpl1.getEmployee2ID();
									int employee2Idx = employee2List.indexOf(empl2.getEmployeeId());

									// a this project and the days worked on it to the list with all other common
									// projects between the pair
									if (employee2Idx != -1) {
										pairsOfEmpl1.getProjectID().get(employee2Idx).add(e1Projects.get(k));
										pairsOfEmpl1.getDaysWorkedTogetherOnCommonProject().get(employee2Idx)
												.add(overlapInDays);

										// add another pair
									} else {
										addNewPairForEmployee(pairsOfEmpl1, e1Projects, k, overlapInDays, empl2);
									}

									// add this pair as the first pair for empl1
								} else {

									employeePairs.put(empl1.getEmployeeId(), new EmployeePairs(empl1.getEmployeeId()));
									pairsOfEmpl1 = employeePairs.get(empl1.getEmployeeId());

									addNewPairForEmployee(pairsOfEmpl1, e1Projects, k, overlapInDays, empl2);
								}
							}
						}
					}
				}
			}
		}
		return employeePairs;
	}

	private static void addNewPairForEmployee(EmployeePairs pairsOfEmpl1, List<Integer> e1Projects, int k,
			int overlapInDays, Employee empl2) {

		pairsOfEmpl1.getEmployee2ID().add(empl2.getEmployeeId());
		pairsOfEmpl1.getProjectID().add(new ArrayList<>());
		pairsOfEmpl1.getProjectID().get(pairsOfEmpl1.getProjectID().size() - 1).add(e1Projects.get(k));
		pairsOfEmpl1.getDaysWorkedTogetherOnCommonProject().add(new ArrayList<>());
		pairsOfEmpl1.getDaysWorkedTogetherOnCommonProject()
				.get(pairsOfEmpl1.getDaysWorkedTogetherOnCommonProject().size() - 1).add(overlapInDays);

	}

	/**
	 * Finds and stores for each emlpoyee its id, project id, start and end date for
	 * every project the employee has participated in.
	 * 
	 * @param file - list with csv separated values in the form EmployeeID,
	 *             ProjectID, DateFrom, DateTo
	 * @return a list of employees
	 * 
	 * @author Valentin
	 */
	private static List<Employee> processEmployeesAndTheirProjects(final File file, final boolean isTesting) {

		List<Employee> employees = new ArrayList<Employee>();
		BufferedReader br = null;
		short currentRow = 1;

		// read the data from a csv-file
		try {

			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);

			// should hold four values employee id, project id, start and end date
			String line;
			String values[];

			line = br.readLine();

			// the file is empty
			if (line == null || line.equals("")) {
				logger.info("Selected file: " + file.getName() + " is empty.");
				if (!isTesting) {
					JOptionPane.showMessageDialog(null, "Selected file is empty! Please choose another file.");
				}
				return new ArrayList<Employee>();

			} else {

				String[] firstLine = line.split(", ");

				// skip first line with headers
				if (firstLine.length == 4 && firstLine[0].trim().equalsIgnoreCase("EmpID")
						&& firstLine[1].trim().equalsIgnoreCase("ProjectID")
						&& firstLine[2].trim().equalsIgnoreCase("DateFrom")
						&& firstLine[3].trim().equalsIgnoreCase("DateTo")) {

					// store the employee entry data if no headers available in first row
				} else if (firstLine.length == 4) {

					try {

						Employee empl = new Employee(Integer.parseInt(firstLine[0].trim()));
						empl.addProjectId(Integer.parseInt(firstLine[1].trim()));
						empl.addStartDate(DateUtility.convertStringToDate(firstLine[2].trim()));
						empl.addEndDate(DateUtility.convertStringToDate(firstLine[3].trim()));

						employees.add(empl);

						logger.info("EmpID: " + empl.getEmployeeId() + ", ProjectID: " + empl.getProjectId(0)
								+ ", Start date: " + empl.getStartDate(0) + ", End Date: " + empl.getEndDate(0));

					} catch (IllegalFieldValueException e) {
						logger.severe(e.getMessage());
						if (!isTesting) {
							JOptionPane.showMessageDialog(null, e.getMessage());
						}
						return new ArrayList<Employee>();
					}

				} else {
					logger.severe("The first line does not have the correct syntax, i.e. 4 comma separated values.");
					if (!isTesting) {
						JOptionPane.showMessageDialog(null,
								"The first line does not have the correct syntax, i.e. 4 comma separated values. \n Please make sure that each row has exactly four values.");
					}
					return new ArrayList<Employee>();
				}
			}

			/*
			 * Retrieves all employees and their respective projects together with the start
			 * and end date of each project. In case a row with fewer or more than four
			 * values is found, program execution is terminated and a pop-up dialog is shown
			 * to the user pointing out the row on which the error was found.
			 */
			while ((line = br.readLine()) != null) {

				// holds the current row number in case an error occurs
				currentRow += 1;
				values = line.split(", ");
				int employeeId = Integer.parseInt(values[0].trim());
				boolean employeeExists = false;

				for (Employee employee : employees) {

					try {

						// employee already exists, only add project and work period
						if (employee.getEmployeeId() == employeeId) {
							employee.addProjectId(Integer.parseInt(values[1].trim()));
							employee.addStartDate(DateUtility.convertStringToDate(values[2].trim()));
							employee.addEndDate(DateUtility.convertStringToDate(values[3].trim()));
							employeeExists = true;

							int lastElement = employee.getListSize() - 1;

							logger.info("Existing employee with EmpID: " + employee.getEmployeeId()
									+ ", added new project with id: " + employee.getProjectId(lastElement)
									+ ", start date: " + employee.getStartDate(lastElement) + ", end Date: "
									+ employee.getEndDate(lastElement));
						}

					} catch (ArrayIndexOutOfBoundsException e) {
						logger.severe("Error found on row " + currentRow);
						if (!isTesting) {
							JOptionPane.showMessageDialog(null, "Program execution terminated. Row " + currentRow
									+ " does not have the correct syntax. \n Row data: " + line);
						}
						return new ArrayList<Employee>();
					} catch (IllegalFieldValueException e) {
						logger.severe(e.getMessage());
						if (!isTesting) {
							JOptionPane.showMessageDialog(null, e.getMessage());
						}
						return new ArrayList<Employee>();
					}

				}

				try {

					// otherwise add new employee
					if (!employeeExists) {

						// create an employee
						Employee empl = new Employee(employeeId);
						empl.addProjectId(Integer.parseInt(values[1].trim()));
						empl.addStartDate(DateUtility.convertStringToDate(values[2].trim()));
						empl.addEndDate(DateUtility.convertStringToDate(values[3].trim()));
						// add to list of employees
						employees.add(empl);

						logger.info("New employee with EmpID: " + empl.getEmployeeId() + ", ProjectID: "
								+ empl.getProjectId(0) + ", Start date: " + empl.getStartDate(0) + ", End Date: "
								+ empl.getEndDate(0));
					}

				} catch (ArrayIndexOutOfBoundsException e) {
					logger.severe("Error found on row " + currentRow);
					if (!isTesting) {
						JOptionPane.showMessageDialog(null, "Program execution terminated. Row " + currentRow
								+ " does not have the correct syntax. \n Row data: " + line);
					}
					return new ArrayList<Employee>();

				} catch (IllegalFieldValueException e) {
					logger.severe(e.getMessage());
					if (!isTesting) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
					return new ArrayList<Employee>();
				}

			}

		} catch (FileNotFoundException e) {
			logger.severe("No such file " + file.getName());
			if (!isTesting) {
				JOptionPane.showMessageDialog(null, "No such file " + file.getName() + " exists.");
			}
			return new ArrayList<Employee>();

		} catch (IOException e) {
			logger.severe("An I/O Exception occured while retrieving data from the file.");
			if (!isTesting) {
				JOptionPane.showMessageDialog(null,
						"An error occured while reading the selected file. Please try again.");
			}
			return new ArrayList<Employee>();

		} finally {

			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				logger.severe("An I/O exception occured while closing the BufferedReader.");
				if (!isTesting) {
					JOptionPane.showMessageDialog(null, "An error occured while closing the file.");
				}
				return employees;
			}
		}
		return employees;
	}

	/**
	 * Goes through the set of employeePairs and finds the one that has the most
	 * days spend together working on common projects.
	 * 
	 * @param employeePairs - the pairs found
	 * 
	 * @return array holding both employee ids, project ids and the work period in days
	 * 
	 * @author Valentin
	 */
	public static String[] findLongestWorkingEmployeePair(HashMap<Integer, EmployeePairs> employeePairs) {

		String empl1ID = null;
		String empl2ID = null;
		String projectID = null;
		String currProjects = null;
		int daysWorkedTogether = 0;
		int days = 0;

		for (EmployeePairs emplPair : employeePairs.values()) {

			for (int i = 0; i < emplPair.getEmployee2ID().size(); i++) {

				List<Integer> projectIDs = emplPair.getProjectID().get(i);
				List<Integer> daysWorked = emplPair.getDaysWorkedTogetherOnCommonProject().get(i);
				// reset the days and projects for every new pair
				days = 0;
				currProjects = "";

				// get all projects and the total amount of days the pair have worked on them
				for (int j = 0; j < projectIDs.size(); j++) {
					days += daysWorked.get(j);
					currProjects += String.valueOf(projectIDs.get(j) + " ");

				}
				// new pair with more days working together on common projects has been found
				if (daysWorkedTogether < days) {
					empl1ID = String.valueOf(emplPair.getEmployee1ID());
					empl2ID = String.valueOf(emplPair.getEmployee2ID().get(i));
					projectID = currProjects;
					daysWorkedTogether = days;
				}
			}
		}

		return new String[] { empl1ID, empl2ID, projectID, String.valueOf(daysWorkedTogether) };
	}

}
