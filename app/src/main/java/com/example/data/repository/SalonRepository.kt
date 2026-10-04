package com.example.data.repository

import com.example.data.dao.SalonProfileDao
import com.example.data.entity.SalonProfileEntity
import com.example.domain.model.SalonProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SalonRepository(private val salonDao: SalonProfileDao) {

    val salonProfile: Flow<SalonProfile> = salonDao.getSalonProfile().map { entity ->
        entity?.toDomain() ?: SalonProfile()
    }

    suspend fun getProfileOnce(): SalonProfile =
        salonDao.getSalonProfileOnce()?.toDomain() ?: SalonProfile()

    suspend fun updateSalonProfile(profile: SalonProfile) {
        salonDao.insertOrUpdate(SalonProfileEntity.fromDomain(profile))
    }
}
