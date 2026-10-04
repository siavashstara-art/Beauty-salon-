package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.camera.CameraCaptureScreen
import com.example.camera.copyUriToLocalCache
import com.example.presentation.screens.AppDestination
import com.example.presentation.screens.ConsultationScreen
import com.example.presentation.screens.CustomerRecoveryScreen
import com.example.presentation.screens.CustomersScreen
import com.example.presentation.screens.HairstylesCatalogScreen
import com.example.presentation.screens.HomeScreen
import com.example.presentation.screens.MakeupCatalogScreen
import com.example.presentation.screens.SalonSettingsScreen
import com.example.presentation.viewmodel.MainViewModel
import com.example.ui.theme.TavanaBeautyTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TavanaBeautyTheme(isRtl = true) {
                TavanaBeautyApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TavanaBeautyApp(viewModel: MainViewModel) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf(AppDestination.HOME) }

    val salonProfile by viewModel.salonProfile.collectAsState()
    val customers by viewModel.allCustomers.collectAsState()
    val recoveryItems by viewModel.recoveryItems.collectAsState()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCameraPermission = isGranted
        if (isGranted) {
            currentScreen = AppDestination.CAMERA
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val localPath = copyUriToLocalCache(context, uri)
            if (localPath != null) {
                viewModel.startNewConsultation(localPath)
                currentScreen = AppDestination.CONSULTATION
            }
        }
    }

    // Back handling for secondary screens
    if (currentScreen != AppDestination.HOME) {
        BackHandler {
            currentScreen = AppDestination.HOME
        }
    }

    when (currentScreen) {
        AppDestination.HOME -> {
            HomeScreen(
                salonProfile = salonProfile,
                totalCustomersCount = customers.size,
                recoveryItems = recoveryItems,
                onNavigate = { dest ->
                    when (dest) {
                        AppDestination.CAMERA -> {
                            if (hasCameraPermission) {
                                currentScreen = AppDestination.CAMERA
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                        AppDestination.GALLERY -> {
                            galleryLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        AppDestination.CONSULTATION -> {
                            viewModel.startNewConsultation()
                            currentScreen = AppDestination.CONSULTATION
                        }
                        else -> {
                            currentScreen = dest
                        }
                    }
                }
            )
        }

        AppDestination.CAMERA -> {
            if (hasCameraPermission) {
                CameraCaptureScreen(
                    onPhotoConfirmed = { photoPath ->
                        viewModel.onPhotoSelected(photoPath)
                        currentScreen = AppDestination.CONSULTATION
                    },
                    onPickFromGallery = {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onCancel = {
                        currentScreen = AppDestination.HOME
                    }
                )
            } else {
                CameraPermissionRationaleScreen(
                    onRequestPermission = {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    onBack = {
                        currentScreen = AppDestination.HOME
                    }
                )
            }
        }

        AppDestination.CONSULTATION -> {
            ConsultationScreen(
                viewModel = viewModel,
                onBack = { currentScreen = AppDestination.HOME },
                onOpenLiveCamera = {
                    if (hasCameraPermission) {
                        currentScreen = AppDestination.CAMERA
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                onFinished = {
                    currentScreen = AppDestination.HOME
                }
            )
        }

        AppDestination.CUSTOMERS -> {
            CustomersScreen(
                viewModel = viewModel,
                onBack = { currentScreen = AppDestination.HOME }
            )
        }

        AppDestination.HAIRSTYLES -> {
            HairstylesCatalogScreen(
                onBack = { currentScreen = AppDestination.HOME }
            )
        }

        AppDestination.MAKEUP -> {
            MakeupCatalogScreen(
                onBack = { currentScreen = AppDestination.HOME }
            )
        }

        AppDestination.RECOVERY -> {
            CustomerRecoveryScreen(
                viewModel = viewModel,
                onBack = { currentScreen = AppDestination.HOME }
            )
        }

        AppDestination.SETTINGS -> {
            SalonSettingsScreen(
                viewModel = viewModel,
                onBack = { currentScreen = AppDestination.HOME }
            )
        }

        AppDestination.GALLERY -> {
            // Handled via launcher directly
            currentScreen = AppDestination.HOME
        }
    }
}

@Composable
fun CameraPermissionRationaleScreen(
    onRequestPermission: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFD9E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "مجوز دسترسی به دوربین",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "جهت عکاسی از چهره مشتری در سالن و ارائه تحلیل فرم صورت، دسترسی به دوربین الزامی است.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onRequestPermission,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("grant_camera_permission_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("اعطای مجوز دوربین", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("بازگشت به صفحه اصلی")
            }
        }
    }
}
