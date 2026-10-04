package com.example.domain.model

enum class HairstyleCategory(val titleFa: String, val titleEn: String) {
    SHORT("کوتاه", "Short"),
    MEDIUM("متوسط", "Medium"),
    LONG("بلند", "Long"),
    BOB("مدل باب (Bob)", "Bob"),
    LAYERED("لایه‌ای و پرحجم", "Layered"),
    CURLY("فر و مواج", "Curly & Wavy"),
    STRAIGHT("لخت و شلاقی", "Straight"),
    UPDO("شینیون و جمع", "Updo"),
    BRIDAL("عروس و مجلسی خاص", "Bridal")
}

data class Hairstyle(
    val id: String,
    val nameFa: String,
    val nameEn: String,
    val category: HairstyleCategory,
    val length: String, // e.g. "کوتاه", "تا روی شانه", "بلند"
    val suitableFaceShapes: List<FaceShape>,
    val description: String,
    val previewAsset: String, // asset identifier or icon name
    val tipsFa: String = ""
)

data class HairstyleRecommendation(
    val hairstyle: Hairstyle,
    val score: Int, // 0 to 100
    val reason: String
)
