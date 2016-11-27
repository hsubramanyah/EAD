package edu.uic.ids517.model;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class InstructorActionBean {

	private DBAccessBean dBAccessBean;
	private FacesContext context;
	private MessageBean messageBean;
	private List<String> courseList;
	private String courseSelected;
	private boolean courseListRendered = false;
	private List<String> testList;
	private String testSelected;
	private boolean testListRendered = false;
	// private List<String> studentList;
	private ResultSet rs;
	private CourseRoster courseRoster;
	private Question question;
	private List<CourseRoster> courseRosterList;
	private List<Question> questionList;
	private boolean renderCourseRosterList = false;
	private boolean renderTestQuestionList = false;
	private StringBuffer sb;
	private String courseCSV;
	private String testCSV;
	private int noOfRows = 0;
	private String chartPath;

	public String getChartPath() {
		return chartPath;
	}

	public void setChartPath(String chartPath) {
		this.chartPath = chartPath;
	}

	public int getNoOfRows() {
		return noOfRows;
	}

	public boolean isRenderTestQuestionList() {
		return renderTestQuestionList;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}

	public void setRenderTestQuestionList(boolean renderTestQuestionList) {
		this.renderTestQuestionList = renderTestQuestionList;
	}

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

	public boolean isRenderCourseRosterList() {
		return renderCourseRosterList;
	}

	public void setRenderCourseRosterList(boolean renderCourseRosterList) {
		this.renderCourseRosterList = renderCourseRosterList;
	}

	public List<CourseRoster> getCourseRosterList() {
		return courseRosterList;
	}

	public void setCourseRosterList(List<CourseRoster> courseRosterList) {
		this.courseRosterList = courseRosterList;
	}

	public List<String> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<String> courseList) {
		this.courseList = courseList;
	}

	public String getCourseSelected() {
		return courseSelected;
	}

	public void setCourseSelected(String courseSelected) {
		this.courseSelected = courseSelected;
	}

	public boolean isCourseListRendered() {
		return courseListRendered;
	}

	public void setCourseListRendered(boolean courseListRendered) {
		this.courseListRendered = courseListRendered;
	}

	public List<String> getTestList() {
		return testList;
	}

	public void setTestList(List<String> testList) {
		this.testList = testList;
	}

	public String getTestSelected() {
		return testSelected;
	}

	public void setTestSelected(String testSelected) {
		this.testSelected = testSelected;
	}

	public boolean isTestListRendered() {
		return testListRendered;
	}

	public void setTestListRendered(boolean testListRendered) {
		this.testListRendered = testListRendered;
	}

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		System.out.println(context);
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dBAccessBean = (DBAccessBean) m.get("dBAccessBean");
		messageBean = (MessageBean) m.get("messageBean");
		listCourse();

	}

	public String listCourse() {

		try {
			messageBean.resetAll();
			String sqlQuery = "Select distinct code from f16g321_course;";
			courseList = dBAccessBean.executequeryList(sqlQuery);
			if (null != courseList) {
				courseListRendered = true;
			}
			return "SUCCESS";
		} catch (Exception e) {
			courseListRendered = false;
			messageBean.setErrorMessage("");
			messageBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}

	public String listTest() {

		try {
			messageBean.resetAll();
			if (null != courseSelected && !"".equals(courseSelected)) {
				String sqlQuery = "select t.test_id from f16g321_test t where t.code='" + courseSelected + "';";
				testList = dBAccessBean.executequeryList(sqlQuery);
				if (null != testList) {
					testListRendered = true;
				}
			} else {
				messageBean.setErrorMessage("Please select Course Name from the list");
				messageBean.setRenderErrorMessage(true);
				return "FAIL";

			}
		} catch (Exception e) {
			testListRendered = false;
			messageBean.setErrorMessage("");
			messageBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		return "SUCCESS";
	}

	public String displayCourseRoster() {
		try {
			messageBean.resetAll();
			renderTestQuestionList = false;
			renderCourseRosterList = false;
			sb = new StringBuffer(
					"Last_Name,First_Name,Username,Student_ID,Last_Access,Availability,Total,Exam01,Exam02,Exam03,Project \n");
			listTest();
			String sqlQuery = "select last_name, first_name, 	user_name, s.uin ,last_logintime "
					+ "from f16g321_student_enroll sc join f16g321_student s on s.uin = sc.uin where sc.code ='"
					+ courseSelected + "';";

			if (dBAccessBean.execute(sqlQuery).equals("SUCCESS")) {

				rs = dBAccessBean.getResultSet();
				noOfRows = dBAccessBean.getNumOfRows();
				System.out.println(noOfRows);
				courseRosterList = new ArrayList<CourseRoster>(noOfRows);
				if (rs != null) {
					while (rs.next()) {
						courseRoster = new CourseRoster();
						courseRoster.setLastName(rs.getString("last_name"));
						courseRoster.setFirstName(rs.getString("first_name"));
						courseRoster.setUserName(rs.getString("user_name"));
						courseRoster.setUin(rs.getString("uin"));
						courseRoster.setLastAccess(rs.getString("last_logintime"));
						courseRoster.setAvailability("Yes");
						courseRosterList.add(courseRoster);
					}

					for (CourseRoster temp : courseRosterList) {
						sqlQuery = "select score from f16g321_scores where test_id = 'Exam01' and uin ="
								+ temp.getUin();
						temp.setExam01(Double.parseDouble(
								dBAccessBean.executequeryList(sqlQuery).toString().replace("[", "").replace("]", "")));

						sqlQuery = "select score from f16g321_scores where test_id = 'Exam02' and uin ="
								+ temp.getUin();
						temp.setExam02(Double.parseDouble(
								dBAccessBean.executequeryList(sqlQuery).toString().replace("[", "").replace("]", "")));

						sqlQuery = "select score from f16g321_scores where test_id = 'Exam03' and uin ="
								+ temp.getUin();
						temp.setExam03(Double.parseDouble(
								dBAccessBean.executequeryList(sqlQuery).toString().replace("[", "").replace("]", "")));

						sqlQuery = "select score from f16g321_scores where test_id = 'Project' and uin ="
								+ temp.getUin();
						temp.setProject(
								Double.parseDouble(dBAccessBean.executequeryList(sqlQuery).toString().replace("[", "").replace("]", "")));
						double total = temp.getExam01() + temp.getExam02() + temp.getExam03() + temp.getProject();
						temp.setTotal(total);
						sb.append(temp.toString());
					}
					renderCourseRosterList = true;
					courseCSV = sb.toString();

				} else {
					messageBean.setErrorMessage("No Roster Available for selected course");
					messageBean.setRenderErrorMessage(true);
					renderCourseRosterList = false;
				}
			}
		} catch (Exception e) {
			messageBean.setErrorMessage("Error in Listing Roster");
			messageBean.setRenderErrorMessage(true);
			renderCourseRosterList = false;
			e.printStackTrace();
			return "FAIL";
		}
		return "SUCCESS";
	}

	public String displayTestQuestions() {
		try {
			messageBean.resetAll();
			sb = new StringBuffer("Question Type,Question Text,Correct Answer,Tolerance\n");
			renderCourseRosterList = false;
			renderTestQuestionList = false;
			if (null != testSelected && !"".equals(testSelected)) {
				String sqlQuery = "select question_type, question_text,correct_ans, tolerance from f16g321_questions where test_id ='"
						+ testSelected + "';";
				if (dBAccessBean.execute(sqlQuery).equals("SUCCESS")) {
					rs = dBAccessBean.getResultSet();
					noOfRows = dBAccessBean.getNumOfRows();
					questionList = new ArrayList<Question>(noOfRows);
					if (rs != null) {
						while (rs.next()) {
							question = new Question();
							question.setQuestionType(rs.getString("question_type"));
							question.setQuestionString(rs.getString("question_text"));
							question.setAnswer(rs.getString("correct_ans"));
							question.setAnswerError(Double.parseDouble(rs.getString("tolerance")));
							questionList.add(question);
							sb.append(question.toString());
						}
						renderTestQuestionList = true;
						testCSV = sb.toString();

					} else {
						messageBean.setErrorMessage("No Questions Available for selected Test");
						messageBean.setRenderErrorMessage(true);
						renderTestQuestionList = false;
					}
				}
			} else {
				messageBean.setErrorMessage("Please select Test from the list");
				messageBean.setRenderErrorMessage(true);
				renderTestQuestionList = false;
			}

		} catch (Exception e) {
			messageBean.setErrorMessage("Error in Listing Questions");
			messageBean.setRenderErrorMessage(true);
			renderTestQuestionList = false;
			return "FAIL";
		}
		return "SUCCESS";
	}

	public String exportRosterCsv() {
		try {
			downloadFile(courseRosterList, courseSelected + "_Roster.csv", "text/comma-separated-values", courseCSV);

		} catch (Exception e) {
			messageBean.setErrorMessage("Error in Exporting Roster");
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		return "SUCCESS";
	}

	public String exportRosterXml() {
		try {
			downloadFile(courseRosterList, courseSelected + "_Roster.xml", "text/xml", courseCSV);
		} catch (Exception e) {
			messageBean.setErrorMessage("Error in Exporting Roster");
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		return "SUCCESS";
	}

	public String exportTestCsv() {
		try {
			downloadFile(questionList, courseSelected + "_" + testSelected + "_Test.csv", "text/comma-separated-values",
					testCSV);

		} catch (Exception e) {
			messageBean.setErrorMessage("Error in ");
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		return "SUCCESS";
	}

	public String exportTestXml() {
		try {
			downloadFile(questionList, courseSelected + "_" + testSelected + "_Test.xml", "text/xml", testCSV);
		} catch (Exception e) {
			messageBean.setErrorMessage("Error in Exporting Roster");
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		return "SUCCESS";
	}

	public String downloadFile(List list, String fileName, String contentType, String classString) {

		try {

			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

			response.reset();
			response.setContentType(contentType);
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			OutputStream output = response.getOutputStream();

			if (fileName.indexOf(".xml") != -1) {
				XMLEncoder e = new XMLEncoder(new BufferedOutputStream(output));
				e.writeObject(list);
				e.close();
				output.close();

			} else if (fileName.indexOf(".csv") != -1) {
				output.write(classString.getBytes());
				output.flush();
				output.close();
			}

			fc.responseComplete();
			return "SUCCESS";

		} catch (Exception e) {
			messageBean.setErrorMessage("Exception in Exporting Roster" + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}
	
	public String createPieChart() {
		// create a dataset...
		try{
			context = FacesContext.getCurrentInstance();
		DefaultPieDataset data = new DefaultPieDataset();
		data.setValue("One", new Double(43.2));
		data.setValue("Two", new Double(10.0));
		data.setValue("Three", new Double(27.5));
		data.setValue("Four", new Double(17.5));
		data.setValue("Five", new Double(11.0));
		data.setValue("Six", new Double(19.4));
		JFreeChart chart = ChartFactory.createPieChart("Pie Chart", data, true, true, false);
		String path = context.getExternalContext().getRealPath("/ChartImages");
		File outChart = new File(path+"/barGraph.png");
		System.out.println(path);
		ChartUtilities.saveChartAsPNG(outChart, chart, 600, 450);
		System.out.println("outChart"+outChart.getAbsolutePath()); 
		chartPath = "/ChartImages/barGraph.png";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "SUCCESS";
	}

}
