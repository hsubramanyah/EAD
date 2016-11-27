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
	private DataModel<String> mColumns;
	//private Object columnValue;
	private HashMap<String,String> hm = new HashMap<String,String>();
	//private List<String> name;
	private String sortColumn;

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

	

	private DBAccessBean dBAccessBean;
	private FacesContext context;
	private MessageBean messageBean;
	private InstructorActionBean instructorActionBean;
	private ResultSet rs;

	public DataModel<String> getmStudentDataModel() {
		return mStudentDataModel;
	}

	public void setmStudentDataModel(DataModel<String> mStudentDataModel) {
		this.mStudentDataModel = mStudentDataModel;
	}

	public DataModel<String> getmColumns() {
		return mColumns;
	}

	public void setmColumns(DataModel<String> mColumns) {
		this.mColumns = mColumns;
	}

	public Object getColumnValue() {
		Object row = mStudentDataModel.getRowData();
		System.out.println("row " + row);
		Object column = mColumns.getRowData();
		System.out.println("col " + column);
		String key = Integer.toString(mStudentDataModel.getRowIndex()) + "," + Integer.toString(mColumns.getRowIndex());
		if (mColumns.getRowIndex() > 5){
			 return Double.parseDouble( hm.get(key));
			 
		}
		return hm.get(key);
	}

	

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		System.out.println(context);
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dBAccessBean = (DBAccessBean) m.get("dBAccessBean");
		messageBean = (MessageBean) m.get("messageBean");
		instructorActionBean = (InstructorActionBean) m.get("instructorActionBean");
	}

	public String displayDynamicCourseRoster() {
		try {
			messageBean.resetAll();

			String sqlQuery = "select uin from f16g321_student_enroll where code ='"
					+ instructorActionBean.getCourseSelected() + "';";

			mStudentDataModel = new ListDataModel<String>(dBAccessBean.executequeryList(sqlQuery));

			List<String> colList = new ArrayList<>(Arrays.asList("Last_Name", "First_Name", "User_Name", "UIN",
					"Last_Access", "Availability", "Total"));
			sqlQuery = "select test_id from f16g321_test where code ='" + instructorActionBean.getCourseSelected()
					+ "';";
			colList.addAll(dBAccessBean.executequeryList(sqlQuery));
			mColumns = new ListDataModel<String>(colList);

			for (int i = 0; i < mStudentDataModel.getRowCount(); i++) {
				for (int j = 0; j < mColumns.getRowCount(); j++) {
					mStudentDataModel.setRowIndex(i);
					mColumns.setRowIndex(j);
					Object row = mStudentDataModel.getRowData();
					Object column = mColumns.getRowData();
					String key = Integer.toString(i) + "," + Integer.toString(j);
					if (j < 5) {
						sqlQuery = "Select " + column + " from f16g321_student where uin = " + row + ";";
					} else if (j > 6) {
						sqlQuery = "Select score from f16g321_scores where uin = " + row + " and code ='"
								+ instructorActionBean.getCourseSelected() + "' and test_id ='" + column + "';";
					} else if (j == 6) {
						sqlQuery = "Select total from f16g321_student_enroll where uin = " + row + " and code ='"
								+ instructorActionBean.getCourseSelected() + "';";
					} else if (j == 5) {
						sqlQuery = "select 'Yes' from dual;";
					}
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
			return "FAIL";
		}
		return "SUCCESS";
	}
}
