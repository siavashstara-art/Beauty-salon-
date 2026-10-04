package com.example.data.repository

import com.example.data.dao.VisitDao
import com.example.data.entity.VisitEntity
import com.example.domain.model.Visit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VisitRepository(private val visitDao: VisitDao) {

    fun getVisitsForCustomer(customerId: Long): Flow<List<Visit>> =
        visitDao.getVisitsForCustomer(customerId).map { list -> list.map { it.toDomain() } }

    val allVisits: Flow<List<Visit>> =
        visitDao.getAllVisits().map { list -> list.map { it.toDomain() } }

    suspend fun getLatestVisit(customerId: Long): Visit? =
        visitDao.getLatestVisitForCustomer(customerId)?.toDomain()

    suspend fun saveVisit(visit: Visit): Long =
        visitDao.insertVisit(VisitEntity.fromDomain(visit))

    suspend fun deleteVisit(visit: Visit) =
        visitDao.deleteVisit(VisitEntity.fromDomain(visit))
}
