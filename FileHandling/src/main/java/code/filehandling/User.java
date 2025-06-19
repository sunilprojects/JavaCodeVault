package code.filehandling;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class User {
	private String firstName;
	private String lastName;
	private String gender;
	private String dob;
	private String address;
	private long phone;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address=address.replaceAll("[^A-Za-z0-9]", "");
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		if(Pattern.matches("[789]{1}\\d+{9}", String.valueOf(phone)))
		this.phone = phone;
		else
			System.out.println("not valid phone");
			
	}
	
	

}
