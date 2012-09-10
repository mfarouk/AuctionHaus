package myebay1

import org.springframework.dao.DataIntegrityViolationException

class ListingController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST",createNewBidding: "POST"]

    //SRV-4: Refactor to use service methods
    def listingService

    def index() {
        //2012.03.11 Vladimir Beliaev:
        // M-1: sort By dateCreated, most recent first
        //
        params.sort = "dateCreated"
        params.order = "desc"
        redirect(action: "list", params: params)
    }

    def list() {

        //2012.03.11  Vladimir Beliaev:
        // M-2: Display 5 listings at a time
        params.max = Math.min(params.max ? params.int('max') : 5, 100)


        //2012.03.15 Vladimir Beliaev
        //M-4: display only listings with endTime is in the future
        def c = Listing.createCriteria()
        def listingList = c.list (params) {
            ge("endTime", new Date())    //greater or equal
        }
        [listingInstanceList: listingList, listingInstanceTotal: Listing.count()]
    }

    def mylist() {

        //2012.03.11  Vladimir Beliaev:
        // M-2: Display 5 listings at a time
        params.max = Math.min(params.max ? params.int('max') : 5, 100)

        if(session.customer){
            def c2 = Listing.createCriteria()
            def listing1 = c2.list(params){
                eq("seller",session.customer)
            }
            [listingInstanceList: listing1, listingInstanceTotal: Listing.count()]
        }
        else {

            redirect(action: "list", params: params)
        }
    }

    def create() {
        [listingInstance: new Listing(params)]
    }

    def save() {
        //SRV-4: Refactor to use service methods
        def listingInstance = listingService.save(new Listing(params))
        if (listingInstance.hasErrors()) {
            render(view: "create", model: [listingInstance: listingInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'listing.label', default: 'Listing'), listingInstance.id])
        redirect(action: "show", id: listingInstance.id)
    }

    def show() {
        def listingInstance = Listing.get(params.id)
        
        //UI-7
        if (listingInstance.endTime < new Date()){
            flash.message = "This listing is completed"
            redirect(action: "list")
        }

        if (!listingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'listing.label', default: 'Listing'), params.id])
            redirect(action: "list")
            return
        }

        [listingInstance: listingInstance]
    }

    def edit() {
        def listingInstance = Listing.get(params.id)
        if (!listingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'listing.label', default: 'Listing'), params.id])
            redirect(action: "list")
            return
        }

        [listingInstance: listingInstance]
    }

    def update() {
        def listingInstance = Listing.get(params.id)
        if (!listingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'listing.label', default: 'Listing'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (listingInstance.version > version) {
                listingInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'listing.label', default: 'Listing')] as Object[],
                        "Another user has updated this Listing while you were editing")
                render(view: "edit", model: [listingInstance: listingInstance])
                return
            }
        }

        listingInstance.properties = params

        if (!listingInstance.save(flush: true)) {
            render(view: "edit", model: [listingInstance: listingInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'listing.label', default: 'Listing'), listingInstance.id])
        redirect(action: "show", id: listingInstance.id)
    }

    def delete() {
        def listingInstance = Listing.get(params.id)
        if (!listingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'listing.label', default: 'Listing'), params.id])
            redirect(action: "list")
            return
        }

        try {
            listingInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'listing.label', default: 'Listing'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'listing.label', default: 'Listing'), params.id])
            redirect(action: "show", id: params.id)
        }
    }

    //L-7: allow to place new bid for the listing
    def createnewbidding(){
        def listingInstance = Listing.get(params.id)
        
        if (!listingInstance) {
            //Bidding biddingInstance = new Bidding(listing: listingInstance, biddingPrice: listingInstance.currentBiddingPrice() + 1 )
            redirect(controller: "bidding" , action: "create")

        }
        else{
//            Map myParams = [biddingPrice: listingInstance.currentBiddingPrice() + 1,
//                    "listing.id": listingInstance.id,
//                    listing:listingInstance,
//                    action:"create",
//                    controller:"bidding"]
            
            redirect(controller: "bidding" , action: "create", id: listingInstance.id)
            //params.putAll(myParams)
            //redirect (params)
        }


    }
    
    def sendEmail(){
            // Check if the listing is complete
            def c = Listing.createCriteria()
            def completedLists = c.list{
                le ("endTime",new Date() )
            }
            //If listing is complete send an email out to the lister and the winner
            if (completedLists){

                sendMail {
                    to completedLists.seller
                    from "pinkpanthers.vova.belkin@gmail.com" //"pinkpanthers.msse@gmail.com"
                    subject "to seller: Listing Complete"
                    body 'The listing is complete'
                }

                if (completedLists?.lastBidding?.bidder !=null) {
                    sendMail {
                        to completedLists.lastBidding.bidder
                        from "pinkpanthers.vova.belkin@gmail.com"//"pinkpanthers.msse@gmail.com"
                        subject "to winner: Listing Complete"
                        body 'The listing is complete'
                    }
                }

            }
    }
}
