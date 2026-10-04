package com.example.data.repository

import com.example.data.dao.SavedPreviewDao
import com.example.data.entity.SavedPreviewEntity
import kotlinx.coroutines.flow.Flow

class PreviewRepository(private val previewDao: SavedPreviewDao) {

    val allSavedPreviews: Flow<List<SavedPreviewEntity>> = previewDao.getAllSavedPreviews()

    fun getPreviewsForCustomer(customerId: Long): Flow<List<SavedPreviewEntity>> =
        previewDao.getPreviewsForCustomer(customerId)

    suspend fun savePreview(preview: SavedPreviewEntity): Long =
        previewDao.insertPreview(preview)

    suspend fun deletePreview(preview: SavedPreviewEntity) =
        previewDao.deletePreview(preview)
}
