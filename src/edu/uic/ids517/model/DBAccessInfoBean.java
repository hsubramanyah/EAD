package edu.uic.ids517.model;

public class DBAccessInfoBean {

	private String userName;
	private String password;
	private String dbms;
	private String dbmsHost;
	private String dbSchema;

	public DBAccessInfoBean() {
		this.userName = "f16g321";
		this.password = "g3218fQHJx";//g3218fQHJx
		this.dbms = "MySQL";
		this.dbmsHost = "131.193.209.57";
		this.dbSchema = "f16g321";

	}

	public DBAccessInfoBean(String userName, String password, String dbms, String dbSchema ,  String dbmsHost) {

		this.userName = userName;
		this.password = password;
		this.dbms = dbms;
		this.dbmsHost = dbmsHost;
		this.dbSchema = dbSchema;
	}

	
	public String getUserName() {
		return userName;
	}

	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	public String getPassword() {
		return password;
	}

	
	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getDbms() {
		return dbms;
	}

	
	public void setDbms(String dbms) {
		this.dbms = dbms;
	}

	
	public String getDbmsHost() {
		return dbmsHost;
	}

	
	public void setDbmsHost(String dbmsHost) {
		this.dbmsHost = dbmsHost;
	}

	
	public String getDbSchema() {
		return dbSchema;
	}

	
	public void setdbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}

}
