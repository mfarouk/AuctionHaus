package myebay1

class BiddingService {

    def save(Bidding bidding) {
        bidding.save(flush: true)
        return bidding
    }
}
