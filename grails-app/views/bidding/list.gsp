
<%@ page import="myebay1.Bidding" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'bidding.label', default: 'Bidding')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-bidding" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-bidding" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<th><g:message code="bidding.listing.label" default="Listing" /></th>
					
						<th><g:message code="bidding.bidder.label" default="Bidder" /></th>
					
						<g:sortableColumn property="biddingPrice" title="${message(code: 'bidding.biddingPrice.label', default: 'Bidding Price')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'bidding.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="lastUpdated" title="${message(code: 'bidding.lastUpdated.label', default: 'Last Updated')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${biddingInstanceList}" status="i" var="biddingInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${biddingInstance.id}">${fieldValue(bean: biddingInstance, field: "listing")}</g:link></td>
					
						<td>${fieldValue(bean: biddingInstance, field: "bidder")}</td>
					
						<td>${fieldValue(bean: biddingInstance, field: "biddingPrice")}</td>
					
						<td><g:formatDate date="${biddingInstance.dateCreated}" /></td>
					
						<td><g:formatDate date="${biddingInstance.lastUpdated}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${biddingInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
