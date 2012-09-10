<%@ page import="myebay1.Bidding" %>



<div class="fieldcontain ${hasErrors(bean: biddingInstance, field: 'listing', 'error')} required">
	<label for="listing">
		<g:message code="bidding.listing.label" default="Listing" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="listing" name="listing.id" from="${myebay1.Listing.list()}" optionKey="id" required="" value="${biddingInstance?.listing?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: biddingInstance, field: 'bidder', 'error')} required">
	<label for="bidder">
		<g:message code="bidding.bidder.label" default="Bidder" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="bidder" name="bidder.id" from="${myebay1.Customer.list()}" optionKey="id" required="" value="${biddingInstance?.bidder?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: biddingInstance, field: 'biddingPrice', 'error')} required">
	<label for="biddingPrice">
		<g:message code="bidding.biddingPrice.label" default="Bidding Price" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="biddingPrice" min="1.0" required="" value="${fieldValue(bean: biddingInstance, field: 'biddingPrice')}"/>
</div>

