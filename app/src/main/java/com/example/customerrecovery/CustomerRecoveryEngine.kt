package com.example.customerrecovery

import com.example.domain.model.Customer
import com.example.domain.model.CustomerRecoveryItem
import com.example.domain.model.CustomerStatus
import com.example.domain.model.FollowUpStatus
import com.example.domain.model.Visit
import java.util.concurrent.TimeUnit

class CustomerRecoveryEngine {

    // A customer without a visit for more than 35 days is considered INACTIVE
    private val inactivityThresholdDays = 35L

    fun evaluateCustomer(
        customer: Customer,
        lastVisit: Visit?,
        currentTimestamp: Long = System.currentTimeMillis()
    ): CustomerRecoveryItem {
        val lastDate = lastVisit?.visitDate ?: customer.lastVisit
        val diffMillis = (currentTimestamp - lastDate).coerceAtLeast(0L)
        val daysPassed = TimeUnit.MILLISECONDS.toDays(diffMillis)

        val status = if (daysPassed >= inactivityThresholdDays) {
            CustomerStatus.INACTIVE_CUSTOMER
        } else {
            CustomerStatus.ACTIVE_CUSTOMER
        }

        val previousService = lastVisit?.serviceProvided ?: "مشاوره و استایل چهره"

        val suggestedNextDate = lastVisit?.nextSuggestedVisit
            ?: (lastDate + TimeUnit.DAYS.toMillis(45L))

        val offer = when {
            daysPassed > 60 -> "پیشنهاد ویژه: احیا و ترمیم مو با ۲۰٪ تخفیف وفاداری به همراه ماساژ رایگان پوست سر"
            daysPassed > 35 -> "یادآوری دوره ریشه‌گیری، ویتامینه یا تمدید فرم کوتاهی ژورنالی"
            else -> "پیگیری رضایت از خدمات قبلی و رزرو مشاوره استایل فصل جدید"
        }

        return CustomerRecoveryItem(
            customer = customer,
            status = status,
            daysSinceLastVisit = daysPassed,
            previousService = previousService,
            suggestedNextVisitDate = suggestedNextDate,
            proposedOffer = offer,
            followUpStatus = FollowUpStatus.PENDING
        )
    }
}
