package edu.uic.ids517.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.fileupload.UploadedFile;

public class UploadFileActionBean {
	private MessageBean messageBean;
	private DBAccessBean dBAccessBean;
	private UploadedFile uploadedFile;
	private String fileLabel;
	private String fileName;
	private String fileType = "";
	private long fileSize;
	private String fileContentType;
	private int numberRows;
	private int numberColumns;
	private String uploadedFileContents;
	private boolean fileImport;
	private boolean fileImportError;
	private FacesContext context;
	private String contextPath;
	private boolean uploadSuccess;
	private boolean uploadTest = false;
	private boolean uploadCourse = false;

	public boolean isUploadTest() {
		return uploadTest;
	}

	public void setUploadTest(boolean uploadTest) {
		this.uploadTest = uploadTest;
	}

	private List<TestRoster> testRoster = new ArrayList<TestRoster>();
	private List<CourseRoster> courseRoster = new ArrayList<CourseRoster>();

	public List<TestRoster> getTestRoster() {
		return testRoster;
	}

	public void setTestRoster(List<TestRoster> testRoster) {
		this.testRoster = testRoster;
	}

	public boolean isUploadSuccess() {
		return uploadSuccess;
	}

	public void setUploadSuccess(boolean uploadSuccess) {
		this.uploadSuccess = uploadSuccess;
	}

	private int crn;
	private String code;
	private String description;
	private int uin;
	private String last_name;
	private String first_name;
	private String user_name;

	private String startDate;
	private String endDate;
	private String testId;
	private String uploadType;
	private String duration;
	private Double pointsPerQues;

	public Double getPointsPerQues() {
		return pointsPerQues;
	}

	public void setPointsPerQues(Double pointsPerQues) {
		this.pointsPerQues = pointsPerQues;
	}

