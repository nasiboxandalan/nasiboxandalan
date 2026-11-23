package com.example.ui

import androidx.lifecycle.viewModelScope
import com.example.data.RemoteEvents
import kotlinx.coroutines.launch
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AgendaEvent
import com.example.data.EventCategory
import com.example.data.EventStatus
import com.example.data.EventsData
import com.example.data.EventsRepository
import com.example.data.PaketData
import com.example.data.PaketKonsumsi
import com.example.data.RadarEvent
import com.example.data.Wilayah
import com.example.util.IndoDateUtils
import com.example.util.YearMonthKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class QuickTimeFilter(val label: String) {
    SEMUA("Semua Acara"),
    MINGGU_INI("Minggu Ini"),
    BULAN_INI("Bulan Ini")
}

enum class CalendarViewMode(val label: String) {
    DAFTAR("Daftar"),
    KALENDER_BULANAN("Kalender Bulanan")
}

data class AgendaUiState(
    val todayIso: String = IndoDateUtils.todayIso(),
    val allEvents: List<AgendaEvent> = EventsData.initialEvents,
    val radarEvents2027: List<RadarEvent> = EventsData.radarEvents2027,
    val paketList: List<PaketKonsumsi> = PaketData.paketList,
    val isRefreshingRemote: Boolean = false,
    val remoteSyncNote: String? = null,
    // Beranda filters
    val homeSearchQuery: String = "",
    val homeQuickFilter: QuickTimeFilter = QuickTimeFilter.SEMUA,
    val homeSelectedWilayah: Wilayah? = null, // null = Semua (Klaten & Boyolali)
    val homeSelectedCategory: EventCategory? = null,
    // Kalender filters
    val calendarViewMode: CalendarViewMode = CalendarViewMode.DAFTAR,
    val calendarSearchQuery: String = "",
    val calendarSelectedMonth: YearMonthKey? = null,
    val calendarActiveGridMonth: YearMonthKey = YearMonthKey(2026, 12),
    val calendarSelectedDayIso: String? = null,
    val calendarSelectedWilayah: Wilayah? = null,
    val calendarSelectedCategory: EventCategory? = null,
    val calendarSelectedStatus: EventStatus? = null
) {
    val upcomingEvents: List<AgendaEvent>
        get() = allEvents
            .filter { it.isEditorialInfo || !IndoDateUtils.isPastEvent(it, todayIso) }
            .sortedWith(compareBy<AgendaEvent> { it.isEditorialInfo }.thenBy { it.tanggalMulai })

    val archivedEvents: List<AgendaEvent>
        get() = allEvents
            .filter { !it.isEditorialInfo && IndoDateUtils.isPastEvent(it, todayIso) }
            .sortedByDescending { it.tanggalMulai }

    val filteredHomeUpcomingEvents: List<AgendaEvent>
        get() = upcomingEvents.filter { event ->
            val matchesWilayah = homeSelectedWilayah == null || event.wilayah == homeSelectedWilayah
            val matchesCategory = homeSelectedCategory == null || event.kategori == homeSelectedCategory
            val matchesTime = when (homeQuickFilter) {
                QuickTimeFilter.SEMUA -> true
                QuickTimeFilter.MINGGU_INI -> IndoDateUtils.isWithinDaysFromToday(event.tanggalMulai, 7, todayIso)
                QuickTimeFilter.BULAN_INI -> IndoDateUtils.isWithinSameMonth(event.tanggalMulai, todayIso)
            }
            val q = homeSearchQuery.trim()
            val matchesQuery = q.isEmpty() ||
                event.judul.contains(q, ignoreCase = true) ||
                event.lokasi.contains(q, ignoreCase = true) ||
                event.penyelenggara.contains(q, ignoreCase = true) ||
                event.wilayah.label.contains(q, ignoreCase = true) ||
                (event.kecamatan?.contains(q, ignoreCase = true) == true) ||
                event.kategori.label.contains(q, ignoreCase = true)
            matchesWilayah && matchesCategory && matchesTime && matchesQuery
        }

    val filteredCalendarEvents: List<AgendaEvent>
        get() = allEvents
            .filter { event ->
                val effStatus = IndoDateUtils.effectiveStatus(event, todayIso)
                val matchesMonth = calendarSelectedMonth == null ||
                    event.tanggalMulai.startsWith(calendarSelectedMonth.isoPrefix)
                val matchesWilayah = calendarSelectedWilayah == null ||
                    event.wilayah == calendarSelectedWilayah
                val matchesCategory = calendarSelectedCategory == null ||
                    event.kategori == calendarSelectedCategory
                val matchesStatus = calendarSelectedStatus == null ||
                    effStatus == calendarSelectedStatus
                val q = calendarSearchQuery.trim()
                val matchesQuery = q.isEmpty() ||
                    event.judul.contains(q, ignoreCase = true) ||
                    event.lokasi.contains(q, ignoreCase = true) ||
                    event.penyelenggara.contains(q, ignoreCase = true) ||
                    event.wilayah.label.contains(q, ignoreCase = true)
                matchesMonth && matchesWilayah && matchesCategory && matchesStatus && matchesQuery
            }
            .sortedBy { it.tanggalMulai }

    val filteredUpcomingCalendarEvents: List<AgendaEvent>
        get() = filteredCalendarEvents.filter { !IndoDateUtils.isPastEvent(it, todayIso) }

    val filteredArchivedCalendarEvents: List<AgendaEvent>
        get() = filteredCalendarEvents
            .filter { IndoDateUtils.isPastEvent(it, todayIso) }
            .sortedByDescending { it.tanggalMulai }

    val eventsInActiveGridMonth: List<AgendaEvent>
        get() = allEvents.filter { event ->
            val effStatus = IndoDateUtils.effectiveStatus(event, todayIso)
            val matchesMonth = event.tanggalMulai.startsWith(calendarActiveGridMonth.isoPrefix)
            val matchesWilayah = calendarSelectedWilayah == null || event.wilayah == calendarSelectedWilayah
            val matchesCategory = calendarSelectedCategory == null || event.kategori == calendarSelectedCategory
            val matchesStatus = calendarSelectedStatus == null || effStatus == calendarSelectedStatus
            val q = calendarSearchQuery.trim()
            val matchesQuery = q.isEmpty() ||
                event.judul.contains(q, ignoreCase = true) ||
                event.lokasi.contains(q, ignoreCase = true) ||
                event.penyelenggara.contains(q, ignoreCase = true)
            matchesMonth && matchesWilayah && matchesCategory && matchesStatus && matchesQuery
        }

    val eventsOnSelectedGridDay: List<AgendaEvent>
        get() = calendarSelectedDayIso?.let { day ->
            eventsInActiveGridMonth.filter { it.tanggalMulai == day }
        } ?: emptyList()

    fun findBySlug(slug: String): AgendaEvent? = allEvents.find { it.slug == slug }

    fun otherEvents(excludeSlug: String, limit: Int = 3): List<AgendaEvent> {
        val upcoming = upcomingEvents.filter { it.slug != excludeSlug }
        val archived = archivedEvents.filter { it.slug != excludeSlug }
        return (upcoming + archived).take(limit)
    }
}

class AgendaViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AgendaUiState())
    val uiState: StateFlow<AgendaUiState> = _uiState.asStateFlow()
        fun refreshEvents(context: android.content.Context) {
        val appContext = context.applicationContext
        viewModelScope.launch {
            RemoteEvents.loadCached(appContext)?.let { cached ->
                _uiState.update { it.copy(allEvents = cached) }
            }
            RemoteEvents.fetch(appContext)?.let { fresh ->
                _uiState.update { it.copy(allEvents = fresh) }
            }
        }
    }

    private var initializedFromContext = false

    fun initializeData(context: Context) {
        if (initializedFromContext) return
        initializedFromContext = true
        val cached = EventsRepository.loadCachedOrFallbackEvents(context)
        _uiState.update { it.copy(allEvents = cached) }
        if (EventsRepository.getActiveRemoteUrl(context).isNotBlank()) {
            refreshRemoteEvents(context)
        }
    }

    fun refreshRemoteEvents(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshingRemote = true, remoteSyncNote = null) }
            val result = EventsRepository.refreshFromRemote(context)
            result.onSuccess { fetched ->
                _uiState.update {
                    it.copy(
                        allEvents = fetched,
                        isRefreshingRemote = false,
                        remoteSyncNote = if (EventsRepository.getActiveRemoteUrl(context).isNotBlank()) {
                            "Data diperbarui (${fetched.size} acara)"
                        } else null
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isRefreshingRemote = false,
                        remoteSyncNote = "Memakai data offline (${err.localizedMessage ?: "gagal terhubung"})"
                    )
                }
            }
        }
    }

    fun onHomeSearchChange(query: String) {
        _uiState.update { it.copy(homeSearchQuery = query) }
    }

    fun onHomeQuickFilterSelect(filter: QuickTimeFilter) {
        _uiState.update { it.copy(homeQuickFilter = filter) }
    }

    fun onHomeWilayahSelect(wilayah: Wilayah?) {
        _uiState.update { it.copy(homeSelectedWilayah = wilayah) }
    }

    fun onHomeCategoryToggle(category: EventCategory?) {
        _uiState.update { state ->
            val next = if (state.homeSelectedCategory == category) null else category
            state.copy(homeSelectedCategory = next)
        }
    }

    fun resetHomeFilters() {
        _uiState.update {
            it.copy(
                homeSearchQuery = "",
                homeQuickFilter = QuickTimeFilter.SEMUA,
                homeSelectedWilayah = null,
                homeSelectedCategory = null
            )
        }
    }

    fun onCalendarViewModeChange(mode: CalendarViewMode) {
        _uiState.update { it.copy(calendarViewMode = mode) }
    }

    fun onCalendarSearchChange(query: String) {
        _uiState.update { it.copy(calendarSearchQuery = query) }
    }

    fun onCalendarMonthSelect(month: YearMonthKey?) {
        _uiState.update { state ->
            state.copy(
                calendarSelectedMonth = month,
                calendarActiveGridMonth = month ?: state.calendarActiveGridMonth,
                calendarSelectedDayIso = null
            )
        }
    }

    fun onCalendarGridMonthChange(month: YearMonthKey) {
        _uiState.update {
            it.copy(
                calendarActiveGridMonth = month,
                calendarSelectedDayIso = null
            )
        }
    }

    fun onCalendarDaySelect(isoDate: String?) {
        _uiState.update { state ->
            val next = if (state.calendarSelectedDayIso == isoDate) null else isoDate
            state.copy(calendarSelectedDayIso = next)
        }
    }

    fun onCalendarWilayahSelect(wilayah: Wilayah?) {
        _uiState.update { it.copy(calendarSelectedWilayah = wilayah) }
    }

    fun onCalendarCategorySelect(category: EventCategory?) {
        _uiState.update { state ->
            val next = if (state.calendarSelectedCategory == category) null else category
            state.copy(calendarSelectedCategory = next)
        }
    }

    fun onCalendarStatusSelect(status: EventStatus?) {
        _uiState.update { state ->
            val next = if (state.calendarSelectedStatus == status) null else status
            state.copy(calendarSelectedStatus = next)
        }
    }

    fun resetCalendarFilters() {
        _uiState.update {
            it.copy(
                calendarSearchQuery = "",
                calendarSelectedMonth = null,
                calendarSelectedWilayah = null,
                calendarSelectedCategory = null,
                calendarSelectedStatus = null,
                calendarSelectedDayIso = null
            )
        }
    }
}
