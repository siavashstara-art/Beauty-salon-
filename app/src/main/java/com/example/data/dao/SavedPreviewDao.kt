package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.SavedPreviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedPreviewDao {
    @Query("SELECT * FROM saved_previews ORDER BY timestamp DESC")
    fun getAllSavedPreviews(): Flow<List<SavedPreviewEntity>>

    @Query("SELECT * FROM saved_previews WHERE customerId = :customerId ORDER BY timestamp DESC")
    fun getPreviewsForCustomer(customerId: Long): Flow<List<SavedPreviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreview(preview: SavedPreviewEntity): Long

    @Delete
    suspend fun deletePreview(preview: SavedPreviewEntity)
}
