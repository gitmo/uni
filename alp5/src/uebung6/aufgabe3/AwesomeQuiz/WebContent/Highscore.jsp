<%@page import="uebung6.aufgabe3.beans.HighscoreBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hall of Fame</title>
</head>
<body>
<jsp:useBean id="highscoreBean"
	class="uebung6.aufgabe3.beans.HighscoreBean" scope="session" />
<jsp:setProperty name="highscoreBean" property="*" />

<jsp:useBean id="halloffame" class="uebung6.aufgabe3.beans.HallOfFame"
	scope="application" />

<%
	halloffame.enter(highscoreBean);
%>

<h3>Hall of Fame</h3>
<table width="100%" cellspacing="0" cellpadding="3" border="0">
	<%
		for (HighscoreBean hs : halloffame.getHallOfFame()) {
	%>
	<tr>
		<td><%=hs.getUser()%></td>
		<td><%=hs.getScore() * 100 / 10%> %</td>
		<td><%=hs.getTime()%> s</td>
	</tr>
	<%
		}
	%>
</table>

</body>
</html>