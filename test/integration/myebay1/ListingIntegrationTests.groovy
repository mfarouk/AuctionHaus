package myebay1

import static org.junit.Assert.*
import org.junit.*
import org.springframework.transaction.annotation.Transactional

class ListingIntegrationTests {

    private Customer seller1
    private Customer seller2

    private Customer bidder1
    private Customer bidder2

    private Listing listing1

    @Before
    void setUp() {
        // Setup logic here
        // Setup logic here
        seller1 = new Customer( email: "seller1@ah.com", password: "seller1")
        seller1.save(flush: true)
        assert !seller1.hasErrors()

        seller2 = new Customer( email: "seller2@ah.com", password: "seller2")
        seller2.save(flush: true)
        assert !seller2.hasErrors()

        bidder1 = new Customer( email: "bidder1@ah.com", password: "bidder1")
        bidder1.save(flush: true)
        assert  !bidder1.hasErrors()

        bidder2 = new Customer( email: "bidder2@ah.com", password: "bidder2")
        bidder2.save(flush: true)
        assert  !bidder2.hasErrors()


    }

    @After
    void tearDown() {
    }

    //valid customers shall exist  before we can start create and save listings and biddings
    //TODO we can't save multiple biddings at this point: it will conflict with price validation process
    //TODO cascading save work only for one bidding at a time
    //TODO this shall be fixed in the future

    @Test
    void testListingWithBiddingsSave() {
        //B-6: test that bids saved when listing saved
        listing1 = new Listing(name: "test item1", startingBidPrice: 1, seller: seller1,endTime: new Date(2012,8,8))

        def maxBid = 0
        Customer winner // used to test winner property of a listing

        //first bidding
        Bidding bidding1 = new Bidding( bidder: bidder1, listing:  listing1, biddingPrice: 1.5)
        listing1.addToBiddings(bidding1)
        listing1.save(flush: true)
        assert !listing1.hasErrors()

        //check if bid was saved: max bid for listing shall be 1.5
        maxBid = Bidding.executeQuery("SELECT max(biddingPrice) from Bidding where listing.id = ?", [listing1.id])
        assert !(maxBid == null)
        assert maxBid[0] == 1.5

        //L-7
        //we shall have the winner at this point
        //the winner shall be    bidder1 (first valid bidding)
        winner = listing1.winner
        assert winner!= null
        assert winner.id == bidder1.id

        //
        //listing1.delete(flush: true)
        //second bidding
        Bidding bidding2 = new Bidding( bidder: bidder2, listing:  listing1, biddingPrice: 2)
        listing1.addToBiddings(bidding2)


        listing1.save(flush: true)
        assert !listing1.hasErrors()

        //check if bid was saved: max bid for listing shall be 2
        maxBid = Bidding.executeQuery("SELECT max(biddingPrice) from Bidding where listing.id = ?", [listing1.id])
        assert !(maxBid == null)
        assert maxBid[0] == 2

        //assert all bids were saved
        def allBiddings
        //allBiddings = Bidding.findAll()   //conflicts with bootstrap
        allBiddings = Bidding.findAllByListing(listing1)
        assert allBiddings.size()== 2

        //L-7
        //we shall have the winner at this point
        //the winner shall be    bidder2 (last bid)
        winner = listing1.winner
        assert winner!= null
        assert winner.id == bidder2.id


    }

}
