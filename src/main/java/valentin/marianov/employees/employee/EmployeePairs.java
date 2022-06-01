package valentin.marianov.employees.employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to store for every employee the employees with which he/she has worked
 * together on one or more projects within a given time period.
 * <p>
 * At later point those pairs are used to find the pair with the longest period
 * of work,
 * 
 * @author Valentin
 */
public class EmployeePairs {

	int employee1ID;
	List<Integer> employee2ID;
	List<List<Integer>> projectID;
	List<List<Integer>> daysWorkedTogether;

	public EmployeePairs(int employee1ID) {

		this.employee1ID = employee1ID;
		this.employee2ID = new ArrayList<Integer>();
		this.projectID = new ArrayList<>();
		this.daysWorkedTogether = new ArrayList<>();
	}

	public int getEmployee1ID() {
		return this.employee1ID;
	}

	public List<Integer> getEmployee2ID() {
		return this.employee2ID;
	}

	public List<List<Integer>> getProjectID() {
		return this.projectID;
	}

	public List<List<Integer>> getDaysWorkedTogetherOnCommonProject() {
		return this.daysWorkedTogether;
	}

	public void addEmployee2(int empl2ID) {
		this.employee2ID.add(empl2ID);
	}

	public void addProjectID(int index, int projectID) {
		this.projectID.get(index).add(projectID);
	}

	public void addDaysWorkedTogether(int index, int daysWorkedTogether) {
		this.daysWorkedTogether.get(index).add(daysWorkedTogether);
	}

}
