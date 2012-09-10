package myebay1
//************************************************************
//            history
//
//2012.02.13 Vladimir Beliaev: initial implementation
//2012.02.14 Farouk Althlathini: Added constraints to pass the unit tests
//2012.02.14 Farouk Althltathini: Modified the password field to hide password characters
//2012.02.16 Vladimir Beliaev: merging
//2012.03.12 Vladimir Beliaev: add number of bids and number of listings readonly property to support
//                             requirement C-4

class Customer {
    static constraints = {
        password size:  6..8, blank: false, password: true
        email  email: true,  unique: true , blank: false
        dateCreated blank: false
        role(inList:["regular", "admin"])

    }
    static hasMany = [ biddings:Bidding]
    //TODO hasMany relationship [listings:Listing]  removed to handle Listing.winner property
    //TODO hasMany relationship [listings:Listing] shall be handled to provide cascading update/delete

    //property email
    String email
    String role = "regular"
    //property password
    String password
    //property dateCreated
    Date dateCreated
    Date lastUpdated
    

    @Override String  toString(){
        return this.email
    }

    //2012.03.11 Vladimir Beliaev
    //C-4: Customer can be deleted only if he has 0 bids and 0 listings
    int numberOfBids
    private setNumberOfBids(int value){

    }

    public getNumberOfBids(){
        List bids = Bidding.findAllByBidder(this)
        if   (bids == null){
            return 0
        }
        else{
            return bids.size()
        }
    }

    //2012.03.12 Vladimir Beliaev
    //C-4: Customer can be deleted only if he has 0 bids and 0 listings
    int numberOfListings
    private setNumberOfListings(int value){
    }

    public getNumberOfListings(){
        List listings = Listing.findAllBySeller(this)
        if   (listings == null){
            return 0
        }
        else{
            return listings.size()
        }

    }
}
    
    
    



