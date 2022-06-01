package valentin.marianov.employees.employee;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Holds the data about every project a given employee has participated in
 * together with the begin and end period of work.
 * 
 * @author Valentin
 */
public class Employee {
	
	private int employeeId;
	private List<Integer> projectIds;
	private List<Date> startDates;
	private List<Date> endDates;
	
	public Employee(final int employeeId) {
		this.employeeId  = employeeId;
		this.projectIds = new ArrayList<Integer>();
		this.startDates = new ArrayList<Date>();
		this.endDates = new ArrayList<Date>();	
	}
	
	public int getEmployeeId() {
		return this.employeeId;
	}
	
	public int getProjectId(final int index) {
		return this.projectIds.get(index);
	}
	
	public List<Integer> getProjects() {
		return this.projectIds;
	}
	
	public int getListSize() {
		return this.projectIds.size();
	}
	
	public void addProjectId(final int val) {
		this.projectIds.add(val);
	}
	
	public Date getStartDate(final int index) {
		return this.startDates.get(index);
	}
	
	public void addStartDate(final Date val) {
		this.startDates.add(val);
	}
	
	public Date getEndDate(final int index) {
		return this.endDates.get(index);
	}
	
	public void addEndDate(final Date val) {
		this.endDates.add(val);
	}
	 
};