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
		Het Cultuurhuis:Reserveren <img
			src='<c:url value="/images/reserveren.png"/>' alt='reserveren' />
	</H1>
	<a href='/index'>Voorstellingen</a>
	
	<div>
	<h3>Voorstelling:</h3>
	<h3 class='attribuut'>${voorstelling.titel}</h3>
	</div>
	<div>
	<h3>Uitvoerders:</h3>
	<h3 class='attribuut'>${voorstelling.uitvoerders}</h3>
	</div>
	<div>
	<h3>Datum</h3>
	<fmt:parseDate value='${voorstelling.datum}' type='both' pattern="yyyy-MM-dd'T'HH:mm" var="datumAlsDate"/>
	<h3 class='attribuut'><fmt:formatDate value='${datumAlsDate}' type='both' pattern='d-MM-yy HH:mm'/></h3>
	</div>
	<div>
	<h3 class='attribuut'>Prijs:</h3>
	<h3><fmt:formatNumber value='${voorstelling.prijs}' type='currency' currencySymbol="€"/></h3>
	</div>
	<div>
	<h3>Vrije plaatsen:</h3>
	<h3 class='attribuut'>${voorstelling.aantalVrijePlaatsen}</h3>
	</div>
	<form method="post">
	<div>
	<h3>Plaatsen</h3>
	<input name="plaatsen" type="number" min='1' required step="1">
	</div>
	<input type="submit" value="Reserveren">
	</form>
	
</body>

</html>