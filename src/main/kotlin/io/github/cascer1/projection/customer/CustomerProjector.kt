package io.github.cascer1.projection.customer

import io.github.cascer1.domain.Customer
import io.github.cascer1.event.customer.CustomerAddressChangeEvent
import io.github.cascer1.event.customer.CustomerChangeEvent
import io.github.cascer1.event.customer.CustomerCreateEvent
import io.github.cascer1.event.customer.CustomerEvent
import io.quarkus.vertx.ConsumeEvent
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CustomerProjector {

    private val customers: HashMap<UUID, Customer> = HashMap()

    fun getCustomer(id: UUID): Customer? {
        return customers[id]?.copy()
    }

    @ConsumeEvent("customerCreate")
    protected fun handleEvent(event: CustomerCreateEvent) {
        println("Received customer create event: ${event.uuid}")
        requireNewCustomer(event)
        customers[event.customerId] = CustomerMapper.buildCustomer(event)
    }

    @ConsumeEvent("customerChange")
    protected fun handleEvent(event: CustomerChangeEvent) {
        println("Received customer change event: ${event.uuid}")
        val customer = requireExistingCustomer(event)

        customers[event.customerId] = CustomerMapper.applyEvent(customer, event)
    }

    @ConsumeEvent("customerAddressChange")
    protected fun handleEvent(event: CustomerAddressChangeEvent) {
        println("Received customer address change event: ${event.uuid}")
        val customer = requireExistingCustomer(event)

        customers[event.customerId] = CustomerMapper.applyEvent(customer, event)
    }

    private fun requireNewCustomer(event: CustomerEvent) {
        if (customers.containsKey(event.customerId)) {
            throw IllegalStateException("Event ${event.uuid} requires new customer, but customer with ID ${event.customerId} already exists.")
        }
    }

    private fun requireExistingCustomer(event: CustomerEvent): Customer {
        return customers[event.customerId]
            ?: throw IllegalStateException("Event ${event.uuid} requires existing customer, but no customer with ID ${event.customerId} is found.")
    }
}