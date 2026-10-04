package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.GeneratedBy

@Entity(tableName = "saved_previews")
data class SavedPreviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val customerId: Long? = null,
    val customerName: String = "",
    val originalImagePath: String,
    val previewImagePath: String,
    val hairstyleId: String,
    val hairstyleNameFa: String,
    val makeupId: String,
    val makeupNameFa: String,
    val faceShape: String,
    val generatedBy: String = GeneratedBy.LOCAL_PROCESSING.name,
    val timestamp: Long = System.currentTimeMillis()
)
