package myebay1


class Complete_listingJob {


    def timeout = 90000l // execute job once in 90 seconds

    def execute() {

            //Call sendEmail in the Listing controller
            //To do: Post a message to JMS queue
            ListingController l = new ListingController()

            l.sendEmail()

            NotificationController N = new NotificationController()
            N.sendJMSMessageCall()
        
    }
}
