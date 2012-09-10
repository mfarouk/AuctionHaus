package myebay1

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(BiddingController)
@Mock([Customer,Listing,Bidding,CustomerService,ListingService,BiddingService])
class BiddingControllerTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    void testInvalidBiddingSave() {
        //Define a seller
        Customer seller1 = new Customer( password: "seller4", email: "seller123@email.test")
        seller1.save(flush: true)
        assert !seller1.hasErrors()

        //Define listing
        Listing listing1 = new Listing(name: "item11", startingBidPrice: 1,
                seller: seller1,endTime: new Date(2012,8,8))
        listing1.save(flush: true)
        assert !listing1.hasErrors()

        //Define bidder
        Customer bidder1 = new Customer( password: "bidder4", email: "bidder123@email.test")
        bidder1.save(flush: true)
        assert !bidder1.hasErrors()


        //Define invalid bid: biddingPrice < currentBidPrice + 0.5
        Bidding bidding123 = new Bidding( biddingPrice: 1.doubleValue(), bidder: bidder1, listing: listing1 )
        bidding123.validate()
        assert  bidding123.hasErrors()

        //prepare request
        request.method = 'POST'

        //prepare params
        Map myParams = [biddingPrice: bidding123.biddingPrice, "bidder.id": bidder1.id,
                       bidder: bidder1,
                       "listing.id": listing1.id,
                       listing:listing1,
                       create:"Create",
                       action:"Save",
                       controller:"bidding"]
        params.putAll(myParams)


        //invoke BiddingController.save()
        controller.save()

        //assert response
        //L-8: redirect to listing detail page
        assert response.redirectedUrl == '/listing/show/' + listing1.id

        //L-8: error validation message on field biddingPrice
        assert flash.errors[0].field == 'biddingPrice'

        //L-8: Also see Bidding controller integration tests: it tests biddingPrice validation when
        //valid bid placed before placing invalid bidding.

    }

    void testValidBiddingSave() {
        //Define a seller
        Customer seller1 = new Customer( password: "seller5", email: "seller123@email.test")
        seller1.save(flush: true)
        assert !seller1.hasErrors()

        //Define listing
        Listing listing1 = new Listing(name: "item12", startingBidPrice: 1,
                seller: seller1,endTime: new Date(2012,8,8))
        listing1.save(flush: true)
        assert !listing1.hasErrors()

        //Define bidder
        Customer bidder1 = new Customer( password: "bidder5", email: "bidder123@email.test")
        bidder1.save(flush: true)
        assert !bidder1.hasErrors()



        Bidding bidding123 = new Bidding( biddingPrice: 1.5.doubleValue(), bidder: bidder1, listing: listing1 )

        //prepare request
        request.method = 'POST'

        //prepare params
        Map myParams = [biddingPrice: bidding123.biddingPrice, "bidder.id": bidder1.id,
                bidder: bidder1,
                "listing.id": listing1.id,
                listing:listing1,
                create:"Create",
                action:"Save",
                controller:"bidding"]
        params.putAll(myParams)


        //invoke BiddingController.save()
        controller.save()

        //assert response: valid bidding should be saved
        //L-8: redirect to bidding detail page (we don't now id, how to access it?)
        assert response.redirectedUrl.startsWith('/bidding/show/')

        //L-8: no error messages
        assert flash.errors == null

    }
}
