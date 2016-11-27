<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Database login</title>
</head>
<body>
	<f:view>
		<h3 align="center">Database Login</h3>
		<hr>
		<br />
		<center>
			<h:form>
				<h:commandButton type="submit" value="Home" action="#{dBAccessActionBean.back}" />
			</h:form>
		</center>
		<br />
		<hr>
		<br />
		<center>
			<h:form>
				<h:outputText value="#{messageBean.errorMessage}" style="color:red"
					rendered="#{messageBean.renderErrorMessage}" />
				<h:panelGrid columns="2">

					<h:outputText value="UserName:" />
					<h:inputText id="userName" value="#{dBAccessInfoBean.userName}" />
					<h:outputText value="Password:" />
					<h:inputSecret id="password" value="#{dBAccessInfoBean.password}" />
					<h:outputText value="DB Host:" />
					<h:inputText id="host" value="#{dBAccessInfoBean.dbmsHost}" />
					<h:outputText value="DB Schema:" />
					<h:inputText id="schema" value="#{dBAccessInfoBean.dbSchema}" />
					<h:outputText value="DBMS:" />
					<h:selectOneListbox value="#{dBAccessInfoBean.dbms}" size="3">
						<f:selectItem itemValue="MySQL" itemLabel="MySQL" />
						<f:selectItem itemValue="DB2" itemLabel="DB2" />
						<f:selectItem itemValue="Oracle" itemLabel="Oracle" />
					</h:selectOneListbox>
					<h:outputText value="" />
					<h:commandButton type="submit" value="Login"
						action="#{dBAccessBean.connectDB}" />
				</h:panelGrid>
			</h:form>
		</center>
	</f:view>
</body>
</html>