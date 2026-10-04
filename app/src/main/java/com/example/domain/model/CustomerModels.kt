package com.example.domain.model

data class Customer(
    val id: Long = 0L,
    val name: String,
    val phone: String,
    val notes: String = "",
    val lastVisit: Long = System.currentTimeMillis(),
    val preferredHairstyle: String = "",
    val preferredMakeup: String = "",
    val photoReference: String = "",
    val totalVisits: Int = 1
)

data class Visit(
    val id: Long = 0L,
    val customerId: Long,
    val visitDate: Long = System.currentTimeMillis(),
    val serviceProvided: String,
    val hairstyleChosen: String = "",
    val makeupChosen: String = "",
    val notes: String = "",
    val photoResultPath: String = "",
    val nextSuggestedVisit: Long? = null
)
