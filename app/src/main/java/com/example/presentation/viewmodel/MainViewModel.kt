package com.example.presentation.viewmodel

import android.app.Application
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.customerrecovery.CustomerContactActionImpl
import com.example.customerrecovery.CustomerRecoveryEngine
import com.example.data.TavanaBeautyDatabase
import com.example.data.entity.SavedPreviewEntity
import com.example.data.repository.CustomerRepository
import com.example.data.repository.PreviewRepository
import com.example.data.repository.SalonRepository
import com.example.data.repository.VisitRepository
import com.example.domain.model.Customer
import com.example.domain.model.CustomerRecoveryItem
import com.example.domain.model.CustomerStatus
import com.example.domain.model.FaceProfile
import com.example.domain.model.Hairstyle
import com.example.domain.model.HairstyleRecommendation
import com.example.domain.model.MakeupRecommendation
import com.example.domain.model.MakeupStyle
import com.example.domain.model.PreviewRequest
import com.example.domain.model.PreviewResult
import com.example.domain.model.SalonProfile
import com.example.domain.model.Visit
import com.example.faceanalysis.FaceAnalysisEngine
import com.example.hairstyle.HairstyleCatalog
import com.example.hairstyle.HairstyleEngine
import com.example.makeup.MakeupCatalog
import com.example.makeup.MakeupEngine
import com.example.preview.PreviewEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

enum class ConsultationStep {
    SELECT_PHOTO,
    FACE_ANALYSIS,
    HAIRSTYLE_SELECTION,
    MAKEUP_SELECTION,
    PREVIEW_STUDIO,
    SAVE_CONFIRMATION
}

