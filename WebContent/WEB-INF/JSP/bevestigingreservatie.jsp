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
	<c:if test="${not empty fout}">
		<p>${fout},gelieve opnieuw een selectie te maken via de link
			"Voorstellingen" hierboven.</p>
	</c:if>
	<h2>Stap 1:wie ben je ?</h2>
	<form method="post">
		<dl>
			<dt>Gebruikersnaam:</dt>
			<dd>
				<input id="gebruikersnaam" type="text" name="gebruikersnaam"
					<c:if test="${not empty gebruiker}">placeholder="${gebruiker.gebruikersnaam}" disabled</c:if>>
			</dd>
			<dt>Paswoord:</dt>
			<dd>
				<input id="paswoord" type="text" name="paswoord"
					<c:if test="${not empty gebruiker}">disabled</c:if>>
			</dd>
		</dl>
		<div>
			<input class="reservatie" type="submit" value="Zoek me op"
				name="opzoeken" <c:if test="${not empty gebruiker}">disabled</c:if>>
		</div>
		<div>
			<input class="reservatie" type="submit" value="Ik ben nieuw"
				name="nieuw" <c:if test="${not empty gebruiker}">disabled</c:if>>
		</div>
		<c:if test="${not empty gebruiker}">
			<p id="gebruiker">${gebruiker.voornaam}&nbsp${gebruiker.familienaam}&nbsp${gebruiker.straat}&nbsp${gebruiker.huisnr}&nbsp${gebruiker.postcode}&nbsp${gebruiker.gemeente}</p>
		</c:if>
		<c:if test="${not empty fouten}">
			<p id="gebruiker">${fouten}</p>
		</c:if>
		<h2>Stap 2:Bevestigen</h2>

		<div>
			<input class="reservatie" type="submit" value="Bevestigen"
				name="bevestigen" <c:if test="${empty gebruiker}">disabled</c:if>>
		</div>

	</form>

</body>
</html>