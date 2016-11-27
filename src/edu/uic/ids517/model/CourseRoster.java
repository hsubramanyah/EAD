package edu.uic.ids517.model;

import java.io.Serializable;

public class CourseRoster implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String lastName;
	private String firstName;
	private String userName;
	private String uin;
	private String lastAccess;
	private String availability;
	private Double total;
	private Double exam01;
	private Double exam02;
	private Double exam03;
	private Double project;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUin() {
		return uin;
	}

	public void setUin(String uin) {
		this.uin = uin;
	}

	public String getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(String lastAccess) {
		this.lastAccess = lastAccess;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getExam01() {
		return exam01;
	}

	public void setExam01(Double exam01) {
		this.exam01 = exam01;
	}

	public Double getExam02() {
		return exam02;
	}

	public void setExam02(Double exam02) {
		this.exam02 = exam02;
	}

	public Double getExam03() {
		return exam03;
	}

	public void setExam03(Double exam03) {
		this.exam03 = exam03;
	}

	public Double getProject() {
		return project;
	}

	public void setProject(Double project) {
		this.project = project;
	}

	public CourseRoster() {

	}

	public CourseRoster(String lastName, String firstName, String userName, String uin, String lastAccess,
			String availability, Double total, Double exam01, Double exam02, Double exam03, Double project) {
		super();
		this.lastName = lastName;
		this.firstName = firstName;
		this.userName = userName;
		this.uin = uin;
		this.lastAccess = lastAccess;
		this.availability = availability;
		this.total = total;
		this.exam01 = exam01;
		this.exam02 = exam02;
		this.exam03 = exam03;
		this.project = project;
	}

	public String toString() {
		return lastName + "," + firstName + "," + userName + "," + uin + "," + lastAccess + "," + availability + ","
				+ total + "," + exam01 + "," + exam02 + "," + exam03 + "," + project + "\n";

	}

}
