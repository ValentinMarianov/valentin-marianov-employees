package valentin.marianov.employees.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import valentin.marianov.employees.employee.EmployeePairs;
import valentin.marianov.employees.employee.EmployeeProcessing;

/**
 * Creates a frame consisting of the following elements:
 * <ul>
 * <li>label
 * <li>text field - showing the current select file
 * <li>browse button - used to browse users local directory and
 * <li>datagrid - table displaying the employee ids, project ids and the work
 * period (in days) for the pair having worked together on common projects for
 * the longest time
 * </ul>
 * 
 * @author Valentin
 */
public class EmployeePairUI {

	private static JFrame frame;

	/**
	 * Creates and displays a window with all the needed interaction elements.
	 * 
	 * @author Valentin
	 */
	public static void initializeFrame() {

		frame = new JFrame("Pairs of employees who have worked together");

		addElementsToFrame();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	/**
	 * Creates all UI elements displayed within the frame. Those include a main
	 * panel holding two subpanels, where the one consists of a label, text field
	 * and a browse button used to select a csv-file from the local repository. The
	 * later holds a data grid in the form of a table used to visualize the pair
	 * with the longest period of working time on common projects.
	 * 
	 * @author Valentin
	 */
	private static void addElementsToFrame() {

		// main panel will hold two child panels
		JPanel mainPanel = new JPanel();
		// child panels for browse button and a table holding the results
		JPanel browseFilePanel = new JPanel();
		JPanel employeePairsPanel = new JPanel();

		// set flow layout for the panel holding a label, text field showing the
		// selected file and 'Browse' button
		LayoutManager layout = new FlowLayout();
		browseFilePanel.setLayout(layout);

		// create label, text field and a button
		final JLabel label = new JLabel("Selected file:");
		JTextField filePathTextField = new JTextField(null, "", 26);
		filePathTextField.setEditable(false);
		JButton browseButton = new JButton("Browse");

		// add the above elements to the first panel
		browseFilePanel.add(label);
		browseFilePanel.add(filePathTextField);
		browseFilePanel.add(browseButton);

		// create table
		DefaultTableModel tableModel = new DefaultTableModel();
		JTable table = new JTable(tableModel);

		// make it scrollable
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);

		addActionListenerForBrowseButton(browseButton, filePathTextField, tableModel);

		// add columns to table
		tableModel.addColumn("Employee ID #1");
		tableModel.addColumn("Employee ID #2");
		tableModel.addColumn("Project ID");
		tableModel.addColumn("Days worked");
		employeePairsPanel.add(scrollPane);

		// center the content of each cell in the datagrid
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// center each cell content
		for (int x = 0; x < table.getColumnCount(); x++) {
			table.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
		}

		// add both sub-panels to main panel
		mainPanel.add(browseFilePanel);
		mainPanel.add(employeePairsPanel);

		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
	}

	/**
	 * Trigged once the user has selected a file. The later is then processed and in
	 * order to find the pair with the longest period of work on common projects (if
	 * any is available).
	 * 
	 * @param browseButton
	 * @param filePathTextField
	 * @param tableModel
	 * 
	 * @author Valentin
	 */
	private static void addActionListenerForBrowseButton(JButton browseButton, JTextField filePathTextField,
			DefaultTableModel tableModel) {

		browseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int option = fileChooser.showOpenDialog(frame);

				if (option == JFileChooser.APPROVE_OPTION) {

					File file = fileChooser.getSelectedFile();
					filePathTextField.setText(file.getName());
					// remove previos results
					tableModel.setRowCount(0);

					// contains all employees having worked together at a project (result may be an
					// empty list when no such pairs have been found)
					HashMap<Integer, EmployeePairs> employeePairs = EmployeeProcessing.findAllEmployeePairs(file,
							false);

					if (employeePairs.size() > 0) {
						String[] longestTogetherWorkingPair = EmployeeProcessing
								.findLongestWorkingEmployeePair(employeePairs);

						tableModel.addRow(new Object[] { longestTogetherWorkingPair[0], longestTogetherWorkingPair[1],
								longestTogetherWorkingPair[2], longestTogetherWorkingPair[3] });
						return;
					}

					filePathTextField.setText("");
				}
			}
		});
	}

};
