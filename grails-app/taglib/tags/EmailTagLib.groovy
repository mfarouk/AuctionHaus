package tags

/**
 * Created by IntelliJ IDEA.
 * User: vbeliaev
 * Date: 3/18/12
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
class EmailTagLib {
    static namespace = "el"

    def renderName = {attrs ->
        if (attrs.seller){
            out << """<span class="property-value" aria-labelledby="seller-label">${parseNameFromEmail(attrs.seller.email)}</span>"""
        }
    }
    
    private String parseNameFromEmail (String email) {
        int index = email.indexOf('@')
        String result = email.substring(0,index)
        return result
    }


}
