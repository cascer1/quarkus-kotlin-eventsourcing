package io.github.cascer1.event.customer

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.cascer1.event.Event
import java.util.UUID

abstract class CustomerEvent(
    @JsonProperty
    val customerId: UUID
): Event()