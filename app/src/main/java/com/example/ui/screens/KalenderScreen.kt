package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.AgendaEvent
import com.example.data.EventCategory
import com.example.data.EventStatus
import com.example.data.Wilayah
import com.example.ui.AgendaUiState
import com.example.ui.CalendarViewMode
import com.example.ui.components.CategoryFilterRow
import com.example.ui.components.EventCard
import com.example.ui.components.EventSearchBar
import com.example.ui.components.LurikDivider
import com.example.ui.components.RegionalThemeCard
import com.example.ui.components.StatusFilterRow
import com.example.ui.components.WilayahFilterRow
import com.example.ui.theme.CardWhite
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenSoft
import com.example.ui.theme.StatusPastBg
import com.example.ui.theme.StatusPastText
import com.example.ui.theme.TurmericAmber
import com.example.ui.theme.TurmericSoft
import com.example.ui.theme.WarmCreamBorder
import com.example.ui.theme.WarmCreamSurface
import com.example.util.IndoDateUtils
import com.example.util.YearMonthKey

@Composable
fun KalenderScreen(
    uiState: AgendaUiState,
    onViewModeChange: (CalendarViewMode) -> Unit,
    onSearchChange: (String) -> Unit,
    onMonthSelect: (YearMonthKey?) -> Unit,
    onGridMonthChange: (YearMonthKey) -> Unit,
    onDaySelect: (String?) -> Unit,
    onWilayahSelect: (Wilayah?) -> Unit,
    onCategorySelect: (EventCategory?) -> Unit,
    onStatusSelect: (EventStatus?) -> Unit,
    onResetFilters: () -> Unit,
    onEventClick: (String) -> Unit,
    onNavigateToPasangAcara: () -> Unit,
    modifier: Modifier = Modifier
) {
    val monthsRange = IndoDateUtils.calendarMonthsRange

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 760.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header + Toggle Daftar / Kalender Bulanan
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Kalender Agenda Klaten & Boyolali",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Pantau jadwal acara Desember 2026 – Desember 2027 beserta arsip kegiatan.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ViewModeToggleButton(
                            selected = uiState.calendarViewMode == CalendarViewMode.DAFTAR,
                            label = "Daftar",
                            icon = Icons.Outlined.FormatListBulleted,
                            onClick = { onViewModeChange(CalendarViewMode.DAFTAR) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("toggle_view_daftar")
                        )
                        ViewModeToggleButton(
                            selected = uiState.calendarViewMode == CalendarViewMode.KALENDER_BULANAN,
                            label = "Kalender Bulanan",
                            icon = Icons.Outlined.CalendarMonth,
                            onClick = { onViewModeChange(CalendarViewMode.KALENDER_BULANAN) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("toggle_view_kalender")
                        )
                    }
                }
            }

            // Search & Filters
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    EventSearchBar(
                        query = uiState.calendarSearchQuery,
                        onQueryChange = onSearchChange,
                        placeholder = "Cari acara di kalender...",
                        testTag = "calendar_search_input"
                    )

                    WilayahFilterRow(
                        selectedWilayah = uiState.calendarSelectedWilayah,
                        onSelectWilayah = onWilayahSelect
                    )

                    if (uiState.calendarViewMode == CalendarViewMode.DAFTAR) {
                        Text(
                            text = "Filter Bulan (Des 2026 – Des 2027)",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = uiState.calendarSelectedMonth == null,
                                onClick = { onMonthSelect(null) },
                                label = { Text("Semua Bulan") },
                                modifier = Modifier.heightIn(min = 48.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ForestGreen,
                                    selectedLabelColor = Color.White,
                                    containerColor = CardWhite
                                )
                            )
                            monthsRange.forEach { ym ->
                                val selected = uiState.calendarSelectedMonth == ym
                                FilterChip(
                                    selected = selected,
                                    onClick = { onMonthSelect(if (selected) null else ym) },
                                    label = { Text(IndoDateUtils.formatMonthShort(ym)) },
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

                    CategoryFilterRow(
                        selectedCategory = uiState.calendarSelectedCategory,
                        onSelectCategory = onCategorySelect
                    )

                    StatusFilterRow(
                        selectedStatus = uiState.calendarSelectedStatus,
                        onSelectStatus = onStatusSelect
                    )
                }
            }

            item { LurikDivider() }

            // Content according to ViewMode
            if (uiState.calendarViewMode == CalendarViewMode.DAFTAR) {
                val upcoming = uiState.filteredUpcomingCalendarEvents
                val archived = uiState.filteredArchivedCalendarEvents
                val hasActiveFilters = uiState.calendarSearchQuery.isNotBlank() ||
                    uiState.calendarSelectedMonth != null ||
                    uiState.calendarSelectedWilayah != null ||
                    uiState.calendarSelectedCategory != null ||
                    uiState.calendarSelectedStatus != null

                if (upcoming.isEmpty() && archived.isEmpty()) {
                    item {
                        KalenderEmptyState(
                            wilayah = uiState.calendarSelectedWilayah,
                            hasActiveFilters = hasActiveFilters,
                            onResetFilters = onResetFilters,
                            onNavigateToPasangAcara = onNavigateToPasangAcara
                        )
                    }
                } else {
                    if (upcoming.isNotEmpty()) {
                        item {
                            Text(
                                text = "Acara Mendatang (${upcoming.size})",
                                style = MaterialTheme.typography.titleLarge,
                                color = ForestGreen
                            )
                        }
                        items(upcoming, key = { "cal_up_${it.id}" }) { event ->
                            EventCard(
                                event = event,
                                todayIso = uiState.todayIso,
                                onClick = { onEventClick(event.slug) }
                            )
                        }
                    } else {
                        item {
                            KalenderEmptyState(
                                wilayah = uiState.calendarSelectedWilayah,
                                hasActiveFilters = hasActiveFilters,
                                onResetFilters = onResetFilters,
                                onNavigateToPasangAcara = onNavigateToPasangAcara
                            )
                        }
                    }

                    if (archived.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = StatusPastBg,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.History,
                                        contentDescription = null,
                                        tint = StatusPastText,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "Arsip Acara (${archived.size})",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = StatusPastText
                                        )
                                        Text(
                                            text = "Acara yang tanggalnya sudah lewat otomatis dipindahkan ke sini.",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = StatusPastText
                                        )
                                    }
                                }
                            }
                        }
                        items(archived, key = { "cal_arc_${it.id}" }) { event ->
                            EventCard(
                                event = event,
                                todayIso = uiState.todayIso,
                                onClick = { onEventClick(event.slug) }
                            )
                        }
                    }
                }
            } else {
                // Monthly Calendar Grid Mode
                item {
                    val activeMonth = uiState.calendarActiveGridMonth
                    val currentIndex = monthsRange.indexOf(activeMonth).coerceAtLeast(0)
                    val gridCells = IndoDateUtils.buildMonthGrid(activeMonth)
                    val monthEvents = uiState.eventsInActiveGridMonth
                    val eventsByDay = monthEvents.groupBy { it.tanggalMulai }

                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        border = BorderStroke(1.dp, WarmCreamBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        if (currentIndex > 0) {
                                            onGridMonthChange(monthsRange[currentIndex - 1])
                                        }
                                    },
                                    enabled = currentIndex > 0,
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                        contentDescription = "Bulan sebelumnya"
                                    )
                                }

                                Text(
                                    text = IndoDateUtils.formatMonthYear(activeMonth),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = ForestGreen
                                )

                                IconButton(
                                    onClick = {
                                        if (currentIndex < monthsRange.lastIndex) {
                                            onGridMonthChange(monthsRange[currentIndex + 1])
                                        }
                                    },
                                    enabled = currentIndex < monthsRange.lastIndex,
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                        contentDescription = "Bulan berikutnya"
                                    )
                                }
                            }

                            Row(modifier = Modifier.fillMaxWidth()) {
                                listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min").forEach { dayName ->
                                    Text(
                                        text = dayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            val rows = gridCells.chunked(7)
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                rows.forEach { week ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        week.forEach { cell ->
                                            if (cell.dayOfMonth == null || cell.isoDate == null) {
                                                Spacer(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .aspectRatio(1f)
                                                )
                                            } else {
                                                val dayEvents = eventsByDay[cell.isoDate].orEmpty()
                                                val isSelected = uiState.calendarSelectedDayIso == cell.isoDate
                                                val isToday = cell.isoDate == uiState.todayIso
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .aspectRatio(1f)
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(
                                                            when {
                                                                isSelected -> ForestGreen
                                                                dayEvents.isNotEmpty() -> TurmericSoft
                                                                isToday -> ForestGreenSoft
                                                                else -> WarmCreamSurface.copy(alpha = 0.45f)
                                                            }
                                                        )
                                                        .clickable { onDaySelect(cell.isoDate) },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Column(
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.Center
                                                    ) {
                                                        Text(
                                                            text = cell.dayOfMonth.toString(),
                                                            style = MaterialTheme.typography.labelLarge,
                                                            color = when {
                                                                isSelected -> Color.White
                                                                else -> MaterialTheme.colorScheme.onBackground
                                                            }
                                                        )
                                                        if (dayEvents.isNotEmpty()) {
                                                            Spacer(modifier = Modifier.height(2.dp))
                                                            Box(
                                                                modifier = Modifier
                                                                    .size(6.dp)
                                                                    .clip(CircleShape)
                                                                    .background(
                                                                        if (isSelected) TurmericAmber else ForestGreen
                                                                    )
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                monthsRange.forEach { ym ->
                                    val selected = ym == activeMonth
                                    Surface(
                                        onClick = { onGridMonthChange(ym) },
                                        shape = RoundedCornerShape(50),
                                        color = if (selected) ForestGreenSoft else WarmCreamSurface,
                                        border = BorderStroke(
                                            1.dp,
                                            if (selected) ForestGreen else WarmCreamBorder
                                        )
                                    ) {
                                        Text(
                                            text = IndoDateUtils.formatMonthShort(ym),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (selected) ForestGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                val displayedEvents: List<AgendaEvent> = if (uiState.calendarSelectedDayIso != null) {
                    uiState.eventsOnSelectedGridDay
                } else {
                    uiState.eventsInActiveGridMonth
                }

                item {
                    val heading = if (uiState.calendarSelectedDayIso != null) {
                        "Acara pada ${IndoDateUtils.formatIndoFull(uiState.calendarSelectedDayIso)}"
                    } else {
                        "Acara di ${IndoDateUtils.formatMonthYear(uiState.calendarActiveGridMonth)} (${displayedEvents.size})"
                    }
                    Text(
                        text = heading,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (displayedEvents.isEmpty()) {
                    item {
                        KalenderEmptyState(
                            wilayah = uiState.calendarSelectedWilayah,
                            hasActiveFilters = uiState.calendarSelectedDayIso != null,
                            onResetFilters = {
                                if (uiState.calendarSelectedDayIso != null) onDaySelect(null) else onResetFilters()
                            },
                            onNavigateToPasangAcara = onNavigateToPasangAcara
                        )
                    }
                } else {
                    items(displayedEvents, key = { "grid_evt_${it.id}" }) { event ->
                        EventCard(
                            event = event,
                            todayIso = uiState.todayIso,
                            onClick = { onEventClick(event.slug) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ViewModeToggleButton(
    selected: Boolean,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.heightIn(min = 48.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) ForestGreen else CardWhite,
        border = BorderStroke(1.dp, if (selected) ForestGreen else WarmCreamBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) Color.White else ForestGreen,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) Color.White else MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun KalenderEmptyState(
    wilayah: Wilayah?,
    hasActiveFilters: Boolean,
    onResetFilters: () -> Unit,
    onNavigateToPasangAcara: () -> Unit
) {
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
            RegionalThemeCard(
                wilayah = wilayah ?: Wilayah.KLATEN,
                title = "Durung ana acara. Duwe info? Kirimi gratis",
                subtitle = "Belum ada kegiatan terjadwal pada pilihan ini. Yuk bantu bagikan info acara di Klaten & Boyolali."
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                if (hasActiveFilters) {
                    OutlinedButton(
                        onClick = onResetFilters,
                        modifier = Modifier.heightIn(min = 48.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Tampilkan Semua")
                    }
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
