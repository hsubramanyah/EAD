<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tests</title>
</head>
<body>
	<f:view>
		<div align="center">

			<h:form>
				<h3 align="center">f16g321 Test</h3>
				<hr />
				<br />
				<h:commandButton type="submit" value="Home" action="index" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Change Role"
					action="selectRole" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Logout"
					action="#{dBAccessActionBean.studentLogout}" />&nbsp;&nbsp;&nbsp;
		</h:form>
			<br /> <br />
		</div>
		<hr />
		<h:form>
			<h:outputText value="#{actionTestBean.test }" />
			<br />
			<div
				style="background-attachment: scroll; overflow: auto; height: 300px; background-repeat: repeat">
				<t:dataTable value="#{actionTestBean.questionLists}" var="row"
					rendered="#{actionTestBean.renderQuestionList}" border="1"
					cellspacing="0" cellpadding="1" columnClasses="columnClass1 border"
					headerClass="headerClass" footerClass="footerClass"
					rowClasses="rowClass2" styleClass="dataTableEx" width="900">


					<h:column>
						<f:facet name="header">
							<h:outputText>Question String</h:outputText>
						</f:facet>
						<h:outputText value="#{row.questionString}" />
					</h:column>

					<h:column>
						<f:facet name="header">
							<h:outputText>Answer</h:outputText>
						</f:facet>
						<h:inputText value="#{row.studentAnswer}" />
					</h:column>
				</t:dataTable>
				<h:commandButton type="submit" value="Submit"
					action="#{actionTestBean.processTest}" />
			</div>

		</h:form>
	</f:view>
</body>
</html>