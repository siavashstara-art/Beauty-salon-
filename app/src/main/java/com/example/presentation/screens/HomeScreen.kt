package com.example.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.CustomerRecoveryItem
import com.example.domain.model.CustomerStatus
import com.example.domain.model.SalonProfile

enum class AppDestination {
    HOME,
    CONSULTATION,
    CAMERA,
    GALLERY,
    CUSTOMERS,
    HAIRSTYLES,
    MAKEUP,
    RECOVERY,
    SETTINGS
}

@Composable
fun HomeScreen(
    salonProfile: SalonProfile,
    totalCustomersCount: Int,
    recoveryItems: List<CustomerRecoveryItem>,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val inactiveCount = recoveryItems.count { it.status == CustomerStatus.INACTIVE_CUSTOMER }
    val activeCount = totalCustomersCount - inactiveCount

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Salon Hero Header
        item(span = { GridItemSpan(2) }) {
            SalonHeroCard(
                salon = salonProfile,
                onSettingsClick = { onNavigate(AppDestination.SETTINGS) }
            )
        }

        // Salon Dashboard Stats
        item(span = { GridItemSpan(2) }) {
            SalonStatsRow(
                totalCustomers = totalCustomersCount,
                activeCustomers = activeCount.coerceAtLeast(0),
                recoveryCount = inactiveCount,
                onRecoveryClick = { onNavigate(AppDestination.RECOVERY) }
            )
        }

        // Section Title
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "خدمات و مدیریت سالن",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
        }

        // 8 Main Action Cards
        // 1. مشاوره زیبایی جدید
        item(span = { GridItemSpan(2) }) {
            PrimaryActionBanner(
                title = "۱. مشاوره زیبایی و استایل جدید",
                subtitle = "عکس مشتری + تحلیل هوشمند چهره + پیشنهاد مدل مو و آرایش",
                icon = Icons.Default.AutoAwesome,
                testTag = "action_new_consultation",
                onClick = { onNavigate(AppDestination.CONSULTATION) }
            )
        }

        // 2. گرفتن عکس مشتری
        item {
            SalonMenuGridItem(
                title = "۲. عکاسی چهره",
                subtitle = "دوربین CameraX با کادر پرتره",
                icon = Icons.Default.PhotoCamera,
                badge = null,
                iconColor = Color(0xFFC76D7E),
                testTag = "action_take_photo",
                onClick = { onNavigate(AppDestination.CAMERA) }
            )
        }

        // 3. انتخاب از گالری
        item {
            SalonMenuGridItem(
                title = "۳. انتخاب از گالری",
                subtitle = "بارگذاری عکس چهره پرسنلی یا مشتری",
                icon = Icons.Default.PhotoLibrary,
                badge = null,
                iconColor = Color(0xFF946A2C),
                testTag = "action_pick_gallery",
                onClick = { onNavigate(AppDestination.GALLERY) }
            )
        }

        // 4. مشتریان
        item {
            SalonMenuGridItem(
                title = "۴. مشتریان سالن",
                subtitle = "$totalCustomersCount پرونده ثبت‌شده",
                icon = Icons.Default.People,
                badge = if (totalCustomersCount > 0) "$totalCustomersCount" else null,
                iconColor = Color(0xFF75565B),
                testTag = "action_customers",
                onClick = { onNavigate(AppDestination.CUSTOMERS) }
            )
        }

        // 5. مدل‌های مو
        item {
            SalonMenuGridItem(
                title = "۵. ژورنال مدل مو",
                subtitle = "کوتاهی، باب، لایه‌ای، شینیون و عروس",
                icon = Icons.Default.Spa,
                badge = "ژورنال",
                iconColor = Color(0xFFC76D7E),
                testTag = "action_hairstyles",
                onClick = { onNavigate(AppDestination.HAIRSTYLES) }
            )
        }

        // 6. آرایش
        item {
            SalonMenuGridItem(
                title = "۶. سبک‌های آرایش",
                subtitle = "نود، کلاسیک، گلم و متناسب فرم صورت",
                icon = Icons.Default.Face,
                badge = "پالت‌ها",
                iconColor = Color(0xFF946A2C),
                testTag = "action_makeup",
                onClick = { onNavigate(AppDestination.MAKEUP) }
            )
        }

        // 7. مشتری‌برگردان
        item {
            SalonMenuGridItem(
                title = "۷. مشتری‌برگردان",
                subtitle = "پیگیری مراجعین قبلی و پیام‌های بازگشت",
                icon = Icons.Default.NotificationsActive,
                badge = if (inactiveCount > 0) "$inactiveCount نیاز به تماس" else null,
                badgeColor = Color(0xFFBA1A1A),
                iconColor = Color(0xFFB3261E),
                testTag = "action_customer_recovery",
                onClick = { onNavigate(AppDestination.RECOVERY) }
            )
        }

        // 8. تنظیمات سالن
        item(span = { GridItemSpan(2) }) {
            SalonMenuHorizontalItem(
                title = "۸. تنظیمات و پروفایل سالن",
                subtitle = "نام سالن: ${salonProfile.salonName} • مدیریت: ${salonProfile.managerName}",
                icon = Icons.Default.Settings,
                testTag = "action_settings",
                onClick = { onNavigate(AppDestination.SETTINGS) }
            )
        }
    }
}

@Composable
fun SalonHeroCard(
    salon: SalonProfile,
    onSettingsClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2B1327)),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF2D102A), Color(0xFF451A3D), Color(0xFF2D102A))
                    )
                )
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = salon.salonName,
                            color = Color(0xFFFFD9E0),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "مدیریت: ${salon.managerName} • ${salon.city}",
                            color = Color(0xFFE5BDC3),
                            fontSize = 13.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD4AF37).copy(alpha = 0.2f))
                            .clickable { onSettingsClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "تنظیمات",
                            tint = Color(0xFFF6BD74)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    color = Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "دستیار اختصاصی مشاوره چهره، استایلینگ مو، میکاپ و پیگیری مشتریان",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SalonStatsRow(
    totalCustomers: Int,
    activeCustomers: Int,
    recoveryCount: Int,
    onRecoveryClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatItemCard(
            title = "کل مشتریان",
            value = "$totalCustomers",
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        StatItemCard(
            title = "مشتریان فعال",
            value = "$activeCustomers",
            containerColor = Color(0xFFE8F5E9),
            contentColor = Color(0xFF1B5E20),
            modifier = Modifier.weight(1f)
        )
        StatItemCard(
            title = "مشتری‌برگردان",
            value = "$recoveryCount",
            containerColor = Color(0xFFFFEBEE),
            contentColor = Color(0xFFC62828),
            modifier = Modifier
                .weight(1f)
                .clickable { onRecoveryClick() }
        )
    }
}

@Composable
fun StatItemCard(
    title: String,
    value: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = contentColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                color = contentColor.copy(alpha = 0.8f),
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PrimaryActionBanner(
    title: String,
    subtitle: String,
    icon: ImageVector,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
fun SalonMenuGridItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badge: String?,
    iconColor: Color,
    badgeColor: Color = MaterialTheme.colorScheme.primary,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .testTag(testTag)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (badge != null) {
                    Surface(
                        color = badgeColor.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = badge,
                            color = badgeColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun SalonMenuHorizontalItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF946A2C).copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF946A2C),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
    }
}
