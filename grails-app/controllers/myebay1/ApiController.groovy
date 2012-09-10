package myebay1

import grails.converters.deep.JSON
import grails.converters.XML
import java.text.SimpleDateFormat

class ApiController {

    static allowedMethods = [customerGet: "GET", customerPost: "POST",
            listingGet: "GET", listingPost: "POST",
            biddingGet: "GET", biddingPost: "POST",
            biddingsForListingGet: "GET"]

    def customerService
    def listingService
    def biddingService

    def customerGet() {
        def customerId =  params.get("id")
        def result = Customer.findById(customerId)

        def acceptType = request.getHeader("accept")

        if (acceptType.contains("json") ){
            render result as JSON
        }

        else if (acceptType.contains("XML")||acceptType.contains("xml") ){
            render result as XML
        }
    }

    //SRV-1: Create a Grails service method that supports creating a new customer (unit test)
    def customerPost(){
        def contentType =  request.getHeader("content")
        if (contentType == null) {
            contentType = request.getHeader("Content-Type")
        }

        if (contentType.contains("json") || contentType.contains("JSON") ){

            def customer = customerService.save( new Customer(request.JSON))

            if (!customer?.hasErrors()){
                render   customer as JSON
            }
            else{
                render customer.errors as JSON
            }

        }
        else if (contentType.contains("XML") || contentType.contains("xml")){
            def customer = customerService.save( new Customer(request.XML))

            if (!customer?.hasErrors()){
                render   customer as XML
            }
            else{
                render customer.errors as XML
            }
        }

        else {
            render "Undefined content type"
        }
    }

    def listingGet(){

        def listingId =  params.get("id")
        def result = Listing.findById(listingId)

        def acceptType = request.getHeader("accept")

        withFormat{
            json { render result as JSON }
            xml { render result as XML }
        }
    }

    //SRV-2: Create a Grails service method that supports creating a new listing (unit test)
    def listingPost(){
        def contentType =  request.getHeader("content")
        if (contentType == null) {
            contentType = request.getHeader("Content-Type")
        }
        
        Listing newListing

        if (contentType.contains("json")|| contentType.contains("JSON") ){
            newListing = new Listing(request.JSON)
        }
        else if (contentType.contains("XML") || contentType.contains("xml")){
            newListing = new Listing(request.XML)
        }

        else {
            render "Undefined content type"
        }

        //set listing endTime
        //"2012-07-05T05:00:00Z"
        SimpleDateFormat parserSDF=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"	)
        newListing.endTime =  parserSDF.parse(request.JSON['endTime'])

        //TODO: this goes to service
        Customer seller = Customer.findById(request.JSON['seller']['id'])
        newListing.seller = seller

        //newListing.save(flush: true)

        newListing = listingService.save(newListing)
        def result
        
        if (!newListing.hasErrors()){
            result = newListing
        }
        else{
            result = newListing.errors
        }

        if (contentType.contains("json") || contentType.contains("JSON") ){
            render result as JSON
        }
        else if (contentType.contains("xml") || contentType.contains("XML") ) {
            render result as XML
        }

        else{
            render "Undefined content type"
        }
//TODO: for some reason does not works only when ".json" explicitly appended to URL
//TODO: does not recognize "Accept" and "Content-Type" request headers
//        withFormat{
//            json {
//                render result as JSON
//                 }
//            xml {
//                render result as XML
//                }
//        }
    }

    // UI-1: supports getting 10 most recent biddings for listing.
    def biddingsForListingGet(){

        long listingId = params.get("id").toLong()

        //UI-1: get 10 most recent bids for listing
        def result = Bidding.biddingsForListing(listingId).list()

        //content negotiation
        //return XML by default
        //for some reason ignores "Accept" header
        //however returns json if URL ends with ".json"
        if (result != null) {
            withFormat{
                json {
                    render result as JSON
                }
                xml {
                    render result as XML
                }
            }
        }

    }

    def biddingGet(){
        def bidId =  params.get("id")
        def result = Bidding.findById(bidId)

        withFormat{
            json { render result as JSON }
            xml { render result as XML }
        }
    }


    def biddingPost(){
        def contentType =  request.getHeader("content")
        if (contentType == null) {
            contentType = request.getHeader("Content-Type")
        }
        Bidding bidding

        if (contentType.contains("json")|| contentType.contains("JSON") ){
            bidding = new Bidding(request.JSON)
        }
        else if (contentType.contains("XML") || contentType.contains("xml")){
            bidding = new Bidding(request.XML)
        }

        else {
            render "Undefined content type"
        }

        //TODO: this goes to service
        Customer bidder = Customer.findById(request.JSON['bidder']['id'])
        Listing listing = Listing.findById(request.JSON['listing']['id'])
        bidding.bidder = bidder
        bidding.listing = listing


        //bidding.save(flush: true)
        bidding = biddingService.save(bidding)

        def result
        if (!bidding.hasErrors()){
            result = bidding
        }
        else{
            result = bidding.errors
        }

        //TODO: for some reason WithFormat{} works only when ".json" explicitly appended to URL
        if (contentType.contains("json") || contentType.contains("JSON") ){
            render result as JSON
        }
        else if (contentType.contains("xml") || contentType.contains("XML") ) {
            render result as XML
        }

        else{
            render "Undefined content type"
        }
    }

}
