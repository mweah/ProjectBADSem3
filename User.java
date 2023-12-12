package isiDB;

public class User {

	String UserID;
	String Email;
	String Username;
	String Password;
	String PhoneNumber;
	String Address;
	String Gender;
	final String Role;
	
	public User(String userID, String email, String username, String password, String phoneNumber, String address,
			String gender, String role) {
		super();
		UserID = userID;
		Email = email;
		Username = username;
		Password = password;
		PhoneNumber = phoneNumber;
		Address = address;
		Gender = gender;
		Role = role;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getRole() {
		return Role;
	}
	
	
	
}
