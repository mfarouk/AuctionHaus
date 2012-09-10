package myebay1

import org.springframework.dao.DataIntegrityViolationException

class BiddingController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    //SRV-4: Refactor to use service methods
    def biddingService

    //Call the checkCustomer method before anything else is executed
    def beforeInterceptor = [action:this.&checkCustomer]

    def index() {

          redirect (action:"list")

    }

    def list() {

        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [biddingInstanceList: Bidding.list(params), biddingInstanceTotal: Bidding.count()]

    }

    //L-7:  redirection to create bid from listing controller
    def create() {

        def bidding =   new Bidding()

        def listing = Listing.get (params.id)
        if (listing != null){
             bidding.listing = listing
             bidding.biddingPrice = listing.currentBiddingPrice() + 1
            flash.message = "L-8: to see validation errors enter invalid (less then " + bidding.biddingPrice + ") price"
        }
        [biddingInstance: bidding]
    
    }

    def save() {
        //SRV-4: refactor to use service methods
        def biddingInstance = biddingService.save( new Bidding(params) )

        if (biddingInstance.hasErrors()) {

            //L-8: redirect to listing detail and display errors
            flash.message = "Bidding creation failed."
            // pass errors to listing controller
            flash.errors =  biddingInstance.errors.fieldErrors
            //  redirect
            redirect(controller: "listing", action: "show", id: biddingInstance.listing.id)
            return
            //this is original code: redirect back to create bidding
            //render(view: "create", model: [biddingInstance: biddingInstance])
            //return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'bidding.label', default: 'Bidding'), biddingInstance.id])
        redirect(action: "show", id: biddingInstance.id)

    }


    def show() {
        

        def biddingInstance = Bidding.get(params.id)
        if (!biddingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bidding.label', default: 'Bidding'), params.id])
            redirect(action: "list")
            return
        }

        [biddingInstance: biddingInstance]

    }

    def edit() {

        def biddingInstance = Bidding.get(params.id)
        if (!biddingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bidding.label', default: 'Bidding'), params.id])
            redirect(action: "list")
            return
        }

        [biddingInstance: biddingInstance]

    }

    def update() {

        def biddingInstance = Bidding.get(params.id)
        if (!biddingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bidding.label', default: 'Bidding'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (biddingInstance.version > version) {
                biddingInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'bidding.label', default: 'Bidding')] as Object[],
                        "Another user has updated this Bidding while you were editing")
                render(view: "edit", model: [biddingInstance: biddingInstance])
                return
            }
        }

        biddingInstance.properties = params

        if (!biddingInstance.save(flush: true)) {
            render(view: "edit", model: [biddingInstance: biddingInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'bidding.label', default: 'Bidding'), biddingInstance.id])
        redirect(action: "show", id: biddingInstance.id)

    }

    def delete() {

        def biddingInstance = Bidding.get(params.id)
        if (!biddingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bidding.label', default: 'Bidding'), params.id])
            redirect(action: "list")
            return
        }

        try {
            biddingInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'bidding.label', default: 'Bidding'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'bidding.label', default: 'Bidding'), params.id])
            redirect(action: "show", id: params.id)
        }

    }

        def checkCustomer() {

            if(!session.customer) {
            // Customer not logged in
            // Should be redirected somewhere else
            redirect(controller:"listing", action:"list")
            flash.message="You have to be logged in to bid or to view biddings, you're being redirect to listings"
            return false
    }
            else{
                  return true
            }

        }
}