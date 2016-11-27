package edu.uic.ids517.model;

//import java.sql.ResultSetMetaData;
//import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.servlet.jsp.jstl.sql.Result;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

public class DBAccessActionBean {

	private String tableName;
	private String sqlQuery;
	private int noOfCols = 0;
	private int noOfRows = 0;
	private Result result;
	private String tableinListColumn;
	private FacesContext context;
	private List<String> columnNames;
	private List<String> tableViewList;
	private List<String> columnNamesSelected;
	private List<String> allTableList = new ArrayList<String>(Arrays.asList("f16g321_user", "f16g321_course",
			"f16g321_student", "f16g321_instructor", "f16g321_ins_course", "f16g321_student_enroll", "f16g321_test",
			"f16g321_questions", "f16g321_feedback", "f16g321_scores"));
	private List<String> createDropTableNames;

	/*
	 * private static final String f16g321_user =
	 * "create table f16g321_user ( user_name VARCHAR(20) not null, password VARCHAR(20) not null, role VARCHAR(20),last_logintime datetime, last_login_ip VARCHAR(20), CONSTRAINT Pk_User primary key (user_name)); "
	 * ;
	 * 
	 * private static final String f16g321_course =
	 * "create table f16g321_course (crn Numeric(10) not null, code VARCHAR(20) not null, description VARCHAR(50) ,CONSTRAINT Pk_code primary key (code))  ;"
	 * ;
	 * 
	 * private static final String f16g321_student =
	 * "create table f16g321_student (uin Numeric(10) not null, last_name VARCHAR(20) not null, first_name VARCHAR(20), user_name VARCHAR(20) not null , last_logintime datetime , last_login_ip VARCHAR(20),CONSTRAINT Pk_Uin primary key (uin)) ;"
	 * ;
	 * 
	 * private static final String f16g321_instructor =
	 * "create table f16g321_instructor (ins_id Numeric(10), last_name VARCHAR(20) not null, first_name VARCHAR(20), user_name VARCHAR(20) not null , CONSTRAINT Pk_ins_id primary key (ins_id)) ;"
	 * ;
	 * 
	 * private static final String f16g321_ins_course =
	 * "create table f16g321_ins_course (ins_id Numeric(10) , code VARCHAR(20) , CONSTRAINT Pk_ins_crn primary key (code,ins_id), CONSTRAINT Fk_ins foreign key (ins_id ) REFERENCES f16g321_instructor(ins_id), CONSTRAINT Fk_crn2 foreign key (code ) REFERENCES f16g321_course(code)); "
	 * ;
	 * 
	 * private static final String f16g321_student_enroll =
	 * "create table f16g321_student_enroll (code VARCHAR(20) , uin Numeric(10) , CONSTRAINT Pk_Crn primary key (code,uin), CONSTRAINT Fk_uin foreign key (uin ) REFERENCES f16g321_student(uin), CONSTRAINT Fk_CRN foreign key (code ) REFERENCES f16g321_course(code)); "
	 * ;
	 * 
	 * private static final String f16g321_test =
	 * "create table f16g321_test (test_id VARCHAR(20) , code VARCHAR(20) , start_time datetime, end_time datetime, duration time, points_per_ques double, total double, CONSTRAINT Pk_testid primary key (test_id)); "
	 * ;
	 * 
	 * private static final String f16g321_questions =
	 * "create table f16g321_questions ( question_id MEDIUMINT NOT NULL AUTO_INCREMENT, test_id VARCHAR(20), question_type VARCHAR(20), question_text text, correct_ans text, tolerance double, CONSTRAINT Pk_qid primary key (question_id), CONSTRAINT Fk_tid foreign key (test_id ) REFERENCES f16g321_test(test_id)); "
	 * ;
	 * 
	 * private static final String f16g321_feedback =
	 * "create table f16g321_feedback (uin Numeric(10) , question_id MEDIUMINT , ans_selected VARCHAR(20) , CONSTRAINT Pk_feed primary key (question_id,uin), CONSTRAINT Fk_qid foreign key (question_id ) REFERENCES f16g321_questions(question_id), CONSTRAINT Fk_uin3 foreign key (uin ) REFERENCES f16g321_student(uin)); "
	 * ;
	 * 
	 * private static final String f16g321_scores =
	 * "create table f16g321_scores (uin Numeric(10), test_id VARCHAR(20), score Numeric(10), CONSTRAINT Pk_score primary key (uin,test_id), CONSTRAINT Fk_tid1 foreign key (test_id ) REFERENCES f16g321_test(test_id), CONSTRAINT Fk_uin1 foreign key (uin ) REFERENCES f16g321_student(uin)); "
	 * ;
	 */
	public List<String> getCreateDropTableNames() {
		return createDropTableNames;
	}

