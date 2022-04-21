package io.github.cascer1.event.customer

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

class CustomerCreateEvent(
    @JsonProperty
    customerId: UUID,
    @JsonProperty
    val firstName: String?,
    @JsonProperty
    val lastName: String?
): CustomerEvent(customerId)