	//private List<String> inputList = new ArrayList<String>();

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dBAccessBean = (DBAccessBean) m.get("dBAccessBean");
		// messageBean = (MessageBean) m.get("messageBean");
		contextPath = context.getExternalContext().getRealPath("/");
	}

	public String processFileUpload() {
		List<String> inputList = new ArrayList<String>();
		String status = "SUCCESS";
		uploadedFileContents = null;
		// messageBean.setErrorMessage("Error");
		// test println only
		// System.out.println("context path: " + contextPath);
		String path = contextPath + "temp";
		// System.out.println("path: " + path);
		File tempFile = null;
		FileOutputStream fos = null;
		int n = 0;
		// System.out.println("Message Bean =" + messageBean);
		// System.err.println(messageBean.getErrorMessage());
		// messageBean.setErrorMessage("");
		fileImport = false;
		fileImportError = true;
		try {
			fileName = uploadedFile.getName();
			fileName = fileName.substring((fileName.lastIndexOf("\\") + 1), fileName.length());
			fileSize = uploadedFile.getSize();
			fileContentType = uploadedFile.getContentType();
			// next line if want upload in String for memory processing
			// uploadedFileContents = new String(uploadedFile.getBytes());
			tempFile = new File(path + "/" + fileName);

			fos = new FileOutputStream(tempFile);

			// next line if want file uploaded to disk

			fos.write(uploadedFile.getBytes());

			fos.close();

			Scanner s;

			s = new Scanner(tempFile);

			String input;
			// String[] values;

			// input = s.nextLine();
			while (s.hasNext()) {
				input = s.nextLine();
				inputList.add(input);

				n++;
			}
			numberRows = n;
			if (uploadType.equals("courseRoster")) {
				updateCourseRoster(inputList);
			} else {
				updateTestRoster(inputList);
			}

			s.close();

			fileImport = true;
			fileImportError = false;
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			fileImportError = true;
		} catch (IOException e) {

			e.printStackTrace();
			fileImportError = true;
		}
		return status;
	}

	private void updateTestRoster(List<String> values) {

		try {
			uploadCourse = false;
			String separator = "";
			if (fileType.equals("tsv"))
				separator = "\t";
			else
				separator = ",";
			String[] input;
			String query = "select count(*) from f16g321_test where test_id ='" + testId + "' and code = '" + code
					+ "'";
			dBAccessBean.execute(query);
			ResultSet rs = dBAccessBean.getResultSet();
			int count = 0;
			if (rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			if (count > 0) {
				query = "update f16g321_test set start_time='" + startDate + "', end_time='" + endDate + "', duration='"
						+ duration + "', points_per_ques=" + pointsPerQues + ", total=" + numberRows * pointsPerQues
						+ " where test_id ='" + testId + "' and code = '" + code + "'";

			} else {
				query = "Insert into f16g321_test(test_id,code,start_time,end_time,duration,points_per_ques,total) values ('"
						+ testId + "'," + code + ",'" + startDate + "','" + endDate + "','" + duration + "',"
						+ pointsPerQues + "," + numberRows * pointsPerQues + ")";
			}
			System.out.println(query);
			dBAccessBean.execute(query);
			for (String val : values) {
System.out.println(values);
				input = val.split(separator);
				String question_type = input[0];
				String question_text = input[1];
				String correct_ans = input[2];
				double tolerance = Double.parseDouble(input[3]);

				String question = "Insert into f16g321_questions(test_id,question_type,question_text,correct_ans,tolerance) values('"
						+ testId + "','" + question_type + "','" + question_text + "','" + correct_ans + "',"
						+ tolerance + ")";
				dBAccessBean.execute(question);

				TestRoster t = new TestRoster(question_type, question_text, correct_ans, tolerance);
				testRoster.add(t);
				// System.out.println("reahed");
			}
			uploadTest = true;

		} catch (Exception e) {
			messageBean.setErrorMessage("Couldnt parse the file. File upload failed");
			e.printStackTrace();
		}

	}

	public List<CourseRoster> getCourseRoster() {
		return courseRoster;
	}

	public void setCourseRoster(List<CourseRoster> courseRoster) {
		this.courseRoster = courseRoster;
	}

	public boolean isUploadCourse() {
		return uploadCourse;
	}

	public void setUploadCourse(boolean uploadCourse) {
		this.uploadCourse = uploadCourse;
	}

	private void updateCourseRoster(List<String> values) {
		try {

			uploadTest = false;
			String separator = "";
			if (fileType.equals("tsv"))
				separator = "\t";
			else
				separator = ",";
			String[] input;
			String[] inputHeader = values.get(0).split(separator);

			String courseQuery = "Insert into f16g321_course(crn,code,description) values(" + crn + ",'" + code + "','"
					+ description + "');";

			dBAccessBean.execute(courseQuery);

			/*
			 * String testQuery =
			 * "Insert into f16g321_test(test_id,code) values ('Exam01','" +
			 * code + "');"; dBAccessBean.execute(testQuery); testQuery =
			 * "Insert into f16g321_test(test_id,code) values ('Exam02','" +
			 * code + "');"; dBAccessBean.execute(testQuery); testQuery =
			 * "Insert into f16g321_test(test_id,code) values ('Exam03','" +
			 * code + "');"; dBAccessBean.execute(testQuery); testQuery =
			 * "Insert into f16g321_test(test_id,code) values ('Project','" +
			 * code + "');"; dBAccessBean.execute(testQuery);
			 */
			int i = 0;
			for (String val : values) {
				input = val.split(separator);
				String testQuery;
				if (i == 0) {
					i++;
					for (int a = 7; a < input.length; a++) {
						testQuery = "Insert into f16g321_test(test_id,code) values ('" + inputHeader[a] + "','" + code
								+ "');";
						dBAccessBean.execute(testQuery);
					}
					continue;
				}

				
				String lastName = input[0];
				String firstName = input[1];
				String userName = input[2];
				String uin = input[3];
				String lastAccess = input[4];
				String availability = input[5];
				Double total = Double.parseDouble((String) input[6]);

				String studentQuery = "Insert into f16g321_student (uin,last_name,first_name,user_name) values(" + uin
						+ ",'" + lastName + "','" + firstName + "','" + userName + "');";

				dBAccessBean.execute(studentQuery);
				String studentScores;
				
				double tempTotal = 0;
				for (int a = 7; a < input.length; a++) {

					studentScores = "Insert into f16g321_scores values(" + uin + ",'" + inputHeader[a] + "',"
							+ Double.parseDouble(input[a]) + ", '" + code + "');";
					dBAccessBean.execute(studentScores);
					tempTotal = tempTotal + Double.parseDouble(input[a]);

				}

				String studentEnroll = "Insert into f16g321_student_enroll values('" + code + "'," + uin + ","
						+ tempTotal + ");";
				dBAccessBean.execute(studentEnroll);
				// Double exam01 = Double.parseDouble((String) input[7]);
				// Double exam02 = Double.parseDouble((String) input[8]);
				// Double exam03 = Double.parseDouble((String) input[9]);
				// Double project = Double.parseDouble((String) input[10]);

				/*
				 * studentScores = "Insert into f16g321_scores values(" + uin +
				 * ",'Exam01'," + exam01 + code + ");";
				 * 
				 * dBAccessBean.execute(studentScores); studentScores =
				 * "Insert into f16g321_scores values(" + uin + ",'Exam02'," +
				 * exam02 + code + ");"; dBAccessBean.execute(studentScores);
				 * studentScores = "Insert into f16g321_scores values(" + uin +
				 * ",'Exam03'," + exam03 + code + ");";
				 * dBAccessBean.execute(studentScores); studentScores =
				 * "Insert into f16g321_scores values(" + uin + ",'Project'," +
				 * project + code + ");"; dBAccessBean.execute(studentScores);
				 * CourseRoster c = new CourseRoster(lastName, firstName,
				 * userName, uin, lastAccess, availability, total, exam01,
				 * exam02, exam03, project); courseRoster.add(c);
				 */
			}

			uploadCourse = true;

		} catch(NumberFormatException n){
			messageBean.setErrorMessage("Data Issue at row     "+values+ ".    File upload failed" + n);
			
		}
		catch (Exception e) {
			messageBean.setErrorMessage("Couldnt parse the file. File upload failed" + e);
			e.printStackTrace();
		}
	}

	/*public void executeCourseRoster(String[] values, int n) {
		last_name = values[3];
		first_name = values[4];
		user_name = values[5];
		uin = Integer.parseInt(values[6]);

		if (n == 0) {
			crn = Integer.parseInt(values[0]);
			code = values[1];
			description = values[2];
			String course = "Insert into f16g321_course(crn,code,description) values(" + crn + ",'" + code + "','"
					+ description + "')";
			dBAccessBean.execute(course);
		}
		String query = "INSERT INTO f16g321_student (uin,last_name,first_name,user_name) VALUES (" + uin + ",'"
				+ last_name + "','" + first_name + "','" + user_name + "')";
		dBAccessBean.execute(query);
	}*/

	/*
	 * public void test(String[] values, int n) { Random random = new Random();
	 * crn = Integer.parseInt(values[0]); code = values[1]; String test_id =
	 * values[2]; String start_time = values[3]; String end_time = values[4];
	 * String duartion = values[5]; int question_id = random.nextInt(999999);
	 * String question_type = values[6]; String question_text = values[7];
	 * String correct_ans = values[8]; double tolerance =
	 * Double.parseDouble(values[9]);
	 * 
	 * if (n == 0) { String test =
	 * "Insert into f16g321_test(test_id,code,start_time,end_time,duration) values ('"
	 * + test_id + "','" + code + "','" + start_time + "','" + end_time + "','"
	 * + duartion + "')";
	 * 
	 * dBAccessBean.execute(test); } String question =
	 * "Insert into f16g321_questions(question_id,test_id,question_type,question_text,correct_ans,tolerance) values("
	 * + question_id + ",'" + test_id + "','" + question_type + "','" +
	 * question_text + "','" + correct_ans + "'," + tolerance + ")";
	 * dBAccessBean.execute(question); }
	 */

	public boolean isFileImport() {
		return fileImport;
	}

	public void setFileImport(boolean fileImport) {
		this.fileImport = fileImport;
	}

	public boolean isFileImportError() {
		return fileImportError;
	}

	public void setFileImportError(boolean fileImportError) {
		this.fileImportError = fileImportError;
	}

	public String getFileLabel() {
		return fileLabel;
	}

	public void setFileLabel(String fileLabel) {
		this.fileLabel = fileLabel;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public MessageBean getMessageBean() {
		return messageBean;
	}

	public void setMessageBean(MessageBean messageBean) {
		this.messageBean = messageBean;
	}

	public DBAccessBean getDbaseBean() {
		return dBAccessBean;
	}

	public void setDbaseBean(DBAccessBean dbaseBean) {
		this.dBAccessBean = dbaseBean;
	}

	public FacesContext getContext() {
		return context;
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public int getNumberRows() {
		return numberRows;
	}

	public void setNumberRows(int numberRows) {
		this.numberRows = numberRows;
	}

	public int getNumberColumns() {
		return numberColumns;
	}

	public void setNumberColumns(int numberColumns) {
		this.numberColumns = numberColumns;
	}

	public String getUploadedFileContents() {
		return uploadedFileContents;
	}

	public void setUploadedFileContents(String uploadedFileContents) {
		this.uploadedFileContents = uploadedFileContents;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public int getCrn() {
		return crn;
	}

	public void setCrn(int crn) {
		this.crn = crn;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getUploadType() {
		return uploadType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	
}
