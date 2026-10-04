package com.example.domain.model

enum class MakeupCategory(val titleFa: String, val titleEn: String) {
    NATURAL("طبیعی و نود (Natural)", "Natural"),
    EVERYDAY("روزمره و سبک", "Everyday"),
    PARTY("مجلسی و شب (Glam)", "Party / Glam"),
    BRIDAL("عروس رویایی (Bridal)", "Bridal"),
    CLASSIC("کلاسیک و رترو (Classic)", "Classic"),
    MODERN("مدرن و ترند (Modern)", "Modern")
}

data class MakeupStyle(
    val id: String,
    val nameFa: String,
    val nameEn: String,
    val category: MakeupCategory,
    val description: String,
    val previewAsset: String,
    val lipTone: String,
    val blushTone: String,
    val eyeStyle: String,
    val recommendedForFaceShapes: List<FaceShape> = FaceShape.entries
)

data class MakeupRecommendation(
    val makeupStyle: MakeupStyle,
    val matchScore: Int,
    val reasonFa: String
)
