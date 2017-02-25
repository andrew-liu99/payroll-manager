/**
 * Payroll Manager interface
 * @author Andrew Liu
 * @date 11/21/2016
 * @teacher Mr. Reid
 * @coursecode ICS4U1
 */
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class PayrollWindow extends javax.swing.JFrame {

	// declare frames
	JFrame frame;
	JFrame sortFrame;
	JFrame resetAllConfirmationFrame;
	JFrame printFrame;
	JFrame printConfirmationFrame;
	JFrame addFrame;
	JFrame editFrame;
	JFrame resetConfirmationFrame;
	JFrame deleteConfirmationFrame;
	JFrame saveFrame;

	JList<String> list;
	// initialize database
	Database JPAS = new Database("rawdata.txt");
	
	private JTextField hourlyRateTextField;
	private JTextField hoursWorkedTextField;
	private JTextField totalOwedTextField;
	private JTextField nameTextField;
	private JScrollPane scrollPane_1;
	private JTextField changePositionTextField;
	private JTextField addHoursTextField;
	private JTextField removeHoursTextField;
	private JTextField newRateDollarTextField;
	private JTextField newRateCentTextField;
	private JLabel totalHoursLabel;
	private JLabel totalAmountLabel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PayrollWindow window = new PayrollWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */

	// run the main frame
	public PayrollWindow() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */

	// function to refresh the employee list once changes are made
	public void refreshList(JList<String> list)
	{
		list.setModel(new AbstractListModel<String>() {
			String[] values = JPAS.getEmployeeList();
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
		});
	}

	// adds a listener to employee list that detects changes in selected index
	private void addListenerToList()
	{
		list.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if (e.getValueIsAdjusting() == false)
				{
				}
				else
				{	
					// fill in employee information of the selected index
					if(list.getSelectedIndex()!=-1)
					{
						// get hourly rate
						String rateString = String.valueOf(JPAS.employeeDatabase[list.getSelectedIndex()].getRate());
						String rateCents = rateString.substring(rateString.length()-2,rateString.length());
						String rateDollars = rateString.substring(0,rateString.length()-2);
						rateString = ("$"+rateDollars+"."+rateCents);
						hourlyRateTextField.setText(rateString);

						// get hours worked
						String hoursString = String.valueOf(JPAS.employeeDatabase[list.getSelectedIndex()].getHoursWorked());
						hoursWorkedTextField.setText(hoursString);

						// get total owed
						String totalString = String.valueOf(JPAS.employeeDatabase[list.getSelectedIndex()].getRate()*JPAS.employeeDatabase[list.getSelectedIndex()].getHoursWorked());
						// ensures a case for $0 owed
						if (totalString.length()==1)
						{
							totalString = "000";
						}		
						String totalCents = totalString.substring(totalString.length()-2,totalString.length());
						String totalDollars = totalString.substring(0,totalString.length()-2);
						totalString = ("$"+totalDollars+"."+totalCents);
						totalOwedTextField.setText(totalString);
					}

					
				}
			}
		});
	}
	
	// main frame
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 579, 401);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("JPAS Payroll Manager");
		
		// store database
		JPAS.readFile();
		JPAS.storeDatabase();
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(34, 36, 243, 265);
		frame.getContentPane().add(scrollPane);

		// employee list to store employees "last name, first name - position"
		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(new AbstractListModel<String>() {
			String[] values = JPAS.getEmployeeList();
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
		});
		addListenerToList();

		scrollPane.setViewportView(list);

		JLabel lblEmployees = new JLabel("Employees");
		lblEmployees.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmployees.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblEmployees.setBounds(34, 11, 243, 21);
		frame.getContentPane().add(lblEmployees);

		// button that transfers user to the frame to add a new employee
		JButton btnAdd = new JButton("Add Employee");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addFrame();
				frame.setVisible(false);
			}
		});
		btnAdd.setBounds(348, 36, 144, 23);
		frame.getContentPane().add(btnAdd);

		// button that saves the database for future use
		JButton btnSaveDatabase = new JButton("Save Database");
		btnSaveDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					saveFrame();
				} catch (IOException e1) {
					System.out.print("Cannot write to the file.");
					e1.printStackTrace();
				}
				frame.setVisible(false);
			}
		});
		btnSaveDatabase.setBounds(348, 315, 144, 23);
		frame.getContentPane().add(btnSaveDatabase);

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new LineBorder(Color.GRAY));
		infoPanel.setBounds(318, 78, 207, 92);
		frame.getContentPane().add(infoPanel);
		infoPanel.setLayout(null);

		JLabel lblTotalOwed = new JLabel("Total Owed:");
		lblTotalOwed.setBounds(20, 64, 88, 14);
		infoPanel.add(lblTotalOwed);

		totalOwedTextField = new JTextField();
		totalOwedTextField.setBounds(118, 61, 73, 20);
		infoPanel.add(totalOwedTextField);
		totalOwedTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		totalOwedTextField.setEditable(false);
		totalOwedTextField.setColumns(10);

		hoursWorkedTextField = new JTextField();
		hoursWorkedTextField.setBounds(118, 36, 73, 20);
		infoPanel.add(hoursWorkedTextField);
		hoursWorkedTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		hoursWorkedTextField.setEditable(false);
		hoursWorkedTextField.setColumns(10);

		JLabel lblHoursWorked = new JLabel("Hours Worked:");
		lblHoursWorked.setBounds(20, 39, 88, 14);
		infoPanel.add(lblHoursWorked);

		JLabel lblHourlyRate = new JLabel("Hourly Rate:");
		lblHourlyRate.setBounds(20, 14, 88, 14);
		infoPanel.add(lblHourlyRate);

		hourlyRateTextField = new JTextField();
		hourlyRateTextField.setBounds(118, 11, 73, 20);
		infoPanel.add(hourlyRateTextField);
		hourlyRateTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		hourlyRateTextField.setEditable(false);
		hourlyRateTextField.setColumns(10);

		// button that transfers user to the frame to edit an employee's info
		JButton btnEditEmployee = new JButton("Edit Employee");
		btnEditEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedIndex() != -1)
				{
					editFrame(list.getSelectedIndex());
					frame.setVisible(false);
				}

			}
		});
		btnEditEmployee.setBounds(348, 186, 144, 23);
		frame.getContentPane().add(btnEditEmployee);

		// button that transfers user to the frame to print statements
		JButton btnPrintStatements = new JButton("Print Statements");
		btnPrintStatements.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printFrame();
				frame.setVisible(false);
			}
		});
		btnPrintStatements.setBounds(348, 228, 144, 23);
		frame.getContentPane().add(btnPrintStatements);

		// button that transfers user to the frame to sort the employee list
		JButton btnSort = new JButton("Sort");
		btnSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sortFrame();
				frame.setVisible(false);;
			}
		});
		btnSort.setBounds(34, 316, 80, 23);
		frame.getContentPane().add(btnSort);

		// button that transfers user to the frame to reset all employees' hours to 0
		JButton btnResetAllHours = new JButton("Reset All Hours");
		btnResetAllHours.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetAllConfirmationFrame();
				frame.setVisible(false);
			}
		});
		btnResetAllHours.setBounds(124, 316, 150, 23);
		frame.getContentPane().add(btnResetAllHours);

		// allows user to see how much all employees have worked
		totalHoursLabel = new JLabel();
		totalHoursLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalHoursLabel.setBounds(287, 252, 266, 29);
		frame.getContentPane().add(totalHoursLabel);
		// allows user to see how much he/she owes all employees
		totalAmountLabel = new JLabel();
		totalAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalAmountLabel.setBounds(287, 272, 266, 29);
		frame.getContentPane().add(totalAmountLabel);
		// fill in the totals
		resetTotals();
	}
	
	// fill in, or reset the totals when changes are made
	public void resetTotals()
	{
		totalHoursLabel.setText("Total Hours Worked: "+JPAS.getTotalHours(0,0)+" hours");
		totalAmountLabel.setText("Total Amount Owed: "+JPAS.rateToString(JPAS.getTotalOwed(0,0)));
	}
	
	// frame to sort employees in the employee list
	public void sortFrame()
	{
		sortFrame = new JFrame();
		sortFrame.setBounds(100, 100, 450, 300);
		sortFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sortFrame.setVisible(true);
		sortFrame.setTitle("Sort Employees");

		final String filename = "statement.csv";

		// don't sort and go back to main frame
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(20, 10, 65, 23);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(true);
				sortFrame.dispose();
			}
		});
		sortFrame.getContentPane().setLayout(null);
		sortFrame.getContentPane().add(btnBack);

		JLabel lblPrintInAlphabetical = new JLabel("Sort in alphabetical order of last names");
		lblPrintInAlphabetical.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPrintInAlphabetical.setBounds(54, 70, 261, 17);
		sortFrame.getContentPane().add(lblPrintInAlphabetical);

		JLabel lblPrintInAscending = new JLabel("Sort in ascending order of hourly rate");
		lblPrintInAscending.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPrintInAscending.setBounds(54, 105, 261, 17);
		sortFrame.getContentPane().add(lblPrintInAscending);

		JLabel lblSortInAscending = new JLabel("Sort in ascending order of hours worked");
		lblSortInAscending.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSortInAscending.setBounds(54, 140, 261, 17);
		sortFrame.getContentPane().add(lblSortInAscending);

		JLabel lblPrintInAscending_1 = new JLabel("Sort in ascending order of amount owed");
		lblPrintInAscending_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPrintInAscending_1.setBounds(54, 175, 261, 17);
		sortFrame.getContentPane().add(lblPrintInAscending_1);

		// sort by alphabetical order
		JButton btnPrintAlphabetical = new JButton("Sort");
		btnPrintAlphabetical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPAS.sortLastName();
				refreshList(list);
				frame.setVisible(true);
				sortFrame.dispose();
			}
		});
		btnPrintAlphabetical.setBounds(317, 70, 65, 23);
		sortFrame.getContentPane().add(btnPrintAlphabetical);
		// sort by hourly rate, low to high
		JButton btnPrintHourlyRate = new JButton("Sort");
		btnPrintHourlyRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPAS.sortHourlyRate();
				refreshList(list);
				frame.setVisible(true);
				sortFrame.dispose();
			}
		});
		btnPrintHourlyRate.setBounds(317, 104, 65, 23);
		sortFrame.getContentPane().add(btnPrintHourlyRate);
		// sort by hours worked, low to high
		JButton btnPrintHoursWorked = new JButton("Sort");
		btnPrintHoursWorked.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPAS.sortHoursWorked();
				refreshList(list);
				frame.setVisible(true);
				sortFrame.dispose();
			}
		});
		btnPrintHoursWorked.setBounds(317, 140, 65, 23);
		sortFrame.getContentPane().add(btnPrintHoursWorked);
		// sort by amount owed, low to high
		JButton btnPrintOwed = new JButton("Sort");
		btnPrintOwed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPAS.sortTotalOwed();
				refreshList(list);
				frame.setVisible(true);
				sortFrame.dispose();
			}
		});
		btnPrintOwed.setBounds(317, 175, 65, 23);
		sortFrame.getContentPane().add(btnPrintOwed);
	}

	// frame to confirm resetting all employees' hours to 0
	public void resetAllConfirmationFrame()
	{
		resetAllConfirmationFrame = new JFrame();
		resetAllConfirmationFrame.setBounds(125, 200, 400, 150);
		resetAllConfirmationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		resetAllConfirmationFrame.getContentPane().setLayout(null);
		resetAllConfirmationFrame.setVisible(true);
		resetAllConfirmationFrame.setTitle("Warning - Reset All Hours?");

		JLabel confirmation = new JLabel("<html>Have you fully paid all employees what is<br>owed and would like to reset all hours to 0?</html>");
		confirmation.setFont(new Font("Tahoma", Font.PLAIN, 13));
		confirmation.setHorizontalAlignment(SwingConstants.CENTER);
		confirmation.setBounds(10, 11, 364, 47);
		resetAllConfirmationFrame.getContentPane().add(confirmation);

		JButton btnYes = new JButton("Yes");
		btnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int i=0; i<JPAS.getEmployeeCount(); i++)
				{
					JPAS.employeeDatabase[i].setHoursWorked(0);
				}
				frame.setVisible(true);
				resetAllConfirmationFrame.dispose();

				// set index to -1 to info in box on right side so its cleared
				list.setSelectedIndex(-1);
			}
		});
		btnYes.setBounds(96, 69, 67, 23);
		resetAllConfirmationFrame.getContentPane().add(btnYes);
		// go back to main frame w/o resetting all hours
		JButton btnNo = new JButton("No");
		btnNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent n) {
				frame.setVisible(true);
				resetAllConfirmationFrame.dispose();
			}
		});
		btnNo.setBounds(210, 69, 67, 23);
		resetAllConfirmationFrame.getContentPane().add(btnNo);

	}

	// frame to select how to print CSV statement
	public void printFrame()
	{
		printFrame = new JFrame();
		printFrame.setBounds(100, 100, 450, 300);
		printFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		printFrame.setVisible(true);
		printFrame.setTitle("Print Employee Information");

		final String filename = "statement.csv";

		JButton btnBack = new JButton("Back");
		btnBack.setBounds(20, 10, 65, 23);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(true);
				printFrame.dispose();
			}
		});
		printFrame.getContentPane().setLayout(null);
		printFrame.getContentPane().add(btnBack);

		JLabel lblPrintInAlphabetical = new JLabel("Print in alphabetical order of last names");
		lblPrintInAlphabetical.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPrintInAlphabetical.setBounds(54, 70, 261, 17);
		printFrame.getContentPane().add(lblPrintInAlphabetical);

		JLabel lblPrintInAscending = new JLabel("Print in ascending order of hourly rate");
		lblPrintInAscending.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPrintInAscending.setBounds(54, 105, 261, 17);
		printFrame.getContentPane().add(lblPrintInAscending);

		JLabel label = new JLabel("Print in ascending order of hours worked");
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label.setBounds(54, 140, 261, 17);
		printFrame.getContentPane().add(label);

		JLabel lblPrintInAscending_1 = new JLabel("Print in ascending order of amount owed");
		lblPrintInAscending_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPrintInAscending_1.setBounds(54, 175, 261, 17);
		printFrame.getContentPane().add(lblPrintInAscending_1);

		// print in alphabetical order of employees
		JButton btnPrintAlphabetical = new JButton("Print");
		btnPrintAlphabetical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				printConfirmationFrame("alphabetical",filename);
				printFrame.dispose();
			}
		});
		btnPrintAlphabetical.setBounds(317, 70, 65, 23);
		printFrame.getContentPane().add(btnPrintAlphabetical);
		// print in ascending order of hourly rate
		JButton btnPrintHourlyRate = new JButton("Print");
		btnPrintHourlyRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printConfirmationFrame("hourlyRate",filename);
				printFrame.dispose();
			}
		});
		btnPrintHourlyRate.setBounds(317, 104, 65, 23);
		printFrame.getContentPane().add(btnPrintHourlyRate);
		// print in ascending order of hours worked
		JButton btnPrintHoursWorked = new JButton("Print");
		btnPrintHoursWorked.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printConfirmationFrame("hoursWorked",filename);
				printFrame.dispose();
			}
		});
		btnPrintHoursWorked.setBounds(317, 140, 65, 23);
		printFrame.getContentPane().add(btnPrintHoursWorked);
		// print in ascending order of amount owed
		JButton btnPrintOwed = new JButton("Print");
		btnPrintOwed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printConfirmationFrame("totalOwed",filename);
				printFrame.dispose();
			}
		});
		btnPrintOwed.setBounds(317, 175, 65, 23);
		printFrame.getContentPane().add(btnPrintOwed);
	}

	// frame to confirm creating a CSV file of the information
	public void printConfirmationFrame(final String order, final String filename)
	{
		printConfirmationFrame = new JFrame();
		printConfirmationFrame.setBounds(125, 200, 400, 150);
		printConfirmationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		printConfirmationFrame.getContentPane().setLayout(null);
		printConfirmationFrame.setTitle("Print CSV Info");

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 11, 364, 57);
		printConfirmationFrame.getContentPane().add(scrollPane);

		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setText("A CVS statement will be created and opened. It is titled '"+filename+"' and is stored at '"+System.getProperty("user.dir")+"/"+filename+"'.");

		// print the CSV file based on the selected order
		JButton btnOkay = new JButton("Okay");
		btnOkay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JPAS.saveDatabase();
				} catch (IOException e2) {
					System.out.print("Cannot write to the file.");
					e2.printStackTrace();
				}
				if(order.equals("alphabetical"))
				{
					JPAS.sortLastName();
				}
				else if(order.equals("hourlyRate"))
				{
					JPAS.sortHourlyRate();
				}
				else if(order.equals("hoursWorked"))
				{
					JPAS.sortHoursWorked();
				}
				else if(order.equals("totalOwed"))
				{
					JPAS.sortTotalOwed();
				}
				// save CSV file and open if possible
				try {
					JPAS.saveCSV(filename);
					frame.setVisible(true);
					printConfirmationFrame.dispose();
				} catch (IOException e1) {
					System.out.println("Cannot write to the file.");
					e1.printStackTrace();
				}
			}
		});
		btnOkay.setBounds(156, 77, 66, 23);
		printConfirmationFrame.getContentPane().add(btnOkay);
		printConfirmationFrame.setVisible(true);
	}

	// frame to add new employee
	public void addFrame()
	{
		addFrame = new JFrame();
		addFrame.setBounds(100, 100, 450, 300);
		addFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addFrame.getContentPane().setLayout(null);
		addFrame.setVisible(true);
		addFrame.setTitle("Add Employee");

		JLabel lblAddEmployee = new JLabel("Add Employee");
		lblAddEmployee.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAddEmployee.setVerticalAlignment(SwingConstants.BOTTOM);
		lblAddEmployee.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddEmployee.setBounds(10, 11, 414, 17);
		addFrame.getContentPane().add(lblAddEmployee);

		JLabel lblNewLabel = new JLabel("Last Name");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(101, 54, 96, 26);
		addFrame.getContentPane().add(lblNewLabel);

		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFirstName.setBounds(101, 86, 96, 26);
		addFrame.getContentPane().add(lblFirstName);

		JLabel lblPosition = new JLabel("Position");
		lblPosition.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPosition.setBounds(101, 118, 96, 26);
		addFrame.getContentPane().add(lblPosition);

		JLabel lblHourlyRate = new JLabel("Hourly Rate");
		lblHourlyRate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblHourlyRate.setBounds(101, 150, 96, 26);
		addFrame.getContentPane().add(lblHourlyRate);

		final JTextField lastNameField = new JTextField();
		lastNameField.setBounds(207, 59, 124, 17);
		addFrame.getContentPane().add(lastNameField);
		lastNameField.setColumns(10);

		final JTextField firstNameField = new JTextField();
		firstNameField.setColumns(10);
		firstNameField.setBounds(207, 91, 124, 17);
		addFrame.getContentPane().add(firstNameField);

		final JTextField positionField = new JTextField();
		positionField.setColumns(10);
		positionField.setBounds(207, 123, 124, 17);
		addFrame.getContentPane().add(positionField);

		final JTextField hourlyRateDollarsField = new JTextField();
		hourlyRateDollarsField.setColumns(10);
		hourlyRateDollarsField.setBounds(207, 155, 55, 17);
		addFrame.getContentPane().add(hourlyRateDollarsField);

		final JTextField hourlyRateCentsField = new JTextField();
		hourlyRateCentsField.setColumns(10);
		hourlyRateCentsField.setBounds(276, 155, 55, 17);
		addFrame.getContentPane().add(hourlyRateCentsField);

		// add employee and go back to initial frame
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// add employee to database and redo employee list
					JPAS.addEmployee(lastNameField.getText(), firstNameField.getText(), positionField.getText(), Integer.parseInt((hourlyRateDollarsField.getText())+(hourlyRateCentsField.getText())), 0);
					refreshList(list);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				resetTotals();
				frame.setVisible(true);
				addFrame.dispose();
			}
		});
		btnAdd.setBounds(182, 204, 65, 23);
		addFrame.getContentPane().add(btnAdd);

		JLabel label = new JLabel(".");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(262, 150, 14, 26);
		addFrame.getContentPane().add(label);

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(true);
				addFrame.dispose();
			}
		});
		btnBack.setBounds(20, 10, 65, 23);
		addFrame.getContentPane().add(btnBack);
	}

	// refresh employee's text info once it is altered
	public void refreshInfoTextArea(int index, JTextArea infoTextArea)
	{
		// get hourly rate
		String rateString = String.valueOf(JPAS.employeeDatabase[list.getSelectedIndex()].getRate());
		String rateCents = rateString.substring(rateString.length()-2,rateString.length());
		String rateDollars = rateString.substring(0,rateString.length()-2);

		// get total owed
		String totalString = String.valueOf(JPAS.employeeDatabase[list.getSelectedIndex()].getRate()*JPAS.employeeDatabase[list.getSelectedIndex()].getHoursWorked());
		// ensures a case for $0 owed
		if (totalString.length()==1)
		{
			totalString = "000";
		}
		// put together total owed

		String totalCents = totalString.substring(totalString.length()-2,totalString.length());
		String totalDollars = totalString.substring(0,totalString.length()-2);

		String newline = System.getProperty("line.separator");
		infoTextArea.setText(	"Position: "+JPAS.employeeDatabase[index].getJobTitle() + newline
				+	"Hourly Rate: $"+rateDollars+"."+rateCents + newline
				+	"Hours Worked: "+String.valueOf(JPAS.employeeDatabase[index].getHoursWorked()) + newline
				+	"Total Owed: $"+totalDollars+"."+totalCents);
	}

	// frame to edit employee information
	public void editFrame(final int index)
	{
		// clear get info text fields
		hourlyRateTextField.setText("");
		hoursWorkedTextField.setText("");
		totalOwedTextField.setText("");
		
		editFrame = new JFrame();
		editFrame.setBounds(100, 100, 450, 300);
		editFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		editFrame.getContentPane().setLayout(null);
		editFrame.setTitle(JPAS.employeeDatabase[index].getFirstName()+" "+JPAS.employeeDatabase[index].getLastName());

		nameTextField = new JTextField();
		nameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		nameTextField.setEditable(false);
		nameTextField.setColumns(1);
		nameTextField.setBounds(20, 64, 152, 20);
		editFrame.getContentPane().add(nameTextField);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(20, 95, 152, 83);
		editFrame.getContentPane().add(scrollPane_1);
		editFrame.setVisible(true);

		// get hourly rate
		String rateString = String.valueOf(JPAS.employeeDatabase[list.getSelectedIndex()].getRate());
		String rateCents = rateString.substring(rateString.length()-2,rateString.length());
		String rateDollars = rateString.substring(0,rateString.length()-2);

		// get total owed
		String totalTemp = String.valueOf(JPAS.employeeDatabase[list.getSelectedIndex()].getRate()*JPAS.employeeDatabase[list.getSelectedIndex()].getHoursWorked());
		// ensures a 0 case
		if(totalTemp.length()==1)
		{
			totalTemp=totalTemp+"00";
		}

		// put together total owed
		String totalCents = totalTemp.substring(totalTemp.length()-2,totalTemp.length());
		String totalDollars = totalTemp.substring(0,totalTemp.length()-2);
		final String totalString = "$"+totalDollars+"."+totalCents;

		final JTextArea infoTextArea = new JTextArea();
		scrollPane_1.setViewportView(infoTextArea);
		infoTextArea.setEditable(false);

		refreshInfoTextArea(index, infoTextArea);

		nameTextField.setText(JPAS.employeeDatabase[index].getFirstName()+" "+JPAS.employeeDatabase[index].getLastName());

		JPanel panel = new JPanel();
		panel.setBounds(200, 52, 224, 142);
		panel.setBorder(new LineBorder(Color.GRAY));
		editFrame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblChangePosition = new JLabel("New Position");
		lblChangePosition.setBounds(6, 11, 90, 14);
		panel.add(lblChangePosition);

		JLabel lblAddHours = new JLabel("Add Hours");
		lblAddHours.setBounds(6, 36, 90, 14);
		panel.add(lblAddHours);

		JLabel lblRemoveHours = new JLabel("Remove Hours");
		lblRemoveHours.setBounds(6, 61, 90, 14);
		panel.add(lblRemoveHours);

		JLabel lblNewRate = new JLabel("New Rate");
		lblNewRate.setBounds(6, 86, 90, 14);
		panel.add(lblNewRate);

		changePositionTextField = new JTextField();
		changePositionTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		changePositionTextField.setBounds(120, 8, 94, 20);
		panel.add(changePositionTextField);
		changePositionTextField.setColumns(10);

		addHoursTextField = new JTextField();
		addHoursTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		addHoursTextField.setColumns(10);
		addHoursTextField.setBounds(120, 33, 94, 20);
		panel.add(addHoursTextField);

		removeHoursTextField = new JTextField();
		removeHoursTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		removeHoursTextField.setColumns(10);
		removeHoursTextField.setBounds(120, 58, 94, 20);
		panel.add(removeHoursTextField);

		newRateDollarTextField = new JTextField();
		newRateDollarTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		newRateDollarTextField.setColumns(10);
		newRateDollarTextField.setBounds(120, 83, 44, 20);
		panel.add(newRateDollarTextField);

		newRateCentTextField = new JTextField();
		newRateCentTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		newRateCentTextField.setColumns(10);
		newRateCentTextField.setBounds(170, 83, 44, 20);
		panel.add(newRateCentTextField);

		changePositionTextField.setText(JPAS.employeeDatabase[index].getJobTitle());
		addHoursTextField.setText("0");
		removeHoursTextField.setText("0");
		newRateDollarTextField.setText(rateDollars);
		newRateCentTextField.setText(rateCents);

		// button to implement changes in position, hours or rate
		JButton btnMakeChanges = new JButton("Make Changes");
		btnMakeChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPAS.employeeDatabase[index].setJobTitle(changePositionTextField.getText());
				JPAS.employeeDatabase[index].setHoursWorked(JPAS.employeeDatabase[index].getHoursWorked()+(Integer.valueOf(addHoursTextField.getText()))-(Integer.valueOf(removeHoursTextField.getText())));
				JPAS.employeeDatabase[index].setRate(Integer.valueOf(newRateDollarTextField.getText()+newRateCentTextField.getText()));
				refreshInfoTextArea(index, infoTextArea);
				resetTotals();
			}
		});
		btnMakeChanges.setBounds(52, 111, 118, 23);
		panel.add(btnMakeChanges);

		JLabel label = new JLabel(".");
		label.setBounds(156, 86, 20, 14);
		panel.add(label);
		label.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(true);
				editFrame.dispose();
			}
		});
		btnBack.setBounds(20, 10, 65, 23);
		editFrame.getContentPane().add(btnBack);

		// get name string
		final String name = JPAS.employeeDatabase[index].getFirstName()+" "+JPAS.employeeDatabase[index].getLastName();

		// button to enter the reset confirmation frame
		JButton btnResetHours = new JButton("Reset Hours");
		btnResetHours.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetConfirmationFrame(index, infoTextArea, name, totalString);
				editFrame.setVisible(false);
			}
		});
		btnResetHours.setBounds(36, 211, 118, 23);
		editFrame.getContentPane().add(btnResetHours);
		// button to enter the delete confirmation frame
		JButton btnFireEmployee = new JButton("Fire Employee");
		btnFireEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteConfirmationFrame(index, infoTextArea, name, totalString);
				editFrame.setVisible(false);
			}
		});
		btnFireEmployee.setBounds(252, 211, 118, 23);
		editFrame.getContentPane().add(btnFireEmployee);

	}

	// frame to rset employee's hours to 0
	public void resetConfirmationFrame (final int index, final JTextArea infoTextArea , String name, String owed)
	{
		resetConfirmationFrame = new JFrame();
		resetConfirmationFrame.setBounds(125, 200, 400, 150);
		resetConfirmationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		resetConfirmationFrame.getContentPane().setLayout(null);
		resetConfirmationFrame.setVisible(true);
		resetConfirmationFrame.setTitle("Reset "+JPAS.employeeDatabase[index].getFirstName()+" "+JPAS.employeeDatabase[index].getLastName()+"'s Hours");

		// warning to ensure owner has paid employee
		JLabel confirmation = new JLabel("<html>Have you fully paid "+name+" the "+owed+"<br>that is owed and would like to reset his hours to 0?</html>");
		confirmation.setFont(new Font("Tahoma", Font.PLAIN, 13));
		confirmation.setHorizontalAlignment(SwingConstants.CENTER);
		confirmation.setBounds(10, 11, 364, 47);
		resetConfirmationFrame.getContentPane().add(confirmation);

		// set hours worked of employee to 0
		JButton btnYes = new JButton("Yes");
		btnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPAS.employeeDatabase[index].setHoursWorked(0);
				refreshInfoTextArea(index, infoTextArea);

				resetTotals();
				editFrame.setVisible(true);
				resetConfirmationFrame.dispose();
			}
		});
		btnYes.setBounds(96, 69, 67, 23);
		resetConfirmationFrame.getContentPane().add(btnYes);

		// don't reset hours to 0 and go back to edit employee frame
		JButton btnNo = new JButton("No");
		btnNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent n) {
				editFrame.setVisible(true);
				resetConfirmationFrame.dispose();
			}
		});
		btnNo.setBounds(210, 69, 67, 23);
		resetConfirmationFrame.getContentPane().add(btnNo);

	}

	// frame to confirm removing an employee
	public void deleteConfirmationFrame (final int index, final JTextArea infoTextArea , String name, String owed)
	{
		deleteConfirmationFrame = new JFrame();
		deleteConfirmationFrame.setBounds(125, 200, 400, 150);
		deleteConfirmationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		deleteConfirmationFrame.getContentPane().setLayout(null);
		deleteConfirmationFrame.setVisible(true);
		deleteConfirmationFrame.setTitle("Warning: Fire "+JPAS.employeeDatabase[index].getFirstName()+" "+JPAS.employeeDatabase[index].getLastName()+"?");

		JLabel confirmation = new JLabel("<html>Have you fully paid "+name+" the "+owed+"<br>that is owed and would like to remove<br>this employee and all data associated?</html>");
		confirmation.setFont(new Font("Tahoma", Font.PLAIN, 13));
		confirmation.setHorizontalAlignment(SwingConstants.CENTER);
		confirmation.setBounds(10, 11, 364, 47);
		deleteConfirmationFrame.getContentPane().add(confirmation);

		// remove emplyoee from database
		JButton btnYes = new JButton("Yes");
		btnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPAS.removeEmployee(index);
				refreshList(list);

				resetTotals();
				frame.setVisible(true);
				editFrame.dispose();
				deleteConfirmationFrame.dispose();
			}
		});
		btnYes.setBounds(96, 69, 67, 23);
		deleteConfirmationFrame.getContentPane().add(btnYes);

		JButton btnNo = new JButton("No");
		btnNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent n) {
				editFrame.setVisible(true);
				deleteConfirmationFrame.dispose();
			}
		});
		btnNo.setBounds(210, 69, 67, 23);
		deleteConfirmationFrame.getContentPane().add(btnNo);
	}

	// frame to save database for future use.
	public void saveFrame() throws IOException
	{
		saveFrame = new JFrame();
		saveFrame.setBounds(125, 200, 400, 150);
		saveFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		saveFrame.getContentPane().setLayout(null);
		saveFrame.setTitle("Database Saved");

		JPAS.saveDatabase();
		
		// button to go back to main frame
		JButton btnOkay = new JButton("Okay");
		btnOkay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFrame.dispose();
				frame.setVisible(true);
			}
		});
		btnOkay.setBounds(156, 77, 66, 23);
		saveFrame.getContentPane().add(btnOkay);

		// confirm to user that frame has been saved
		JLabel lblNewLabel_1 = new JLabel("Database has been saved for future use.");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 26, 364, 34);
		saveFrame.getContentPane().add(lblNewLabel_1);
		saveFrame.setVisible(true);
	}

}