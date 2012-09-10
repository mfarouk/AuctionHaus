package myebay1

import static org.junit.Assert.*
import org.junit.*

class CustomerIntegrationTests {

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }


    //C-2: E-mail address must be unique field
    //shall be integration test
    @Test
    void testNonUniqueEmailSave(){
        Customer c1 = new Customer( password: "123456", email: "valid@email.test")
        Customer c2 = new Customer( password: "654321", email: "valid@email.test")
        c1.save(flush: true)
        c2.save(flush: true)
        assert !c1.hasErrors()             //first customer shall be saved OK
        assert c2.hasErrors()              //saving second customer shall cause error
        assert c2.errors['email'] != null  //the reason for error - invalid (non-unique) email field

        //c1.delete(flush: true)
        //c1.delete(flush: true)
    }
}
