<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="uebung6.aufgabe3.data.Data"%>
<%@ page import="uebung6.aufgabe3.beans.BeanBag"%>
<%!int numberOfQuestions = Data.questions.length;%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JSP Quiz</title>
</head>

<jsp:useBean id="quizstate" class="uebung6.aufgabe3.beans.BeanBag"
	scope="session" />

<%
	int currentQuestion = quizstate.getCurrentQuestion();
	if (currentQuestion >= numberOfQuestions) {
		response.sendRedirect("Result.jsp");
		return;
	}
%>

<body>
<form action="Answer.jsp" method="post">
<table width="100%" cellspacing="0" cellpadding="3" border="0">
	<tr>
		<td class="subHeading">
		<p class="mainWords"><%=Data.questions[currentQuestion].getQuestion()%>
		</p>
		</td>
	</tr>
	<%
		int counter = 0;
		for (String option : Data.questions[currentQuestion].getAnswers()) {
	%>
	<tr>
		<td class="normalText"><input type="radio" name="answer"
			value="<%=counter++%>"><%=option%></td>
	</tr>
	<%
		}
	%>
</table>
<br>

<center><input type="submit"
	value="<%=(currentQuestion < numberOfQuestions) ? "Next Question"
					: "Check your result"%>">
</center>
</form>
<hr>
</body>
</html>