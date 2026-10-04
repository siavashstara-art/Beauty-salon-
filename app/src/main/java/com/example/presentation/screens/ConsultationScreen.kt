package com.example.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.camera.copyUriToLocalCache
import com.example.domain.model.FaceProfile
import com.example.domain.model.FaceShape
import com.example.domain.model.Hairstyle
import com.example.domain.model.HairstyleRecommendation
import com.example.domain.model.MakeupRecommendation
import com.example.domain.model.MakeupStyle
import com.example.presentation.viewmodel.ConsultationState
import com.example.presentation.viewmodel.ConsultationStep
import com.example.presentation.viewmodel.MainViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultationScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onOpenLiveCamera: () -> Unit,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val state by viewModel.consultationState.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val localPath = copyUriToLocalCache(context, uri)
            if (localPath != null) {
                viewModel.onPhotoSelected(localPath)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "مشاوره زیبایی و انتخاب استایل",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("consultation_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Steps Progress Bar Indicator
            ConsultationStepHeader(currentStep = state.step)

            when (state.step) {
                ConsultationStep.SELECT_PHOTO -> {
                    PhotoSourceSelectionStep(
                        onCameraClick = onOpenLiveCamera,
                        onGalleryClick = {
                            galleryLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )
                }

                ConsultationStep.FACE_ANALYSIS -> {
                    FaceAnalysisResultStep(
                        state = state,
                        onProceedToHairstyle = {
                            viewModel.goToStep(ConsultationStep.HAIRSTYLE_SELECTION)
                        },
                        onRetake = {
                            viewModel.goToStep(ConsultationStep.SELECT_PHOTO)
                        }
                    )
                }

                ConsultationStep.HAIRSTYLE_SELECTION -> {
                    HairstyleSelectionStep(
                        recommendations = state.hairstyleRecommendations,
                        selectedHairstyle = state.selectedHairstyle,
                        onSelect = { viewModel.selectHairstyle(it) },
                        onNext = { viewModel.goToStep(ConsultationStep.MAKEUP_SELECTION) },
                        onBackStep = { viewModel.goToStep(ConsultationStep.FACE_ANALYSIS) }
                    )
                }

                ConsultationStep.MAKEUP_SELECTION -> {
                    MakeupSelectionStep(
                        recommendations = state.makeupRecommendations,
                        selectedMakeup = state.selectedMakeup,
                        onSelect = { viewModel.selectMakeup(it) },
                        onNext = { viewModel.generateStudioPreview() },
                        onBackStep = { viewModel.goToStep(ConsultationStep.HAIRSTYLE_SELECTION) },
                        isGeneratingPreview = state.isGeneratingPreview
                    )
                }

                ConsultationStep.PREVIEW_STUDIO -> {
                    StudioPreviewStep(
                        state = state,
                        onProceedToSave = { viewModel.goToStep(ConsultationStep.SAVE_CONFIRMATION) },
                        onBackStep = { viewModel.goToStep(ConsultationStep.MAKEUP_SELECTION) }
                    )
                }

                ConsultationStep.SAVE_CONFIRMATION -> {
                    SaveConsultationStep(
                        state = state,
                        onSave = { name, phone, notes, days ->
                            viewModel.saveConsultation(name, phone, notes, days)
                        },
                        onFinished = onFinished
                    )
                }
            }
        }
    }
}

@Composable
fun ConsultationStepHeader(currentStep: ConsultationStep) {
    val steps = listOf(
        "۱. عکس",
        "۲. چهره",
        "۳. مو",
        "۴. آرایش",
        "۵. استودیو",
        "۶. ثبت"
    )

    val stepIndex = when (currentStep) {
        ConsultationStep.SELECT_PHOTO -> 0
        ConsultationStep.FACE_ANALYSIS -> 1
        ConsultationStep.HAIRSTYLE_SELECTION -> 2
        ConsultationStep.MAKEUP_SELECTION -> 3
        ConsultationStep.PREVIEW_STUDIO -> 4
        ConsultationStep.SAVE_CONFIRMATION -> 5
    }

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            steps.forEachIndexed { index, title ->
                val isActive = index == stepIndex
                val isDone = index < stepIndex
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    color = when {
                        isActive -> MaterialTheme.colorScheme.primary
                        isDone -> Color(0xFF2E7D32)
                        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    }
                )
            }
        }
    }
}

