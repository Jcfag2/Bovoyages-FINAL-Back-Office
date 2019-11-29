<%@page import="com.sun.jdi.Value"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Destinations</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
<h2>Recherche de toutes les destinations existantes</h2>
<form action="GetAllDestinationsServlet" METHOD="POST" enctype="multipart/form-data">
<button type="submit" >Chercher</button>
</form>
<div class="container">
<h2><span class="badge badge-primary">Service d'Authentification</span></h2>
</div>
<div class="container">
<form action="AuthentificationServlet" METHOD="POST" enctype="multipart/form-data">
  <div class="form-group">
    <label for="exampleInputEmail1">identifiant :</label>
    <input id="identifiant" name="identifiant" type="text" class="form-control" placeholder="identifiant" aria-label="Username" aria-describedby="basic-addon1">
  </div>
  <div class="form-group">
    <label for="exampleInputPassword1">mot de passe :</label>
    <input id="password" name="password" type="password" class="form-control" id="exampleInputPassword1" placeholder="mot de passe">
  </div>
  <button type="submit" class="btn btn-primary">Submit</button>
</form>
</div>
</body>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</html>