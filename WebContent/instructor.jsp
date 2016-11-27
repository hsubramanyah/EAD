<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>f16g321 Instructor Page</title>
</head>
<body>
	<f:view>  
		<h3 align="center">f16g321 Instructor Page</h3>
		<hr />
		<br />

		<div align="center">

			<h:form>
				<h:commandButton type="submit" value="Change Role"
					action="#{dBAccessActionBean.back}" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Upload Roster / Test"
					action="#{dBAccessActionBean.next}" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Logout"
					action="#{dBAccessActionBean.logout}" />
				<h:commandButton type="submit" value="test"
					action="#{test.displayCourseRoster}" />

			</h:form>
			<br /> <br />
		</div>
		<hr />
		<br />
		<div align="left">
			<h:form>
				<h:outputText value="#{messageBean.errorMessage}" style="color:red"
					rendered="#{messageBean.renderErrorMessage}" />
				<h:outputText value="#{messageBean.successMessage}"
					style="color:green" rendered="#{messageBean.renderSuccessMessage}" />
				<h:panelGrid columns="6">
					<h:commandButton type="submit" value="List Courses"
						action="#{instructorActionBean.listCourse}" />
					<h:commandButton type="submit" value="List Tests"
						action="#{instructorActionBean.listTest}" />
					<h:commandButton type="submit" value="Display Course Roster"
						action="#{instructorActionBean.displayCourseRoster}" />
					<h:commandButton type="submit" value="Display Test Questions"
						action="#{instructorActionBean.displayTestQuestions}" />
						<h:commandButton type="submit" value="Display Test chart"
						action="#{instructorActionBean.createPieChart}" />


				</h:panelGrid>
				<h:panelGrid columns="2">

					<h:selectOneListbox size="10" styleClass="selectOneListbox_mono"
						value="#{instructorActionBean.courseSelected}"
						rendered="#{instructorActionBean.courseListRendered}">
						<f:selectItems value="#{instructorActionBean.courseList}" />
					</h:selectOneListbox>

					<h:selectOneListbox size="10" styleClass="selectOneListbox_mono"
						value="#{instructorActionBean.testSelected}"
						rendered="#{instructorActionBean.testListRendered}">
						<f:selectItems value="#{instructorActionBean.testList}" />
					</h:selectOneListbox>
				</h:panelGrid>
				<br />
				<hr />
				<br />

				<h:panelGrid columns="2"
					rendered="#{instructorActionBean.renderCourseRosterList}">
					<h:outputText value="Course Roster" />
					<h:outputText value="" />
					<h:outputText value="No of Students:" />
					<h:outputText id="CourseNoOfRows"
						value="#{instructorActionBean.noOfRows}" />
					<h:commandButton type="submit" value="Export Roster(CSV)"
						action="#{instructorActionBean.exportRosterCsv}" />
					<h:commandButton type="submit" value="Export Roster(XML)"
						action="#{instructorActionBean.exportRosterXml}" />
				</h:panelGrid>
				<h:panelGrid columns="2"
					rendered="#{instructorActionBean.renderTestQuestionList}">
					<h:outputText value="Test Questions" />
					<h:outputText value="" />
					<h:outputText value="No of Questions:" />
					<h:outputText id="testNoOfRows"
						value="#{instructorActionBean.noOfRows}" />
					<h:commandButton type="submit" value="Export Roster(CSV)"
						action="#{instructorActionBean.exportTestCsv}" />
					<h:commandButton type="submit" value="Export Roster(XML)"
						action="#{instructorActionBean.exportTestXml}" />
				</h:panelGrid>
				<hr />
			</h:form>
			<h:form rendered="#{instructorActionBean.renderCourseRosterList}">
				<div
					style="background-attachment: scroll; overflow: auto; height: 300px; background-repeat: repeat">
					<t:dataTable value="#{instructorActionBean.courseRosterList}"
						var="row"
						rendered="#{instructorActionBean.renderCourseRosterList}"
						border="1" cellspacing="0" cellpadding="1"
						columnClasses="columnClass1 border" headerClass="headerClass"
						footerClass="footerClass" rowClasses="rowClass2"
						styleClass="dataTableEx" width="600"
						sortColumn="#{instructorActionBean.sortColumn}"
						sortAscending="#{instructorActionBean.ascending}"
						preserveDataModel="true" preserveSort="true" sortable="true">


						<h:column>
							<f:facet name="header">
								<h:outputText>Last_Name</h:outputText>
							</f:facet>
							<h:outputText value="#{row.lastName}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText>First_Name</h:outputText>
							</f:facet>
							<h:outputText value="#{row.firstName}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Username</h:outputText>
							</f:facet>
							<h:outputText value="#{row.userName}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Student_ID</h:outputText>
							</f:facet>
							<h:outputText value="#{row.uin}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Last_Access</h:outputText>
							</f:facet>
							<h:outputText value="#{row.lastAccess}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Availability</h:outputText>
							</f:facet>
							<h:outputText value="#{row.availability}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Total</h:outputText>
							</f:facet>
							<h:outputText value="#{row.total}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Exam01</h:outputText>
							</f:facet>
							<h:outputText value="#{row.exam01}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Exam02</h:outputText>
							</f:facet>
							<h:outputText value="#{row.exam02}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Exam03</h:outputText>
							</f:facet>
							<h:outputText value="#{row.exam03}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Project</h:outputText>
							</f:facet>
							<h:outputText value="#{row.project}" />
						</h:column>
					</t:dataTable>
				</div>
			</h:form>

			<h:form rendered="#{instructorActionBean.renderTestQuestionList}">
				<div
					style="background-attachment: scroll; overflow: auto; height: 300px; background-repeat: repeat">
					<t:dataTable value="#{instructorActionBean.questionList}" var="row"
						rendered="#{instructorActionBean.renderTestQuestionList}"
						border="1" cellspacing="0" cellpadding="1"
						columnClasses="columnClass1 border" headerClass="headerClass"
						footerClass="footerClass" rowClasses="rowClass2"
						styleClass="dataTableEx" width="600"
						sortColumn="#{instructorActionBean.sortColumn}"
						sortAscending="#{instructorActionBean.ascending}"
						preserveDataModel="true" preserveSort="true" sortable="true">


						<h:column>
							<f:facet name="header">
								<h:outputText>Question Type</h:outputText>
							</f:facet>
							<h:outputText value="#{row.questionType}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText>Question</h:outputText>
							</f:facet>
							<h:outputText value="#{row.questionString}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Correct Answer</h:outputText>
							</f:facet>
							<h:outputText value="#{row.answer}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Tolerance</h:outputText>
							</f:facet>
							<h:outputText value="#{row.answerError}" />
						</h:column>

					</t:dataTable>
				</div>
			</h:form>
		</div>

		<div
			style="background-attachment: scroll; overflow: auto; height: 300px; background-repeat: repeat">
			<h:form>
				<t:dataTable value="#{test.mStudentDataModel}" var="row"
					rendered="true"
					border="1" cellspacing="0" cellpadding="1"
					columnClasses="columnClass1 border" headerClass="headerClass"
					footerClass="footerClass" rowClasses="rowClass2"
					styleClass="dataTableEx" width="600"
					sortColumn="#{test.sortColumn}" 
					sortAscending="#{test.ascending}"
					preserveDataModel="true" preserveSort="true" sortable="true">

					<t:columns value="#{test.mColumns}" var="column" sortable="true">
						<f:facet name="header">
							<h:outputText value="#{column}" />
						</f:facet>
						<h:outputText value="#{test.columnValue}" />
					</t:columns>
				</t:dataTable>
			</h:form>
		</div>
		<h:form>
		<div style="background-attachment: scroll; overflow:auto;
                    		height:300px; background-repeat: repeat" align="center">
				<h:graphicImage value="#{instructorActionBean.chartPath}"
					height="450" width="600" rendered="true" alt="test" />
			
			</div>
			</h:form>
	</f:view>
</body>
</html>