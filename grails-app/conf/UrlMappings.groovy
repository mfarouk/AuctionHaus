//History:
//2012.03.12 Vladimir Beliaev : make listing list main page
class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
        //2012.03.10
        // M-1: make listing list main page
 //     "/"(controller:"/listing")
        "/"	{
            controller	= 'listing'
            action		= { 'index' }
            view		= { 'list' }
        }

        //SRV-1: get customer
        "/api/customer/$id"  {
            controller	= 'api'
            action		= { 'customerGet' }
        }

        //SRV-1: post (save new) customer
        "/api/customer"  {
            controller	= 'api'
            action		= { 'customerPost' }
        }

        //SRV-2: get listing
        "/api/listing/$id"  {
            controller	= 'api'
            action		= { 'listingGet' }
        }

        //UI-1: get bids for listing
        "/api/listing/$id/biddings"  {
            controller	= 'api'
            action		= { 'biddingsForListingGet' }
        }
        "/listing/show/$id/biddings"  {
            controller	= 'api'
            action		= { 'biddingsForListingGet' }
        }

        //SRV-2: post (save new) listing
        "/api/listing"  {
            controller	= 'api'
            action		= { 'listingPost' }
        }

        //SRV-3: get bidding
        "/api/bidding/$id"  {
            controller	= 'api'
            action		= { 'biddingGet' }
        }

        //SRV-3: post (save new) bidding
        "/api/bidding"  {
            controller	= 'api'
            action		= { 'biddingPost' }
        }
		"500"(view:'/error')
	}
}
