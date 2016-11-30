<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Student Login</title>
</head>
<body>
<f:view>
<h3 align="center">f16g321 Upload File</h3>
		<hr />
		<br />

		<div align="center">
			<h:form>
				<h:commandButton type="submit" value="Change Role"
					action="selectRole" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Logout"
					action="#{dBAccessActionBean.logout}" />&nbsp;&nbsp;&nbsp;
		</h:form>
			<br /> <br />
		</div>
		
		<hr />
		<br />
		<center>
		<h:form>
		<h:outputText value="#{messageBean.errorMessage}" style="color:red"
					rendered="#{studentLoginBean.errorMessage}" />
		<h:panelGrid columns="2">
		
		<h:outputText value="UserName:" />
					<h:inputText id="userName" value="#{studentLoginBean.userName}" />
					<h:outputText value="" /><br/>
		<h:commandButton type="submit" value="Login"
						action="#{studentLoginBean.login}" />
		</h:panelGrid>
		</h:form>
		</center>
</f:view>
</body>
</html>