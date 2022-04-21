package io.github.cascer1.logic

import io.github.cascer1.projection.customer.CustomerProjector
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import javax.inject.Inject

@QuarkusTest
internal class CustomerAggregateTest {
    @Inject
    lateinit var aggregate: CustomerAggregate

    @Inject
    lateinit var projector: CustomerProjector

    @Test
    fun createCustomer() {
        val firstName = "Billy"
        val lastName = "Tables"

        val createdCustomerUuid = aggregate.createCustomer(firstName, lastName)

        Thread.sleep(100)

        val createdCustomer = projector.getCustomer(createdCustomerUuid)
        assertNotNull(createdCustomer)
        assertEquals(firstName, createdCustomer!!.firstName)
        assertEquals(lastName, createdCustomer.lastName)
    }

    @Test
    fun updateCustomer() {
        val firstName = "Billy"
        val lastName = "Tables"

        val customerUUID = aggregate.createCustomer(firstName, lastName)

        val newFirstName = "Jill"
        val newLastName = "DocumentStores"

        aggregate.updateCustomer(customerUUID, newFirstName, newLastName)

        Thread.sleep(100)

        val changedCustomer = projector.getCustomer(customerUUID)

        assertNotNull(changedCustomer)
        assertEquals(newFirstName, changedCustomer!!.firstName)
        assertEquals(newLastName, changedCustomer.lastName)
    }

    @Test
    fun getCustomer() {
        val createdCustomerUuid = aggregate.createCustomer("a", "b")
        aggregate.updateCustomer(createdCustomerUuid, "c", "d")
        aggregate.updateCustomer(createdCustomerUuid, "e", "f")

        val finalCustomer = aggregate.getCustomer(createdCustomerUuid)

        assertNotNull(finalCustomer)
        assertEquals("e", finalCustomer!!.firstName)
        assertEquals("f", finalCustomer.lastName)
    }
}