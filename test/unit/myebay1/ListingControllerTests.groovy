package myebay1

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(ListingController)
@Mock([Customer,Listing,Bidding,CustomerService,ListingService,BiddingService])
class ListingControllerTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    void testRedirectToCreateBidding() {
        //define customer
        Customer seller1 = new Customer( password: "seller5", email: "seller05@email.test")
        seller1.save(flush: true)
        assert !seller1.hasErrors()

        //Define listing
        Listing listing1 = new Listing(name: "item12", startingBidPrice: 1,
                seller: seller1,endTime: new Date(2012,8,8))
        listing1.save(flush: true)
        assert !listing1.hasErrors()

        //prepare request
        request.method = 'POST'

        //prepare params
        Map myParams = [id: listing1.id,
                action:"createnewbidding",
                controller:"listing"]
        params.putAll(myParams)

        //invoke BiddingController.save()
        controller.createnewbidding()

        //assert response
        //L-7: redirect to create bidding for this listing
        assert response.redirectedUrl == '/bidding/create/'  +  listing1.id

    }
}
