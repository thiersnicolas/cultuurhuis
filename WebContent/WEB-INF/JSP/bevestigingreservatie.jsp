<%@page contentType='text/html' pageEncoding='UTF-8' session='false'%>
<%@taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>
<%@taglib prefix='fmt' uri='http://java.sun.com/jsp/jstl/fmt'%>
<!doctype html>
<html>
<head>
<c:import url="/WEB-INF/JSP/head.jsp">
	<c:param name="title" value="Bevestiging reservatie"></c:param>
</c:import>
</head>
<body>
	<h1>
		Het Cultuurhuis:bevestiging reservaties<img
			src='<c:url value="/images/bevestig.png"/>' alt='bevestig' />
	</h1>
	<a href='/cultuurhuis/index.htm'>Voorstellingen</a>
	<c:if test="${empty fout}">
		<a href='/cultuurhuis/reservatiemandje'>Reservatiemandje</a>
	</c:if>
	<c:if test="${not empty fout}">${fout}, gelieve opnieuw een selectie te maken via de link "Voorstellingen" hierboven.</c:if>
	<h2>Stap 1:wie ben je ?</h2>
	<form method="post">
		<dl>
			<dt>Gebruikersnaam:</dt>
			<dd>
				<input type="text">
			</dd>
			<dt>Paswoord:</dt>
			<dd>
				<input type="text">
			</dd>
		</dl>
		<input type="submit" value="Zoek me op" name="opzoeken">
		<input type="submit" value="Ik ben nieuw" name="nieuw">
		<c:if test=""><h3></h3></c:if>

	</form>

</body>
</html>