	private boolean tableListRendered;
	private boolean columnListRendered = false;
	private boolean queryRendered = false;
	private DBAccessBean dBAccessBean;
	private MessageBean messageBean;
	// private ResultSetMetaData resultSetMetaData;
	HashMap<String, String> createHashMap = new HashMap<String, String>();

	public DBAccessActionBean() {
		createHashMap.put("f16g321_user",
				"create table f16g321_user ( user_name VARCHAR(20) not null, password VARCHAR(20) not null, role VARCHAR(20),last_logintime datetime, last_login_ip VARCHAR(20), CONSTRAINT Pk_User primary key (user_name)); ");

		createHashMap.put("f16g321_course",
				"create table f16g321_course (crn Numeric(10) not null, code VARCHAR(20) not null, description VARCHAR(50) ,CONSTRAINT Pk_code primary key (code))  ;");

		createHashMap.put("f16g321_student",
				"create table f16g321_student (uin Numeric(10) not null, last_name VARCHAR(20) not null, first_name VARCHAR(20), user_name VARCHAR(20) not null , last_access datetime , last_login_ip VARCHAR(20),CONSTRAINT Pk_Uin primary key (uin)) ;");

		createHashMap.put("f16g321_instructor",
				"create table f16g321_instructor (ins_id Numeric(10), last_name VARCHAR(20) not null, first_name VARCHAR(20), user_name VARCHAR(20) not null , CONSTRAINT Pk_ins_id primary key (ins_id)) ;");

		createHashMap.put("f16g321_ins_course",
				"create table f16g321_ins_course (ins_id Numeric(10) , code VARCHAR(20) , CONSTRAINT Pk_ins_crn primary key (code,ins_id), CONSTRAINT Fk_ins foreign key (ins_id ) REFERENCES f16g321_instructor(ins_id), CONSTRAINT Fk_crn2 foreign key (code ) REFERENCES f16g321_course(code)); ");

		createHashMap.put("f16g321_student_enroll",
				"create table f16g321_student_enroll (code VARCHAR(20) , uin Numeric(10) , total double, CONSTRAINT Pk_Crn primary key (code,uin), CONSTRAINT Fk_uin foreign key (uin ) REFERENCES f16g321_student(uin), CONSTRAINT Fk_CRN foreign key (code ) REFERENCES f16g321_course(code)); ");

		createHashMap.put("f16g321_test",
				"create table f16g321_test (test_id VARCHAR(20) , code VARCHAR(20) , start_time datetime, end_time datetime, duration time, points_per_ques double, total double, CONSTRAINT Pk_testid primary key (test_id)); ");

		createHashMap.put("f16g321_questions",
				"create table f16g321_questions ( question_id MEDIUMINT NOT NULL AUTO_INCREMENT, test_id VARCHAR(20), question_type VARCHAR(20), question_text text, correct_ans text, tolerance double, CONSTRAINT Pk_qid primary key (question_id), CONSTRAINT Fk_tid foreign key (test_id ) REFERENCES f16g321_test(test_id)); ");

		createHashMap.put("f16g321_feedback",
				"create table f16g321_feedback (uin Numeric(10) , question_id MEDIUMINT , ans_selected VARCHAR(20) , CONSTRAINT Pk_feed primary key (question_id,uin), CONSTRAINT Fk_qid foreign key (question_id ) REFERENCES f16g321_questions(question_id), CONSTRAINT Fk_uin3 foreign key (uin ) REFERENCES f16g321_student(uin)); ");

		createHashMap.put("f16g321_scores",
				"create table f16g321_scores (uin Numeric(10), test_id VARCHAR(20), score Numeric(10), code VARCHAR(20) , CONSTRAINT Pk_score primary key (uin,test_id), CONSTRAINT Fk_tid1 foreign key (test_id ) REFERENCES f16g321_test(test_id), CONSTRAINT Fk_uin1 foreign key (uin ) REFERENCES f16g321_student(uin),CONSTRAINT Fk_code4 foreign key (code) REFERENCES  f16g321_course(code)); ");

	}

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		System.out.println(context);
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dBAccessBean = (DBAccessBean) m.get("dBAccessBean");
		messageBean = (MessageBean) m.get("messageBean");
		listTables();

	}

	public Result getResult() {
		return result;
	}

	public boolean isTableListRendered() {
		return tableListRendered;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public List<String> getTableViewList() {
		return tableViewList;
	}

	public List<String> getColumnNamesSelected() {
		return columnNamesSelected;
	}

	public void setColumnNamesSelected(List<String> columnNamesSelected) {
		this.columnNamesSelected = columnNamesSelected;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public int getNoOfCols() {
		return noOfCols;
	}

	public int getNoOfRows() {
		return noOfRows;
	}

	public boolean isColumnListRendered() {
		return columnListRendered;
	}

	public boolean isQueryRendered() {
		return queryRendered;
	}

	/*
	 * public void setAllTableList(List<String> allTableList) {
	 * this.allTableList = allTableList; }
	 */

	public void setCreateDropTableNames(List<String> createDropTableNames) {
		this.createDropTableNames = createDropTableNames;
	}

	public List<String> getAllTableList() {
		return allTableList;
	}

	public String listTables() {
		try {
			messageBean.resetAll();
			tableViewList = dBAccessBean.tableList();
			if (null != tableViewList) {
				tableListRendered = true;
			}
			return "SUCCESS";
		} catch (Exception e) {
			tableListRendered = false;
			messageBean.setErrorMessage("");
			messageBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}

	public String listColumns() {

		try {
			messageBean.resetAll();
			if (null != tableName && !"".equals(tableName)) {
				columnNames = dBAccessBean.columnList(tableName);
				tableinListColumn = tableName;
				queryRendered = false;
				sqlQuery = "";
				if (null != columnNames) {
					columnListRendered = true;
				}
			} else {
				messageBean.setErrorMessage("Please select Table Name from the list");
				messageBean.setRenderErrorMessage(true);
				return "FAIL";

			}
		} catch (Exception e) {
			columnListRendered = false;
			messageBean.setErrorMessage("");
			messageBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}

		return "SUCCESS";

	}

	public String selectAllColumn() {
		messageBean.resetAll();
		listColumns();
		if (null != tableinListColumn && !"".equals(tableinListColumn)) {
			sqlQuery = "select * from " + tableinListColumn + " ;";
			dBAccessBean.execute(sqlQuery);
			noOfCols = dBAccessBean.getNumOfCols();
			noOfRows = dBAccessBean.getNumOfRows();
			dBAccessBean.generateResult();
			result = dBAccessBean.getResult();
			columnNamesSelected = dBAccessBean.columnList(tableinListColumn);
			queryRendered = true;
			return "SUCCESS";
		} else {
			messageBean.setErrorMessage("Please select Table Name from list");
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}

	public String selectCustomColumn() {
		messageBean.resetAll();
		if (null != columnNamesSelected && !columnNamesSelected.isEmpty()) {
			tableName = tableinListColumn;
			sqlQuery = "select " + columnNamesSelected.toString().replace("[", "").replace("]", "") + " from "
					+ tableinListColumn + " ;";
			dBAccessBean.execute(sqlQuery);
			noOfCols = dBAccessBean.getNumOfCols();
			noOfRows = dBAccessBean.getNumOfRows();
			dBAccessBean.generateResult();
			result = dBAccessBean.getResult();
			// columnNames = dBAccessBean.columnList(tableName);
			queryRendered = true;
			return "SUCCESS";
		} else {
			messageBean.setErrorMessage("Please select Columns to display");
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}

	public String processSQLQuery() {
		messageBean.resetAll();
		try {
			if (dBAccessBean.execute(sqlQuery).equals("SUCCESS")) {
				if (sqlQuery.toLowerCase().startsWith("select")) {
					noOfCols = dBAccessBean.getNumOfCols();
					noOfRows = dBAccessBean.getNumOfRows();
					dBAccessBean.generateResult();
					result = dBAccessBean.getResult();
					columnNamesSelected = dBAccessBean.getColumnNamesSelected();

					queryRendered = true;
				} else {
					messageBean.setRenderSuccessMessage(true);
					messageBean.setSuccessMessage("SQL Query Successfully excecuted");
					queryRendered = false;
				}
			} else {
				queryRendered = false;
				return "FAIL";
			}
			return "SUCCESS";
		} catch (Exception e) {
			messageBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}

	/*
	 * public String connectDB() { try { dBAccessBean.connectDB(); return
	 * "SUCCESS"; } catch (Exception e) { return "FAIL"; } }
	 */

	public String logout() {
		dBAccessBean.close();
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "LOGOUT";
	}

	public String createAllTables() {
		messageBean.resetAll();
		String status = "SUCCESS";

		try {
			dBAccessBean.execute("set foreign_key_checks=0");
			for (String temp : createHashMap.keySet()) {
				System.out.println(temp);
				dBAccessBean.getConnection().setAutoCommit(false);
				if (dBAccessBean.tableList().contains(temp)) {
					continue;
				}
				status = dBAccessBean.execute(createHashMap.get(temp));
				if (status.equals("FAIL")) {
					dBAccessBean.getConnection().rollback();
					dBAccessBean.getConnection().setAutoCommit(true);
					messageBean.setRenderErrorMessage(true);
					messageBean.setErrorMessage(messageBean.getErrorMessage() + "     Transactions Rolled Back");
					return "FAIL";

				}
				dBAccessBean.getConnection().commit();
				dBAccessBean.getConnection().setAutoCommit(true);
				messageBean.setSuccessMessage("Tables are created");
				messageBean.setRenderSuccessMessage(true);
			}

		} catch (Exception e) {
			messageBean.setErrorMessage(e.getMessage());
			messageBean.setRenderErrorMessage(true);
			status = "FAIL";
		}

		return status;
	}

	public String createTables() {
		messageBean.resetAll();
		String status = "SUCCESS";
		try {
			if (null != createDropTableNames && !createDropTableNames.isEmpty()) {
				dBAccessBean.execute("set foreign_key_checks=0");
				for (String temp : createDropTableNames) {
					dBAccessBean.getConnection().setAutoCommit(false);
					status = dBAccessBean.execute(createHashMap.get(temp));
					if (status.equals("FAIL")) {
						dBAccessBean.getConnection().rollback();
						dBAccessBean.getConnection().setAutoCommit(true);
						messageBean.setRenderErrorMessage(true);
						messageBean.setErrorMessage(messageBean.getErrorMessage() + "     Transactions Rolled Back");
						return "FAIL";

					}

				}
				dBAccessBean.getConnection().commit();
				dBAccessBean.getConnection().setAutoCommit(true);
				messageBean.setSuccessMessage(
						createDropTableNames.toString().replace("[", "").replace("]", "") + " Tables are created");
				messageBean.setRenderSuccessMessage(true);

			} else {
				messageBean.setErrorMessage("Please select Table to Create");
				messageBean.setRenderErrorMessage(true);
				status = "FAIL";
			}
		} catch (Exception e) {
			messageBean.setErrorMessage(e.getMessage());
			messageBean.setRenderErrorMessage(true);
			status = "FAIL";
		}

		return status;
	}

	public String dropAllTables() {
		messageBean.resetAll();
		String status = "SUCCESS";
		try {
			dBAccessBean.execute("set foreign_key_checks=0");
			dBAccessBean.getConnection().setAutoCommit(false);
			for (String temp : createHashMap.keySet()) {
				if (!dBAccessBean.tableList().contains(temp)) {
					continue;
				}
				status = dBAccessBean.execute("drop table " + temp);
				if (status.equals("FAIL")) {
					dBAccessBean.getConnection().rollback();
					dBAccessBean.getConnection().setAutoCommit(true);
					messageBean.setRenderErrorMessage(true);
					messageBean.setErrorMessage(messageBean.getErrorMessage() + "     Transactions Rolled Back");
					return "FAIL";

				}

			}
			dBAccessBean.getConnection().commit();
			dBAccessBean.getConnection().setAutoCommit(true);
			messageBean.setSuccessMessage("Tables are dropped");
			messageBean.setRenderSuccessMessage(true);

		} catch (Exception e) {
			messageBean.setErrorMessage(e.getMessage());
			messageBean.setRenderErrorMessage(true);
			status = "FAIL";
		}
		return status;
	}

	public String dropTables() {
		messageBean.resetAll();
		String status = "SUCCESS";
		try {
			dBAccessBean.getConnection().setAutoCommit(false);
			if (null != createDropTableNames && !createDropTableNames.isEmpty()) {
				dBAccessBean.execute("set foreign_key_checks=0");
				for (String temp : createDropTableNames) {
					status = dBAccessBean.execute("drop table " + temp);
					if (status.equals("FAIL")) {
						dBAccessBean.getConnection().rollback();
						dBAccessBean.getConnection().setAutoCommit(true);
						messageBean.setRenderErrorMessage(true);
						messageBean.setErrorMessage(messageBean.getErrorMessage() + "     Transactions Rolled Back");
						return "FAIL";

					}

				}
				dBAccessBean.getConnection().commit();
				dBAccessBean.getConnection().setAutoCommit(true);
				messageBean.setSuccessMessage(
						createDropTableNames.toString().replace("[", "").replace("]", "") + " Tables are Dropped");
				messageBean.setRenderSuccessMessage(true);
			} else {
				messageBean.setErrorMessage("Please select Table to Drop");
				messageBean.setRenderErrorMessage(true);
				status = "FAIL";
			}
		} catch (Exception e) {
			messageBean.setErrorMessage(e.getMessage());
			messageBean.setRenderErrorMessage(true);
			status = "FAIL";
		}

		return status;
	}

	public String back() {
		messageBean.resetAll();
		return "BACK";
	}

	public String next() {
		messageBean.resetAll();
		return "NEXT";
	}

}
