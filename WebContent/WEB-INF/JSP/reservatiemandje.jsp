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
	<c:if test="${not empty fout}">
		${fout}, gelieve opnieuw een selectie te maken via de link "Voorstellingen" hierboven.
	</c:if>
	<c:if test="${not empty reservatiemandje}">
		<a href='/cultuurhuis/index.htm'>Voorstellingen</a>
		<a href='/cultuurhuis/bevestigingreservatie'>Bevestiging
			reservatie</a>
		<form method="post">
			<table class='voorstellingen'>
				<thead>
					<tr>
						<th scope="col">Datum</th>
						<th scope="col">Titel</th>
						<th scope="col">Uitvoerders</th>
						<th scope="col">Prijs</th>
						<th scope="col">Plaatsen</th>
						<th scope="col"><input type='submit' value="Verwijderen"></th>
					</tr>
				</thead>
				<tbody class='zebra'>
					<c:forEach var="entry" items="${reservatiemandje}">
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
							<td class='checkbox'><input type="checkbox"
								name='idsverwijderen' value='${entry.key.id}'></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<p>Te betalen: &euro;${totaleprijs}</p>
		</form>
	</c:if>