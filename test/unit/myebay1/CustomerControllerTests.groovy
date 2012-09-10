package myebay1

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(CustomerController)
@Mock([Customer,Listing,Bidding])
class CustomerControllerTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    //C-4: can delete customer without listings and biddings
    void testCustomerWithoutListingsAndBiddingsDelete() {

        //Define a customer without listings and biddings
        Customer customer1 = new Customer( password: "123456", email: "valid1234@email.test")
        customer1.save(flush: true)

        Listing listing1 = new Listing ()

        request.method = 'POST'
        params.id = customer1.id

        controller.delete()

        //C-4: customer was deleted, redirect to customer list
        assert response.redirectedUrl == '/customer/list'
        
        //C-4: assert customer was deleted
        assert Customer.get(customer1.id) == null

    }

    //C-4: Can't delete customer with listing 
    void testCustomerWithListingDelete() {

        //Define a customer with listing
        Customer customer1 = new Customer( password: "123456", email: "valid3456@email.test")
        customer1.save(flush: true)

        Listing listing1 = new Listing(name: "item1", startingBidPrice: 1,
                seller: customer1,endTime: new Date(2012,8,8))
        listing1.save(flush: true)

        request.method = 'POST'
        params.id = customer1.id

        controller.delete()

        //C-4: assert redirected back to customer detail page
        assert response.redirectedUrl == '/customer/show/' +  customer1.id //redirect back to Customer details

        //C-4: assert message is displayed
        assert   flash.message == "C-4: Can't delete customer if it has listings and biddings."

        //C-4: assert error message
        assert flash.errors[0].defaultMessage == "C-4: Can't delete customer if it has listings and biddings."

        //C-4: assert customer is not deleted
        assert Customer.get(customer1.id) != null

    }

    //C-4: Can't delete customer with bidding
    void testCustomerWithBiddingDelete() {

        //Define a customer with listing
        Customer customer1 = new Customer( password: "123456", email: "valid3456@email.test")
        customer1.save(flush: true)

        Listing listing1 = new Listing(name: "item1", startingBidPrice: 1,
                seller: customer1,endTime: new Date(2012,8,8))
        listing1.save(flush: true)

        //define customer with bidding
        Customer customer2 = new Customer( password: "654321", email: "valid654@email.test")
        customer2.save(flush: true)

        Bidding bidding1 = new Bidding( biddingPrice: 2.doubleValue(), bidder: customer2, listing: listing1 )
        bidding1.save(flush: true)
        

        request.method = 'POST'
        params.id = customer2.id

        controller.delete()

        //C-4: assert redirected back to customer detail page
        assert response.redirectedUrl == '/customer/show/' +  customer2.id //redirect back to Customer details

        //C-4: assert message is displayed
        assert   flash.message == "C-4: Can't delete customer if it has listings and biddings."

        //C-4: assert error message
        assert flash.errors[0].defaultMessage == "C-4: Can't delete customer if it has listings and biddings."

        //C-4: assert customer is not deleted
        assert Customer.get(customer2.id) != null

    }
}
