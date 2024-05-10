<%-- <%@ page import= "java.util.ArrayList"%>
<%@ page import= "com.entite.Employe"%> --%>

<%@ page import= "java.util.Vector"%>
<%@ page import= "com.entite.Etudiant"%>

<%
Vector<Etudiant> etudiant= (Vector<Etudiant>)request.getAttribute("etudiant");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Exercice</title>
</head>
<body>
    <h1>Liste des etudiants</h1>

    <table border="1px solid" style="border-collapse: collapse;" >
        <thead>
            <tr>
                <th>Nom</th>
                <th>Prenom</th>
                <th>age</th>
            </tr>
        </thead>

        <tbody>
            <% for (Etudiant olona : etudiant) {%>
                <tr>
                    <td><%out.println(olona.getNom());%></td>
                    <td><%out.println(olona.getPrenom());%></td>
                    <td><%out.println(olona.getAge());%></td>
                </tr>
            <% } %>

            <form method="post" action="/emp/save">
                <p>Entrez un Nom : <input type="text" name="nom"></p>
                <p>Entrez un Prenom : <input type="text" name="prenom"></p>
                <p>Entrez un Age : <input type="text" name="age"></p>
                <input type="submit" value="Ok">
            </form>
        </tbody>
        <tfoot>

        </tfoot>
    </table>
</body>
</html>