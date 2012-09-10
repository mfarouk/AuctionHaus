//History:
//2012.03.12 Vladimir Beliaev: put some data to run app
import myebay1.Customer
import myebay1.Listing
import myebay1.Bidding

class BootStrap {

    def init = { servletContext ->

        //admin customer
        Customer admin = Customer.findOrSaveWhere([email:"admin@admin.com",password:"password",role:"admin"])
        //admin.save()

        Customer seller6 = Customer.findOrSaveWhere([email: 'vladimir@seller2.com', password: 'beliaev'])
        Customer seller2 = Customer.findOrSaveWhere([email: 'seller2@seller.com', password: 'seller2'])
        Customer seller1 = Customer.findOrSaveWhere([email: 'seller1@seller.com', password: 'seller1'])
        Customer seller3 = Customer.findOrSaveWhere([email: 'seller3@seller.com', password: 'seller3'])
        Customer seller4 = Customer.findOrSaveWhere([email: 'seller4@seller.com', password: 'seller4'])
        Customer seller5 = Customer.findOrSaveWhere([email: 'seller5@seller.com', password: 'seller5'])

        //this customer has no listings or bids
        Customer emptyCustomer = Customer.findOrSaveWhere([email: 'empty@seller.com', password: 'empty'])

        Customer bidder1 = Customer.findOrSaveWhere([email: 'belia001@seller.edu', password: 'belia001'])
        Customer bidder2 = Customer.findOrSaveWhere([email: 'vladimir_beliaev@serller.com', password: 'vladimi'])

        Listing listing2 = new Listing(name: "item2", description: "item2 description", startingBidPrice: 10,
                seller: seller2, endTime: new Date().parse('yyyy/MM/dd', '2012/07/05'))
        listing2.save(flush: true)

        Listing listing3 = new Listing(name: "item3", description: "item3 description", startingBidPrice: 30,
                seller: seller3, endTime: new Date().parse('yyyy/MM/dd', '2012/07/04'))
        listing3.save(flush: true)

        Listing listing4 = new Listing(name: "item4", description: "item4 description", startingBidPrice: 40,
                seller: seller4, endTime: new  Date().parse('yyyy/MM/dd', '2012/07/03'))
        listing4.save(flush: true)

        Listing listing5 = new Listing(name: "item5", description: "item5 description", startingBidPrice: 50,
                seller: seller5, endTime: new Date().parse('yyyy/MM/dd', '2012/07/02'))
        listing5.save(flush: true)

        Listing listing6 = new Listing(name: "item6", description: "item6 description", startingBidPrice: 60,
                seller: seller6, endTime: new Date().parse('yyyy/MM/dd', '2012/07/01'))
        listing6.save(flush: true)
        //-----------------------------------------------------------------------------------------------------
        //M-6: listing with bids
        Listing listingWithBids = new Listing(name: "M-6: listing with bids", description: "This listing already has 2 bids", startingBidPrice: 1,
                seller: seller6, endTime: new Date().parse('yyyy/MM/dd', '2012/07/06'))
        listingWithBids.save(flush: true)

        //create some bids
        Bidding bidding1 = new Bidding(bidder: bidder1, biddingPrice: 3, listing: listingWithBids)
        bidding1.save(flush: true)

        Bidding bidding2 = new Bidding(bidder: bidder2, biddingPrice: 6, listing: listingWithBids)
        bidding2.save(flush: true)
        //-----------------------------------------------------------------------------------------------------

        //M-4: create listing which is just 1 minute in the future
        //when it is expired it shall not be displayed in list view
        //C-1,C-2: notification shall be sent when listing is expired

        Date date1 = new Date()
        date1.minutes = date1.minutes + 1
        Listing listingWithBidsWhichExpiresInMinute = new Listing(name: "M-4: test item",
                description: """M-4: end time for this listing will expire in 1 minute and listing
                              shall not be listed after refresh.
                              C-1,C-2:  Notification will be sent to seller and winner when listing completes.""",
                startingBidPrice: 60,
                seller: seller6, endTime: date1)
        listingWithBidsWhichExpiresInMinute.save(flush: true)

        //create some bids
        Bidding bidding3 = new Bidding(bidder: bidder1, biddingPrice: 61, listing: listingWithBidsWhichExpiresInMinute)
        bidding3.save(flush: true)

        Bidding bidding4 = new Bidding(bidder: bidder2, biddingPrice: 62, listing: listingWithBidsWhichExpiresInMinute)
        bidding4.save(flush: true)
        //------------------------------------------------------------------------------------------------

    }
    def destroy = {
    }

    //Initial user for testing login
//    class ApplicationBootStrap {
//        def init = { //servletContext ->
//            //new Customer(email:"eg@eg.com",password:"password").save()
//         }
//        def destroy = {
//        } }
}
