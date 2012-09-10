package myebay1

import static org.junit.Assert.*
import org.junit.*

class BiddingIntegrationTests {

    private Customer seller1
    private Customer seller2

    private Customer bidder1
    private Customer bidder2

    private Listing listing1
    private Listing listing2
    

    @Before
    void setUp() {
        // before bidding starts
        //valid customers and valid listings shall be created

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
        bidder2.save()
        assert  !bidder2.hasErrors()

        listing1 = new Listing(name: "item1", description: "item1 description", startingBidPrice: 1, seller: seller1, endTime: new Date(2012,8,8))
        listing1.save(flush: true)
        assert !listing1.hasErrors()

        listing2 = new Listing(name: "item2", description: "item2 description", startingBidPrice: 1, seller: seller2, endTime: new Date(2012,8,8))
        listing2.save(flush: true)
        assert !listing2.hasErrors()
    }


    @After
    void tearDown() {
        
    }

    @Test
    void testBiddingProcess() {
        //B:5 tests that bidding price shall be 0.5 greater then current max bid or starting price
        //L:7 we shall have a winner at the end

        //first bidding
        //valid price   (1.5)
        //no errors expected
        double biddingPrice = this.listing1.startingBidPrice  + 0.5 //1.5

        Bidding bidding1 = new Bidding( listing: this.listing1, bidder: this.bidder1, biddingPrice: biddingPrice)
        bidding1.save(flush: true)
        listing1.refresh()
        assert !bidding1.hasErrors()
        assert !listing1.hasErrors()
        assert listing1.currentBiddingPrice() == biddingPrice
        //B-4 listing has list of bids for that listing
        //assert that new bidding is in listing.biddings
        assert  listing1.biddings.size() == 1
        assert  listing1.biddings.asList()[0].id == bidding1.id

        //second bid
        //valid price   (2)
        //no errors expected
        biddingPrice = 2.doubleValue()    //2
        Bidding bidding2 = new Bidding( listing: this.listing1, bidder: this.bidder2, biddingPrice: biddingPrice)
        bidding2.save(flush: true)
        listing1.refresh()
        assert !bidding2.hasErrors()
        assert  listing1.biddings.size() == 2
        int currentBiddingPrice =  listing1.currentBiddingPrice()
        assert listing1.currentBiddingPrice() == biddingPrice


        //third bid
        //invalid price:  new biddingPrice (2.49) less than currentPrice + 0.5
        //bidding shall be rejected

        Bidding bidding3 = new Bidding( listing: this.listing1, bidder: this.bidder1, biddingPrice: biddingPrice + 0.49)
        bidding3.save(flush: true)

        //assert bidding validation failed
        assert bidding3.hasErrors()
        assert bidding3.errors['biddingPrice'] != null

        // no new bidding added
        listing1.refresh()
        assert !listing1.hasErrors()
        assert  listing1.biddings.size() == 2
        assert listing1.currentBiddingPrice() == biddingPrice //2.0

        //make biddingPrice valid
        biddingPrice = 2.5
        bidding3.biddingPrice = biddingPrice
        bidding3.save(flush: true)

        listing1.refresh()
        assert !bidding3.hasErrors()
        assert  listing1.biddings.size() == 3
        currentBiddingPrice =  listing1.currentBiddingPrice()
        assert listing1.currentBiddingPrice() == biddingPrice

        //L-7
        //we shall have the winner at this point
        //the winner shall be    bidder1 (last valid bid)
        Customer winner = listing1.winner
        assert winner!= null
        assert winner.id == bidder1.id
    }
}
