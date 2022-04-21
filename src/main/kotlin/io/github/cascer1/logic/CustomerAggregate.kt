package io.github.cascer1.logic

import io.github.cascer1.domain.Customer
import io.github.cascer1.event.CustomerEventStore
import io.github.cascer1.event.customer.CustomerChangeEvent
import io.github.cascer1.event.customer.CustomerCreateEvent
import io.github.cascer1.projection.customer.CustomerIdProjector
import io.github.cascer1.projection.customer.CustomerMapper
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class CustomerAggregate {
    @Inject
    protected lateinit var eventStore: CustomerEventStore

    @Inject
    protected lateinit var customerIdProjector: CustomerIdProjector

    fun createCustomer(firstName: String?, lastName: String?): UUID {
        val uuid = generateCustomerId()
        val events = listOf(CustomerCreateEvent(uuid, firstName, lastName))

        eventStore.save(events)

        return uuid
    }

    fun updateCustomer(id: UUID, firstName: String? = null, lastName: String? = null) {
        val events = eventStore.getEvents(id)

        if (events.isEmpty()) {
            throw IllegalStateException("Customer with ID $id does not exist")
        }

        val existingCustomer = CustomerMapper.buildCustomer(events)
        val changed = (firstName != existingCustomer.firstName || lastName != existingCustomer.lastName)

        if (!changed) {
            return
        }

        val event = CustomerChangeEvent(id, firstName, lastName)

        eventStore.save(listOf(event))
    }

    fun getCustomer(id: UUID): Customer? {
        val events = eventStore.getEvents(id)

        if (events.isEmpty()) {
            return null
        }

        return CustomerMapper.buildCustomer(events)
    }

    private fun generateCustomerId(): UUID {
        var generated = UUID.randomUUID()

        while (customerIdProjector.customerIdExists(generated)) {
            generated = UUID.randomUUID()
        }

        return generated
    }
}