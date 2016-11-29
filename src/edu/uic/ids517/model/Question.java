package edu.uic.ids517.model;

import java.io.Serializable;

public class Question implements Serializable {

	private static final long serialVersionUID = 1L;
	private int questionId;
	private String questionType;
	private String questionString;
	private double answer;
	private double answerError;
	private double studentAnswer;
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

	public double getStudentAnswer() {
		return studentAnswer;
	}

	public void setStudentAnswer(double studentAnswer) {
		this.studentAnswer = studentAnswer;
	}

	public double getAnswer() {
		return answer;
	}

	public void setAnswer(double answer) {
		this.answer = answer;
	}

	public String toString() {
		return questionType + "," + questionString + "," + answer + "," + answerError + "\n";

	}

}
