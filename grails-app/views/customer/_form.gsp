<%@ page import="myebay1.Customer" %>



<div class="fieldcontain ${hasErrors(bean: customerInstance, field: 'password', 'error')} required">
	<label for="password">
		<g:message code="customer.password.label" default="Password" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="password" name="password" maxlength="8" required="" value="${customerInstance?.password}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: customerInstance, field: 'email', 'error')} required">
	<label for="email">
		<g:message code="customer.email.label" default="Email" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="email" name="email" required="" value="${customerInstance?.email}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: customerInstance, field: 'role', 'error')} ">
	<label for="role">
		<g:message code="customer.role.label" default="Role" />
		
	</label>
	<g:select name="role" from="${customerInstance.constraints.role.inList}" value="${customerInstance?.role}" valueMessagePrefix="customer.role" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: customerInstance, field: 'biddings', 'error')} ">
	<label for="biddings">
		<g:message code="customer.biddings.label" default="Biddings" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${customerInstance?.biddings?}" var="b">
    <li><g:link controller="bidding" action="show" id="${b.id}">${b?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="bidding" action="create" params="['customer.id': customerInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'bidding.label', default: 'Bidding')])}</g:link>
</li>
</ul>

</div>

