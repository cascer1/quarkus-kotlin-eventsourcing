package io.github.cascer1.projection.customer

import io.github.cascer1.event.customer.CustomerCreateEvent
import io.quarkus.vertx.ConsumeEvent
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CustomerIdProjector {
    private val customerIds: HashSet<UUID> = HashSet()

    @ConsumeEvent("customerCreate")
    fun handleCustomerCreateEvent(event: CustomerCreateEvent) {
        println("Received customer create event: ${event.uuid}")
        customerIds.add(event.customerId)
    }

    fun customerIdExists(id: UUID): Boolean {
        return customerIds.contains(id)
    }
}