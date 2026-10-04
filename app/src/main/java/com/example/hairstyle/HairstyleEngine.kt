package com.example.hairstyle

import com.example.domain.model.FaceProfile
import com.example.domain.model.FaceShape
import com.example.domain.model.Hairstyle
import com.example.domain.model.HairstyleRecommendation

class HairstyleEngine {

    fun recommendHairstyles(faceProfile: FaceProfile): List<HairstyleRecommendation> {
        val shape = faceProfile.estimatedFaceShape

        return HairstyleCatalog.allHairstyles.map { hairstyle ->
            val (score, reason) = evaluateSuitability(hairstyle, shape, faceProfile)
            HairstyleRecommendation(
                hairstyle = hairstyle,
                score = score,
                reason = reason
            )
        }.sortedByDescending { it.score }
    }

    private fun evaluateSuitability(
        hairstyle: Hairstyle,
        faceShape: FaceShape,
        faceProfile: FaceProfile
    ): Pair<Int, String> {
        // Base score: is this hairstyle in suitable shapes?
        val isDirectMatch = hairstyle.suitableFaceShapes.contains(faceShape)
        var score = if (isDirectMatch) 88 else 60

        // Additional contextual adjustments
        if (faceShape == FaceShape.OVAL) {
            score += 8 // Oval is versatile
        }

        // Adjust based on face proportions if detected
        if (faceProfile.faceDetected) {
            val ratio = if (faceProfile.faceWidth > 0) faceProfile.faceHeight / faceProfile.faceWidth else 1.2f
            if (ratio > 1.35f && (hairstyle.id == "hair_bob_classic" || hairstyle.id == "hair_curtain_bangs_lob")) {
                score += 6
            }
            if (ratio < 1.15f && (hairstyle.id == "hair_layered_long" || hairstyle.id == "hair_sleek_straight")) {
                score += 5
            }
        }

        score = score.coerceIn(40, 98)

        val reason = when {
            isDirectMatch && faceShape != FaceShape.UNKNOWN -> {
                "تطابق عالی با فرم چهره ${faceShape.titleFa}: ${hairstyle.tipsFa}"
            }
            faceShape == FaceShape.UNKNOWN -> {
                "مدل محبوب و سازگار با انواع استایل‌های سالنی: ${hairstyle.description}"
            }
            else -> {
                "با تنظیم قد و حجم‌دهی متناسب توسط هیر استایلیست قابل اجرا است: ${hairstyle.tipsFa}"
            }
        }

        return Pair(score, reason)
    }
}
