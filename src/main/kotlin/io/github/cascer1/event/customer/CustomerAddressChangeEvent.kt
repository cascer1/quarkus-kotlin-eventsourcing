package io.github.cascer1.event.customer

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

class CustomerAddressChangeEvent(
    customerId: UUID,
    @JsonProperty
    val street: String?,
    @JsonProperty
    val number: String?,
    @JsonProperty
    val postalCode: String?,
    @JsonProperty
    val place: String?,
    @JsonProperty
    val country: String?
): CustomerEvent(customerId)