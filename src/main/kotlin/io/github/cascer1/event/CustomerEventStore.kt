package io.github.cascer1.event

import io.github.cascer1.event.customer.CustomerAddressChangeEvent
import io.github.cascer1.event.customer.CustomerChangeEvent
import io.github.cascer1.event.customer.CustomerCreateEvent
import io.github.cascer1.event.customer.CustomerEvent
import io.vertx.mutiny.core.eventbus.EventBus;
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import kotlin.collections.ArrayList

@ApplicationScoped
class CustomerEventStore {
    @Inject
    protected lateinit var bus: EventBus

    val events: ArrayList<CustomerEvent> = ArrayList()

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    fun save(events: List<CustomerEvent>) {
        events.forEach {
            it.uuid = generateUuid()
            this.events.add(it)
        }
        events.forEach {
            publishEvent(it)
        }
    }

    @Transactional(Transactional.TxType.MANDATORY)
    protected fun publishEvent(event: CustomerEvent) {
        when (event) {
            is CustomerCreateEvent -> {
                bus.publish("customerCreate", event)
            }
            is CustomerChangeEvent -> {
                bus.publish("customerChange", event)
            }
            is CustomerAddressChangeEvent -> {
                bus.publish("customerAddressChange", event)
            }
            else -> {
                throw IllegalArgumentException("unexpected event type")
            }
        }
    }

    fun getEvents(customerId: UUID): List<CustomerEvent> {
        return events.filter { it.customerId == customerId }.toList()
    }

    fun getEvents(predicate: (CustomerEvent) -> Boolean): List<CustomerEvent> {
        return events.filter(predicate).toList()
    }

    private fun generateUuid(): UUID {
        var uuid = UUID.randomUUID()

        while (events.any { it.uuid == uuid }) {
            uuid = UUID.randomUUID()
        }

        return uuid
    }
}