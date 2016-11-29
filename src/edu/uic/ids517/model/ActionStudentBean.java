package edu.uic.ids517.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.jstl.sql.Result;

public class ActionStudentBean {
	private String course;

	private String test;

	private List<String> courses;
	private List<String> tests;

	private List<String> availableTests;
	private boolean renderCourseList;
	private boolean renderTestList;

	private DBAccessBean dbaseBean;
	private FacesContext context;
	private Result result;

private StudentLogin studentLoginBean;
	public ActionStudentBean() {
		setCourses(new ArrayList<String>());

		renderCourseList = true;
	}

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		System.out.println(context);
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbaseBean = (DBAccessBean) m.get("dBAccessBean");
		studentLoginBean = (StudentLogin) m.get("studentLoginBean");
		

		String query = "select distinct se.code from f16g321_student_enroll se join f16g321_student s on s.uin=se.uin where s.user_name='"+studentLoginBean.getUserName()+"';";
		if(dbaseBean!=null)
		courses = dbaseBean.executequeryList(query);
		// System.out.println(courses.toString());
		setRenderCourseList(true);
		// System.out.println("query executed"+" Rendered = "
		// +renderCourseList);

	}

	public void listTests() {
		String query = "select t.test_id from f16g321_test t join f16g321_course c on c.code=t.code where c.code='"
				+ course + "';";
		tests = dbaseBean.executequeryList(query);
		// System.out.println(tests.toString());
		setRenderTestList(true);
		// System.out.println("query executed" + " Rendered = " +
		// renderTestList);
	}

public String takeTest(){
	java.sql.Timestamp sqlDate = new java.sql.Timestamp(System.currentTimeMillis());
	String query = "select t.test_id from f16g321_test t join f16g321_course c on c.code=t.code where c.code='"
			+ course + "' and t.end_time > '"+sqlDate +"';";
	availableTests=dbaseBean.executequeryList(query);
	
	if(availableTests.contains(test)){
		return "Test";
	}
	else
	return "Feedback";
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
