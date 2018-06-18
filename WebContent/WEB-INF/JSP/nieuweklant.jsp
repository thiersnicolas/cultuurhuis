<%@page contentType='text/html' pageEncoding='UTF-8' session='false'%>
<%@taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>
<!doctype html>
<html>
<head>
<c:import url="/WEB-INF/JSP/head.jsp">
	<c:param name="title" value="Nieuwe klant"></c:param>
</c:import>
</head>
<body>
	<H1>
		Het Cultuurhuis:reservatiemandje <img
			src='<c:url value="/images/nieuweklant.png"/>' alt='nieuwe klant' />
	</H1>
	<a href='/cultuurhuis/index.htm'>Voorstellingen</a>
	<a href='/cultuurhuis/reservatiemandje'>Reservatiemandje</a>
	<a href='/cultuurhuis/bevestigingreservatie'>Bevestiging reservatie</a>
	<form method="post">
		<dl>
			<dt>Voornaam:</dt>
			<dd class="klantattribuut">
				<input type="text" name="voornaam" required>
			</dd>

			<dt>Familienaam:</dt>
			<dd class="klantattribuut">
				<input type="text" name="familienaam" required>
			</dd>
			
			<dt>Straat:</dt>
			<dd class="klantattribuut">
				<input type="text" name="straat" required>
			</dd>

			<dt>Huisnr:</dt>
			<dd class="klantattribuut">
				<input type="text" name="huisnr" required>
			</dd>

			<dt>Postcode:</dt>
			<dd class="klantattribuut">
				<input type="text" name="postcode" required>
			</dd>

			<dt>Gemeente:</dt>
			<dd class="klantattribuut">
				<input type="text" name="gemeente" required>
			</dd>


			<dt>Gebruikersnaam:</dt>
			<dd class="klantattribuut">
				<input type="text" name="gebruikersnaam" required>
			</dd>
			
			<dt>Paswoord:</dt>
			<dd class="klantattribuut">
				<input type="text" name="paswoord" required>
			</dd>
			
			<dt>Herhaal paswoord:</dt>
			<dd class="klantattribuut">
				<input type="text" name="herhaalpaswoord" required>
			</dd>

			<dt>
				<input type="submit" name="nieuweklant">
			</dt>
		</dl>
	</form>
	<ul>
	<c:forEach var="fout" items="${fouten}">
		<li>${fout}</li>
	</c:forEach>
	</ul>
</body>
</html>