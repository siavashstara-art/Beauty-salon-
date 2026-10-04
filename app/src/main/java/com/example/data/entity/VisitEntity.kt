package com.example.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.domain.model.Visit

@Entity(
    tableName = "visits",
    foreignKeys = [
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["customerId"])]
)
data class VisitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val customerId: Long,
    val visitDate: Long,
    val serviceProvided: String,
    val hairstyleChosen: String,
    val makeupChosen: String,
    val notes: String = "",
    val photoResultPath: String = "",
    val nextSuggestedVisit: Long? = null
) {
    fun toDomain(): Visit = Visit(
        id = id,
        customerId = customerId,
        visitDate = visitDate,
        serviceProvided = serviceProvided,
        hairstyleChosen = hairstyleChosen,
        makeupChosen = makeupChosen,
        notes = notes,
        photoResultPath = photoResultPath,
        nextSuggestedVisit = nextSuggestedVisit
    )

    companion object {
        fun fromDomain(visit: Visit): VisitEntity = VisitEntity(
            id = visit.id,
            customerId = visit.customerId,
            visitDate = visit.visitDate,
            serviceProvided = visit.serviceProvided,
            hairstyleChosen = visit.hairstyleChosen,
            makeupChosen = visit.makeupChosen,
            notes = visit.notes,
            photoResultPath = visit.photoResultPath,
            nextSuggestedVisit = visit.nextSuggestedVisit
        )
    }
}
