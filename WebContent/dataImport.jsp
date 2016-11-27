<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload File</title>
</head>
<body>
	<f:view>
		<h3 align="center">f16g321 Upload File</h3>
		<hr />
		<br />

		<div align="center">
			<h:form>
				<h:commandButton type="submit" value="Home" action="index" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Change Role"
					action="selectRole" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Logout"
					action="#{dBAccessActionBean.logout}" />&nbsp;&nbsp;&nbsp;
		</h:form>
			<br /> <br />
		</div>
		<hr />
		<br />
		<h:form enctype="multipart/form-data">
			<h:panelGrid columns="2"
				style="background-color: Beige;
border-bottom-style: solid;
border-top-style: solid;
border-left-style: solid;
border-right-style: solid">

				<h:outputLabel value="CRN:" />
				<h:inputText id="crn" value="#{uploadFileActionBean.crn}" size="30" />
				<h:outputLabel value="Course Code:" />
				<h:inputText id="code" value="#{uploadFileActionBean.code}"
					size="30" />
				<h:outputLabel value="Course Description:" />
				<h:inputText id="description"
					value="#{uploadFileActionBean.description}" size="30" />
				
				<h:outputLabel value="Select File to Upload:" />
				<t:inputFileUpload id="fileUpload"
					value="#{uploadFileActionBean.uploadedFile}" required="false"
					size="60" />
				<h:outputLabel value="File Label:" />
				<h:inputText id="fileLabel"
					value="#{uploadFileActionBean.fileLabel}" size="60" />
				<h:outputLabel value=" " />
				<h:selectOneListbox value="#{uploadFileActionBean.fileType}"
					size="2">
					<f:selectItem itemValue="tsv" itemLabel="Tab Separated Values" />
					<f:selectItem itemValue="csv" itemLabel="CSV" />
				</h:selectOneListbox>
				<h:outputLabel value=" " />
				<h:selectOneListbox value="#{uploadFileActionBean.uploadType}"
					size="2">
					<f:selectItem itemValue="courseRoster" itemLabel="Course Roster" />
					<f:selectItem itemValue="testRoster" itemLabel="Test Roster" />
				</h:selectOneListbox>
				<h:outputLabel value="Test ID:" />
				<h:inputText id="testId" value="#{uploadFileActionBean.testId}"
					size="30" />
				<h:outputLabel value="Start Date:" />
				<h:inputText id="startDate"
					value="#{uploadFileActionBean.startDate}" size="30" />
				<h:outputLabel value="End Date:" />
				<h:inputText id="endDate" value="#{uploadFileActionBean.endDate}"
					size="30" />
				<h:outputLabel value="Duration:" />
				<h:inputText id="duration" value="#{uploadFileActionBean.duration}"
					size="30" />
					<h:outputLabel value="Points per Question:" /> 
				<h:inputText id="points" value="#{uploadFileActionBean.pointsPerQues}"
					size="30" />
				<h:commandButton id="upload"
					action="#{uploadFileActionBean.processFileUpload}" value="Submit" />
			</h:panelGrid>
			<h:outputLabel rendered="#{uploadFileActionBean.fileImport }"
				value="Number of records imported: " />
			<h:outputText rendered="#{uploadFileActionBean.fileImport }"
				value="#{uploadFileActionBean.numberRows }" />
			<h:outputText rendered="#{uploadFileActionBean.fileImportError }"
				value="#{messageBean.errorMessage }" />
			<br />
			<h:outputLabel rendered="#{uploadFileActionBean.uploadTest }"
				value="File uploaded: " />
			<hr />
			<div
				style="background-attachment: scroll; overflow: auto; height: 400px; background-repeat: repeat">
				<t:dataTable value="#{uploadFileActionBean.testRoster}" var="row"
					rendered="#{uploadFileActionBean.uploadTest}" border="1"
					cellspacing="0" cellpadding="1" columnClasses="columnClass1 border"
					headerClass="headerClass" footerClass="footerClass"
					rowClasses="rowClass2" styleClass="dataTableEx" width="900">

					<h:column>
						<h:outputText value="#{row.question_type}" />
					</h:column>

					<h:column>
						<h:outputText value="#{row.question_text}" />
					</h:column>
					<h:column>
						<h:outputText value="#{row.correct_ans}" />
					</h:column>
					<h:column>
						<h:outputText value="#{row.tolerance}" />
					</h:column>

				</t:dataTable>
				<t:dataTable value="#{uploadFileActionBean.courseRoster}" var="row"
					rendered="#{uploadFileActionBean.uploadCourse}" border="1"
					cellspacing="0" cellpadding="1" columnClasses="columnClass1 border"
					headerClass="headerClass" footerClass="footerClass"
					rowClasses="rowClass2" styleClass="dataTableEx" width="900">

					<h:column>
						<h:outputText value="#{row.lastName}" />
					</h:column>

					<h:column>
						<h:outputText value="#{row.firstName}" />
					</h:column>
					<h:column>
						<h:outputText value="#{row.userName}" />
					</h:column>
					<h:column>
						<h:outputText value="#{row.uin}" />
					</h:column>
					<h:column>
						<h:outputText value="#{row.lastAccess}" />
					</h:column>

					<h:column>
						<h:outputText value="#{row.availability}" />
					</h:column>
					<h:column>
						<h:outputText value="#{row.total}" />
					</h:column>
					<h:column>
						<h:outputText value="#{row.exam01}" />
					</h:column>

					<h:column>
						<h:outputText value="#{row.exam02}" />
					</h:column>
					<h:column>
						<h:outputText value="#{row.exam03}" />
					</h:column>
					<h:column>
						<h:outputText value="#{row.project}" />
					</h:column>
				</t:dataTable>
			</div>
		

		</h:form>

	</f:view>
</body>
</html>