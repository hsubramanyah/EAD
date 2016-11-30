<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Review</title>
</head>
<body>
<f:view>
<div align="center">

			<h:form>
				<h3 align="center">f16g321 Score</h3>
				<hr />
				<br />
				<h:commandButton type="submit" value="Student Home Page" action="student" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Change Role"
					action="selectRole" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Logout"
					action="#{dBAccessActionBean.studentLogout}" />&nbsp;&nbsp;&nbsp;
		</h:form>
			<br /> <br />
		</div>
		<hr />
		<p>
			Course :
			<h:outputText value="#{actionStudentBean.course }"></h:outputText>
		</p>
		<p>
			Test :
			<h:outputText value="#{actionStudentBean.test }"></h:outputText>
		</p>
		<p>
			Score :
			<h:outputText value="#{actionStudentBean.availableScore }"></h:outputText>
		</p>
		<br/>
		<hr />
		<h:form>
	<div
				style="background-attachment: scroll; overflow: auto; height: 300px; background-repeat: repeat">
				<t:dataTable value="#{feedbackBean.f}" var="row"
					rendered="#{feedbackBean.renderAns}" border="1"
					cellspacing="0" cellpadding="1" columnClasses="columnClass1 border"
					headerClass="headerClass" footerClass="footerClass"
					rowClasses="rowClass2" styleClass="dataTableEx" width="900">


					<h:column>
						<f:facet name="header">
							<h:outputText>Question String</h:outputText>
						</f:facet>
						<h:outputText value="#{row.questionText}" />
					</h:column>

					<h:column>
						<f:facet name="header">
							<h:outputText>Student Answer</h:outputText>
						</f:facet>
						<h:outputText value="#{row.studentAns}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText>Correct Answer</h:outputText>
						</f:facet>
						<h:outputText value="#{row.correctAns}" />
					</h:column>
				</t:dataTable>
				
			</div>
			</h:form>
</f:view>
</body>
</html>