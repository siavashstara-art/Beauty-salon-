package com.example.makeup

import com.example.domain.model.FaceProfile
import com.example.domain.model.MakeupCategory
import com.example.domain.model.MakeupRecommendation
import com.example.domain.model.MakeupStyle

class MakeupEngine {

    fun recommendMakeup(
        faceProfile: FaceProfile,
        preferredCategory: MakeupCategory? = null
    ): List<MakeupRecommendation> {
        val shape = faceProfile.estimatedFaceShape

        return MakeupCatalog.allMakeupStyles.map { style ->
            val isCategoryMatch = preferredCategory == null || style.category == preferredCategory
            val isShapeMatch = style.recommendedForFaceShapes.contains(shape)

            var score = 75
            if (isCategoryMatch) score += 15
            if (isShapeMatch) score += 10
            if (faceProfile.lightingScore < 0.4f && style.category == MakeupCategory.NATURAL) {
                // Lower contrast style recommended in softer light
                score += 5
            }

            val reason = when {
                isShapeMatch -> "هارمونی کامل با کانتورینگ متناسب ${shape.titleFa} و پالت انتخابی."
                else -> "سبک جذاب و پرطرفدار سالن با امکان تنظیم توناژ لب و سایه."
            }

            MakeupRecommendation(
                makeupStyle = style,
                matchScore = score.coerceIn(50, 99),
                reasonFa = reason
            )
        }.sortedByDescending { it.matchScore }
    }
}
