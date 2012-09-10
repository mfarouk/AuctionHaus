<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
    %{--
    UI-1: include jquery
    --}%
    <g:javascript library="jquery" plugin="jquery"/>
    <g:layoutHead/>
    <r:layoutResources />
</head>
<body>
%{--2012.03.24 Change logo--}%
<div id="grailsLogo" role="banner"><a href="http://localhost:8080/MyEbay1"><img src="${resource(dir: 'images', file: 'AuctionHouse_logo.png')}" alt="Grails"/></a></div>
<div id="loginBox" class="loginBox">
    <g:if test="${session?.customer}">
        <div style="margin-top:20px">
            <div style="float:right;">
                <a href="#">Profile</a> |
                <g:link controller="customer" action="logout">Logout</g:link><br>
            </div>
            Welcome Back:        ${session.customer}
            <br><br>
        </div>
    %{--<div id="customerId1">${session.customer.id}</div>--}%
        <input id="customerId" type='hidden' name='customerId' value='${session.customer.id}' />
    </g:if>
    <g:else>
        <g:form
                name="loginForm"
                url="[controller:'customer',action:'authenticate']">
            <div>Email:</div>
            <input id="email" type='text' name='email' value='${customer?.email}' />
            <div>Password:</div>
            <input id="password" type='password' name='password'
                   value='${customer?.password}' />
            <input type = "submit" value= "Login"/>
        </g:form>
    </g:else>
</div>
<div id="navPane">
    <g:if test="${session?.customer}">
        <ul>
            <li><g:link controller="customer" action="list">Customers</g:link></li>
            <li><g:link controller="listing" action="mylist">My Listings</g:link></li>
        </ul>
    </g:if>
    <g:else>
        <div id="registerPane">
            If you don't have an account
            <g:link controller ="customer" action="register">Register here</g:link>
            to start your own listings
        </div>
    </g:else>
</div>

<g:layoutBody/>
<div class="footer" role="contentinfo"></div>
<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
<g:javascript library="application"/>
<r:layoutResources />
</body>
</html>