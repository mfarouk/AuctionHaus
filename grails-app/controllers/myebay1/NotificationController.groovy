package myebay1

class NotificationController {

    def index() {


    }

    def sendJMSMessageCall(){

        def c = Listing.createCriteria()
        def completedLists = c.list{
            le ("endTime",new Date() )
        }
        //If listing is complete send a JMS Message out to the lister and the winner
        try {

        if (completedLists){
            def message = "Listing has Expired"
            sendJMSMessage("queue.notification", message)
            render message

    }
        }catch (Exception e)
        {

        }

    }

}