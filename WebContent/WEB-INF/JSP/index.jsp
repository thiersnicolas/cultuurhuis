<%@page contentType='text/html' pageEncoding='UTF-8' session='false'%>
<%@taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>
<%@taglib prefix='fmt' uri='http://java.sun.com/jsp/jstl/fmt'%>
<!doctype html>
<html lang='nl'>
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
	<c:if test="${not empty session}">
		<a href='/cultuurhuis/reservatiemandje'>reservatiemandje</a>
		<a href='/cultuurhuis/bevestigingreservatie'>Bevestiging
			reservatie</a>
	</c:if>
	<h2>Genres</h2>
	<c:forEach var='genre' items='${genres}'>
		<c:url value='/index.htm' var='genresURL'>
			<c:param name='id' value='${genre.id}' />
		</c:url>
		<a href='${genresURL}'>${genre.naam}</a>
	</c:forEach>
	<c:if test="${not empty setvoorstellingengenre}">
		<h2>${genrenaam}&nbspvoorstellingen</h2>
		<table class='voorstellingen'>
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
			<tbody class='zebra'>
				<c:forEach var="voorstelling" items="${setvoorstellingengenre}">
					<fmt:parseDate value='${voorstelling.datum}' type='both'
						pattern="yyyy-MM-dd'T'HH:mm" var="datumAlsDate" />
					<c:url value='/reserveren' var='reserverenURL'>
						<c:param name='id' value='${voorstelling.id}' />
					</c:url>
					<tr>
						<td><fmt:formatDate value='${datumAlsDate}' type='both'
								pattern='d-MM-yy HH:mm' /></td>
						<td>${voorstelling.titel}</td>
						<td>${voorstelling.uitvoerders}</td>
						<td><fmt:formatNumber value='${voorstelling.prijs}'
								type='currency' currencySymbol="€" /></td>
						<td class='vrijeplaatsen'>${voorstelling.aantalVrijePlaatsen}</td>
						<td><a href="${reserverenURL}">Reserveren</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	<c:if test="${empty setvoorstellingengenre and not empty param.id}">
		<h2>Er zijn geen voorstellingen van het genre ${genrenaam}
			gepland op dit ogenblik</h2>
	</c:if>
</body>
</html>