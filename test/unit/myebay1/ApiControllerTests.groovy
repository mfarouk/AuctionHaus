package myebay1



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ApiController)
@Mock([Customer,Listing,Bidding,CustomerService,ListingService,BiddingService])
class ApiControllerTests {

    void testCreateCustomer() {
        request.method = 'POST'
        request.addHeader("Content-Type", "application/json")
        request.setJson("""{"class" : "myebay1.Customer",
                        "biddings" : [],
                        "email" : "json@seller.com",
                        "password" : "json11"
                        }""")
        controller.customerPost()

        def customer = new Customer(response.json)
        assert customer !=null

    }

    void testCreateListing(){
        def customer = new Customer(email: "testCustomer@test.com", password: "test00")
        customer.save(flush: true)

        assert !customer.hasErrors()

         def jsonListing = """ {
                                "class" : "myebay1.Listing",
                                "description" : "Created via Web Service",
                                "endTime" : "2012-07-05T05:00:00Z",
                                "name" : "testJson",
                                "seller" : {
                                            "class" : "myebay1.Customer",
                                            "id" : """ + customer.id + """
                                            },
                                "startingBidPrice" : 10
                                }  """

        request.method = 'POST'
        request.addHeader("Content-Type", "application/json")
        request.setJson(jsonListing)
        controller.listingPost()

        def listing = new Listing(response.json)

        assert  response.json['id'] != null
        assert listing != null

    }

    void testCreateBidding(){

        Customer seller = new Customer(email: 'testSeller01@test.com', password: "test857")
        seller.save(flush: true)
        assert !seller.hasErrors()
        
        Listing listing = new Listing(name:"testItem" ,endTime: new Date()+2 ,startingBidPrice: "2",seller: seller )
        listing.save(flush: true)
        assert !listing.hasErrors()

        Customer bidder = new Customer(email: 'testBidder01@test.com', password: "test857")
        bidder.save(flush: true)
        assert !bidder.hasErrors()
        
        def biddingJson = """
        {
            "class" : "myebay1.Bidding",
            "bidder" : {
                            "class" : "Customer",
                            "id" : """+ bidder.id+ """
                        },
            "biddingPrice" : 17,
            "listing" : {
                            "class" : "Listing",
                            "id" : """+ listing.id+ """
                        }
        } """

        request.method = 'POST'
        request.addHeader("Content-Type", "application/json")
        request.setJson(biddingJson)
        controller.biddingPost()

        def bidding = new Bidding(response.json)

        assert  response.json['id'] != null
        assert bidding != null
    }
}

