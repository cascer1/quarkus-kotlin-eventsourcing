package io.github.cascer1.event

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.*

abstract class Event {
    @JsonProperty
    var uuid: UUID? = null
        set(value) {
            if (value == null) {
                throw IllegalArgumentException("Cannot set event ID to null")
            }

            if (field != null) {
                throw IllegalStateException("Cannot change existing event ID")
            }

            field = value
        }
    @JsonProperty
    val created: LocalDateTime = LocalDateTime.now()
}