@Composable
fun PhotoSourceSelectionStep(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFD9E0)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "شروع مشاوره تخصصی زیبایی",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "برای تحلیل فرم صورت و پیشنهاد دقیق مدل مو و آرایش، یک عکس واضح و روبرو از چهره مشتری وارد کنید.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = onCameraClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("take_photo_step_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Default.PhotoCamera, contentDescription = null)
            Spacer(modifier = Modifier.width(10.dp))
            Text("عکاسی مستقیم با دوربین سالن", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = onGalleryClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("pick_gallery_step_button"),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Default.PhotoLibrary, contentDescription = null)
            Spacer(modifier = Modifier.width(10.dp))
            Text("انتخاب عکس از گالری دستگاه", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FaceAnalysisResultStep(
    state: ConsultationState,
    onProceedToHairstyle: () -> Unit,
    onRetake: () -> Unit
) {
    if (state.isAnalyzing) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp,
                modifier = Modifier.size(54.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "درحال تحلیل هندسه چهره و تناسبات...",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "بررسی تقارن، فواصل چشم‌ها، نور و فرم صورت",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        }
        return
    }

    val profile = state.faceProfile ?: return

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Photo and Basic Status
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (state.photoPath != null) {
                        AsyncImage(
                            model = File(state.photoPath),
                            contentDescription = "عکس تحلیل شده",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(14.dp))
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Surface(
                            color = if (profile.faceDetected) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (profile.faceDetected) "✓ چهره با موفقیت شناسایی شد" else "⚠ چهره نیاز به بررسی بیشتر",
                                color = if (profile.faceDetected) Color(0xFF1B5E20) else Color(0xFFC62828),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "کیفیت تصویر: ${profile.imageQuality}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "ضریب اطمینان مدل: ${(profile.confidence * 100).toInt()}%",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Face Shape Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2B1327)),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "فرم چهره تخمینی",
                            color = Color(0xFFFFD9E0),
                            fontSize = 14.sp
                        )
                        Surface(
                            color = Color(0xFFD4AF37).copy(alpha = 0.25f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = profile.estimatedFaceShape.titleEn,
                                color = Color(0xFFF6BD74),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = profile.estimatedFaceShape.titleFa,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = profile.estimatedFaceShape.descriptionFa,
                        color = Color(0xFFFFD9E0).copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // Lighting & Features Breakdown
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "شاخص‌های نور و هندسه تصویر",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    // Lighting meter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("نور محیطی سالن", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${(profile.lightingScore * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    LinearProgressIndicator(
                        progress = { profile.lightingScore.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = Color(0xFFD4AF37)
                    )

                    // Eye distance
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("فاصله چشم‌ها", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${profile.eyeDistance.toInt()} px", fontSize = 12.sp)
                    }

                    // Face angle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("زاویه انحراف سر", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${profile.faceAngle.toInt()}°", fontSize = 12.sp)
                    }
                }
            }
        }

        // Ethical Disclaimer
        item {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Color(0xFF946A2C),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = profile.disclaimer,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Navigation Actions
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onRetake,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                ) {
                    Text("عکاسی مجدد")
                }

                Button(
                    onClick = onProceedToHairstyle,
                    modifier = Modifier
                        .weight(1.4f)
                        .height(52.dp)
                        .testTag("proceed_to_hairstyles_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("مشاهده پیشنهادهای مو", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun HairstyleSelectionStep(
    recommendations: List<HairstyleRecommendation>,
    selectedHairstyle: Hairstyle?,
    onSelect: (Hairstyle) -> Unit,
    onNext: () -> Unit,
    onBackStep: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "پیشنهادهای هوشمند مدل مو",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "مرتب‌شده بر اساس سازگاری با فرم چهره مشتری",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(recommendations) { rec ->
                val isSelected = selectedHairstyle?.id == rec.hairstyle.id
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(rec.hairstyle) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = rec.hairstyle.nameFa,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${rec.hairstyle.category.titleFa} • قد: ${rec.hairstyle.length}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Match Score Badge
                            Surface(
                                color = if (rec.score >= 85) Color(0xFFE8F5E9) else Color(0xFFFFF8E1),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (rec.score >= 85) Color(0xFF2E7D32) else Color(0xFFF57F17),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${rec.score}٪",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (rec.score >= 85) Color(0xFF2E7D32) else Color(0xFFF57F17)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = rec.hairstyle.description,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            color = Color.Black.copy(alpha = 0.04f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "💡 استدلال استایلیست: ${rec.reason}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Bottom Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBackStep,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                Text("مرحله قبل")
            }

            Button(
                onClick = onNext,
                enabled = selectedHairstyle != null,
                modifier = Modifier
                    .weight(1.4f)
                    .height(52.dp)
                    .testTag("confirm_hairstyle_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("انتخاب سبک آرایش", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun MakeupSelectionStep(
    recommendations: List<MakeupRecommendation>,
    selectedMakeup: MakeupStyle?,
    onSelect: (MakeupStyle) -> Unit,
    onNext: () -> Unit,
    onBackStep: () -> Unit,
    isGeneratingPreview: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "انتخاب سبک و پالت آرایش",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "تطبیق کانتورینگ، رژلب و سایه با چهره مشتری",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(recommendations) { rec ->
                val isSelected = selectedMakeup?.id == rec.makeupStyle.id
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(rec.makeupStyle) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = rec.makeupStyle.nameFa,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = rec.makeupStyle.category.titleFa,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Surface(
                                color = Color(0xFFE8F5E9),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "${rec.matchScore}٪ هماهنگی",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = Color(0xFF2E7D32),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = rec.makeupStyle.description,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        // Palette tags
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = Color(0xFFC76D7E).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "لب: ${rec.makeupStyle.lipTone}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF7A233F),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }

                            Surface(
                                color = Color(0xFF946A2C).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "گونه: ${rec.makeupStyle.blushTone}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF4C2700),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBackStep,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                Text("مرحله قبل")
            }

            Button(
                onClick = onNext,
                enabled = selectedMakeup != null && !isGeneratingPreview,
                modifier = Modifier
                    .weight(1.4f)
                    .height(52.dp)
                    .testTag("generate_preview_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                if (isGeneratingPreview) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("مشاهده استودیو Preview", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StudioPreviewStep(
    state: ConsultationState,
    onProceedToSave: () -> Unit,
    onBackStep: () -> Unit
) {
    val result = state.previewResult ?: return
    var showOriginal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "پیش‌نمایش استودیویی سالن توانا",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ترکیب تصویر مشتری با استایل‌های انتخابی",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedButton(
                onClick = { showOriginal = !showOriginal }
            ) {
                Text(if (showOriginal) "مشاهده استودیو" else "عکس اولیه")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Image Canvas Frame
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            border = BorderStroke(2.dp, Color(0xFFD4AF37)),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val imageToShow = if (showOriginal) result.originalImage else result.previewImage
            AsyncImage(
                model = File(imageToShow),
                contentDescription = "استودیو پیش‌نمایش مشتری",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1E101D))
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Selected Specs Summary Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "📌 استایل‌های انتخاب‌شده:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• مدل مو: ${result.selectedHairstyle.nameFa}",
                    fontSize = 12.sp
                )
                Text(
                    text = "• سبک آرایش: ${result.selectedMakeup.nameFa}",
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBackStep,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                Text("تغییر سبک‌ها")
            }

            Button(
                onClick = onProceedToSave,
                modifier = Modifier
                    .weight(1.4f)
                    .height(52.dp)
                    .testTag("save_consultation_flow_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("ثبت مشتری و ذخیره", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SaveConsultationStep(
    state: ConsultationState,
    onSave: (String, String, String, Int) -> Unit,
    onFinished: () -> Unit
) {
    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var nextVisitDays by remember { mutableIntStateOf(35) }

    if (state.isSavedSuccess) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "پرونده مشتری با موفقیت ذخیره شد!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "اطلاعات مشاوره، مدل مو، سبک آرایش و زمان پیشنهادی مراجعه بعدی در پایگاه داده سالن ثبت گردید.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onFinished,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("finish_consultation_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("بازگشت به صفحه اصلی سالن", fontWeight = FontWeight.Bold)
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "ثبت اطلاعات مشتری و پیگیری مراجعه بعدی",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "ثبت در پایگاه داده محلی (Offline-first) با حفظ کامل حریم خصوصی",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            OutlinedTextField(
                value = clientName,
                onValueChange = { clientName = it },
                label = { Text("نام و نام خانوادگی مشتری") },
                placeholder = { Text("مثال: مهسا کاظمی") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("client_name_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = clientPhone,
                onValueChange = { clientPhone = it },
                label = { Text("شماره همراه (جهت پیگیری و یادآوری)") },
                placeholder = { Text("09123456789") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("client_phone_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("یادداشت‌های فنی استایلیست") },
                placeholder = { Text("توناژ رنگ، حساسیت پوست، پایه دکلره و...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            Text(
                text = "زمان پیشنهادی مراجعه بعدی برای ترمیم / ریشه‌گیری:",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(20, 35, 45, 60).forEach { days ->
                    FilterChip(
                        selected = nextVisitDays == days,
                        onClick = { nextVisitDays = days },
                        label = { Text("$days روز دیگر") }
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    onSave(clientName, clientPhone, notes, nextVisitDays)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("save_final_customer_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("ثبت نهایی و تکمیل مشاوره", fontWeight = FontWeight.Bold)
            }
        }
    }
}
