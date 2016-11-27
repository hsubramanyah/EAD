package edu.uic.ids517.model;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

public class DBAccessBean {

	private Connection connection;
	private DatabaseMetaData databaseMetaData;
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData resultSetMetaData;
	private List<String> columnNamesSelected;
	private FacesContext context;
	//private Boolean status;
	private static final String[] TABLE_TYPES = { "TABLE", "VIEW" };
	private String jdbcDriver;
	private String url;
	private DBAccessInfoBean dBAccessInfoBean;
	private MessageBean messageBean;
	private static final String MY_SQL = "MySQL";
	private static final String DB2 = "DB2";
	private static final String ORACLE = "Oracle";
	private String message = "";
	//private Boolean renderMessage = false;
	private int numOfCols = 0;
	private int numOfRows = 0;

	// constants for SQLException
	private static final String ACCESS_DENIED = "28000";
	private static final String INVALID_DB_SCHEMA = "42000";
	private static final String TIMEOUT = "08S01";
	private static final String INVALID_PORT = "08001";

	public DBAccessBean() {

	}

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		System.out.println(context);
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dBAccessInfoBean = (DBAccessInfoBean) m.get("dBAccessInfoBean");
		messageBean = (MessageBean) m.get("messageBean");

	}

	public List<String> getColumnNamesSelected() {
		return columnNamesSelected;
	}

	public ResultSetMetaData getResultSetMetaData() {
		return resultSetMetaData;
	}

	private Result result;

	public Result getResult() {
		return result;
	}

	/*public Boolean getStatus() {
		return status;
	}

	public Boolean getRenderMessage() {
		return renderMessage;
	}*/

	public ResultSet getResultSet() {
		return resultSet;
	}

	public Connection getConnection() {
		return connection;
	}

	public int getNumOfCols() {
		return numOfCols;
	}

	public int getNumOfRows() {
		return numOfRows;
	}

	public String getMessage() {
		return message;
	}

	public String connectDB() {
		messageBean.setRenderErrorMessage(false);
		String dbms = dBAccessInfoBean.getDbms();
		switch (dbms) {
		case MY_SQL:
			jdbcDriver = "com.mysql.jdbc.Driver";
			url = "jdbc:mysql://" + dBAccessInfoBean.getDbmsHost() + ":3306/" + dBAccessInfoBean.getDbSchema()
					+ "?&useSSL=false";
			break;
		case DB2:
			jdbcDriver = "com.ibm.db2.jcc.DB2Driver";
			url = "jdbc:db2://" + dBAccessInfoBean.getDbmsHost() + ":50000/" + dBAccessInfoBean.getDbSchema();
			break;
		case ORACLE:
			jdbcDriver = "oracle.jdbc.driver.OracleDriver";
			url = "jdbc:oracle:thin:@" + dBAccessInfoBean.getDbmsHost() + ":1521:" + dBAccessInfoBean.getDbSchema();
			break;
		}
		try {

			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(url, dBAccessInfoBean.getUserName(),
					dBAccessInfoBean.getPassword());
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			databaseMetaData = connection.getMetaData();
			//status = Boolean.TRUE;
			return "SUCCESS";
		} catch (ClassNotFoundException ce) {
			message = "Database: " + dBAccessInfoBean.getDbms() + " not supported.";
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);

			return "FAIL";
		} catch (SQLException se) {
			if (se.getSQLState().equals(ACCESS_DENIED)) {
				message = "Invalid credentials!";
			} else if (se.getSQLState().equals(INVALID_DB_SCHEMA)) {
				message = "Invalid database schema!";
			} else if (se.getSQLState().equals(TIMEOUT)) {
				message = "Check host & port!";
			} else if (se.getSQLState().equals(INVALID_PORT)) {
				message = "Invalid port. It must contain only digits!";
			} else {
				message = "SQL Exception occurred!\n" + "Error Code: " + se.getErrorCode() + "\n"
						+ "SQL State: " + se.getSQLState() + "\n" + "Message :" + se.getMessage() + "\n\n";
			}
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		} catch (Exception e) {
			message = "Exception occurred: " + e.getMessage();
			close();
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);
			return "FAIL";

		}

	}

	public void close() {
		try {
			if (resultSet != null) {

				resultSet.close();
			}
			if (statement != null) {

				statement.close();
			}
			if (connection != null) {

				connection.close();

			}
			//status = Boolean.FALSE;
		} catch (SQLException e) {
			
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n"
					+ "SQL State: " + e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);
		}
	}

	public List<String> tableList() {
		List<String> tableList = null;
		try {
			if (databaseMetaData != null) {
				resultSet = databaseMetaData.getTables(null, dBAccessInfoBean.getUserName(), null, TABLE_TYPES);
				resultSet.last();
				int numberOfRows = resultSet.getRow();
				tableList = new ArrayList<String>(numberOfRows);
				resultSet.beforeFirst();
				String tableName = "";
				if (resultSet != null) {
					while (resultSet.next()) {
						tableName = resultSet.getString("TABLE_NAME");
						if (!dBAccessInfoBean.getDbms().equalsIgnoreCase("oracle") || tableName.length() < 4)
							tableList.add(tableName);
						else if (!tableName.substring(0, 4).equalsIgnoreCase("BIN$"))
							tableList.add(tableName);
					}
				}
			}
		} catch (SQLException e) {
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n"
					+ "SQL State: " + e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);
		}

		return tableList;
	}

	public List<String> columnList(String tableName) {
		List<String> columnList = new ArrayList<String>();
		try {
			if (databaseMetaData != null) {
				resultSet = databaseMetaData.getColumns(null, dBAccessInfoBean.getDbSchema(), tableName, null);

				String columnName = "";
				if (resultSet != null) {
					while (resultSet.next()) {
						columnName = resultSet.getString("COLUMN_NAME");
						columnList.add(columnName);
					}
				}
			}
		} catch (SQLException e) {
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n"
					+ "SQL State: " + e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);
		}
		return columnList;
	}

	public String execute(String query) {
		try {
			if (connection != null && statement != null) {
				if (query.toLowerCase().startsWith("select")) {
					resultSet = statement.executeQuery(query);
					if (resultSet != null) {
						resultSetMetaData = resultSet.getMetaData();
						numOfCols = resultSetMetaData.getColumnCount();
						resultSet.last();
						numOfRows = resultSet.getRow();
						resultSet.beforeFirst();
						columnNamesSelected = new ArrayList<String>(numOfCols);
						for (int i = 0; i < numOfCols; i++) {
							columnNamesSelected.add(resultSetMetaData.getColumnName(i + 1));
						}

					}

				} else {
					// UPDATE,INSERT,DELETE
					statement.executeUpdate(query);

				}
				

			}
			return "SUCCESS";
		} catch (SQLException e) {
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n"
					+ "SQL State: " + e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}

	public void generateResult() {
		if (resultSet != null) {
			result = ResultSupport.toResult(resultSet);
		}
	}
	public List<String> executequeryList(String query) {

		List<String> queryList = new ArrayList<String>();
		try {
			execute(query);
			
				if (resultSet != null) {
					int numOfCols = resultSetMetaData.getColumnCount();
					resultSet.last();
					numOfRows = resultSet.getRow();
					resultSet.beforeFirst();
					while (resultSet.next()) {
						String[] output = new String[numOfCols];

						for (int i = 0; i < numOfCols; i++) {
							output[i] = resultSet.getString(i + 1);
							queryList.add(output[i]);
						}
						
					}
					
				
			}
			
		} catch (SQLException e) {
			System.err.println("SQLState: " + ((SQLException) e).getSQLState());

			System.err.println("Error Code: " + ((SQLException) e).getErrorCode());

			System.err.println("Message: " + e.getMessage());
			close();
		}
		return queryList;
	}
}
