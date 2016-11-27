package edu.uic.ids517.model;

import java.io.Serializable;

public class Question implements Serializable {

	private static final long serialVersionUID = 1L;
	private int questionId;
	private String questionType;
	private String questionString;
	private String answer;
	private double answerError;
	private String studentAnswer;
	private String courseName;

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getQuestionString() {
		return questionString;
	}

	public double getAnswerError() {
		return answerError;
	}

	public void setAnswerError(double answerError) {
		this.answerError = answerError;
	}

	public void setQuestionString(String questionString) {
		this.questionString = questionString;
	}

	public String getStudentAnswer() {
		return studentAnswer;
	}

	public void setStudentAnswer(String studentAnswer) {
		this.studentAnswer = studentAnswer;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String toString() {
		return questionType + "," + questionString + "," + answer + "," + answerError + "\n";

	}

}
