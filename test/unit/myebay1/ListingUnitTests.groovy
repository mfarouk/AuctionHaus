package myebay1

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(Listing)
class ListingUnitTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }


    //L-1: Listings have the following required fields: name, end date/time,
    // and starting bid price (unit test)
    void testRequiredProperties() {

        //Define a customer to be used in all property tests
        Customer c = new Customer( password: "123456", email: "valid@email.test")

        //Testing blank name
        Listing L = new Listing(name:"" ,endTime: new Date()+2 ,startingBidPrice: "2",seller: c )

        //Testing blank endTime
        Listing L1 = new Listing(name: "ValidListingName", endTime: "", startingBidPrice: "5", seller: c)

        //Testing blank startingBidPrice
        Listing L2 = new Listing(name: "AnotherValidListing", endTime: new Date()+2, startingBidPrice: "", seller: c)

        //Testing an all Valid Listing - Happy Path
        Listing L3 = new Listing(name: "LastValidListing", endTime: new Date()+2,
                                 startingBidPrice: 50.doubleValue(),seller: c)



        L.validate()
        L1.validate()
        L2.validate()
        L3.validate()

        assert L.hasErrors()
        assert L1.hasErrors()
        assert L2.hasErrors()
        assert L.errors['name']!=null
        assert L1.errors['endTime']!=null
        assert L2.errors['startingBidPrice']!=null
        assert !L3.hasErrors()
    }
    //L-2: Listings have the following optional fields: description (unit test)
    void testOptionalDescription(){
        //Listing with a description
        Customer c = new Customer( password: "123456", email: "valid@email.test")
        Listing L = new Listing(name: "LastValidListing", endTime: new Date()+2, startingBidPrice: "50",seller: c, description: "Decription Text ...")
        //Listing with no description
        Listing L1 = new Listing(name: "LastValidListing", endTime: new Date()+2, startingBidPrice: "50",seller: c)

        L.validate()
        L1.validate()

        assert !L.hasErrors()
        assert !L1.hasErrors()

    }
    //L-3: Listings are required to have a seller (Customer) (unit test)
    void testSellerProperty(){
        Customer c = new Customer( password: "123456", email: "valid@email.test")
        //Testing a blank seller
        Listing L = new Listing(name: "LastValidListing", endTime: new Date()+2, startingBidPrice: "50",seller:"")
        //Testing a valid Seller
        Listing L1 = new Listing(name: "LastValidListing", endTime: new Date()+2, startingBidPrice: "50",seller: c)
        //Testing a null Seller
        Listing L2 = new Listing(name: "LastValidListing", endTime: new Date()+2, startingBidPrice: "50",seller: null)
        //Testing an non existent Customer as a seller
        Listing L3 = new Listing(name: "LastValidListing", endTime: new Date()+2, startingBidPrice: "50",seller: "customer")

        L.validate()
        L1.validate()
        L2.validate()
        L3.validate()

        assert L.hasErrors()
        assert !L1.hasErrors()
        assert L2.hasErrors()
        assert L3.hasErrors()

        assert L.errors['seller']!=null
        assert L2.errors['seller']!=null
        assert L3.errors['seller']!=null
    }
    //L-4: Listing descriptions must be less than 256 characters (unit test)
    void testDescriptionLength(){

        Customer c = new Customer( password: "123456", email: "valid@email.test")
        //Test a 256 character description
        Listing L = new Listing(name: "LastValidListing", endTime: new Date()+2, startingBidPrice: "50",seller: c, description: "Decription Text ...")
        //Test a 257 character description
        //..
        //Test a 255 character description

    }
    //L-5: Listing end date/time must be in the future (unit test)
    void testEndTime(){

        Customer c = new Customer( password: "123456", email: "valid@email.test")
        //Test endTime = now  - 1 minute: otherwise may fail due to little timing between initialisation and validation
        Listing L1 = new Listing(name: "LastValidListing", endTime: new Date(), startingBidPrice: "50",seller: c)
        L1.endTime.minutes =  L1.endTime.minutes - 1
        //Test endTime = yesterday
        Listing L2 = new Listing(name: "LastValidListing", endTime: new Date()-1, startingBidPrice: "50",seller: c)
        //Test endTime = A time long ago
        Listing L3 = new Listing(name: "LastValidListing", endTime: new Date()-100, startingBidPrice: "50",seller: c)
        //Test endTime = A time way in the future
        Listing L4 = new Listing(name: "LastValidListing", endTime: new Date()+100, startingBidPrice: "50",seller: c)
        // Test endTime = tomorrow
        Listing L5 = new Listing(name: "LastValidListing", endTime: new Date()+1, startingBidPrice: "50",seller: c)
        // Test invalid endTime
        Listing L6 = new Listing(name: "LastValidListing", endTime: c, startingBidPrice: "50",seller: c)

        L1.validate()
        L2.validate()
        L3.validate()
        L4.validate()
        L5.validate()
        L6.validate()

        assert L1.hasErrors()
        assert L2.hasErrors()
        assert L3.hasErrors()
        assert !L4.hasErrors()
        assert !L5.hasErrors()
        assert L6.hasErrors()

    }
    //L-6: Listing name must be less than 64 characters (unit test)
    void testListingNameLength(){
        Customer c = new Customer( password: "123456", email: "valid@email.test")
        char[] data = new char [64]
        char[] data1 = new char [65]
        char[] data2 = new char [63]
        char[] data3 = new char [1]
        char[] data4 = new char [0]
        String str = new String(data)
        String str1 = new String(data1)
        String str2 = new String(data2)
        String str3 = new String(data3)
        String str4 = new String(data4)

        //Testing a 64 character string
        Listing L = new Listing(name: str, endTime: new Date()+2, startingBidPrice: "50",seller: c)
        //Testing a 65 character string
        Listing L1 = new Listing(name: str1, endTime: new Date()+2, startingBidPrice: "50",seller: c)
        //Testing 63 character string
        Listing L2 = new Listing(name: str2, endTime: new Date()+2, startingBidPrice: "50",seller: c)
        //Testing a 1 character string
        Listing L3 = new Listing(name: str3, endTime: new Date()+2, startingBidPrice: "50",seller: c)
        //Testing a 0 character string
        Listing L4 = new Listing(name: str4, endTime: new Date()+2, startingBidPrice: "50",seller: c)

        L.validate()
        L1.validate()
        L2.validate()
        L3.validate()
        L4.validate()

        assert L.hasErrors()
        assert L1.hasErrors()
        assert !L2.hasErrors()
        assert !L3.hasErrors()
        assert L4.hasErrors()

    }
    //L-7: Listing has a nullable field for the winner (Customer) (unit test)
     void testWinnerField(){
        //Define a customer to be used in   tests
        Customer seller = new Customer( password: "123456", email: "seller@email.test")
        //Define a listing to be used in   tests
        Listing l = new Listing(name:"item1" ,endTime: new Date()+2 ,startingBidPrice: "2",seller: seller )
        l.save(flush: true)
        assert !l.hasErrors()
        assert l.winner == null

        //winner is read only property which set as a result of bidding process
        //we shall place valid bids and save it
        //it is tested as part of Listing and Bidding Integration Tests

    }
}
