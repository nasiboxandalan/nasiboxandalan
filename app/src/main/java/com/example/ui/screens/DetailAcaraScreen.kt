package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.data.AgendaEvent
import com.example.data.Wilayah
import com.example.ui.AgendaUiState
import com.example.ui.components.CategoryIllustrationIcon
import com.example.ui.components.EventCard
import com.example.ui.components.KonsumsiDialog
import com.example.ui.components.LurikDivider
import com.example.ui.components.ShareCardSection
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CardWhite
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenSoft
import com.example.ui.theme.IndigoLurik
import com.example.ui.theme.IndigoSoft
import com.example.ui.theme.SogaBrown
import com.example.ui.theme.SogaSoft
import com.example.ui.theme.TurmericAmber
import com.example.ui.theme.TurmericSoft
import com.example.ui.theme.WarmCreamBorder
import com.example.ui.theme.WarmCreamSurface
import com.example.util.ExternalIntents
import com.example.util.IndoDateUtils
import com.example.util.ReminderHelper

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailAcaraScreen(
    slug: String,
    uiState: AgendaUiState,
    onShowMessage: (String) -> Unit,
    onEventClick: (String) -> Unit,
    onNavigateToBeranda: () -> Unit,
    onNavigateToKalender: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val event: AgendaEvent? = uiState.findBySlug(slug)
    var showKonsumsiDialog by remember { mutableStateOf(false) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (event != null) {
            if (granted) {
                ReminderHelper.scheduleHMinusOneReminder(context, event)
                    .onSuccess { msg -> onShowMessage(msg) }
                    .onFailure { onShowMessage("Gagal menjadwalkan pengingat.") }
            } else {
                onShowMessage("Izin notifikasi ditolak. Gunakan 'Tambah ke Kalender' sebagai gantinya.")
            }
        }
    }

    if (event == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                border = BorderStroke(1.dp, WarmCreamBorder),
                modifier = Modifier.widthIn(max = 480.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SearchOff,
                        contentDescription = null,
                        tint = ForestGreen,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Acara Tidak Ditemukan (404)",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Maaf, info acara yang kamu cari tidak tersedia atau tautannya sudah berubah.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = onNavigateToKalender,
                            modifier = Modifier.heightIn(min = 48.dp)
                        ) {
                            Text("Buka Kalender")
                        }
                        Button(
                            onClick = onNavigateToBeranda,
                            modifier = Modifier.heightIn(min = 48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                        ) {
                            Text("Ke Beranda")
                        }
                    }
                }
            }
        }
        return
    }

    val effectiveStatus = IndoDateUtils.effectiveStatus(event, uiState.todayIso)
    val countdown = IndoDateUtils.getCountdownLabel(event, uiState.todayIso)
    val otherEvents = uiState.otherEvents(excludeSlug = event.slug, limit = 3)

    if (showKonsumsiDialog) {
        KonsumsiDialog(
            event = event,
            onDismiss = { showKonsumsiDialog = false },
            onSubmitWhatsApp = { waMessage ->
                showKonsumsiDialog = false
                val opened = ExternalIntents.openWhatsApp(context, waMessage)
                if (!opened) {
                    onShowMessage("Tidak dapat membuka WhatsApp. Silakan hubungi 0812-2920-3175.")
                }
            }
        )
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 760.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Event Header & Main Info Card
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    border = BorderStroke(1.dp, WarmCreamBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        if (!event.posterUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = event.posterUrl,
                                contentDescription = "Poster ${event.judul}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                                    .clip(RoundedCornerShape(16.dp))
                            )
                        }

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            StatusBadge(status = effectiveStatus)

                            if (countdown.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = SogaSoft
                                ) {
                                    Text(
                                        text = countdown,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SogaBrown,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = if (event.wilayah == Wilayah.BOYOLALI) IndigoSoft else ForestGreenSoft
                            ) {
                                Text(
                                    text = event.wilayah.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (event.wilayah == Wilayah.BOYOLALI) IndigoLurik else ForestGreen,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = ForestGreenSoft
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    CategoryIllustrationIcon(category = event.kategori, size = 15.dp)
                                    Text(
                                        text = event.displayKategori,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ForestGreen
                                    )
                                }
                            }
                        }

                        Text(
                            text = event.judul,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = WarmCreamSurface,
                            border = BorderStroke(1.dp, WarmCreamBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                DetailRow(
                                    icon = Icons.Outlined.CalendarMonth,
                                    label = "Tanggal & Jam",
                                    value = buildString {
                                        append(IndoDateUtils.formatEventDateDisplay(event))
                                        if (!event.jam.isNullOrBlank()) append(" • ${event.jam}")
                                    }
                                )
                                DetailRow(
                                    icon = Icons.Outlined.LocationOn,
                                    label = "Lokasi",
                                    value = buildString {
                                        append(event.lokasi)
                                        if (!event.kecamatan.isNullOrBlank()) {
                                            append(" • ${event.kecamatan}, ${event.wilayah.label}")
                                        }
                                    }
                                )
                                DetailRow(
                                    icon = Icons.Outlined.Groups,
                                    label = "Penyelenggara",
                                    value = event.penyelenggara
                                )
                                if (!event.caraDaftar.isNullOrBlank()) {
                                    DetailRow(
                                        icon = Icons.Outlined.Info,
                                        label = "Info Pendaftaran / Kontak",
                                        value = event.caraDaftar
                                    )
                                }
                            }
                        }

                        // Ringkasan / Deskripsi SEO + Artikel Lengkap
                        Text(
                            text = event.deskripsi,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        if (!event.artikelLengkap.isNullOrBlank()) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                event.artikelLengkap.split("\n\n").forEach { block ->
                                    val trimmed = block.trim()
                                    if (trimmed.isNotEmpty()) {
                                        val isHeading = trimmed.endsWith("?") ||
                                            (trimmed.endsWith(":") && trimmed.length < 70 && !trimmed.contains("\n"))
                                        Text(
                                            text = trimmed,
                                            style = if (isHeading) {
                                                MaterialTheme.typography.titleMedium
                                            } else {
                                                MaterialTheme.typography.bodyMedium
                                            },
                                            color = if (isHeading) ForestGreen else MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                }
                            }
                        }

                        // Ingatkan Saya (Local Notification H-1 & CalendarContract)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = IndigoSoft,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "Ingatkan Saya",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = IndigoLurik
                                )
                                Text(
                                    text = "Pasang pengingat notifikasi lokal H-1 atau simpan langsung ke kalender HP Anda.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            val needsPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                                ContextCompat.checkSelfPermission(
                                                    context,
                                                    Manifest.permission.POST_NOTIFICATIONS
                                                ) != PackageManager.PERMISSION_GRANTED
                                            if (needsPermission) {
                                                notificationPermissionLauncher.launch(
                                                    Manifest.permission.POST_NOTIFICATIONS
                                                )
                                            } else {
                                                ReminderHelper.scheduleHMinusOneReminder(context, event)
                                                    .onSuccess { msg -> onShowMessage(msg) }
                                                    .onFailure { onShowMessage("Gagal menjadwalkan pengingat.") }
                                            }
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .heightIn(min = 48.dp)
                                            .testTag("remind_me_notification_button"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = IndigoLurik)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.NotificationsActive,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Notif H-1")
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            val ok = ReminderHelper.openAddToDeviceCalendar(context, event)
                                            if (!ok) onShowMessage("Aplikasi kalender tidak ditemukan.")
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .heightIn(min = 48.dp)
                                            .testTag("add_to_calendar_button"),
                                        shape = RoundedCornerShape(12.dp),
                                        border = BorderStroke(1.dp, IndigoLurik)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.EventAvailable,
                                            contentDescription = null,
                                            tint = IndigoLurik,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Kalender HP", color = IndigoLurik)
                                    }
                                }
                            }
                        }

                        // Source Link Buttons
                        if (event.sumberUrl.isNotBlank()) {
                            OutlinedButton(
                                onClick = {
                                    val ok = ExternalIntents.openUrl(context, event.sumberUrl)
                                    if (!ok) onShowMessage("Tidak bisa membuka tautan sumber.")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp)
                                    .testTag("source_link_button"),
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, ForestGreen)
                            ) {
                                Text(
                                    text = "Sumber: ${event.sumberLabel}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = ForestGreen
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                                    contentDescription = "Buka tautan sumber di browser",
                                    tint = ForestGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        if (!event.sumberSekunderUrl.isNullOrBlank()) {
                            OutlinedButton(
                                onClick = {
                                    val ok = ExternalIntents.openUrl(context, event.sumberSekunderUrl)
                                    if (!ok) onShowMessage("Tidak bisa membuka tautan agenda daerah.")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp),
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, IndigoLurik)
                            ) {
                                Text(
                                    text = event.sumberSekunderLabel ?: "Pantau Agenda Daerah",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = IndigoLurik
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                                    contentDescription = "Buka agenda daerah",
                                    tint = IndigoLurik,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Catering Banner
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = ForestGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Restaurant,
                                contentDescription = null,
                                tint = TurmericAmber
                            )
                            Text(
                                text = "Butuh konsumsi untuk acara ini?",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "Pesan nasi box atau snack box dari Nasi Box Andalan (mulai Rp20.000, area Klaten & Boyolali). Isi form singkat lalu langsung kirim ke WhatsApp.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TurmericSoft
                        )
                        Button(
                            onClick = { showKonsumsiDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .testTag("detail_minta_penawaran_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TurmericAmber,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Text(
                                text = "Pesen Konsumsi Nasi Box",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item { LurikDivider() }

            // Share Card 4:5 Section
            item {
                ShareCardSection(
                    event = event,
                    todayIso = uiState.todayIso,
                    onShareWhatsApp = {
                        ExternalIntents.shareEventToWhatsApp(context, event)
                    },
                    onCopyLink = {
                        val link = ExternalIntents.buildEventShareLink(event.slug)
                        ExternalIntents.copyToClipboard(context, event.judul, link)
                        onShowMessage("Link acara berhasil disalin: $link")
                    },
                    onDownloadImage = {
                        val result = ExternalIntents.downloadShareCardPng(context, event)
                        result.onSuccess { msg ->
                            onShowMessage(msg)
                        }.onFailure {
                            onShowMessage("Gagal mengunduh gambar otomatis. Silakan screenshot kartu acara di atas ya!")
                        }
                    }
                )
            }

            if (otherEvents.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Acara Lainnya",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                items(otherEvents, key = { "other_${it.id}" }) { other ->
                    EventCard(
                        event = other,
                        todayIso = uiState.todayIso,
                        onClick = { onEventClick(other.slug) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ForestGreen,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(18.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = ForestGreen
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
