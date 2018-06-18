<%@page contentType='text/html' pageEncoding='UTF-8' session='false'%>
<%@taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>
<%@taglib prefix='fmt' uri='http://java.sun.com/jsp/jstl/fmt'%>
<!doctype html>
<html lang='nl'>
<head>
<c:import url="/WEB-INF/JSP/head.jsp">
	<c:param name="title" value="Overzicht"></c:param>
</c:import>
</head>
<body>
	<H1>
		Het Cultuurhuis:overizcht <img
			src='<c:url value="/images/reserveren.png"/>'
			alt='ovezicht' />
	</H1>
		<a href='/cultuurhuis/index.htm'>Voorstellingen</a>
	<h2>Gelukte reserveringen</h2>
	<c:if test="${not empty geluktereserveringen}">
	<table class='voorstellingen'>
				<thead>
					<tr>
						<th scope="col">Datum</th>
						<th scope="col">Titel</th>
						<th scope="col">Uitvoerders</th>
						<th scope="col">Prijs</th>
						<th scope="col">Plaatsen</th>
					</tr>
				</thead>
				<tbody class='zebra'>
					<c:forEach var="entry" items="${geluktereserveringen}">
						<fmt:parseDate value='${entry.key.datum}' type='both'
							pattern="yyyy-MM-dd'T'HH:mm" var="datumAlsDate" />
						<tr>
							<td><fmt:formatDate value='${datumAlsDate}' type='both'
									pattern='d-MM-yy HH:mm' /></td>
							<td>${entry.key.titel}</td>
							<td>${entry.key.uitvoerders}</td>
							<td><fmt:formatNumber value='${entry.key.prijs}'
									type='currency' currencySymbol="€" /></td>
							<td class='vrijeplaatsen'>${entry.value}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</c:if>
	<h2>Mislukte reserveringen</h2>
		<c:if test="${not empty misluktereserveringen}">
	<table class='voorstellingen'>
				<thead>
					<tr>
						<th scope="col">Datum</th>
						<th scope="col">Titel</th>
						<th scope="col">Uitvoerders</th>
						<th scope="col">Prijs(&euro;)</th>
						<th scope="col">Plaatsen</th>
						<th scope="col">Vrije plaatsen</th>
					</tr>
				</thead>
				<tbody class='zebra'>
					<c:forEach var="entry" items="${misluktereserveringen}">
						<fmt:parseDate value='${entry.key.datum}' type='both'
							pattern="yyyy-MM-dd'T'HH:mm" var="datumAlsDate" />
						<tr>
							<td><fmt:formatDate value='${datumAlsDate}' type='both'
									pattern='d-MM-yy HH:mm' /></td>
							<td>${entry.key.titel}</td>
							<td>${entry.key.uitvoerders}</td>
							<td><fmt:formatNumber value='${entry.key.prijs}'
									type='currency' currencySymbol="€" /></td>
							<td class='vrijeplaatsen'>${entry.value}</td>
							<td class='vrijeplaatsen'>${entry.key.aantalVrijePlaatsen}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</c:if>