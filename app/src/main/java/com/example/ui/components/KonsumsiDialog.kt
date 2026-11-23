package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.AgendaEvent
import com.example.ui.theme.CardWhite
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.TurmericSoft
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WhatsAppGreen
import com.example.util.IndoDateUtils

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun KonsumsiDialog(
    event: AgendaEvent,
    onDismiss: () -> Unit,
    onSubmitWhatsApp: (message: String) -> Unit
) {
    val budgetOptions = listOf(
        "Rp20.000 (Paket 1 / Resepsi)",
        "Rp22.000 (Paket 2)",
        "Rp25.000 (Paket 3)",
        "Rp34.000 (Paket Resmi)",
        "Request Menu / Custom Budget"
    )
    val defaultDate = IndoDateUtils.formatEventDateDisplay(event)

    var jumlahPeserta by remember { mutableStateOf("50") }
    var tanggalPengiriman by remember { mutableStateOf(defaultDate) }
    var budgetPerBox by remember { mutableStateOf(budgetOptions[0]) }
    var lokasiPengantaran by remember { mutableStateOf(event.lokasi) }
    var catatan by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = WarmCream,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Restaurant,
                        contentDescription = null,
                        tint = ForestGreen
                    )
                    Text(
                        text = "Minta Penawaran Nasi Box",
                        style = MaterialTheme.typography.titleLarge,
                        color = ForestGreen
                    )
                }

                Text(
                    text = "Untuk acara: ${event.judul}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = jumlahPeserta,
                    onValueChange = { jumlahPeserta = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Perkiraan jumlah peserta (box)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("konsumsi_jumlah_input")
                )

                OutlinedTextField(
                    value = tanggalPengiriman,
                    onValueChange = { tanggalPengiriman = it },
                    label = { Text("Tanggal pengiriman") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Budget per box",
                        style = MaterialTheme.typography.labelLarge
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        budgetOptions.forEach { option ->
                            val selected = budgetPerBox == option
                            FilterChip(
                                selected = selected,
                                onClick = { budgetPerBox = option },
                                label = { Text(option) },
                                modifier = Modifier.heightIn(min = 48.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ForestGreen,
                                    selectedLabelColor = Color.White,
                                    containerColor = CardWhite
                                )
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = lokasiPengantaran,
                    onValueChange = { lokasiPengantaran = it },
                    label = { Text("Lokasi pengantaran") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = catatan,
                    onValueChange = { catatan = it },
                    label = { Text("Catatan tambahan (opsional)") },
                    placeholder = { Text("Misal: jam antar pagi, butuh snack box, tanpa pedas") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage != null) {
                    Surface(
                        color = TurmericSoft,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorMessage!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.heightIn(min = 48.dp)
                    ) {
                        Text("Batal")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            when {
                                jumlahPeserta.isBlank() || (jumlahPeserta.toIntOrNull() ?: 0) <= 0 ->
                                    errorMessage = "Mohon isi perkiraan jumlah peserta."
                                tanggalPengiriman.isBlank() ->
                                    errorMessage = "Mohon isi tanggal pengiriman."
                                lokasiPengantaran.isBlank() ->
                                    errorMessage = "Mohon isi lokasi pengantaran."
                                else -> {
                                    val msg = buildString {
                                        append("Halo Nasi Box Andalan, saya ingin minta penawaran konsumsi untuk acara:\n")
                                        append("Acara: ${event.judul}\n")
                                        append("Perkiraan jumlah: $jumlahPeserta box\n")
                                        append("Tanggal kirim: $tanggalPengiriman\n")
                                        append("Budget per box: $budgetPerBox\n")
                                        append("Lokasi antar: $lokasiPengantaran\n")
                                        if (catatan.isNotBlank()) append("Catatan: $catatan\n")
                                        append("\nMohon info rekomendasi paketnya ya, terima kasih!")
                                    }
                                    onSubmitWhatsApp(msg)
                                }
                            }
                        },
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .testTag("konsumsi_submit_wa_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WhatsAppGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Kirim ke WhatsApp")
                    }
                }
            }
        }
    }
}
