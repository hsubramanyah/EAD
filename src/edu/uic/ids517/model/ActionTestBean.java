package edu.uic.ids517.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.jstl.sql.Result;

public class ActionTestBean implements Serializable {
	private List<Question> questionLists;
	private Question question;
	private boolean renderQuestionList;
	ResultSet resultSet, rs;
	private String test;
	private ActionStudentBean studentBean;
	private List<String> questionList;
	private List<String> columnNames;
	private int score;
	private DBAccessBean dbaseBean;
	private boolean renderQuestion;
	private Result result;
	private StudentLogin studentLoginBean;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbaseBean = (DBAccessBean) m.get("dBAccessBean");
		studentBean = (ActionStudentBean) m.get("actionStudentBean");
		studentLoginBean = (StudentLogin) m.get("studentLoginBean");
		question = (Question) m.get("question");
		test = studentBean.getTest();
		if (!studentBean.isTestScore())
			loadQuestion();
	}

	public void loadQuestion() {
		String query = "select * from f16g321_questions q where q.test_id='" + test + "'";
		if (dbaseBean != null) {
			dbaseBean.execute(query);

			setRenderQuestionList(true);
			rs = dbaseBean.getResultSet();

			if (rs == null) {
				
			} else {
				try {
					while (rs.next()) {
						Question questionRecord = new Question();

						questionRecord.setQuestionString(rs.getString("question_text"));
						questionRecord.setAnswer(rs.getDouble("correct_ans"));
						questionRecord.setAnswerError(rs.getDouble("tolerance"));

						questionLists.add(questionRecord);

					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public ActionTestBean() {
		setColumnNames(new ArrayList<String>());
		questionLists = new ArrayList<Question>();

	}

	public String processTest() {
		int count = 0;
		try {
			String studentUser = "select s.uin from f16g321_student s where s.user_name='"
					+ studentLoginBean.getUserName() + "';";
			dbaseBean.execute(studentUser);
			rs = dbaseBean.getResultSet();
			String uin = "";
			if (rs != null && rs.next()) {
				uin = rs.getString(1);
			}

			String questionId = "select q.question_id from f16g321_questions q where q.test_id='" + test + "' ;";
			dbaseBean.execute(questionId);
			rs = dbaseBean.getResultSet();
			List<Integer> qId = new ArrayList<>();
			while (rs.next()) {
				qId.add(rs.getInt(1));
			}

			
			String updateAns = "select  count(*) from f16g321_feedback f join f16g321_questions q on q.question_id=f.question_id join f16g321_student s on s.uin=f.uin where q.test_id='"
					+ test + "' and s.uin=" + uin + ";";
			dbaseBean.execute(updateAns);
			rs = dbaseBean.getResultSet();
			int exists = 0;
			if (rs != null && rs.next()) {
				exists = rs.getInt(1);
			}

			int qi = 0;
			String feedback = "";
			for (Question q : questionLists) {
				if ((q.getAnswer() - q.getAnswerError() <= q.getStudentAnswer())
						&& (q.getAnswer() + q.getAnswerError() >= q.getStudentAnswer()))
					count++;
				if (exists == 0) {
					feedback = "Insert into f16g321_feedback values(" + uin + "," + qId.get(qi) + ","
							+ q.getStudentAnswer() + ");";
				} else {
					feedback = "update f16g321_feedback f set f.ans_selected=" + q.getStudentAnswer() + " where f.uin="
							+ uin + " and f.question_id=" + qId.get(qi) + ";";
				}
				dbaseBean.execute(feedback);

				
				qi++;

			}
			qi = 0;
			score = count;
			
			String testPoints = "select t.points_per_ques from f16g321_test t where t.test_id='" + test
					+ "' and t.code=" + studentBean.getCourse() + "; ";
			dbaseBean.execute(testPoints);
			int points = 0;
			ResultSet rs = dbaseBean.getResultSet();
			if (rs != null && rs.next()) {
				points = rs.getInt(1);
			}
			score *= points;
			String scoreQuery = "select count(*) from f16g321_scores sc join f16g321_student s on s.uin =sc.uin where sc.test_id='"
					+ test + "' and s.user_name='" + studentLoginBean.getUserName() + "';";
			dbaseBean.execute(scoreQuery);
			rs = dbaseBean.getResultSet();

			if (rs != null && rs.next()) {
				count = rs.getInt(1);
			}

			if (count > 0) {
				scoreQuery = "update f16g321_scores sc set sc.score=" + score + " where sc.uin=" + uin + " and sc.code="
						+ studentBean.getCourse() + " and sc.test_id='" + test + "';";
			} else {
				scoreQuery = "insert into f16g321_scores values(" + uin + ",'" + test + "'," + score + ","
						+ studentBean.getCourse() + ");";
			}
			dbaseBean.execute(scoreQuery);
			
			String totalScore = "select sum(score) from f16g321_scores where code = '"+studentBean.getCourse()+"' and uin ="+uin+";";
			dbaseBean.execute(totalScore);
			rs = dbaseBean.getResultSet();
			double total;
			if (rs != null && rs.next()) {
				total = rs.getDouble(1);
				totalScore = "update f16g321_student_enroll set total = "+total+" where code = '"+studentBean.getCourse()+"' and uin ="+uin+";";
				dbaseBean.execute(totalScore);
			}
		
		
		} catch (SQLException e) {
			return "FAIL";
		}
		return "Success";
	}

	public boolean isRenderQuestionList() {
		return renderQuestionList;
	}

	public void setRenderQuestionList(boolean renderQuestionList) {
		this.renderQuestionList = renderQuestionList;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public List<String> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<String> questionList) {
		this.questionList = questionList;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public boolean isRenderQuestion() {
		return renderQuestion;
	}

	public void setRenderQuestion(boolean renderQuestion) {
		this.renderQuestion = renderQuestion;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public List<Question> getQuestionLists() {
		return questionLists;
	}

	public void setQuestionLists(List<Question> questionLists) {
		this.questionLists = questionLists;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

}