data class ConsultationState(
    val step: ConsultationStep = ConsultationStep.SELECT_PHOTO,
    val photoPath: String? = null,
    val isAnalyzing: Boolean = false,
    val faceProfile: FaceProfile? = null,
    val hairstyleRecommendations: List<HairstyleRecommendation> = emptyList(),
    val selectedHairstyle: Hairstyle? = null,
    val makeupRecommendations: List<MakeupRecommendation> = emptyList(),
    val selectedMakeup: MakeupStyle? = null,
    val isGeneratingPreview: Boolean = false,
    val previewResult: PreviewResult? = null,
    val savedCustomerId: Long? = null,
    val isSavedSuccess: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = TavanaBeautyDatabase.getDatabase(application)
    val customerRepository = CustomerRepository(db.customerDao())
    val salonRepository = SalonRepository(db.salonProfileDao())
    val visitRepository = VisitRepository(db.visitDao())
    val previewRepository = PreviewRepository(db.savedPreviewDao())

    private val faceAnalysisEngine = FaceAnalysisEngine()
    private val hairstyleEngine = HairstyleEngine()
    private val makeupEngine = MakeupEngine()
    private val previewEngine = PreviewEngine(application)
    private val recoveryEngine = CustomerRecoveryEngine()
    val contactAction = CustomerContactActionImpl()

    // Salon Profile State
    val salonProfile: StateFlow<SalonProfile> = salonRepository.salonProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SalonProfile())

    // Customers State
    val allCustomers: StateFlow<List<Customer>> = customerRepository.allCustomers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Visits
    val allVisits: StateFlow<List<Visit>> = visitRepository.allVisits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Customer Recovery Items (Active vs Inactive)
    val recoveryItems: StateFlow<List<CustomerRecoveryItem>> = combine(
        allCustomers,
        allVisits
    ) { customers, visits ->
        customers.map { customer ->
            val latestVisit = visits.filter { it.customerId == customer.id }
                .maxByOrNull { it.visitDate }
            recoveryEngine.evaluateCustomer(customer, latestVisit)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Consultation Workflow State
    private val _consultationState = MutableStateFlow(ConsultationState())
    val consultationState: StateFlow<ConsultationState> = _consultationState.asStateFlow()

    fun startNewConsultation(initialPhotoPath: String? = null) {
        _consultationState.value = ConsultationState(
            step = if (initialPhotoPath != null) ConsultationStep.FACE_ANALYSIS else ConsultationStep.SELECT_PHOTO,
            photoPath = initialPhotoPath
        )
        if (initialPhotoPath != null) {
            runFaceAnalysis(initialPhotoPath)
        }
    }

    fun onPhotoSelected(path: String) {
        _consultationState.update {
            it.copy(
                photoPath = path,
                step = ConsultationStep.FACE_ANALYSIS
            )
        }
        runFaceAnalysis(path)
    }

    private fun runFaceAnalysis(photoPath: String) {
        viewModelScope.launch {
            _consultationState.update { it.copy(isAnalyzing = true) }
            val bitmap = BitmapFactory.decodeFile(photoPath)
            if (bitmap != null) {
                val profile = faceAnalysisEngine.analyzeFace(bitmap)
                val hairRecs = hairstyleEngine.recommendHairstyles(profile)
                val makeupRecs = makeupEngine.recommendMakeup(profile)

                _consultationState.update {
                    it.copy(
                        isAnalyzing = false,
                        faceProfile = profile,
                        hairstyleRecommendations = hairRecs,
                        selectedHairstyle = hairRecs.firstOrNull()?.hairstyle,
                        makeupRecommendations = makeupRecs,
                        selectedMakeup = makeupRecs.firstOrNull()?.makeupStyle
                    )
                }
            } else {
                _consultationState.update { it.copy(isAnalyzing = false) }
            }
        }
    }

    fun selectHairstyle(hairstyle: Hairstyle) {
        _consultationState.update { it.copy(selectedHairstyle = hairstyle) }
    }

    fun selectMakeup(makeup: MakeupStyle) {
        _consultationState.update { it.copy(selectedMakeup = makeup) }
    }

    fun goToStep(step: ConsultationStep) {
        _consultationState.update { it.copy(step = step) }
    }

    fun generateStudioPreview() {
        val state = _consultationState.value
        val path = state.photoPath ?: return
        val hair = state.selectedHairstyle ?: HairstyleCatalog.allHairstyles.first()
        val makeup = state.selectedMakeup ?: MakeupCatalog.allMakeupStyles.first()
        val profile = state.faceProfile ?: FaceProfile(faceDetected = false)

        viewModelScope.launch {
            _consultationState.update { it.copy(isGeneratingPreview = true) }
            val request = PreviewRequest(
                originalImagePath = path,
                selectedHairstyle = hair,
                selectedMakeup = makeup,
                faceProfile = profile
            )
            val result = previewEngine.generatePreview(request)
            _consultationState.update {
                it.copy(
                    isGeneratingPreview = false,
                    previewResult = result,
                    step = ConsultationStep.PREVIEW_STUDIO
                )
            }
        }
    }

    fun saveConsultation(
        clientName: String,
        clientPhone: String,
        notes: String,
        scheduleNextVisitDays: Int
    ) {
        val state = _consultationState.value
        val hair = state.selectedHairstyle ?: return
        val makeup = state.selectedMakeup ?: return
        val result = state.previewResult

        viewModelScope.launch {
            // Find existing or create new customer
            val existing = allCustomers.value.find { it.phone == clientPhone }
            val customerId = if (existing != null) {
                customerRepository.updateCustomer(
                    existing.copy(
                        name = clientName.ifBlank { existing.name },
                        preferredHairstyle = hair.nameFa,
                        preferredMakeup = makeup.nameFa,
                        photoReference = result?.previewImage ?: state.photoPath.orEmpty(),
                        lastVisit = System.currentTimeMillis(),
                        totalVisits = existing.totalVisits + 1,
                        notes = if (notes.isNotBlank()) "${existing.notes}\n$notes" else existing.notes
                    )
                )
                existing.id
            } else {
                customerRepository.saveCustomer(
                    Customer(
                        name = clientName.ifBlank { "مشتری جدید" },
                        phone = clientPhone,
                        preferredHairstyle = hair.nameFa,
                        preferredMakeup = makeup.nameFa,
                        photoReference = result?.previewImage ?: state.photoPath.orEmpty(),
                        notes = notes,
                        lastVisit = System.currentTimeMillis(),
                        totalVisits = 1
                    )
                )
            }

            // Save Visit Record
            val nextVisitMillis = if (scheduleNextVisitDays > 0) {
                System.currentTimeMillis() + TimeUnit.DAYS.toMillis(scheduleNextVisitDays.toLong())
            } else null

            visitRepository.saveVisit(
                Visit(
                    customerId = customerId,
                    visitDate = System.currentTimeMillis(),
                    serviceProvided = "مشاوره زیبایی و انتخاب استایل",
                    hairstyleChosen = hair.nameFa,
                    makeupChosen = makeup.nameFa,
                    notes = notes,
                    photoResultPath = result?.previewImage.orEmpty(),
                    nextSuggestedVisit = nextVisitMillis
                )
            )

            // Save Preview Entity
            if (result != null) {
                previewRepository.savePreview(
                    SavedPreviewEntity(
                        customerId = customerId,
                        customerName = clientName,
                        originalImagePath = result.originalImage,
                        previewImagePath = result.previewImage,
                        hairstyleId = hair.id,
                        hairstyleNameFa = hair.nameFa,
                        makeupId = makeup.id,
                        makeupNameFa = makeup.nameFa,
                        faceShape = state.faceProfile?.estimatedFaceShape?.name ?: "UNKNOWN",
                        generatedBy = result.generatedBy.name
                    )
                )
            }

            _consultationState.update {
                it.copy(
                    savedCustomerId = customerId,
                    isSavedSuccess = true,
                    step = ConsultationStep.SAVE_CONFIRMATION
                )
            }
        }
    }

    fun updateSalonProfile(updated: SalonProfile) {
        viewModelScope.launch {
            salonRepository.updateSalonProfile(updated)
        }
    }

    fun createCustomerQuick(name: String, phone: String, notes: String) {
        viewModelScope.launch {
            customerRepository.saveCustomer(
                Customer(
                    name = name,
                    phone = phone,
                    notes = notes,
                    lastVisit = System.currentTimeMillis()
                )
            )
        }
    }
}
