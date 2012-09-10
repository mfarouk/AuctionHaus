package myebay1

class ListingService {

    def save(Listing listing) {
        listing.save(flush: true)
        return listing
    }
}
