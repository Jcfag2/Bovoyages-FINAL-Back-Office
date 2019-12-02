<%@page import="com.sun.jdi.Value"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Destinations</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<style type="text/css">
#bloc{
    margin-right: 50px;

}
#presentation{
	height: 200px;
	width: 200px;
    margin-top: 10px;
    margin-bottom: 10px;
    margin-right: 10px;
    display: block;

}
</style>
</head>
<body>
<h2>Navigateur</h2>
<table>
<tr>
<td>
<form action="GetAllDestinationsServlet" METHOD="POST" enctype="multipart/form-data">
<button class="btn btn-secondary" type="submit" >Toutes les destinations</button>
</form>
</td>
<td>
<div class="dropdown">
  <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
    choix d'une destination
  </button>
  <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
  <c:forEach items="${destinations}" var="destinations">
    <div>
    <form action="GetOptionsDestinationsServlet" METHOD="POST"> 
    <input type="hidden" name="id" id="id" value="${ destinations.id }">
    <input type="hidden" name="region" id="region" value="${ destinations.region }">
    <input type="hidden" name="description" id="description" value="${ destinations.description }">
    <button type="submit" class="btn btn-light">${destinations.region}</button>
    </form>
    </div>
  </c:forEach>
  </div>
</div>
</td>
<td>
      <form action="AjoutDestinationsServlet" METHOD="POST" enctype="multipart/form-data">
        <input name="region" id="region" placeholder="region">
        <input name="description" id="description" placeholder="description">
        <input type="file" name="simple-file">
        <button class="btn btn-secondary" type="submit">ajout</button>
      </form>
</td>
<td>
<a class="btn btn-danger" href="index.jsp">Log out</a>
</td>
</tr>
</table>
<h2>Destinations par noms</h2>
<table>
<tr>
<td>
   <div id="bloc">
   <c:forEach items="${destinations}" var="destinations">
       <div id="presentation" > 
      <form action="GetOptionsDestinationsServlet" METHOD="POST">
        <input type="hidden" name="id" id="id" value="${ destinations.id }">
        <input type="hidden" name="region" id="region" value="${ destinations.region }">
        <input type="hidden" name="description" id="description" value="${ destinations.description }">
        <button type="submit" class="btn btn-light">${destinations.region}</button>
      </form>
      </div> 
   </c:forEach>
   </div>
</td>
<td>
   <div id="bloc">
   <c:forEach items="${imagesDestination}" var="imagesDestination">
      <div id="presentation" > 
      <img src="images/${imagesDestination.image}" width="300" /><br>
      </div> 
   </c:forEach>
   </div>
</td>
</tr>
</table>
<a href="index.jsp">Retour</a>
</body>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</html>