<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="uebung6.aufgabe3.model.Question"%>
<%@ page import="uebung6.aufgabe3.data.Data"%>
<%@ page import="uebung6.aufgabe3.beans.BeanBag"%>
<%!Data data = new Data();%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Results</title>
</head>

<%
	session = request.getSession();
	BeanBag beanBag = (BeanBag) session.getAttribute("quizstate");
	if (beanBag == null) {
		out.println("Take the Quiz first!");
		response.sendRedirect("Question.jsp");
		return;
	}
	float total = beanBag.getTotalTimeSec();
	int score = beanBag.getScore();
	int percentage = score * 100 / Data.questions.length;
	session.invalidate();
%>


<body>
<h3>Result:</h3>
<p>Total Time: <%=total%>s
<p>Score: <%=score%> (<%=percentage%>%)
<form action="Highscore.jsp" method="post">Username: <input
	type="text" name="user" /> <input type="hidden" name="time"
	value=<%=total%> /> <input type="hidden" name="score" value=<%=score%> />
<input type="submit" value="Enter the Hall of Fame!" /></form>

<hr>
<h3>Correct Answers:</h3>
<table width="100%" cellspacing="5" cellpadding="5" border="0">
	<%
		int currentQuestion = 0;
		for (Question q : Data.questions) {
	%>
	<tr>
		<td class="subHeading">
		<p class="mainWords"><%=q.getQuestion()%></p>
		</td>
	</tr>
	<%
		int currentAnswer = 0;
			for (String option : q.getAnswers()) {
	%>
	<tr>
		<td class="normalText">
		<%
			boolean correct, given;
					correct = q.getSolution() == currentAnswer;
					given = beanBag.answerGivenFor(currentQuestion) == currentAnswer;
					currentAnswer++;
		%> <img
			src="/AwesomeQuiz/images/<%=(correct) ? "tick" : "cross"%>.gif">
		&nbsp;<%=given ? "<b>" + option + "</b>" : option%></td>
	</tr>
	<%
		}
			currentQuestion++;
		}
		currentQuestion = 0;
	%>
</table>

</body>
</html>