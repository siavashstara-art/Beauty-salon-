package com.example.domain.model

data class SalonProfile(
    val id: Long = 1L,
    val salonName: String = "استودیو زیبایی توانا",
    val managerName: String = "سرکار خانم توانا",
    val city: String = "تهران",
    val phone: String = "02188000000",
    val whatsapp: String = "+989120000000",
    val address: String = "بلوار اندرزگو، مجتمع زیبایی توانا",
    val logo: String = ""
)
