package com.example.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.SalonProfile
import com.example.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalonSettingsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentProfile by viewModel.salonProfile.collectAsState()

    var salonName by remember(currentProfile) { mutableStateOf(currentProfile.salonName) }
    var managerName by remember(currentProfile) { mutableStateOf(currentProfile.managerName) }
    var city by remember(currentProfile) { mutableStateOf(currentProfile.city) }
    var phone by remember(currentProfile) { mutableStateOf(currentProfile.phone) }
    var whatsapp by remember(currentProfile) { mutableStateOf(currentProfile.whatsapp) }
    var address by remember(currentProfile) { mutableStateOf(currentProfile.address) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("تنظیمات و پروفایل سالن", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("settings_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت")
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "اطلاعات سالن و مدیریت",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "این اطلاعات در بالای صفحات، پیش‌نمایش‌های مشتریان و پیام‌های پیگیری استفاده می‌شود و به صورت محلی در دستگاه ذخیره می‌گردد.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                        lineHeight = 18.sp
                    )
                }
            }

            OutlinedTextField(
                value = salonName,
                onValueChange = { salonName = it },
                label = { Text("نام سالن زیبایی") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_salon_name"),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = managerName,
                onValueChange = { managerName = it },
                label = { Text("نام مدیر / مسئول سالن") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_manager_name"),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("شهر") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_city"),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("شماره تلفن سالن") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_salon_phone"),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = whatsapp,
                onValueChange = { whatsapp = it },
                label = { Text("شماره واتساپ جهت ارسال پیام بازگشت") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_salon_whatsapp"),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("آدرس سالن") },
                minLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_salon_address"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val updated = SalonProfile(
                        id = currentProfile.id,
                        salonName = salonName.ifBlank { "استودیو زیبایی توانا" },
                        managerName = managerName.ifBlank { "مدیریت سالن" },
                        city = city.ifBlank { "تهران" },
                        phone = phone,
                        whatsapp = whatsapp,
                        address = address,
                        logo = currentProfile.logo
                    )
                    viewModel.updateSalonProfile(updated)
                    Toast.makeText(context, "اطلاعات سالن با موفقیت به‌روزرسانی شد", Toast.LENGTH_SHORT).show()
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("save_salon_settings_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("ذخیره تغییرات سالن", fontWeight = FontWeight.Bold)
            }
        }
    }
}
