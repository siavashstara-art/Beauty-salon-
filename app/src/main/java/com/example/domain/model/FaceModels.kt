package com.example.domain.model

import android.graphics.PointF
import android.graphics.RectF

enum class FaceShape(val titleFa: String, val titleEn: String, val descriptionFa: String) {
    OVAL(
        titleFa = "بیضی (Oval)",
        titleEn = "Oval",
        descriptionFa = "فرم ایده‌آل و متقارن، تناسب عالی طول و عرض چهره."
    ),
    ROUND(
        titleFa = "گرد (Round)",
        titleEn = "Round",
        descriptionFa = "عرض و طول چهره نزدیک به هم با گونه‌های پر و زاویه فک نرم."
    ),
    SQUARE(
        titleFa = "مربعی (Square)",
        titleEn = "Square",
        descriptionFa = "خط فک مشخص و قدرتمند، عرض پیشانی و فک تقریباً برابر."
    ),
    HEART(
        titleFa = "قلبی (Heart)",
        titleEn = "Heart",
        descriptionFa = "پیشانی پهن‌تر، چانه کشیده و ظریف با انحنای زیبای گونه."
    ),
    LONG(
        titleFa = "کشیده / مستطیلی (Oblong/Long)",
        titleEn = "Long",
        descriptionFa = "طول چهره به وضوح بیشتر از عرض با پیشانی و گونه‌های کشیده."
    ),
    UNKNOWN(
        titleFa = "نامشخص / نیاز به عکس واضح‌تر",
        titleEn = "Unknown",
        descriptionFa = "زاویه یا نور برای تخمین قطعی کافی نبوده است."
    )
}

data class FaceProfile(
    val faceDetected: Boolean,
    val faceBounds: RectF? = null,
    val faceAngle: Float = 0f,
    val eyeDistance: Float = 0f,
    val eyeLeft: PointF? = null,
    val eyeRight: PointF? = null,
    val mouthPosition: PointF? = null,
    val nosePosition: PointF? = null,
    val faceWidth: Float = 0f,
    val faceHeight: Float = 0f,
    val lightingScore: Float = 0.8f, // 0.0 to 1.0
    val imageQuality: String = "مناسب",
    val estimatedFaceShape: FaceShape = FaceShape.UNKNOWN,
    val confidence: Float = 0.5f,
    val disclaimer: String = "تحلیل فرم صورت بر اساس هندسه دوبعدی عکس انجام شده و صرفاً راهنمای مشاوره زیبایی است، نه تشخیص پزشکی یا علمی قطعی."
)
