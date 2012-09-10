package myebay1



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Customer)
class CustomerTests {




    @Test
    // C-1: Customers have email address, password and created date fields
    void testCustomerMissingProperty(){

        //Test no provided email address
        Customer c = new Customer (password: "123456", email: "")

        //Test no created date
        Customer c1 = new Customer(password: "123456",email: "xyz@yahoo.com",dateCreated: "")

        //Test no password
        Customer c2 = new Customer(password: "", email: "xyz@yahoo.com")

        //Test for nulls
        Customer c3 = new Customer(password: null, email: "xyz@yahoo.com")
        Customer c4 = new Customer(password: "123456",email: null)



        //Validate customers
        c.validate()
        c1.validate()
        c2.validate()
        c3.validate()
        c4.validate()

        assert c.hasErrors()
        assert c1.hasErrors()
        assert c2.hasErrors()
        assert c3.hasErrors()
        assert c4.hasErrors()

        assert c.errors['email'] != null
        assert c1.errors['dateCreated']  != null
        assert c2.errors['password'] != null
        assert c3.errors['password'] != null
        assert c4.errors['email'] != null

    }

    //C-3: Email address must be of a valid form (@.*)
    @Test
    void testCustomerInvalidEmailSave(){
        //Email without "@"
        Customer c = new Customer( password: "123456", email: "This is invalid E-mail.")
        //Email with "@" as the last character and no "."
        Customer c1 = new Customer(password: "123456", email: "xyz@")
        //Email with "@" at the beginning
        Customer c2 = new Customer(password: "123456", email: "@gmail.com")
        //Email with a "." right after the "@"
        Customer c3 = new Customer(password: "123456", email:  "xyz@.com" )

        c.validate()
        c1.validate()
        c2.validate()
        c3.validate()
        assert c.hasErrors()
        assert c1.hasErrors()
        assert c2.hasErrors()
        assert c3.hasErrors()
        assert c.errors['email'] != null
        assert c1.errors ['email'] != null
        assert c2.errors ['email'] != null
        assert c3.errors ['email'] != null


    }

    // C-4: Password must be between 6-8 characters (unit test)
    @Test
    void testPasswordLength(){

        //Invalid Passwords
        Customer c = new Customer( password: "12345", email: "valid@email.test")
        Customer c1 = new Customer(password: "123456789", email: "valid@email.test")

        //Valid Passwords
        Customer c2 = new Customer(password: "123456", email: "valid@email.test")
        Customer c3 = new Customer(password: "12345678", email: "valid@email.test")

        c.validate()
        c1.validate()
        c2.validate()
        c3.validate()

        assert c.hasErrors()
        assert c1.hasErrors()
        assert !c2.hasErrors()
        assert !c3.hasErrors()
        assert c.errors ['password'] != null
        assert c1.errors ['password'] != null


    }

    //Testing the happy path
    @Test
    void testValidCustomerSave(){
        Customer c = new Customer( password: "123456", email: "valid@email.test")
        c.save()
        assert !c.hasErrors()
    }
}
