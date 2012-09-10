%{--History--}%
%{--2012.03.12 Vladimir Beliaev: M-6   number of bids visible for each listing--}%
<%@ page import="myebay1.Listing" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'listing.label', default: 'Listing')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<a href="#list-listing" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
        %{--2011.03.11 Vladimir Beliaev
            add links to customer and bidding--}%
        <li><a class="home" href="${createLink(uri: '/customer')}">Customers</a></li>
        <li><a class="home" href="${createLink(uri: '/bidding')}">Biddings</a></li>
    </ul>
</div>
<div id="list-listing" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="name" title="${message(code: 'listing.name.label', default: 'Name')}" />

            <th><g:message code="listing.seller.label" default="Seller" /></th>

            <g:sortableColumn property="description" title="${message(code: 'listing.description.label', default: 'Description')}" />

            <g:sortableColumn property="startingBidPrice" title="${message(code: 'listing.startingBidPrice.label', default: 'Starting Bid Price')}" />

            <g:sortableColumn property="endTime" title="${message(code: 'listing.endTime.label', default: 'End Time')}" />
            %{--2012.03.11 Vladimir Beliaev
         M-6: number of bids--}%
            <th><g:message code="listing.numberOfBids.label" default="Num Of Bids" /></th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${listingInstanceList}" status="i" var="listingInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show" id="${listingInstance.id}">${fieldValue(bean: listingInstance, field: "name")}</g:link></td>

                <td>${fieldValue(bean: listingInstance, field: "seller")}</td>

                <td>${fieldValue(bean: listingInstance, field: "description")}</td>

                <td>${fieldValue(bean: listingInstance, field: "startingBidPrice")}</td>

                <td><g:formatDate date="${listingInstance.endTime}" /></td>

                %{--2012.03.11 Vladimir Beliaev
                M-6: number of bids--}%
                <td>${fieldValue(bean: listingInstance, field: "numberOfBids")}</td>

            </tr>
        </g:each>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${listingInstanceTotal}" />
    </div>
</div>
</body>
</html>
