<%@page contentType='text/html' pageEncoding='UTF-8' session='false'%>
<%@taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>
<%@taglib prefix='fmt' uri='http://java.sun.com/jsp/jstl/fmt'%>
<!doctype html>
<html>
<head>
<c:import url="/WEB-INF/JSP/head.jsp">
	<c:param name="title" value="Reserveren"></c:param>
</c:import>
</head>
<body>
	<H1>
		Het Cultuurhuis:reserveren <img
			src='<c:url value="/images/reserveren.png"/>' alt='reserveren' />
	</H1>
	<a href='/cultuurhuis/index.htm'>Voorstellingen</a>
	<form method="post">
		<dl>

			<dt>Voorstelling:</dt>
			<c:if test="${not empty foutid}">${fout}, gelieve opnieuw een selectie te maken via de link "Voorstellingen" hierboven.</c:if>
			<c:if test="${empty foutid}">
				<dd class="attribuut">${voorstelling.titel}</dd>

				<dt>Uitvoerders:</dt>
				<dd class='attribuut'>${voorstelling.uitvoerders}</dd>

				<dt>Datum</dt>
				<fmt:parseDate value='${voorstelling.datum}' type='both'
					pattern="yyyy-MM-dd'T'HH:mm" var="datumAlsDate" />
				<dd class='attribuut'>
					<fmt:formatDate value='${datumAlsDate}' type='both'
						pattern='d-MM-yy HH:mm' />
				</dd>

				<dt>Prijs:</dt>
				<dd class='attribuut'>
					<fmt:formatNumber value='${voorstelling.prijs}' type='currency'
						currencySymbol="€" />
				</dd>

				<dt>Vrije Plaatsen:</dt>
				<dd class='attribuut'>${voorstelling.aantalVrijePlaatsen}</dd>


				<dt>Plaatsen:</dt>
				<dd class='attribuut'>
					<input name="plaatsen" type="number" required min='1' step="1" placeholder="${plaatsenEerdereReservatie}">
				</dd>

				<dt>
					<input type="submit" value="Reserveren">
					<c:if test="${not empty foutplaatsen}">Tik een getalen tussen 1 en ${voorstelling.aantalVrijePlaatsen}</c:if>
				</dt>
			</c:if>
		</dl>
	</form>
</body>

</html>