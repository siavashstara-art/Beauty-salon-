package com.example.domain.model

enum class CustomerStatus {
    ACTIVE_CUSTOMER,
    INACTIVE_CUSTOMER
}

enum class FollowUpStatus {
    PENDING,
    CONTACTED,
    BOOKED,
    NOT_INTERESTED
}

data class CustomerRecoveryItem(
    val customer: Customer,
    val status: CustomerStatus,
    val daysSinceLastVisit: Long,
    val previousService: String,
    val suggestedNextVisitDate: Long?,
    val proposedOffer: String,
    val followUpStatus: FollowUpStatus = FollowUpStatus.PENDING
)

interface CustomerContactAction {
    fun generateMessage(item: CustomerRecoveryItem, salon: SalonProfile): String
    fun getWhatsAppUri(phone: String, text: String): String
    fun getSmsUri(phone: String, text: String): String
}
