package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.AgendaEvent
import com.example.data.Wilayah
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
import com.example.util.IndoDateUtils

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EventCard(
    event: AgendaEvent,
    todayIso: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val effectiveStatus = IndoDateUtils.effectiveStatus(event, todayIso)
    val isPast = IndoDateUtils.isPastEvent(event, todayIso)
    val dateParts = IndoDateUtils.getDateBlockParts(event)
    val countdown = IndoDateUtils.getCountdownLabel(event, todayIso)
    val formattedDate = IndoDateUtils.formatEventDateDisplay(event)

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .testTag("event_card_${event.slug}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 4.dp),
        border = BorderStroke(1.dp, WarmCreamBorder)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Optional remote poster image via Coil
            if (!event.posterUrl.isNullOrBlank()) {
                AsyncImage(
                    model = event.posterUrl,
                    contentDescription = "Poster ${event.judul}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Status + Countdown + Category + Wilayah
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    StatusBadge(status = effectiveStatus)

                    if (countdown.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (isPast) SogaSoft else TurmericSoft
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Schedule,
                                    contentDescription = null,
                                    tint = SogaBrown,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = countdown,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SogaBrown
                                )
                            }
                        }
                    }

                    // Wilayah badge
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
                }

                // Main scannable row: Big Date Block on left + Event details on right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Big Date Block
                    Column(
                        modifier = Modifier
                            .width(66.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isPast) SogaSoft else ForestGreenSoft)
                            .padding(vertical = 10.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dateParts.topLabel,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isPast) SogaBrown else ForestGreen
                        )
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isPast) SogaBrown else TurmericAmber)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = dateParts.bottomLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isPast) Color.White else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    // Title + Category + Location + Organizer
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            CategoryIllustrationIcon(
                                category = event.kategori,
                                size = 18.dp
                            )
                            Text(
                                text = event.displayKategori,
                                style = MaterialTheme.typography.labelMedium,
                                color = SogaBrown
                            )
                        }

                        Text(
                            text = event.judul,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = buildString {
                                append(formattedDate)
                                if (!event.jam.isNullOrBlank()) append(" • ${event.jam}")
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = ForestGreen,
                            fontWeight = FontWeight.SemiBold
                        )

                        EventMetaRow(
                            icon = Icons.Outlined.LocationOn,
                            text = buildString {
                                append(event.lokasi)
                                if (!event.kecamatan.isNullOrBlank() &&
                                    !event.lokasi.contains(event.kecamatan, ignoreCase = true)
                                ) {
                                    append(" (${event.kecamatan})")
                                }
                            }
                        )
                        EventMetaRow(
                            icon = Icons.Outlined.Groups,
                            text = event.penyelenggara
                        )
                    }
                }

                // Footer CTA cue
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sumber: ${event.sumberLabel}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Lihat Detail",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = ForestGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EventMetaRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ForestGreen,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
