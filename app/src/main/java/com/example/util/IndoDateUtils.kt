package com.example.util

import com.example.data.AgendaEvent
import com.example.data.EventStatus
import java.util.Calendar
import java.util.concurrent.TimeUnit

data class YearMonthKey(
    val year: Int,
    val month: Int // 1..12
) {
    val isoPrefix: String
        get() = "%04d-%02d".format(year, month)
}

data class CalendarDayCell(
    val dayOfMonth: Int?,
    val isoDate: String?
)

data class DateBlockParts(
    val topLabel: String, // e.g., "05" or "APR"
    val bottomLabel: String // e.g., "APR" or "2027"
)

object IndoDateUtils {

    private val namaHari = arrayOf(
        "Minggu",
        "Senin",
        "Selasa",
        "Rabu",
        "Kamis",
        "Jumat",
        "Sabtu"
    )

    private val namaBulanPanjang = arrayOf(
        "Januari",
        "Februari",
        "Maret",
        "April",
        "Mei",
        "Juni",
        "Juli",
        "Agustus",
        "September",
        "Oktober",
        "November",
        "Desember"
    )

    private val namaBulanPendek = arrayOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "Mei",
        "Jun",
        "Jul",
        "Agu",
        "Sep",
        "Okt",
        "Nov",
        "Des"
    )

    val calendarMonthsRange: List<YearMonthKey> = buildList {
        add(YearMonthKey(2026, 12))
        for (m in 1..12) {
            add(YearMonthKey(2027, m))
        }
    }

    fun getTimeGreetingJavanese(hourOfDay: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)): String {
        return when (hourOfDay) {
            in 4..10 -> "Sugeng enjang"
            in 11..14 -> "Sugeng siang"
            in 15..17 -> "Sugeng sonten"
            else -> "Sugeng dalu"
        }
    }

    fun todayIso(): String {
        val cal = Calendar.getInstance()
        return "%04d-%02d-%02d".format(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun parseIso(iso: String): Triple<Int, Int, Int>? {
        val parts = iso.trim().split("-")
        if (parts.size != 3) return null
        val y = parts[0].toIntOrNull() ?: return null
        val m = parts[1].toIntOrNull() ?: return null
        val d = parts[2].toIntOrNull() ?: return null
        if (m !in 1..12 || d !in 1..31) return null
        return Triple(y, m, d)
    }

    fun toCalendar(iso: String): Calendar? {
        val (y, m, d) = parseIso(iso) ?: return null
        return Calendar.getInstance().apply {
            clear()
            set(Calendar.YEAR, y)
            set(Calendar.MONTH, m - 1)
            set(Calendar.DAY_OF_MONTH, d)
        }
    }

    fun getDateBlockParts(event: AgendaEvent): DateBlockParts {
        val (y, m, d) = parseIso(event.tanggalMulai) ?: return DateBlockParts("--", "---")
        val monthShort = namaBulanPendek[m - 1].uppercase()
        return if (!event.keteranganTanggal.isNullOrBlank()) {
            DateBlockParts(monthShort, y.toString())
        } else {
            DateBlockParts("%02d".format(d), monthShort)
        }
    }

    fun getCountdownLabel(event: AgendaEvent, todayIso: String = todayIso()): String {
        if (isPastEvent(event, todayIso)) return "Sudah lewat"
        if (!event.keteranganTanggal.isNullOrBlank()) {
            val (y, m, _) = parseIso(event.tanggalMulai) ?: return "Segera"
            return "${namaBulanPendek[m - 1]} $y"
        }
        val todayCal = toCalendar(todayIso) ?: return ""
        val targetCal = toCalendar(event.tanggalMulai) ?: return ""
        val diffMillis = targetCal.timeInMillis - todayCal.timeInMillis
        val diffDays = TimeUnit.MILLISECONDS.toDays(diffMillis).toInt()
        return when {
            diffDays <= 0 -> "Hari ini"
            diffDays == 1 -> "Besok"
            diffDays < 30 -> "$diffDays hari lagi"
            else -> {
                val months = (diffDays / 30).coerceAtLeast(1)
                "$months bulan lagi"
            }
        }
    }

    fun formatIndoFull(isoDate: String): String {
        val cal = toCalendar(isoDate) ?: return isoDate
        val dayName = namaHari[cal.get(Calendar.DAY_OF_WEEK) - 1]
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val monthName = namaBulanPanjang[cal.get(Calendar.MONTH)]
        val year = cal.get(Calendar.YEAR)
        return "$dayName, $day $monthName $year"
    }

    fun formatIndoShort(isoDate: String): String {
        val (y, m, d) = parseIso(isoDate) ?: return isoDate
        return "$d ${namaBulanPendek[m - 1]} $y"
    }

    fun formatEventDateDisplay(event: AgendaEvent): String {
        if (!event.keteranganTanggal.isNullOrBlank()) {
            return event.keteranganTanggal
        }
        val start = formatIndoFull(event.tanggalMulai)
        val end = event.tanggalSelesai?.takeIf { it.isNotBlank() && it != event.tanggalMulai }
        return if (end != null) {
            "$start – ${formatIndoFull(end)}"
        } else {
            start
        }
    }

    fun formatMonthYear(ym: YearMonthKey): String {
        return "${namaBulanPanjang[ym.month - 1]} ${ym.year}"
    }

    fun formatMonthShort(ym: YearMonthKey): String {
        return "${namaBulanPendek[ym.month - 1]} ${ym.year}"
    }

    fun isPastEvent(event: AgendaEvent, todayIso: String = todayIso()): Boolean {
        val effectiveEnd = event.tanggalSelesai?.takeIf { it.isNotBlank() } ?: event.tanggalMulai
        return effectiveEnd < todayIso
    }

    fun effectiveStatus(event: AgendaEvent, todayIso: String = todayIso()): EventStatus {
        return if (!event.isEditorialInfo && isPastEvent(event, todayIso)) {
            EventStatus.SUDAH_LEWAT
        } else {
            event.status
        }
    }

    fun isWithinDaysFromToday(isoDate: String, daysAhead: Int, todayIso: String = todayIso()): Boolean {
        if (isoDate < todayIso) return false
        val startCal = toCalendar(todayIso) ?: return false
        val endCal = (startCal.clone() as Calendar).apply {
            add(Calendar.DAY_OF_YEAR, daysAhead)
        }
        val endIso = "%04d-%02d-%02d".format(
            endCal.get(Calendar.YEAR),
            endCal.get(Calendar.MONTH) + 1,
            endCal.get(Calendar.DAY_OF_MONTH)
        )
        return isoDate <= endIso
    }

    fun isWithinSameMonth(isoDate: String, todayIso: String = todayIso()): Boolean {
        return isoDate.length >= 7 && todayIso.length >= 7 &&
            isoDate.substring(0, 7) == todayIso.substring(0, 7)
    }

    fun buildMonthGrid(ym: YearMonthKey): List<CalendarDayCell> {
        val cal = Calendar.getInstance().apply {
            clear()
            set(Calendar.YEAR, ym.year)
            set(Calendar.MONTH, ym.month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val leadingBlanks = if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - Calendar.MONDAY
        val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val cells = mutableListOf<CalendarDayCell>()
        repeat(leadingBlanks) {
            cells.add(CalendarDayCell(null, null))
        }
        for (day in 1..maxDay) {
            val iso = "%04d-%02d-%02d".format(ym.year, ym.month, day)
            cells.add(CalendarDayCell(day, iso))
        }
        while (cells.size % 7 != 0) {
            cells.add(CalendarDayCell(null, null))
        }
        return cells
    }
}
