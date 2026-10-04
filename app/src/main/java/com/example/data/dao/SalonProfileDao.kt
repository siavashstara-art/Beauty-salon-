package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.SalonProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SalonProfileDao {
    @Query("SELECT * FROM salon_profile WHERE id = 1 LIMIT 1")
    fun getSalonProfile(): Flow<SalonProfileEntity?>

    @Query("SELECT * FROM salon_profile WHERE id = 1 LIMIT 1")
    suspend fun getSalonProfileOnce(): SalonProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: SalonProfileEntity)

    @Update
    suspend fun update(profile: SalonProfileEntity)
}
