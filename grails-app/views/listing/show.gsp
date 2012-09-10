
<%@ page import="myebay1.Listing" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'listing.label', default: 'Listing')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
        %{--UI-1: start--}%
        <script type="text/javascript">
            window.setInterval(update, 5000);

            function update() {
                var pathname = window.location.pathname + '/biddings.json';
                $.ajax({url:pathname,
                    dataType:"json",
                    success: function(json) {
                        displayBiddings(json);
                    },
                    error: function(xhr){
                        $('#biddingsForThisListing').html(xhr.toString());
                    }

                })
            }

            function displayBiddings(json){
                $('#biddingsForThisListing').html("")

                var biddingPrice =   $('#minBidPrice').val();

                $.each(json, function(i,bidding){
                            if (i == 0)  {
                                var newBiddingPrice =   bidding.biddingPrice + 1;
                                if(newBiddingPrice > biddingPrice){
                                    $('#biddingsForThisListingResult').html("")
                                    $('#biddingsForThisListingResult').append("<p>New bid submitted</p>");
                                }
                                else{
                                    $('#biddingsForThisListingResult').html("")
                                    $('#biddingsForThisListingResult').append("<p>No new bids</p>");
                                }
                                $('#minBidPrice').val(bidding.biddingPrice + 1);
                            }
                            $('#biddingsForThisListing').append("<p>bid " + (i + 1) +": bidder: "+ bidding.bidder.email+"; biddingPrice = " + bidding.biddingPrice + ";</p>");
                        }
                );

            }


            //UI-2: submit bid without leaving the page
            function submitBid(){

                var customerId =     $('#customerId').val();
                if (customerId == null)
                {
                    alert ("You have to login before placing bids.");
                    return;
                }

                var pathName = 'http://localhost:8080/MyEbay1/api/bidding.json';
                var biddingPrice =   $('#minBidPrice').val();
                var listingId =      $('#id').val();

                var biddingBody = '{ "class" : "myebay1.Bidding","bidder" : { "class" : "Customer", "id" : '+ customerId +'},' +
                        '"biddingPrice" : ' + biddingPrice +', "listing" : { "class" : "Listing", "id" : '+ listingId +' }}';

                $.ajax({url:pathName,
                    method: 'POST',
                    type: 'POST',
                    dataType:"json",
                    contentType: 'application/json',
                    data: biddingBody,

                    success: function(json) {
                        //if bid successfully placed just update bids

                        //TODO: display error message if bidding was not placed
                        //displayResult(json);

                        update()
                    },
                    error: function(xhr){
                        alert ("Error creating new bid")
                    }

                })
            }

            function displayResult(json){
                $('#biddingsForThisListingResult').html("")
                $.each(json, function(i,error){

                            $('#biddingsForThisListingResult').append("<p>error.message;</p>");
                        }
                );

            }

        </script>
        %{--UI-1: end--}%
	</head>
	<body>
		<a href="#show-listing" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>


			</ul>
		</div>

        %{--UI-1--}%
        <div id="biddingsForThisListingHeader">
            <h1>UI-1: List of 3 most recent (most recent first) bids for this listing. Refreshed every 5 sec.</h1>
        </div>
        <div id="biddingsForThisListing">Wait.....</div>

        %{--UI-3--}%
        <div id="displayCurrentMinBidPrice">
            <br>
            <h1>UI-3: Minimum bid Price for this listing = </h1>
            %{--<g:textArea name="minBidPrice" cols="3" rows="2"  value="${listingInstance?. currentBiddingPrice() + 1}"/>--}%
            <g:textField name="minBidPrice" value="${listingInstance?. currentBiddingPrice() + 1}" />
        </div>

        <div id="placeBid">
            <h1 id="placeBidHeader">UI-2: submit bid without leaving the page.</h1>
            <g:actionSubmit value = "UI-2: click button to submit bid without leaving the page." onclick="submitBid()" />
        </div>
        <div id="biddingsForThisListingResult">Bidding result will be displayed here.</div>
        %{--UI-1: end--}%

		<div id="show-listing" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list listing">

                %{--2012.03.18 Vladimir beliaev--}%
                %{--L-7: place new bid from the listing--}%
                %{--<g:link controller="bidding" action="create" id="${listingInstance?.seller?.id}">Create New Bidding</g:link>--}%
                <g:link  action="createnewbidding" id="${listingInstance?.id}">L-7: Create new Bid for this Listing</g:link>

                <g:if test="${listingInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="listing.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${listingInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${listingInstance?.seller}">
				<li class="fieldcontain">
					<span id="seller-label" class="property-label"><g:message code="listing.seller.label" default="L-6: Seller" /></span>

                    %{--2012.03.18 vladimir Beliaev--}%
                    %{--L-6: show only user portion of email--}%
                    <g:link controller="customer" action="show" id="${listingInstance?.seller?.id}">
                    <el:renderName seller="${listingInstance.seller}"/>
                    </g:link>

				</li>
				</g:if>
			
				<g:if test="${listingInstance?.description}">
				<li class="fieldcontain">
					<span id="description-label" class="property-label"><g:message code="listing.description.label" default="Description" /></span>
					
						<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${listingInstance}" field="description"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${listingInstance?.startingBidPrice}">
				<li class="fieldcontain">
					<span id="startingBidPrice-label" class="property-label"><g:message code="listing.startingBidPrice.label" default="Starting Bid Price" /></span>
					
						<span class="property-value" aria-labelledby="startingBidPrice-label"><g:fieldValue bean="${listingInstance}" field="startingBidPrice"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${listingInstance?.endTime}">
				<li class="fieldcontain">
					<span id="endTime-label" class="property-label"><g:message code="listing.endTime.label" default="End Time" /></span>
					
						<span class="property-value" aria-labelledby="endTime-label"><g:formatDate date="${listingInstance?.endTime}" /></span>
					
				</li>
				</g:if>

                %{--L-3: show most recent bid--}%
                <g:if test="${listingInstance?.lastBidding}">
                    <li class="fieldcontain">
                        <span id="last-bidding-label" class="property-label"><g:message code="listing.biddings.label"  default="L-3: Most Recent Bid" /></span>

                        <span class="property-value" aria-labelledby="last-bidding-label">
                            <g:link controller="bidding" action="show" id="${listingInstance?.lastBidding?.id}">${listingInstance?.lastBidding?.encodeAsHTML()}</g:link>
                        </span>

                    </li>
                </g:if>
			
				<g:if test="${listingInstance?.biddings}">
				<li class="fieldcontain">
					<span id="biddings-label" class="property-label"><g:message code="listing.biddings.label" default="All Biddings" /></span>
					
						<g:each in="${listingInstance.biddings}" var="b">
						<span class="property-value" aria-labelledby="biddings-label"><g:link controller="bidding" action="show" id="${b.id}">${b?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>



                %{--2012.03.18 Vladimir Beliaev--}%
                %{--L-8: display validation errors if attempt to place bid failed--}%
                <g:if test="${flash.errors}">
                          <g:each in="${flash.errors}" var="error">
                            <ul class="errors" role="alert">
                                %{--<li>${error}</li>--}%
                                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                            </ul>
                        </g:each>

                </g:if>

            </ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${listingInstance?.id}" />
					<g:link class="edit" action="edit" id="${listingInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
