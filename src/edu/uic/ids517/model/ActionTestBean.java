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

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		System.out.println(context);
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbaseBean = (DBAccessBean) m.get("dBAccessBean");
		// messageBean = (MessageBean) m.get("messageBean");
		studentBean = (ActionStudentBean) m.get("actionStudentBean");
		question = (Question) m.get("question");
		test = studentBean.getTest();
		loadQuestion();
	}

	public void loadQuestion() {
		String query = "select * from f16g321_questions q where q.test_id='" + test + "'";
		if(dbaseBean!=null){
		dbaseBean.execute(query);

		setRenderQuestionList(true);
		rs = dbaseBean.getResultSet();
	

		if (rs == null) {
			// message bean
		} else {
			try {
				while (rs.next()) {
					Question questionRecord = new Question();

					questionRecord.setQuestionString(rs.getString("question_text"));
					questionRecord.setAnswer(rs.getString("correct_ans"));
					questionRecord.setAnswerError(rs.getDouble("tolerance"));

					questionLists.add(questionRecord);

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
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
		// System.out.println(studentAnswer.toString());
		int count = 0;
		for (Question q : questionLists) {
			if (q.getAnswer().equals(q.getStudentAnswer()))
				count++;
		}
		score=count;
		//System.out.println(count);
		
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
