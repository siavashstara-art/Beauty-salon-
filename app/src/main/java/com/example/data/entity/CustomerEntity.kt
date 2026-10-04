package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Customer

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val phone: String,
    val notes: String = "",
    val lastVisit: Long = System.currentTimeMillis(),
    val preferredHairstyle: String = "",
    val preferredMakeup: String = "",
    val photoReference: String = "",
    val totalVisits: Int = 1
) {
    fun toDomain(): Customer = Customer(
        id = id,
        name = name,
        phone = phone,
        notes = notes,
        lastVisit = lastVisit,
        preferredHairstyle = preferredHairstyle,
        preferredMakeup = preferredMakeup,
        photoReference = photoReference,
        totalVisits = totalVisits
    )

    companion object {
        fun fromDomain(customer: Customer): CustomerEntity = CustomerEntity(
            id = customer.id,
            name = customer.name,
            phone = customer.phone,
            notes = customer.notes,
            lastVisit = customer.lastVisit,
            preferredHairstyle = customer.preferredHairstyle,
            preferredMakeup = customer.preferredMakeup,
            photoReference = customer.photoReference,
            totalVisits = customer.totalVisits
        )
    }
}
