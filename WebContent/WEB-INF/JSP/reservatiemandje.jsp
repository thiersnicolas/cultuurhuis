<%@page contentType='text/html' pageEncoding='UTF-8' session='false'%>
<%@taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>
<%@taglib prefix='fmt' uri='http://java.sun.com/jsp/jstl/fmt'%>
<!doctype html>
<html>
<head>
<c:import url="/WEB-INF/JSP/head.jsp">
	<c:param name="title" value="Reservatiemandje"></c:param>
</c:import>
</head>
<body>
	<H1>
		Het Cultuurhuis:reservatiemandje <img
			src='<c:url value="/images/mandje.png"/>' alt='mandje' />
	</H1>
	<a href='/cultuurhuis/index.htm'>Voorstellingen</a>
	<a href='/cultuurhuis/bevestigingreservatie'>Bevestiging reservatie</a>
	<form method="post">