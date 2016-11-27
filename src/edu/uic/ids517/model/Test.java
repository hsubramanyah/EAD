package edu.uic.ids517.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
@ManagedBean
public class Test {

	private DataModel<String> mStudentDataModel;
	private DataModel mColumns;
	private Object ColumnValue;
	private HashMap hm =  new HashMap();
	private List<String> name ;
	private String sortColumn ;
	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	private boolean ascending = true;
	public List<String> getName() {
		return name;
	}

	public void setName(List<String> name) {
		this.name = name;
	}

	private DBAccessBean dBAccessBean;
	private FacesContext context;
	private MessageBean messageBean;
	private ResultSet rs;
	public DataModel getmStudentDataModel() {
		return mStudentDataModel;
	}

	public void setmStudentDataModel(DataModel mStudentDataModel) {
		this.mStudentDataModel = mStudentDataModel;
	}

	public DataModel getmColumns() {
		return mColumns;
	}

	public void setmColumns(DataModel mColumns) {
		this.mColumns = mColumns;
	}

	public Object getColumnValue() {
		Object row = mStudentDataModel.getRowData();
		System.out.println("row "+row);
		Object column = mColumns.getRowData();
		System.out.println("col "+column);
		String key = Integer.toString(mStudentDataModel.getRowIndex()) + Integer.toString(mColumns.getRowIndex());
		
		return ColumnValue = hm.get(key);
	}

	public void setColumnValue(Object columnValue) {
		ColumnValue = columnValue;
	}

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		System.out.println(context);
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dBAccessBean = (DBAccessBean) m.get("dBAccessBean");
		messageBean = (MessageBean) m.get("messageBean");

	}

	public String displayCourseRoster() {
		try {
			messageBean.resetAll();

			String sqlQuery = "select uin from f16g321_student_enroll where code ='517';";

			mStudentDataModel = new ListDataModel<String>(dBAccessBean.executequeryList(sqlQuery));

			//List colList = new ArrayList<>(Arrays.asList("last_name", "first_name", "user_name", "uin",
					//"Last_Access", "Availability", "Total"));
			List colList = new ArrayList<>(Arrays.asList("last_name", "first_name", "user_name", "uin"));
			sqlQuery = "select test_id from f16g321_test where code ='517';";
			//colList.addAll(dBAccessBean.executequeryList(sqlQuery));
			mColumns = new ListDataModel(colList);

			for (int i = 0; i < mStudentDataModel.getRowCount(); i++) {
				for (int j = 0; j < mColumns.getRowCount(); j++) {
					mStudentDataModel.setRowIndex(i);
					mColumns.setRowIndex(j);
					Object row = mStudentDataModel.getRowData();
					Object column = mColumns.getRowData();
					String key = Integer.toString(i) + Integer.toString(j);
					sqlQuery = "Select " +column+" from f16g321_student where uin = "+ row + ";";
					if (dBAccessBean.execute(sqlQuery).equals("SUCCESS")) {
						System.out.println("88");
						rs = dBAccessBean.getResultSet();
						if (rs != null && rs.next()) {
							
								hm.put(key, rs.getString(1));
								
							
					
						}
					}
				}
			}

		} catch (Exception e) {
e.printStackTrace();
		}
		return "";
	}
}
