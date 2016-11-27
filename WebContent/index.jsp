<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>IDS517 f16g321 Home</title>
</head>
<body>
	<f:view>


		<h3 align="center">IDS517 f16g321 Home</h3>
		<hr />
		<br />

		<div align="center">
			<h:form>
				<h:commandButton type="submit" value="DataBase Login"
					action="databaseLogin" />
			</h:form>
			<br /> <a href="Documents/Prog_Document_Guide.pdf" target="_blank">Programmer's
				Guide</a> &nbsp;&nbsp;&nbsp;&nbsp; <a href="Documents/User_Manual.pdf"
				target="_blank">User Guide</a> <br /> <br />
			<hr />
		</div>
		<p>Developer's Details</p>

		<p>1. Ankita Jhawar</p>
		<p>2. Gunasagar K J</p>
		<p>3. Subramanya Hebbar</p>

	</f:view>
</body>
</html>