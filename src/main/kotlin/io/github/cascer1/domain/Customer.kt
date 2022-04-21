package io.github.cascer1.domain

import java.util.UUID

data class Customer(
    val id: UUID,
    var firstName: String?,
    var lastName: String?,
    var address: CustomerAddress? = null
)