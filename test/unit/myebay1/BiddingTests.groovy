package myebay1

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(Bidding)
class BiddingTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    void testRequiredProperties() {

        //Define a customer to be used in   tests
        Customer seller = new Customer( password: "123456", email: "seller@email.test")
        //Define a listing to be used in   tests       
        Listing l = new Listing(name:"item1" ,endTime: new Date()+2 ,startingBidPrice: "2",seller: seller )
        //Define a customer to be used in   tests as bidder
        Customer bidder = new Customer( password: "123456", email: "bidder@email.test")

        //Testing valid bidding
        Bidding b = new Bidding( biddingPrice: 2.5.doubleValue(), bidder: bidder, listing: l )
        b.validate()
        assert !b.hasErrors()
        
        //Bidding time is read-only. It shall be set if bidding is valid
        assert b.biddingTime != null

        //B-1: bidding shall have valid bidding price
        Bidding b2 = new Bidding( bidder: bidder, listing: l )
        b2.validate()
        assert b2.hasErrors()
        assert b2.errors['biddingPrice'] != null
        assert b2.errors['biddingTime'] != null

        //B-2: bidding are required to be for a listing
        Bidding b3 = new Bidding(  biddingPrice: 2.5.doubleValue(), bidder: bidder )
        b3.validate()
        assert b3.hasErrors()
        assert b3.errors['listing'] != null
        assert b3.errors['biddingTime'] != null

        //B-3: bidding are required to have a bidder
        Bidding b4 = new Bidding( biddingPrice: 2.5.doubleValue(), listing: l )
        b4.validate()
        assert b4.hasErrors()
        assert b4.errors['bidder'] != null

        //B-4: Listing has a list of bids for that listing
        // this requirement tested in Bidding Integration Test
        //

        //B-5: see Bidding Integration test

        //B-6: see Listing Integration Test

    }
}
