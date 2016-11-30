package edu.uic.ids517.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

public class FeedbackBean {
private String questionText;
private double correctAns;
private double studentAns;
private double points;
private DBAccessBean dbaseBean;
private ActionStudentBean studentBean;
private StudentLogin studentLoginBean;
ResultSet resultSet, rs;
private List<FeedbackBean> f;

private boolean renderAns;
public boolean isRenderAns() {
	return renderAns;
}

public void setRenderAns(boolean renderAns) {
	this.renderAns = renderAns;
}

@PostConstruct
public void init() {
	FacesContext context = FacesContext.getCurrentInstance();
	System.out.println(context);
	Map<String, Object> m = context.getExternalContext().getSessionMap();
	dbaseBean = (DBAccessBean) m.get("dBAccessBean");
	// messageBean = (MessageBean) m.get("messageBean");
	studentBean = (ActionStudentBean) m.get("actionStudentBean");
	studentLoginBean = (StudentLogin) m.get("studentLoginBean");
	f=new ArrayList<>();
	
}
public String getFB(){
	feedback();
	return "Review";
}
public List<FeedbackBean> getF() {
	return f;
}

public void setF(List<FeedbackBean> f) {
	this.f = f;
}

void feedback(){
	f.clear();
	String userName= studentLoginBean.getUserName();
	String code = studentBean.getCourse();
	String test = studentBean.getTest();
	try {
		String studentUser= "select s.uin from f16g321_student s where s.user_name='"+studentLoginBean.getUserName()+"';";
		//System.out.println(studentUser);
		dbaseBean.execute(studentUser);
		rs = dbaseBean.getResultSet();
		String uin="";
		if (rs != null && rs.next()) {
			uin=rs.getString(1);
		}
		
		String feedback="select  f.question_id,f.ans_selected,q.question_text,q.correct_ans,t.points_per_ques from f16g321_feedback f join f16g321_student s on s.uin = f.uin join f16g321_questions q on q.question_id =f.question_id join f16g321_test t on t.test_id=q.test_id where s.uin="+uin+" and t.code="+code+" and t.test_id='"+test+"';";
		dbaseBean.execute(feedback);
		rs = dbaseBean.getResultSet();
		while(rs.next()){
			FeedbackBean feed = new FeedbackBean();
			feed.setQuestionText(rs.getString(3));
			feed.setCorrectAns(rs.getDouble(4));
			feed.setStudentAns(rs.getDouble(2));
			feed.setPoints(rs.getDouble(5));
			f.add(feed);
		}
		renderAns=true;
	}catch(SQLException e){
			
		}
}

public String getQuestionText() {
	return questionText;
}
public void setQuestionText(String questionText) {
	this.questionText = questionText;
}
public double getCorrectAns() {
	return correctAns;
}
public void setCorrectAns(double correctAns) {
	this.correctAns = correctAns;
}
public double getStudentAns() {
	return studentAns;
}
public void setStudentAns(double studentAns) {
	this.studentAns = studentAns;
}
public double getPoints() {
	return points;
}
public void setPoints(double points) {
	this.points = points;
}

}
