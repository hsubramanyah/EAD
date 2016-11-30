package edu.uic.ids517.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.jstl.sql.Result;

public class ActionStudentBean {
	private String course = "";
	private String test = "";
	private List<String> courses;
	private List<String> tests;
	private int availableScore;

	public int getAvailableScore() {
		return availableScore;
	}

	public void setAvailableScore(int availableScore) {
		this.availableScore = availableScore;
	}

	private List<String> availableTests;

	private boolean testScore;

	public boolean isTestScore() {
		return testScore;
	}

	public void setTestScore(boolean testScore) {
		this.testScore = testScore;
	}

	private boolean renderCourseList;
	private boolean renderTestList;
	private DBAccessBean dbaseBean;
	private FacesContext context;
	private Result result;
	private StudentLogin studentLoginBean;
	private MessageBean messageBean;

	public ActionStudentBean() {
		setCourses(new ArrayList<String>());

		renderCourseList = true;
	}

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbaseBean = (DBAccessBean) m.get("dBAccessBean");
		studentLoginBean = (StudentLogin) m.get("studentLoginBean");
		messageBean = (MessageBean) m.get("messageBean");
		String query = "select distinct se.code from f16g321_student_enroll se join f16g321_student s on s.uin=se.uin where s.user_name='"
				+ studentLoginBean.getUserName() + "';";

		if (dbaseBean != null)
			courses = dbaseBean.executequeryList(query);
		setRenderCourseList(true);

	}

	public void listTests() {
		messageBean.resetAll();
		if(course.isEmpty()){
			messageBean.setErrorMessage("Please select Course Name from the list");
			messageBean.setRenderErrorMessage(true);
			return;
		}
		String query = "select t.test_id from f16g321_test t join f16g321_course c on c.code=t.code where c.code='"
				+ course + "';";
		tests = dbaseBean.executequeryList(query);
		setRenderTestList(true);

	}

	public String takeTest() {
		messageBean.resetAll();
		if(course.isEmpty()){
			messageBean.setErrorMessage("Please select Course Name from the list");
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		} else if (test.isEmpty()){
			messageBean.setErrorMessage("Please List Test and select Test Name from the list");
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		
		java.sql.Timestamp sqlDate = new java.sql.Timestamp(System.currentTimeMillis());
		String query = "select t.test_id from f16g321_test t join f16g321_course c on c.code=t.code where c.code='"
				+ course + "' and t.end_time > '" + sqlDate + "';";
		availableTests = dbaseBean.executequeryList(query);

		if (availableTests.contains(test)) {
			testScore = false;
			return "Test";
		} else {
			testScore = true;
			updateScore();
			return "Feedback";

		}
	}

	public void updateScore() {
		messageBean.resetAll();
		String scoreQuery = "select sc.score from f16g321_scores sc join f16g321_student s on s.uin =sc.uin where sc.test_id='"
				+ test + "' and s.user_name='" + studentLoginBean.getUserName() + "';";
		if (dbaseBean != null) {
			dbaseBean.execute(scoreQuery);
		}
		ResultSet rs = dbaseBean.getResultSet();
		try {
			if (rs != null) {
				rs.first();
				availableScore = rs.getInt(1);
			}
		} catch (SQLException e) {

		}
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public List<String> getCourses() {
		return courses;
	}

	public void setCourses(List<String> courses) {
		this.courses = courses;
	}

	public boolean isRenderCourseList() {
		return renderCourseList;
	}

	public void setRenderCourseList(boolean renderCoourseList) {
		this.renderCourseList = renderCoourseList;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public boolean isRenderTestList() {
		return renderTestList;
	}

	public void setRenderTestList(boolean renderTestList) {
		this.renderTestList = renderTestList;
	}

	public List<String> getTests() {
		return tests;
	}

	public void setTests(List<String> tests) {
		this.tests = tests;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

}
