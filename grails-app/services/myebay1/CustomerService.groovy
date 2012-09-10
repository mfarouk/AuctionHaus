package myebay1

class CustomerService {

    def save(Customer customer) {
        customer.save(flush: true)
        return customer
    }
}

