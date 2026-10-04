package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.SalonProfile

@Entity(tableName = "salon_profile")
data class SalonProfileEntity(
    @PrimaryKey
    val id: Long = 1L,
    val salonName: String,
    val managerName: String,
    val city: String,
    val phone: String,
    val whatsapp: String,
    val address: String,
    val logo: String = ""
) {
    fun toDomain(): SalonProfile = SalonProfile(
        id = id,
        salonName = salonName,
        managerName = managerName,
        city = city,
        phone = phone,
        whatsapp = whatsapp,
        address = address,
        logo = logo
    )

    companion object {
        fun fromDomain(profile: SalonProfile): SalonProfileEntity = SalonProfileEntity(
            id = profile.id,
            salonName = profile.salonName,
            managerName = profile.managerName,
            city = profile.city,
            phone = profile.phone,
            whatsapp = profile.whatsapp,
            address = profile.address,
            logo = profile.logo
        )
    }
}
