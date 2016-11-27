<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create or Drop Tables</title>
</head>
<body>
	<f:view>
		<h3 align="center">f16g321 Create or Drop Tables</h3>
		<hr />
		<br />

		<div align="center">

			<h:form>
				<h:commandButton type="submit" value="Database Admin Page"
					action="#{dBAccessActionBean.back}" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Change Role"
					action="#{dBAccessActionBean.next}" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Logout"
					action="#{dBAccessActionBean.logout}" />
			</h:form>
		</div>
		<hr />
		<br />
		<div align="center">

			<h:form>
				<h:panelGrid columns="4">
					<h:commandButton type="submit" value="Create All Tables"
						action="#{dBAccessActionBean.createAllTables}" />
					<h:commandButton type="submit" value="Create Selected Tables"
						action="#{dBAccessActionBean.createTables}" />
					<h:commandButton type="submit" value="Drop All Tables"
						action="#{dBAccessActionBean.dropAllTables}" />
					<h:commandButton type="submit" value="Drop Selected Tables"
						action="#{dBAccessActionBean.dropTables}" />
				</h:panelGrid>
				<br />
				<h:selectManyListbox size="10" styleClass="selectManyListbox"
					value="#{dBAccessActionBean.createDropTableNames}">
					<f:selectItems value="#{dBAccessActionBean.allTableList}" />
				</h:selectManyListbox>
				<br />
				<h:outputText value="#{messageBean.errorMessage}" style="color:red"
					rendered="#{messageBean.renderErrorMessage}" />
				<h:outputText value="#{messageBean.successMessage}"
					style="color:green" rendered="#{messageBean.renderSuccessMessage}" />
			</h:form>
		</div>
	</f:view>
</body>
</html>