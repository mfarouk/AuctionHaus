package myebay1



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(BiddingService)
@Mock([Customer,Listing,Bidding,CustomerService,ListingService])
class BiddingServiceTests {

    // C-3: test that completed listing not accepting bids
    void testBidOnCompletedItem() {

        Customer customer = Customer.findOrSaveWhere([email: 'test846@gmail.com', password: 'test846'])
        customer.save(flush: true)
        
        Date date1 = new Date()
        date1.seconds = date1.seconds + 2

        Listing listingWithBidsWhichExpiresInTwoSeconds = new Listing(name: "C-3: test item",
                description: """Test Completed Listing""",
                startingBidPrice: 1,
                seller: customer, endTime: date1)
        listingWithBidsWhichExpiresInTwoSeconds.save(flush: true)
        assert !listingWithBidsWhichExpiresInTwoSeconds.hasErrors()

        sleep(2100)

        Bidding bid = service.save(new Bidding(bidder: customer,
                                   biddingPrice: 6,
                                   listing: listingWithBidsWhichExpiresInTwoSeconds))

        assert  bid.hasErrors()
        assert  bid.errors['listing'] != null


    }
}
