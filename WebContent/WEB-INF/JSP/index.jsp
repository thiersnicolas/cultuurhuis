<%@page contentType='text/html' pageEncoding='UTF-8' session='false'%>
<%@taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>
<!doctype html>
<html>
<head>
<c:import url="/WEB-INF/JSP/head.jsp">
	<c:param name="title" value="Cultuurhuis"></c:param>
</c:import>
</head>
<body>
	<H1>
		Het Cultuurhuis:voorstellingen <img
			src='<c:url value="/images/voorstellingen.png"/>'
			alt='voorstellingen' />
	</H1>
	<h2>Genres</h2>
	<c:forEach var='genre' items='${genres}'>
		<c:url value='/index.htm' var='genresURL'>
			<c:param name='id' value='${genre.id}' />
		</c:url>
		<a href='${genresURL}'>${genre.naam}</a>
	</c:forEach>
	<c:if test="${not empty lijstvoorstellingengenre}">
		<h2>${genrenaam}&nbspvoorstellingen</h2>
		<table border="">
			<thead>
				<tr>
					<th scope="col">Datum</th>
					<th scope="col">Titel</th>
					<th scope="col">Uitvoerders</th>
					<th scope="col">Prijs</th>
					<th scope="col">Vrije plaatsen</th>
					<th scope="col">Reserveren</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="voorstelling" items="${lijstvoorstellingengenre}">
					<tr>
						<td>${voorstelling.datum}</td>
						<td>${voorstelling.titel}</td>
						<td>${voorstelling.uitvoerders}</td>
						<td>${voorstelling.prijs}</td>
						<td>${voorstelling.aantalVrijePlaatsen}</td>
						<td><a href="/reserveren">Reserveren</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
</body>
</html>