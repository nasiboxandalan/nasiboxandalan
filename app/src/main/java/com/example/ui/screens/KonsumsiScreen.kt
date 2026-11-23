package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Map
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.PaketData
import com.example.data.PaketKonsumsi
import com.example.ui.AgendaUiState
import com.example.ui.components.LurikDivider
import com.example.ui.theme.CardWhite
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenSoft
import com.example.ui.theme.TurmericAmber
import com.example.ui.theme.TurmericAmberDark
import com.example.ui.theme.TurmericSoft
import com.example.ui.theme.WarmCreamBorder
import com.example.ui.theme.WarmCreamSurface
import com.example.ui.theme.WhatsAppGreen
import com.example.util.ExternalIntents

private data class GalleryItem(
    val title: String,
    val caption: String,
    val imageRes: Int
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun KonsumsiScreen(
    uiState: AgendaUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val highlights = listOf(
        "Porsi Mengenyangkan",
        "Kemasan Rapi & Higienis",
        "Bisa Kirim On Time"
    )
    val galleryItems = listOf(
        GalleryItem(
            title = "Kemasan Asli Nasi Box Andalan",
            caption = "Dus spesial hijau-merah bersih, kokoh, siap antar untuk pengajian, rapat, dan event warga.",
            imageRes = R.drawable.img_nasi_box_asli
        ),
        GalleryItem(
            title = "Dokumentasi Acara Warga & Komunitas",
            caption = "Melayani acara seminar, wayangan, pengajian desa, hingga gathering kantor.",
            imageRes = R.drawable.img_galeri_acara_andalan
        )
    )

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
            // 1. Hero Card
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = ForestGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = TurmericAmber
                        ) {
                            Text(
                                text = "NASI BOX ANDALAN • KLATEN",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }

                        Text(
                            text = "Konsumsi Acara Beres, Panitia Tenang",
                            style = MaterialTheme.typography.displaySmall,
                            color = Color.White
                        )

                        Text(
                            text = "${PaketData.TAGLINE}. Pilihan menu nasi box ${PaketData.PRICE_RANGE_LABEL} per box untuk acara di ${PaketData.AREA_LAYANAN}, siap antar tepat waktu ke lokasi acaramu.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.92f)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            highlights.forEach { badgeText ->
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = Color.White.copy(alpha = 0.14f),
                                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                                ) {
                                    Text(
                                        text = badgeText,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                ExternalIntents.openWhatsApp(
                                    context,
                                    "Halo Nasi Box Andalan, saya ingin konsultasi pesen konsumsi acara."
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 50.dp)
                                .testTag("konsumsi_hero_wa_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TurmericAmber,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Chat,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Pesen Konsumsi via WhatsApp",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }

            // 2. Pilihan Menu Nasi Box
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Pilihan Menu Nasi Box",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Pilihan menu Nasi Box Andalan (${PaketData.PRICE_RANGE_LABEL} / box). Kami menerima request menu dan budget menyesuaikan kebutuhan pelanggan.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(uiState.paketList, key = { it.id }) { paket ->
                PaketCard(
                    paket = paket,
                    onPesanClick = {
                        ExternalIntents.openWhatsApp(
                            context,
                            "Halo Nasi Box Andalan, saya tertarik memesan ${paket.nama} (${paket.hargaLabel}). Mohon info ketersediaan untuk tanggal acara saya."
                        )
                    }
                )
            }

            // Info Request Menu & Budget Fleksibel
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = TurmericSoft,
                    border = BorderStroke(1.dp, TurmericAmber.copy(alpha = 0.55f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Butuh Request Menu & Budget Khusus?",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Kami menerima request isi lauk, snack box, maupun penyesuaian budget sesuai anggaran acara Anda.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(
                            onClick = {
                                ExternalIntents.openWhatsApp(
                                    context,
                                    "Halo Nasi Box Andalan, saya ingin request menu & penyesuaian budget konsumsi acara."
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .testTag("konsumsi_request_custom_wa_button"),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, ForestGreen)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Chat,
                                contentDescription = null,
                                tint = ForestGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Diskusi Request Menu via WhatsApp",
                                color = ForestGreen,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }

            item { LurikDivider() }

            // 3. Cocok Untuk & Area Layanan
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    border = BorderStroke(1.dp, WarmCreamBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Cocok untuk Berbagai Acara",
                            style = MaterialTheme.typography.titleLarge,
                            color = ForestGreen
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PaketData.cocokUntukList.forEach { item ->
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = ForestGreenSoft
                                ) {
                                    Text(
                                        text = item,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = ForestGreen,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                                    )
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = TurmericSoft,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.LocalShipping,
                                    contentDescription = null,
                                    tint = TurmericAmberDark
                                )
                                Column {
                                    Text(
                                        text = "Area Layanan: ${PaketData.AREA_LAYANAN}",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = "Pengantaran langsung ke gedung acara, kantor, masjid, desa, atau titik kumpul rombongan.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Cara Pesan 3 Langkah
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
                    border = BorderStroke(1.dp, WarmCreamBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Cara Pesan (3 Langkah Mudah)",
                            style = MaterialTheme.typography.titleLarge,
                            color = ForestGreen
                        )
                        OrderStep("1", "Chat WhatsApp", "Kirim tanggal acara, jumlah box, dan perkiraan budget.")
                        OrderStep("2", "Konfirmasi menu & jumlah", "Kami kirim pilihan menu lengkap yang pas untuk acaramu.")
                        OrderStep("3", "Diantar ke lokasi", "Pesanan dimasak segar dan diantar tepat waktu ke lokasi acara.")
                    }
                }
            }

            // 5. Galeri Foto Asli
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Galeri Nasi Box Andalan",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Foto kemasan nyata dus Nasi Box Andalan dan dokumentasi pengantaran acara.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(galleryItems, key = { it.title }) { item ->
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = CardWhite),
                                border = BorderStroke(1.dp, WarmCreamBorder),
                                modifier = Modifier.width(260.dp)
                            ) {
                                Column {
                                    Image(
                                        painter = painterResource(id = item.imageRes),
                                        contentDescription = item.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(4f / 3f)
                                    )
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                        Text(
                                            text = item.caption,
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

            // 6. Alamat & Lokasi Google Maps + WA
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    border = BorderStroke(1.dp, WarmCreamBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = ForestGreen
                            )
                            Text(
                                text = "Lokasi Dapur & Pemesanan",
                                style = MaterialTheme.typography.titleLarge,
                                color = ForestGreen
                            )
                        }
                        Text(
                            text = "${PaketData.BRAND_OWNER}\n${PaketData.ALAMAT_LENGKAP}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { ExternalIntents.openGoogleMapsNasiBox(context) },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                                    .testTag("konsumsi_lihat_lokasi_button"),
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, ForestGreen)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Map,
                                    contentDescription = null,
                                    tint = ForestGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Lihat Lokasi", color = ForestGreen)
                            }

                            Button(
                                onClick = {
                                    ExternalIntents.openWhatsApp(
                                        context,
                                        "Halo Nasi Box Andalan, saya ingin pesen konsumsi acara."
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                                    .testTag("konsumsi_footer_wa_button"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = WhatsAppGreen,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Chat,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pesen Konsumsi")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaketCard(
    paket: PaketKonsumsi,
    onPesanClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(1.dp, WarmCreamBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("paket_card_${paket.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = paket.imageRes),
                    contentDescription = paket.nama,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                )
                if (paket.badge != null) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = TurmericAmber,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = paket.badge,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = paket.nama,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = paket.hargaLabel,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = ForestGreen
                )
                Text(
                    text = paket.subjudul,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = WarmCreamSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Isi Menu:",
                            style = MaterialTheme.typography.labelMedium,
                            color = ForestGreen
                        )
                        paket.isiMenu.forEach { menu ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Check,
                                    contentDescription = null,
                                    tint = ForestGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = menu,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                Text(
                    text = paket.catatan,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    onClick = onPesanClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .testTag("pesan_paket_${paket.id}_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WhatsAppGreen,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Restaurant,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pesen Konsumsi via WhatsApp", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
private fun OrderStep(
    stepNumber: String,
    title: String,
    description: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(ForestGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
        }
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
