<%--
  Created by IntelliJ IDEA.
  User: farouka
  Date: 3/26/12
  Time: 9:53 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
</head>
<body id="body">
       <h>Registration</h>
<p>Complete the form below to create an account!</p>
<g:hasErrors bean="${customer}">
    <div class="errors">
        <g:renderErrors bean="${customer}"></g:renderErrors>

    </div>
</g:hasErrors>
<g:form action="register" name="registrationForm">
    <div class="formField">
        <label for="email">Email:</label>
        <g:textField name="email" value="${customer?.email}" />
        <label for="password">Password:</label>
        <g:passwordField name="password"
        value="${customer?.password}"/>
    </div>
    <g:submitButton class ="formButton"
        name="register"
        value="Register"/>
</g:form>

</body>
</html>