package io.github.cascer1.domain

data class CustomerAddress(
    var street: String? = null,
    var number: String? = null,
    var postalCode: String? = null,
    var place: String? = null,
    var country: String? = null
)
