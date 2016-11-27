package edu.uic.ids517.model;

public class TestRoster {
private  String question_type ;
private String question_text;
private String correct_ans;
private double tolerance ;

public TestRoster(String qt,String ques,String ans,double tol){
	question_type=qt;
	question_text=ques;
	correct_ans=ans;
	tolerance=tol;
	
}
public String getQuestion_type() {
	return question_type;
}
public void setQuestion_type(String question_type) {
	this.question_type = question_type;
}
public String getQuestion_text() {
	return question_text;
}
public void setQuestion_text(String question_text) {
	this.question_text = question_text;
}
public String getCorrect_ans() {
	return correct_ans;
}
public void setCorrect_ans(String correct_ans) {
	this.correct_ans = correct_ans;
}
public double getTolerance() {
	return tolerance;
}
public void setTolerance(double tolerance) {
	this.tolerance = tolerance;
}

}
