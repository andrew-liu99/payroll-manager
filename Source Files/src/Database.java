/**
 * Database class to hold array of employees
 * @author Andrew Liu
 * @date 11/21/2016
 * @teacher Mr. Reid
 * @coursecode ICS4U1
 */
import java.awt.Desktop;
import java.io.*;
import java.util.*;
public class Database {
	// Attributes
	public Employee []employeeDatabase;
	public String []employeeList;
	public int employeeCount;
	public String filename;

	// Getters and Setters
	public Employee[] getEmployeeDatabase() {
		return employeeDatabase;
	}
	public void setEmployeeDatabase(Employee[] employeeDatabase) {
		this.employeeDatabase = employeeDatabase;
	}
	public String[] getEmployeeList() {
		return employeeList;
	}
	public void setEmployeeList(String[] employeeList) {
		this.employeeList = employeeList;
	}
	public int getEmployeeCount() {
		return employeeCount;
	}
	public void setEmployeeCount(int employeeCount) {
		this.employeeCount = employeeCount;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}

	// Constructors
	public Database(String file) throws FileNotFoundException
	{
		this.filename = file;
	}

	// read .txt file and create corresponding array
	public void readFile() throws FileNotFoundException
	{
		Scanner sc = new Scanner(new FileReader(this.filename));
		// count amount of employees in the file
		this.employeeCount = 0;
		while(sc.hasNextLine())
		{
			sc.nextLine();
			this.employeeCount++;
		}
		sc.close();
		// create array of employees, size of employeeCount + 50 for adding employees
		this.employeeDatabase = new Employee[employeeCount + 50];
	}

	// store database with employees when program is opened
	public void storeDatabase() throws IOException
	{
		Scanner sc = new Scanner(new FileReader(this.filename));
		// read employees from list and store all employees with correspoding info
		for (int i=0; i<employeeCount; i++)
		{
			String line = sc.nextLine();
			String[] lineSplit = line.split(",");
			this.employeeDatabase[i] = new Employee(lineSplit[0], lineSplit[1],lineSplit[2],
				Integer.valueOf(lineSplit[3]), Integer.valueOf(lineSplit[4]));
		}
		sc.close();
		// fill string array for list display
		fillEmployeeList();
	}

	// fill the employee first name, last name, position list for display
	public void fillEmployeeList()
	{
		this.employeeList = new String[employeeCount];
		for(int i=0;i<employeeCount;i++)
		{
			employeeList[i]=(employeeDatabase[i].getLastName()+", "+employeeDatabase[i].getFirstName()+" - "+employeeDatabase[i].getJobTitle());
		}
	}

	// guarantees array is big enough for adding employee
	public void makeRoom() throws IOException
	{
		// resize array if its too small
		if(this.employeeCount >= employeeDatabase.length)
		{
			// print to .txt for refill later
			saveDatabase();
			// copy database and remake one with larger size
			this.employeeDatabase = new Employee[employeeCount + 50];
			// refill database
			storeDatabase();
		}	
	}	

	// save employee database for future use
	public void saveDatabase() throws IOException
	{
		// print all employees
		PrintWriter pw = new PrintWriter(new FileWriter(this.filename));
		for(int i=0; i<employeeCount; i++)
		{
			pw.println(this.employeeDatabase[i]);
		}
		pw.close();
	}

	// reverse bubble sort (lowest value gets pushed to the front)
	public void sortLastName()
	{
		// stop cycling when no swaps are made in an entire cycle 
		boolean cycle = true;
		while (cycle == true)
		{
			cycle = false;
			// cycle through list BACKWARDS (when adding an employee, this will optimize time)
			for (int i=this.employeeCount-1; i>0; i--)
			{
				// compare one value with the next
				int compare = this.employeeDatabase[i].getLastName().compareToIgnoreCase(this.employeeDatabase[i-1].getLastName());
				if (compare<=0)
				{
					// boolean to determine order in case of same last name
					boolean smallerSecondValue = true;
					int compareFirst = this.employeeDatabase[i].getFirstName().compareToIgnoreCase(this.employeeDatabase[i-1].getFirstName());
					if (compareFirst > 0)
					{
						smallerSecondValue = false;
					}
					// swap values
					if (compare<0 || smallerSecondValue==false)
					{
						// temporary Employee to swap values
						Employee temporary;
						temporary = this.employeeDatabase[i];
						this.employeeDatabase[i] = this.employeeDatabase[i-1];
						this.employeeDatabase[i-1] = temporary;

						cycle = true;
					}
				}
			}
		}
		// fill string array for list display
		fillEmployeeList();
	}

	// reverse bubble sort for hours worked
	public void sortHoursWorked()
	{
		// placeholder student for swaps
		boolean swap = true;
		while (swap == true)
		{
			swap = false;
			// cycle through list BACKWARDS (when adding an employee, this will optimize time)
			for (int i=this.employeeCount-1; i>0; i--)
			{
				// compare one value with the next
				if(this.employeeDatabase[i].getHoursWorked() < this.employeeDatabase[i-1].getHoursWorked())
				{
					// swap values
					Employee temporary;
					temporary = this.employeeDatabase[i];
					this.employeeDatabase[i] = this.employeeDatabase[i-1];
					this.employeeDatabase[i-1] = temporary;

					swap = true;
				}
			}
		}
		// fill string array for list display
		fillEmployeeList();
	}

