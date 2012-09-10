package myebay1

import static org.junit.Assert.*
import org.junit.*

class BiddingControllerIntegrationTests extends GroovyTestCase {

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testInvalidBiddingSave() {
        //L-8: this test is similar to BiddingController Unit test, but before placing invalid Bidding
        //we place valid bid: somehow bidding price validation in Unit test ignores previous bids ( I assume
        // because Spring environment is not  fully initialized for Unit Tests)

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

        //define and save valid bid
        Bidding bidding0 = new Bidding( biddingPrice: 1.5.doubleValue(), bidder: bidder1, listing: listing1 )
        bidding0.save(flush: true)
        assert  !bidding0.hasErrors()


        //Define invalid bid: biddingPrice < currentBidPrice + 0.5
        Bidding bidding123 = new Bidding( biddingPrice: 1.6.doubleValue(), bidder: bidder1, listing: listing1 )
        bidding123.validate()
        assert  bidding123.hasErrors()

        BiddingController biddingController  = new BiddingController()

        //prepare request
        biddingController.request.method = 'POST'

        //prepare params
        Map myParams = [biddingPrice: bidding123.biddingPrice, "bidder.id": bidder1.id,
                bidder: bidder1,
                "listing.id": listing1.id,
                listing:listing1,
                create:"Create",
                action:"Save",
                controller:"bidding"]
        biddingController.params.putAll(myParams)


        //invoke BiddingController.save()
        biddingController.save()

        //assert response
        //L-8: redirect to listing detail page
        assert biddingController.response.redirectedUrl == '/listing/show/' + listing1.id

        //L-8: error validation message on field biddingPrice
        assert biddingController.flash.errors[0].field == 'biddingPrice'

    }
}
