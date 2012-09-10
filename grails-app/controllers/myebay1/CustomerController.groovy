package myebay1

import org.springframework.dao.DataIntegrityViolationException

class CustomerController {
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    //SRV-4: refactor to use CustomerService
    def customerService

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        if (!session.customer){
         redirect(controller: "listing",action: "list")
         flash.message = "Sorry, you cannot access this info while not logged in. You are being redirected to listings"
        }
        else

        if( !(session.customer.role == "admin") ){
            flash.message = "Sorry, you can only see your own entry. You are being redirected to listings"
            redirect(controller: "listing", action: "list")
        }
        else
        {
        params.max = Math.min(params.max ? params.int('max') : 4, 100)
        [customerInstanceList: Customer.list(params), customerInstanceTotal: Customer.count()]
        }
    }

    def create() {
        [customerInstance: new Customer(params)]
    }

    def save() {
        def customerInstance = customerService.save(new Customer(params))

        if (customerInstance.hasErrors()) {
            render(view: "create", model: [customerInstance: customerInstance])
            return
        }

//        def customerInstance = new Customer(params)
//        if (!customerInstance.save(flush: true)) {
//            render(view: "create", model: [customerInstance: customerInstance])
//            return
//        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'customer.label', default: 'Customer'), customerInstance.id])
        redirect(action: "show", id: customerInstance.id)
    }

    def show() {
        def customerInstance = Customer.get(params.id)

        if( !(session.customer.role == "admin") ){
            flash.message = "Sorry, you can only see your own entries."
            redirect(action: "list")
        }
        if (!customerInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'customer.label', default: 'Customer'), params.id])
            redirect(action: "list")
            return
        }

        [customerInstance: customerInstance]
    }

    def edit() {
        def customerInstance = Customer.get(params.id)

        if( !(session.customer.role == "admin") ){
            flash.message = "Sorry, you can only edit your own entries."
            redirect(action: "list")
        }
        if (!customerInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'customer.label', default: 'Customer'), params.id])
            redirect(action: "list")
            return
        }
        else
        {
        [customerInstance: customerInstance]
        }
    }

    def update() {
        def customerInstance = Customer.get(params.id)
        if (!customerInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'customer.label', default: 'Customer'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (customerInstance.version > version) {
                customerInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'customer.label', default: 'Customer')] as Object[],
                          "Another user has updated this Customer while you were editing")
                render(view: "edit", model: [customerInstance: customerInstance])
                return
            }
        }

        customerInstance.properties = params

        if (!customerInstance.save(flush: true)) {
            render(view: "edit", model: [customerInstance: customerInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'customer.label', default: 'Customer'), customerInstance.id])
        redirect(action: "show", id: customerInstance.id)
    }

    def delete() {
        def customerInstance = Customer.get(params.id)
        if (!customerInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'customer.label', default: 'Customer'), params.id])
            redirect(action: "list")
            return
        }

        //2012.03.11 Vladimir Beliaev
        //C-4: Customer can be deleted only if he has 0 bids and 0 listings
        //TODO: controller test
        if (customerInstance.numberOfBids > 0 || customerInstance.numberOfListings > 0)  {
            //C-4: display message as flash.message
            flash.message = "C-4: Can't delete customer if it has listings and biddings."

            //C-4: display message as flash.errors
            customerInstance.errors.reject("customer.delete.haslistingorbidding", "C-4: Can't delete customer if it has listings and biddings." )
            flash.errors = customerInstance.errors.allErrors

            redirect(action: "show", id: params.id)
            return
        }

        try {
            customerInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'customer.label', default: 'Customer'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'customer.label', default: 'Customer'), params.id])
            redirect(action: "show", id: params.id)
        }
    }

    def register = {
        if(request.method == 'POST')
        {
            def c = new Customer(params)
            if (c.save()){
                session.customer = c
                redirect(controller: "listing")
            }
            else {
                return [customer:c]
            }
        }
    }

    def login ={
        if (request.method =='POST'){
            redirect(action:"authenticate")
        }
    }

    def authenticate = {
        def customer = Customer.findByEmailAndPassword(params.email,params.password)
        if(customer){
            session.customer = customer
            redirect(controller:"listing")
            flash.message = "Welcome Back ${params.email}"

        }else{
            redirect(controller: "listing")
            flash.message = "Sorry, ${params.email}. Please try again."
        }

    }


    def logout = {
        flash.message = "Goodbye ${session.customer}"
        session.customer = null
        redirect(controller:"listing", action:"list")
    }
}