	// reverse bubble sort for hourly rate
	public void sortHourlyRate()
	{
		// placeholder student for swaps
		boolean swap = true;
		while (swap == true)
		{
			swap = false;
			// cycle through list BACKWARDS (when adding an employee, this will optimize time)
			for (int i=this.employeeCount-1; i>0; i--)
			{
				// compare one value with the next
				if(this.employeeDatabase[i].getRate() < this.employeeDatabase[i-1].getRate())
				{
					// swap values
					Employee temporary;
					temporary = this.employeeDatabase[i];
					this.employeeDatabase[i] = this.employeeDatabase[i-1];
					this.employeeDatabase[i-1] = temporary;

					swap = true;
				}
			}
		}
		// fill string array for list display
		fillEmployeeList();
	}

	// reverse bubble sort for total owed
	public void sortTotalOwed()
	{
		// placeholder student for swaps
		boolean swap = true;
		while (swap == true)
		{
			swap = false;
			// cycle through list BACKWARDS (when adding an employee, this will optimize time)
			for (int i=this.employeeCount-1; i>0; i--)
			{
				// compare one value with the next
				if((this.employeeDatabase[i].getHoursWorked()*this.employeeDatabase[i].getRate()) < this.employeeDatabase[i-1].getHoursWorked()*this.employeeDatabase[i-1].getRate())
				{
					// swap values
					Employee temporary;
					temporary = this.employeeDatabase[i];
					this.employeeDatabase[i] = this.employeeDatabase[i-1];
					this.employeeDatabase[i-1] = temporary;

					swap = true;
				}
			}
		}
		// fill string array for list display
		fillEmployeeList();
	}

	// add employee to database and string array
	public void addEmployee(String lastName, String firstName, String jobTitle, int rate, int hoursWorked) throws IOException
	{
		makeRoom();
		// fill employee in database
		employeeDatabase[employeeCount] = new Employee(lastName, firstName, jobTitle, rate, hoursWorked);
		employeeCount++;

		// fill string array for list display
		fillEmployeeList();
	}

	// remove employee from database and string array
	public void removeEmployee(int index)
	{
		// replace entry with next one, thus overwriting the employee at index
		for(int i=index; i<employeeCount-1; i++)
		{
			employeeDatabase[i]=employeeDatabase[i+1];
		}
		employeeCount--;

		// fill string array for list display
		fillEmployeeList();
	}

	public String rateToString(int rate)
	{
		String rateString = String.valueOf(rate);
		// ensures a case for $0 owed
		if (rateString.length()==1)
		{
			rateString = "000";
		}		
		String totalCents = rateString.substring(rateString.length()-2,rateString.length());
		String totalDollars = rateString.substring(0,rateString.length()-2);
		rateString = ("$"+totalDollars+"."+totalCents);
		return rateString;
	}	
	
	// recursively add all employee's hours
	public int getTotalHours(int index, int value)
	{
		if(index==employeeCount)
		{
			return value;
		}
		value = getTotalHours(index+1, value+employeeDatabase[index].getHoursWorked());
		return value;
	}

	// recursively add total owed for all employees
	public int getTotalOwed(int index, int value)
	{
		if(index==employeeCount)
		{
			return value;
		}
		value = getTotalOwed(index+1, value+(employeeDatabase[index].getHoursWorked()
				*employeeDatabase[index].getRate()));
		return value;
	}	

	// add hours to an employee
	public void addHours(int index, int additionalHours)
	{
		employeeDatabase[index].setHoursWorked(employeeDatabase[index].getHoursWorked() + additionalHours);
	}

	// save database to a CSV file
	public void saveCSV(String filename) throws IOException
	{
		PrintWriter pw = new PrintWriter(new FileWriter(filename));
		String rateString;
		String rateCents;
		String rateDollars;
		String totalString;
		// headings
		pw.println("Last Name,First Name,Position,Hourly Rate,Hours Worked,Total Owed");
		// print all employees' info
		for(int i=0; i<employeeCount; i++)
		{
			// calculations for hourly rate and total owed
			// get hourly rate
			rateString = String.valueOf(employeeDatabase[i].getRate());
			rateCents = rateString.substring(rateString.length()-2,rateString.length());
			rateDollars = rateString.substring(0,rateString.length()-2);
			rateString = ("$"+rateDollars+"."+rateCents);

			// get total owed
			totalString = String.valueOf(employeeDatabase[i].getRate()*employeeDatabase[i].getHoursWorked());
			// ensures a case for $0 owed
			if (totalString.length()==1)
			{
				totalString = "000";
			}		
			String totalCents = totalString.substring(totalString.length()-2,totalString.length());
			String totalDollars = totalString.substring(0,totalString.length()-2);
			totalString = ("$"+totalDollars+"."+totalCents);

			// print data for each student
			pw.println(employeeDatabase[i].getLastName()+","+employeeDatabase[i].getFirstName()+","+employeeDatabase[i].getJobTitle()+","+rateString+","+employeeDatabase[i].getHoursWorked()+","+"=D"+(i+2)+"*E"+(i+2));
		}
		pw.println();
		// print totals with excel calculations
		pw.println("Totals,-,-,-,=SUM(E2:E"+(employeeCount+1)+"),=SUM(F2:F"+(employeeCount+1)+")");

		pw.flush();
		pw.close();
		
		if (Desktop.isDesktopSupported())
		{
			Desktop.getDesktop().open(new File("statement.csv"));
		}
	}
}
