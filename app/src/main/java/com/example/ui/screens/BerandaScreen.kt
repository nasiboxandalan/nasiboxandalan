package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.EventNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.EventCategory
import com.example.data.PaketData
import com.example.data.Wilayah
import com.example.ui.AgendaUiState
import com.example.ui.QuickTimeFilter
import com.example.ui.components.CategoryFilterRow
import com.example.ui.components.EventCard
import com.example.ui.components.EventSearchBar
import com.example.ui.components.LurikDivider
import com.example.ui.components.QuickTimeFilterRow
import com.example.ui.components.RegionalThemeCard
import com.example.ui.components.WilayahFilterRow
import com.example.ui.theme.CardWhite
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenSoft
import com.example.ui.theme.SogaBrown
import com.example.ui.theme.TurmericAmber
import com.example.ui.theme.TurmericAmberDark
import com.example.ui.theme.TurmericSoft
import com.example.ui.theme.WarmCreamBorder
import com.example.ui.theme.WarmCreamSurface
import com.example.ui.theme.WhatsAppGreen
import com.example.util.ExternalIntents
import com.example.util.IndoDateUtils

@Composable
fun BerandaScreen(
    uiState: AgendaUiState,
    onSearchChange: (String) -> Unit,
    onQuickFilterSelect: (QuickTimeFilter) -> Unit,
    onWilayahSelect: (Wilayah?) -> Unit,
    onCategoryToggle: (EventCategory?) -> Unit,
    onResetFilters: () -> Unit,
    onRefreshRemote: () -> Unit,
    onEventClick: (String) -> Unit,
    onNavigateToKalender: () -> Unit,
    onNavigateToPasangAcara: () -> Unit,
    onNavigateToKonsumsi: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val upcoming = uiState.filteredHomeUpcomingEvents
    val javaneseGreeting = remember { IndoDateUtils.getTimeGreetingJavanese() }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 760.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 0. Collection notice banner
            item {
                Surface(
                    onClick = onNavigateToPasangAcara,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_notice_banner"),
                    shape = RoundedCornerShape(16.dp),
                    color = TurmericSoft,
                    border = BorderStroke(1.dp, TurmericAmber.copy(alpha = 0.55f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Campaign,
                            contentDescription = null,
                            tint = TurmericAmberDark,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "Kami sedang mengumpulkan agenda Klaten & Boyolali Desember 2026–Desember 2027. Punya info acara? Kirim gratis.",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = TurmericAmberDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // 1. Hero Section: Full unobstructed illustration on top + crisp green headline card overlapping neatly below
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    colors = CardDefaults.cardColors(containerColor = ForestGreenDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_hero_klaten),
                                contentDescription = stringResource(R.string.cd_hero_banner),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            // Gentle gradient only at bottom edge so joglo, candi, sawah & penari stay crystal clear
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colorStops = arrayOf(
                                                0.0f to Color.Transparent,
                                                0.55f to ForestGreenDark.copy(alpha = 0.12f),
                                                1.0f to ForestGreenDark.copy(alpha = 0.92f)
                                            )
                                        )
                                    )
                            )
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = TurmericAmber,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = "ARENA ACARA KLATEN & BOYOLALI",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ForestGreen)
                                .padding(horizontal = 20.dp, vertical = 18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "$javaneseGreeting, sedulur!",
                                style = MaterialTheme.typography.labelLarge,
                                color = TurmericSoft
                            )

                            Text(
                                text = "Ana Acara Apa Minggu Iki?",
                                style = MaterialTheme.typography.displaySmall,
                                color = Color.White
                            )

                            Text(
                                text = "Temukan kegiatan warga, pengajian, budaya, dan event menarik di Klaten & Boyolali.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.92f)
                            )

                            EventSearchBar(
                                query = uiState.homeSearchQuery,
                                onQueryChange = onSearchChange,
                                placeholder = "Cari nama acara, lokasi, atau penyelenggara...",
                                testTag = "home_search_input"
                            )

                            QuickTimeFilterRow(
                                selectedFilter = uiState.homeQuickFilter,
                                onSelectFilter = onQuickFilterSelect
                            )
                        }
                    }
                }
            }

            // 2. Wilayah & Category Chips
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Pilih Wilayah",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    WilayahFilterRow(
                        selectedWilayah = uiState.homeSelectedWilayah,
                        onSelectWilayah = onWilayahSelect
                    )

                    if (uiState.homeSelectedWilayah == Wilayah.KLATEN) {
                        RegionalThemeCard(
                            wilayah = Wilayah.KLATEN,
                            title = "Wilayah Klaten",
                            subtitle = "Candi, tradisi lurik, festival desa, dan agenda warga Se-Klaten."
                        )
                    } else if (uiState.homeSelectedWilayah == Wilayah.BOYOLALI) {
                        RegionalThemeCard(
                            wilayah = Wilayah.BOYOLALI,
                            title = "Wilayah Boyolali",
                            subtitle = "Lereng Merapi-Merbabu, agenda komunitas, wisata rombongan, & pengajian Boyolali."
                        )
                    }

                    Text(
                        text = "Pilih Kategori",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    CategoryFilterRow(
                        selectedCategory = uiState.homeSelectedCategory,
                        onSelectCategory = onCategoryToggle
                    )
                }
            }

            item { LurikDivider() }

            // 3. Acara Mendatang Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Acara Mendatang",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Diurutkan dari tanggal terdekat • ${upcoming.size} acara aktif",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (!uiState.remoteSyncNote.isNullOrBlank()) {
                            Text(
                                text = uiState.remoteSyncNote,
                                style = MaterialTheme.typography.bodySmall,
                                color = SogaBrown
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onRefreshRemote,
                            enabled = !uiState.isRefreshingRemote,
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            if (uiState.isRefreshingRemote) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = ForestGreen
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Outlined.Refresh,
                                    contentDescription = "Segarkan data acara",
                                    tint = ForestGreen
                                )
                            }
                        }
                        if (uiState.archivedEvents.isNotEmpty()) {
                            TextButton(
                                onClick = onNavigateToKalender,
                                modifier = Modifier.heightIn(min = 48.dp)
                            ) {
                                Text(
                                    text = "Arsip (${uiState.archivedEvents.size})",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = ForestGreen
                                )
                            }
                        }
                    }
                }
            }

            // Upcoming Events List or Friendly Empty State
            if (upcoming.isEmpty()) {
                item {
                    val activeWilayah = uiState.homeSelectedWilayah
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        border = BorderStroke(1.dp, WarmCreamBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (activeWilayah != null) {
                                RegionalThemeCard(
                                    wilayah = activeWilayah,
                                    title = "Durung ana acara ing ${activeWilayah.label}",
                                    subtitle = "Punya info kegiatan di ${activeWilayah.label}? Kirim gratis agar warga sekitar tahu!"
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(ForestGreenSoft),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.EventNote,
                                        contentDescription = null,
                                        tint = ForestGreen,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Text(
                                    text = "Belum ada acara yang cocok di filter ini",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            Text(
                                text = "Coba tampilkan semua wilayah/acara, lihat arsip di Kalender, atau kirim info acara baru gratis.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                OutlinedButton(
                                    onClick = onResetFilters,
                                    modifier = Modifier.heightIn(min = 48.dp),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Text("Reset Filter")
                                }
                                Button(
                                    onClick = onNavigateToPasangAcara,
                                    modifier = Modifier.heightIn(min = 48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                                ) {
                                    Text("Pasang Acara Gratis")
                                }
                            }
                        }
                    }
                }
            } else {
                items(upcoming, key = { it.id }) { event ->
                    EventCard(
                        event = event,
                        todayIso = uiState.todayIso,
                        onClick = { onEventClick(event.slug) }
                    )
                }
            }

            // Radar Event Tahunan 2027
            if (uiState.radarEvents2027.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Event Tahunan yang Perlu Masuk Radar 2027",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Ini bukan jadwal terkonfirmasi 2027. Namun, karena pernah tercantum dalam informasi atau kalender 2026, nama-nama berikut layak dipantau untuk pembaruan selanjutnya.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        uiState.radarEvents2027.forEach { radar ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = CardWhite),
                                border = BorderStroke(1.dp, WarmCreamBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = TurmericSoft
                                    ) {
                                        Text(
                                            text = "RADAR",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SogaBrown,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                        )
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = radar.nama,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                        Text(
                                            text = radar.keterangan,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item { LurikDivider() }

            // 4. Kenapa Agenda Klaten?
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Kenapa Agenda Klaten?",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    BenefitPointCard(
                        icon = Icons.Outlined.EventAvailable,
                        title = "Satu tempat untuk semua info acara Klaten & Boyolali",
                        description = "Info kegiatan warga, pengajian, budaya, komunitas, hingga pemerintahan tersusun rapi dalam satu direktori."
                    )
                    BenefitPointCard(
                        icon = Icons.Outlined.AddCircleOutline,
                        title = "Penyelenggara bisa pasang acara gratis",
                        description = "Kirim detail acaramu tanpa biaya, langsung dibuatkan halaman info dan kartu share 4:5 siap bagikan."
                    )
                    BenefitPointCard(
                        icon = Icons.Outlined.Restaurant,
                        title = "Butuh konsumsi? Langsung dari Nasi Box Andalan",
                        description = "Panitia acara praktis minta penawaran nasi box & snack box sesuai budget langsung lewat WhatsApp."
                    )
                }
            }

            // 5. Section Penyelenggara Acara?
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = TurmericSoft),
                    border = BorderStroke(1.dp, TurmericAmber.copy(alpha = 0.6f)),
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
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                tint = TurmericAmberDark
                            )
                            Text(
                                text = "Penyelenggara Acara?",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Text(
                            text = "Pasang acaramu gratis, dapat halaman & kartu share siap bagikan",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Button(
                            onClick = onNavigateToPasangAcara,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .testTag("home_pasang_acara_cta"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForestGreen,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Pasang Acara Gratis")
                        }
                    }
                }
            }

            // 6. Promo Nasi Box Andalan
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = ForestGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Nasi Box Andalan • Konsumsi Acara",
                            style = MaterialTheme.typography.labelLarge,
                            color = TurmericSoft
                        )
                        Text(
                            text = "Siapkan Konsumsi Acara Warga Klaten & Boyolali",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                        Text(
                            text = "Nasi box & snack box hangat, bersih, dan tepat waktu. Pilihan paket ${PaketData.PRICE_RANGE_LABEL}, melayani pengantaran area Klaten dan Boyolali.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.92f)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    ExternalIntents.openWhatsApp(
                                        context,
                                        "Halo Nasi Box Andalan, saya ingin pesen konsumsi untuk acara di Klaten/Boyolali."
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                                    .testTag("home_minta_penawaran_wa"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TurmericAmber,
                                    contentColor = MaterialTheme.colorScheme.onBackground
                                )
                            ) {
                                Text("Pesen Konsumsi")
                            }
                            OutlinedButton(
                                onClick = onNavigateToKonsumsi,
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                                    .testTag("home_lihat_paket"),
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, Color.White),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                            ) {
                                Text("Lihat Paket")
                            }
                        }
                    }
                }
            }

            // 7. Footer Info
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = WarmCreamSurface,
                    border = BorderStroke(1.dp, WarmCreamBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "AGENDA KLATEN",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = ForestGreen
                        )
                        Text(
                            text = "Direktori Acara & Konsumsi Warga Klaten dan Boyolali • by Nasi Box Andalan",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = ForestGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = PaketData.ALAMAT_LENGKAP,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = { ExternalIntents.openGoogleMapsNasiBox(context) },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Lihat Lokasi")
                            }
                            Button(
                                onClick = {
                                    ExternalIntents.openWhatsApp(
                                        context,
                                        "Halo Nasi Box Andalan, saya ingin bertanya seputar Agenda Klaten / pesen konsumsi acara."
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Chat,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("WA ${PaketData.WHATSAPP_DISPLAY}")
                            }
                        }

                        HorizontalDivider(color = WarmCreamBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = onNavigateToKalender) {
                                Text("Kalender", color = ForestGreen)
                            }
                            TextButton(onClick = onNavigateToPasangAcara) {
                                Text("Pasang Acara", color = ForestGreen)
                            }
                            TextButton(onClick = onNavigateToKonsumsi) {
                                Text("Konsumsi Acara", color = ForestGreen)
                            }
                        }

                        Text(
                            text = "Info acara dikumpulkan dari sumber resmi dan penyelenggara. Tanggal dapat berubah, cek ulang ke penyelenggara.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BenefitPointCard(
    icon: ImageVector,
    title: String,
    description: String
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(1.dp, WarmCreamBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ForestGreenSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ForestGreen
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
