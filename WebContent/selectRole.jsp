<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>IDS517 f16g321 Select Role</title>
</head>
<body>
	<f:view>

		<h3 align="center">IDS517 f16g321 Select Role</h3>
		<hr>
		<br />
		<center>
			<h:form>
				<h:commandButton type="submit" value="Logout"
					action="#{dBAccessActionBean.logout}" />
			</h:form>
		</center>
		<br />
		<hr>
		<h3>Select the below roles:</h3>
		<h:form>
			<h:commandButton type="submit" value="Student" action="student">
			</h:commandButton>
		</h:form>
		<br />
		<br />
		<h:form>

			<h:commandButton type="submit" value="Instructor" action="instructor" />

		</h:form>
		<br />
		<br />
		<h:form>
			<h:commandButton type="submit" value="DBA" action="dBAccess" />
		</h:form>
	</f:view>
</body>
</html>