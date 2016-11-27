<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Feedback</title>
</head>
<body>
	<f:view>
		<div align="center">

			<h:form>
				<h3 align="center">f16g321 Score</h3>
				<hr />
				<br />
				<h:commandButton type="submit" value="Home" action="index" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Change Role"
					action="selectRole" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Logout"
					action="#{dBAccessActionBean.logout}" />&nbsp;&nbsp;&nbsp;
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
			<h:outputText value="#{actionTestBean.score }"></h:outputText>
		</p>
	</f:view>
</body>
</html>