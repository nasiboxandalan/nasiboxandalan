package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.EventStatus
import com.example.ui.theme.StatusConfirmedBg
import com.example.ui.theme.StatusConfirmedBorder
import com.example.ui.theme.StatusConfirmedText
import com.example.ui.theme.StatusPastBg
import com.example.ui.theme.StatusPastBorder
import com.example.ui.theme.StatusPastText
import com.example.ui.theme.StatusPendingBg
import com.example.ui.theme.StatusPendingBorder
import com.example.ui.theme.StatusPendingText
import com.example.ui.theme.StatusScheduledBg
import com.example.ui.theme.StatusScheduledBorder
import com.example.ui.theme.StatusScheduledText

@Composable
fun StatusBadge(
    status: EventStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, borderColor) = statusColors(status)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = status.label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

private fun statusColors(status: EventStatus): Triple<Color, Color, Color> {
    return when (status) {
        EventStatus.TERKONFIRMASI -> Triple(StatusConfirmedBg, StatusConfirmedText, StatusConfirmedBorder)
        EventStatus.DIJADWALKAN -> Triple(StatusScheduledBg, StatusScheduledText, StatusScheduledBorder)
        EventStatus.MENUNGGU_KONFIRMASI -> Triple(StatusPendingBg, StatusPendingText, StatusPendingBorder)
        EventStatus.SUDAH_LEWAT -> Triple(StatusPastBg, StatusPastText, StatusPastBorder)
    }
}
