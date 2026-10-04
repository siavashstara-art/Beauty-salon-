package com.example.domain.model

enum class GeneratedBy {
    LOCAL_TEMPLATE,
    LOCAL_PROCESSING,
    AI_ENGINE
}

data class PreviewRequest(
    val originalImagePath: String,
    val selectedHairstyle: Hairstyle,
    val selectedMakeup: MakeupStyle,
    val faceProfile: FaceProfile
)

data class PreviewResult(
    val originalImage: String,
    val selectedHairstyle: Hairstyle,
    val selectedMakeup: MakeupStyle,
    val previewImage: String,
    val generatedBy: GeneratedBy,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
