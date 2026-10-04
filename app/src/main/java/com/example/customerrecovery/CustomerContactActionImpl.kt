package com.example.customerrecovery

import android.net.Uri
import com.example.domain.model.CustomerContactAction
import com.example.domain.model.CustomerRecoveryItem
import com.example.domain.model.SalonProfile
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class CustomerContactActionImpl : CustomerContactAction {

    override fun generateMessage(item: CustomerRecoveryItem, salon: SalonProfile): String {
        val customerName = item.customer.name
        val days = item.daysSinceLastVisit
        val salonName = salon.salonName

        return """
            سلام $customerName عزیز، وقت بخیر 🌸
            از آخرین دیدار ما در «$salonName» حدود $days روز گذشته است.
            ${item.proposedOffer}
            
            خوشحال می‌شویم برای رزرو نوبت یا مشاوره مجدد با ما در تماس باشید:
            📞 ${salon.phone}
            📍 ${salon.address}
        """.trimIndent()
    }

    override fun getWhatsAppUri(phone: String, text: String): String {
        // Formats phone to international if needed and prepares WhatsApp Click-to-Chat URI
        val cleanPhone = phone.replace("+", "").replace(" ", "").replace("-", "")
        val formattedPhone = if (cleanPhone.startsWith("0")) "98" + cleanPhone.substring(1) else cleanPhone
        val encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString())
        return "https://api.whatsapp.com/send?phone=$formattedPhone&text=$encodedText"
    }

    override fun getSmsUri(phone: String, text: String): String {
        val encodedText = Uri.encode(text)
        return "sms:$phone?body=$encodedText"
    }
}
