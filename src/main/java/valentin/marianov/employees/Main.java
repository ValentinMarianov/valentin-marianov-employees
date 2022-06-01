package valentin.marianov.employees;

import java.util.logging.Level;
import java.util.logging.Logger;

import valentin.marianov.employees.ui.EmployeePairUI;

/**
 * Initializes the User Interface (JFrame) containing a browse button in order
 * to select a file as well as a datagrid structure. The previous is then
 * processed and stored in order to find employees who have worked together on a
 * project within the same date range (so called employee pairs).
 * <p>
 * After finding all pairs, the next task is to determine the pair with the
 * longest working period on one or more projects. Finally, the result is
 * displayed in a table with four columns, holding the ids of both employees and
 * the projects but also the time period (in days) that they have worked
 * together.
 * 
 * @author Valentin
 */
public class Main {

	private static Logger logger = Logger.getLogger("employees-logger");

	public static void main(String[] args) {
		logger.setLevel(Level.ALL);
		EmployeePairUI.initializeFrame();
	}

}
