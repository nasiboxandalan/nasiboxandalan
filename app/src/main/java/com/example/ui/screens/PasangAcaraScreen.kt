package com.example.ui.screens

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.EventCategory
import com.example.data.Wilayah
import com.example.ui.theme.CardWhite
import com.example.ui.theme.ErrorRedSoft
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenSoft
import com.example.ui.theme.TurmericAmber
import com.example.ui.theme.TurmericSoft
import com.example.ui.theme.WarmCreamBorder
import com.example.ui.theme.WhatsAppGreen
import com.example.util.ExternalIntents
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasangAcaraScreen(
    onNavigateToBeranda: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var namaAcara by remember { mutableStateOf("") }
    var wilayah by remember { mutableStateOf(Wilayah.KLATEN) }
    var kategori by remember { mutableStateOf(EventCategory.KOMUNITAS) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var tanggalMulai by remember { mutableStateOf("") }
    var tanggalSelesai by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var penyelenggara by remember { mutableStateOf("") }
    var kontakWa by remember { mutableStateOf("") }
    var linkDaftar by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var butuhKonsumsi by remember { mutableStateOf(false) }
    var setujuBenar by remember { mutableStateOf(false) }

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }
    var errors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var isSubmitted by remember { mutableStateOf(false) }

    fun millisToIso(millis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        return sdf.format(Date(millis))
    }

    if (showStartPicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { tanggalMulai = millisToIso(it) }
                    showStartPicker = false
                }) { Text("Pilih") }
            },
            dismissButton = {
                TextButton(onClick = { showStartPicker = false }) { Text("Batal") }
            }
        ) {
            DatePicker(state = state)
        }
    }

    if (showEndPicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { tanggalSelesai = millisToIso(it) }
                    showEndPicker = false
                }) { Text("Pilih") }
            },
            dismissButton = {
                TextButton(onClick = { showEndPicker = false }) { Text("Batal") }
            }
        ) {
            DatePicker(state = state)
        }
    }

    if (isSubmitted) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                border = BorderStroke(1.dp, WarmCreamBorder),
                modifier = Modifier
                    .widthIn(max = 520.dp)
                    .testTag("pasang_acara_success_card")
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = ForestGreen,
                        modifier = Modifier.size(60.dp)
                    )
                    Text(
                        text = "Matur nuwun! Acaramu tak cek disik",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        color = ForestGreen
                    )
                    Text(
                        text = "Tim kami akan memverifikasi detail acara dan segera menayangkannya. Jika WhatsApp belum terbuka otomatis, pastikan pesan sudah terkirim ke admin (0812-2920-3175).",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = {
                            namaAcara = ""
                            tanggalMulai = ""
                            tanggalSelesai = ""
                            jam = ""
                            lokasi = ""
                            penyelenggara = ""
                            kontakWa = ""
                            linkDaftar = ""
                            deskripsi = ""
                            butuhKonsumsi = false
                            setujuBenar = false
                            errors = emptyMap()
                            isSubmitted = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                    ) {
                        Text("Kirim Acara Lainnya")
                    }
                    OutlinedButton(
                        onClick = onNavigateToBeranda,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Kembali ke Beranda")
                    }
                }
            }
        }
        return
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 760.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = TurmericSoft
                    ) {
                        Text(
                            text = "GRATIS UNTUK PENYELENGGARA",
                            style = MaterialTheme.typography.labelSmall,
                            color = ForestGreen,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }
                    Text(
                        text = "Pasang Acara Gratis di Agenda Klaten",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Publikasikan kegiatan warga, komunitas, seminar, atau pengajian di Klaten & Boyolali agar makin banyak yang hadir.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 3 Steps
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ForestGreenSoft),
                    border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Cara Kerja (3 Langkah Mudah)",
                            style = MaterialTheme.typography.titleMedium,
                            color = ForestGreen
                        )
                        StepRow("1", "Isi form", "Lengkapi detail acara di bawah ini dalam 2 menit.")
                        StepRow("2", "Kami verifikasi", "Admin cek kebenaran info via WhatsApp.")
                        StepRow("3", "Acara tayang + kartu share", "Acara tampil di direktori lengkap dengan kartu 4:5 siap sebar.")
                    }
                }
            }

            // Form Card
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    border = BorderStroke(1.dp, WarmCreamBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = namaAcara,
                            onValueChange = { namaAcara = it },
                            label = { Text("Nama Acara *") },
                            placeholder = { Text("Contoh: Jalan Sehat & Bazar UMKM Delanggu") },
                            isError = errors.containsKey("namaAcara"),
                            supportingText = errors["namaAcara"]?.let { err -> { Text(err) } },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("form_nama_acara")
                        )

                        // Wilayah selector
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Wilayah Kabupaten *",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Wilayah.entries.forEach { w ->
                                    FilterChip(
                                        selected = wilayah == w,
                                        onClick = { wilayah = w },
                                        label = { Text(w.label) },
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

                        // Category dropdown
                        ExposedDropdownMenuBox(
                            expanded = categoryExpanded,
                            onExpandedChange = { categoryExpanded = !categoryExpanded }
                        ) {
                            OutlinedTextField(
                                value = kategori.label,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Kategori *") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = categoryExpanded,
                                onDismissRequest = { categoryExpanded = false }
                            ) {
                                EventCategory.entries.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat.label) },
                                        onClick = {
                                            kategori = cat
                                            categoryExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = tanggalMulai,
                            onValueChange = { tanggalMulai = it },
                            label = { Text("Tanggal Mulai *") },
                            placeholder = { Text("YYYY-MM-DD (atau tekan ikon kalender)") },
                            trailingIcon = {
                                IconButton(onClick = { showStartPicker = true }) {
                                    Icon(
                                        imageVector = Icons.Outlined.CalendarMonth,
                                        contentDescription = "Pilih tanggal mulai"
                                    )
                                }
                            },
                            isError = errors.containsKey("tanggalMulai"),
                            supportingText = errors["tanggalMulai"]?.let { err -> { Text(err) } },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("form_tanggal_mulai")
                        )

                        OutlinedTextField(
                            value = tanggalSelesai,
                            onValueChange = { tanggalSelesai = it },
                            label = { Text("Tanggal Selesai (opsional)") },
                            placeholder = { Text("YYYY-MM-DD jika lebih dari 1 hari") },
                            trailingIcon = {
                                IconButton(onClick = { showEndPicker = true }) {
                                    Icon(
                                        imageVector = Icons.Outlined.CalendarMonth,
                                        contentDescription = "Pilih tanggal selesai"
                                    )
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = jam,
                            onValueChange = { jam = it },
                            label = { Text("Jam (opsional)") },
                            placeholder = { Text("Contoh: 08.00 – 12.00 WIB") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = lokasi,
                            onValueChange = { lokasi = it },
                            label = { Text("Lokasi & Kecamatan *") },
                            placeholder = { Text("Contoh: Alun-Alun Klaten / Simpang Lima Boyolali") },
                            isError = errors.containsKey("lokasi"),
                            supportingText = errors["lokasi"]?.let { err -> { Text(err) } },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("form_lokasi")
                        )

                        OutlinedTextField(
                            value = penyelenggara,
                            onValueChange = { penyelenggara = it },
                            label = { Text("Penyelenggara / Komunitas *") },
                            placeholder = { Text("Contoh: Karang Taruna Kec. Klaten Tengah") },
                            isError = errors.containsKey("penyelenggara"),
                            supportingText = errors["penyelenggara"]?.let { err -> { Text(err) } },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("form_penyelenggara")
                        )

                        OutlinedTextField(
                            value = kontakWa,
                            onValueChange = { kontakWa = it },
                            label = { Text("Kontak Penyelenggara (WhatsApp) *") },
                            placeholder = { Text("Contoh: 0812xxxxxxxx") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            isError = errors.containsKey("kontakWa"),
                            supportingText = errors["kontakWa"]?.let { err -> { Text(err) } },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("form_kontak_wa")
                        )

                        OutlinedTextField(
                            value = linkDaftar,
                            onValueChange = { linkDaftar = it },
                            label = { Text("Link Pendaftaran / Sumber Info (opsional)") },
                            placeholder = { Text("https://...") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = deskripsi,
                            onValueChange = { deskripsi = it },
                            label = { Text("Deskripsi Singkat *") },
                            placeholder = { Text("Jelaskan rangkaian acara, siapa yang boleh ikut, dll.") },
                            minLines = 3,
                            isError = errors.containsKey("deskripsi"),
                            supportingText = errors["deskripsi"]?.let { err -> { Text(err) } },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("form_deskripsi")
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { butuhKonsumsi = !butuhKonsumsi }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = butuhKonsumsi,
                                onCheckedChange = { butuhKonsumsi = it },
                                colors = CheckboxDefaults.colors(checkedColor = ForestGreen)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Saya butuh penawaran konsumsi untuk acara ini (Nasi Box Andalan)",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { setujuBenar = !setujuBenar }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = setujuBenar,
                                onCheckedChange = { setujuBenar = it },
                                colors = CheckboxDefaults.colors(checkedColor = ForestGreen),
                                modifier = Modifier.testTag("form_setuju_checkbox")
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Info yang saya isi benar dan dapat dipertanggungjawabkan *",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (errors.containsKey("setujuBenar")) {
                            Surface(
                                color = ErrorRedSoft,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = errors["setujuBenar"]!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }

                        if (errors.isNotEmpty()) {
                            Text(
                                text = "Mohon periksa kembali kolom yang bertanda merah di atas.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Button(
                            onClick = {
                                val newErrors = mutableMapOf<String, String>()
                                if (namaAcara.isBlank()) newErrors["namaAcara"] = "Nama acara wajib diisi."
                                if (tanggalMulai.isBlank()) newErrors["tanggalMulai"] = "Tanggal mulai wajib diisi."
                                if (lokasi.isBlank()) newErrors["lokasi"] = "Lokasi acara wajib diisi."
                                if (penyelenggara.isBlank()) newErrors["penyelenggara"] = "Nama penyelenggara wajib diisi."
                                if (kontakWa.isBlank()) newErrors["kontakWa"] = "Nomor WhatsApp penyelenggara wajib diisi."
                                if (deskripsi.isBlank()) newErrors["deskripsi"] = "Deskripsi singkat wajib diisi."
                                if (!setujuBenar) newErrors["setujuBenar"] = "Centang persetujuan bahwa informasi yang diisi benar."

                                errors = newErrors
                                if (newErrors.isEmpty()) {
                                    val msg = buildString {
                                        append("*PENGAJUAN PASANG ACARA - AGENDA KLATEN*\n")
                                        append("Nama Acara: ${namaAcara.trim()}\n")
                                        append("Wilayah: ${wilayah.label}\n")
                                        append("Kategori: ${kategori.label}\n")
                                        append("Tanggal Mulai: ${tanggalMulai.trim()}\n")
                                        if (tanggalSelesai.isNotBlank()) append("Tanggal Selesai: ${tanggalSelesai.trim()}\n")
                                        if (jam.isNotBlank()) append("Jam: ${jam.trim()}\n")
                                        append("Lokasi: ${lokasi.trim()}\n")
                                        append("Penyelenggara: ${penyelenggara.trim()}\n")
                                        append("Kontak WA: ${kontakWa.trim()}\n")
                                        if (linkDaftar.isNotBlank()) append("Link Pendaftaran: ${linkDaftar.trim()}\n")
                                        append("Deskripsi: ${deskripsi.trim()}\n")
                                        append("Butuh Penawaran Konsumsi: ${if (butuhKonsumsi) "Ya" else "Belum"}\n")
                                        append("Konfirmasi: Info yang diisi benar.")
                                    }
                                    ExternalIntents.openWhatsApp(context, msg)
                                    isSubmitted = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 52.dp)
                                .testTag("form_submit_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WhatsAppGreen,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Send,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Kirim Info Acara via WhatsApp",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepRow(
    number: String,
    title: String,
    subtitle: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(TurmericAmber),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = ForestGreen
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
