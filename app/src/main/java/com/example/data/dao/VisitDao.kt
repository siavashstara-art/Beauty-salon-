package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.VisitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitDao {
    @Query("SELECT * FROM visits WHERE customerId = :customerId ORDER BY visitDate DESC")
    fun getVisitsForCustomer(customerId: Long): Flow<List<VisitEntity>>

    @Query("SELECT * FROM visits WHERE customerId = :customerId ORDER BY visitDate DESC LIMIT 1")
    suspend fun getLatestVisitForCustomer(customerId: Long): VisitEntity?

    @Query("SELECT * FROM visits ORDER BY visitDate DESC")
    fun getAllVisits(): Flow<List<VisitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: VisitEntity): Long

    @Delete
    suspend fun deleteVisit(visit: VisitEntity)
}
