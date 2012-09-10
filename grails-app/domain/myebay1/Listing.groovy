package myebay1

//************************************************************
//            history
//
//2012.02.13 Vladimir Beliaev: initial implementation
//2012.02.14 Farouk A:
//2012.02.16 Vladimir Beliaev: merging
//                  - winner property
//                  -  getting current winning Bidding and current biddingPrice

class Listing {

    static constraints = {
        name blank: false, size: 1..63
        seller nullable: false
        description nullable: true, size: 0..256
        startingBidPrice min: 1.doubleValue(), blank: false
        endTime datetime: true, blank: false, min: new Date()
        winner nullable: true
        biddings nullable: true
    }

    static belongsTo = [seller:Customer]
    static hasMany = [biddings:Bidding]

    static namedQueries = {
        //support for EMail notification
        //get listings which are completed and
        completedListings {
            le ("endTime",new Date())
            le  ("numberOfNotifications", 1)
        }
    }

    int numberOfNotifications
    
    Customer winner
    private void setWinner(Customer value) {
       this.winner = value
    }
    public Customer getWinner(){
        Bidding winnerBidding
        try{
            winnerBidding = this.getLastBidding()
        }
        catch(e) {}

        if (winnerBidding != null) {
            return winnerBidding.bidder
        }
        else{
            return null
        }

    }

    String name

    @Override String toString()  {
        return this.name
    }

    String description
    
    double startingBidPrice
    
    Date endTime
    Date dateCreated
    Date lastUpdated
    
    Bidding lastBidding
    private setLastBidding(Bidding value)  {
    }

    public getLastBidding (){
        return winnerBidding()
    }

    //get the highest bidding for this listing
    private Bidding winnerBidding (){
        def maxBid
        def maxBiddingPrice
        maxBiddingPrice = this.currentBiddingPrice()
        maxBid = Bidding.find("from Bidding as b where b.biddingPrice = ? and b.listing.id =?",[maxBiddingPrice,this.id])
        if(maxBid != null) {
            return maxBid
        }
        return null
    }

    //get current max bidding price for this listing
    public double currentBiddingPrice() {
        double currentPrice = this.startingBidPrice
        def maxBid
        def winnerBidding

        try {
             maxBid = Bidding.executeQuery("SELECT max(biddingPrice) from Bidding where listing.id = ?", [this.id])
        }
        catch(ex){
        }
        if(maxBid != null && maxBid[0] != null) {
            currentPrice = maxBid[0]
        }
        return currentPrice
    }

    int  numberOfBids
    private setNumberOfBids(int value) {
    }

    //2012.03.11 Vladimir Beliaev: num of bids
    public getNumberOfBids()
    {
        if (this.biddings == null) {
            return 0
        }
        else{
            return this.biddings.size()
        }
    }

}
