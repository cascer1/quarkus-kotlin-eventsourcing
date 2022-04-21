package io.github.cascer1.projection.customer

import io.github.cascer1.domain.Customer
import io.github.cascer1.domain.CustomerAddress
import io.github.cascer1.event.customer.CustomerAddressChangeEvent
import io.github.cascer1.event.customer.CustomerChangeEvent
import io.github.cascer1.event.customer.CustomerCreateEvent
import io.github.cascer1.event.customer.CustomerEvent

object CustomerMapper {

    fun buildCustomer(events: List<CustomerEvent>): Customer {
        if (events.isEmpty()) {
            throw IllegalArgumentException("Cannot create customer with zero events")
        }

        val firstEvent = events.first();

        if (firstEvent !is CustomerCreateEvent) {
            throw IllegalArgumentException("First customer event must be create")
        }

        var customer = buildCustomer(firstEvent)

        events.drop(1).forEach { customer = applyEvent(customer, it) }

        return customer
    }

    fun buildCustomer(event: CustomerCreateEvent): Customer {
        return Customer(event.customerId, event.firstName, event.lastName)
    }

    fun applyEvent(customer: Customer, event: CustomerEvent): Customer {
        return when (event) {
            is CustomerCreateEvent -> {
                throw IllegalArgumentException("Cannot apply create event to existing customer")
            }
            is CustomerChangeEvent -> {
                applyEvent(customer, event)
            }
            is CustomerAddressChangeEvent -> {
                applyEvent(customer, event)
            }
            else -> {
                throw IllegalArgumentException("Unsupported event type: ${event.javaClass}")
            }
        }
    }

    fun applyEvent(customer: Customer, event: CustomerChangeEvent): Customer {
        val newCustomer = customer.copy()

        event.firstName?.let {
            newCustomer.firstName = it
        }

        event.lastName?.let {
            newCustomer.lastName = it
        }

        return newCustomer
    }

    fun applyEvent(customer: Customer, event: CustomerAddressChangeEvent): Customer {
        val newCustomer = customer.copy()
        val address = newCustomer.address?.copy() ?: CustomerAddress()


        event.street?.let {
            address.street = it
        }

        event.number?.let {
            address.number = it
        }

        event.postalCode?.let {
            address.postalCode = it
        }

        event.place?.let {
            address.place = it
        }

        event.country?.let {
            address.country = it
        }

        newCustomer.address = address

        return newCustomer
    }
}