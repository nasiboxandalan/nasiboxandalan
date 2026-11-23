package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.Share
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.AgendaEvent
import com.example.data.PaketData
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenSoft
import com.example.ui.theme.TurmericAmber
import com.example.ui.theme.TurmericSoft
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmCreamBorder
import com.example.ui.theme.WarmCreamSurface
import com.example.ui.theme.WhatsAppGreen
import com.example.util.IndoDateUtils

@Composable
fun ShareCardSection(
    event: AgendaEvent,
    todayIso: String,
    onShareWhatsApp: () -> Unit,
    onCopyLink: () -> Unit,
    onDownloadImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Kartu Acara Siap Share",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Bagikan info acara ini langsung ke grup WhatsApp atau unduh gambar rasio 4:5 untuk status & Instagram.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // 4:5 Visual Card Preview
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f),
            shape = RoundedCornerShape(22.dp),
            border = BorderStroke(1.5.dp, WarmCreamBorder),
            colors = CardDefaults.cardColors(containerColor = WarmCream),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Green Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ForestGreen)
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Column {
                        Text(
                            text = "AGENDA KLATEN",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Direktori Acara & Konsumsi Warga Klaten",
                            style = MaterialTheme.typography.bodySmall,
                            color = TurmericSoft
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .background(TurmericAmber)
                )

                // Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatusBadge(status = IndoDateUtils.effectiveStatus(event, todayIso))
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = ForestGreenSoft
                            ) {
                                Text(
                                    text = event.kategori.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ForestGreen,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }

                        Text(
                            text = event.judul,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = WarmCreamSurface,
                        border = BorderStroke(1.dp, WarmCreamBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ShareCardInfoRow(
                                icon = Icons.Outlined.CalendarMonth,
                                label = "Tanggal",
                                value = buildString {
                                    append(IndoDateUtils.formatEventDateDisplay(event))
                                    if (!event.jam.isNullOrBlank()) append(" • ${event.jam}")
                                }
                            )
                            ShareCardInfoRow(
                                icon = Icons.Outlined.LocationOn,
                                label = "Lokasi",
                                value = event.lokasi
                            )
                            ShareCardInfoRow(
                                icon = Icons.Outlined.Groups,
                                label = "Penyelenggara",
                                value = event.penyelenggara
                            )
                        }
                    }
                }

                // Footer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ForestGreen)
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Column {
                        Text(
                            text = "Konsumsi acara? Nasi Box Andalan",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${PaketData.ALAMAT_LENGKAP} • WA ${PaketData.WHATSAPP_DISPLAY}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TurmericSoft
                        )
                    }
                }
            }
        }

        // Action Buttons
        Button(
            onClick = onShareWhatsApp,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .testTag("share_whatsapp_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WhatsAppGreen,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.Share,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Bagikan ke WhatsApp", style = MaterialTheme.typography.labelLarge)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onCopyLink,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp)
                    .testTag("copy_link_button"),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, ForestGreen)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ContentCopy,
                    contentDescription = null,
                    tint = ForestGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Salin Link",
                    style = MaterialTheme.typography.labelLarge,
                    color = ForestGreen
                )
            }

            OutlinedButton(
                onClick = onDownloadImage,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp)
                    .testTag("download_image_button"),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, TurmericAmber)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Download,
                    contentDescription = null,
                    tint = ForestGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Unduh Gambar",
                    style = MaterialTheme.typography.labelLarge,
                    color = ForestGreen
                )
            }
        }
    }
}

@Composable
private fun ShareCardInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ForestGreen,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(16.dp)
        )
        Column {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = ForestGreen
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
