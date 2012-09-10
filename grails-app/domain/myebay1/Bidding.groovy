package myebay1
//************************************************************
//            history
//
//2012.02.13 Vladimir Beliaev: initial implementation
//2012.02.16 Vladimir Beliaev: biddingPrice validation
//2012.03.12 Vladimir Beliaev: biddingPrice validation use validator.



 //represents customer(bidder) bidding for listing
class Bidding {

    static constraints = {
        listing nullable: false,  validator: {  val, obj ->
            if(val.endTime >= new Date()) {
                 return true
            }
            else{
                return false
            }

        }
        bidder nullable: false
        biddingPrice min: 1.doubleValue(), validator: {val, obj ->
            //2012.03.12 Vladimir Beliaev
            if(obj.dateCreated != null){
                //listing integration test: validate only biddings which has not been saved
                return true
            }
            else if (obj.listing == null) {
                return false
            }
            else if (val - 0.499 > obj.listing.currentBiddingPrice()){
                 obj.biddingTime = new Date()
                 return true
            }
            else {
                 return false
            }
        }

        biddingTime nullable: false, datetime: true
    }


    static belongsTo = [listing:Listing, bidder:Customer]

    static namedQueries = {
        //UI-1: named query  supports for  10 most recent bids for listing
        biddingsForListing { listingId ->
            listing{
                eq('id', listingId)
            }
            maxResults(10)
            order("dateCreated", "desc")
        }
    }


    //property bidding price
    double biddingPrice     //shall be greater than  max among bids for listing
    public double getBiddingPrice(){
        return this.biddingPrice
    }
    public void setBiddingPrice( double value){
         this.biddingPrice = value
    }


    Date dateCreated
    Date lastUpdated

    //TODO: Add bidding time
    Date biddingTime
    private setBiddingTime(Date value) {
        this.biddingTime = value
    }
    public Date getBiddingTime(){
        return this.biddingTime
    }

    @Override String  toString(){
        return this.bidder.toString() + ": "  + this.biddingPrice.toString()
    }


}
