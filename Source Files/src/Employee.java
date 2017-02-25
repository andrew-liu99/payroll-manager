/**
 * Employee class to represent individual employees
 * @author Andrew Liu
 * @date 11/21/2016
 * @teacher Mr. Reid
 * @coursecode ICS4U1
 */
public class Employee {

	// Attributes
	private String lastName;
	private String firstName;
	private String jobTitle;
	private int rate;
	private int hoursWorked;
	
	// Constructors
	public Employee (String lastName, String firstName, String jobTitle, int rate, int hoursWorked)
	{
		this.lastName = lastName;
		this.firstName = firstName;
		this.jobTitle = jobTitle;
		this.rate = rate;
		this.hoursWorked = hoursWorked;
	}
	
	// toString
	public String toString()
	{
		return(lastName + "," + firstName + "," + jobTitle + "," + rate + "," + hoursWorked);
	}
	
	// Getters and Setters
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public int getHoursWorked() {
		return hoursWorked;
	}
	public void setHoursWorked(int hoursWorked) {
		this.hoursWorked = hoursWorked;
	}

}
