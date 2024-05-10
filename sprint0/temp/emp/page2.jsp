<%
    String nom = (String)request.getAttribute("nom");
    String prenom = (String)request.getAttribute("prenom");
    String age = (String)request.getAttribute("age");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h1> Bienvenue dans la page 2 </h1>
    <p>Votre nom est : <% print(nom); %></p>
    <p>Votre prenom est : <% print(prenom); %></p>
    <p>Vous avez : <% print(age); %> ans</p>
</body>
</html>