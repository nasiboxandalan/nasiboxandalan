package com.example.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object RemoteEvents {
    const val EVENTS_URL =
        "https://raw.githubusercontent.com/nasiboxandalan/agenda-klaten-data/main/events.json"

    private const val PREFS = "agenda_cache"
    private const val KEY_JSON = "events_json"

    suspend fun fetch(context: Context): List<AgendaEvent>? = withContext(Dispatchers.IO) {
        try {
            val conn = (URL(EVENTS_URL).openConnection() as HttpURLConnection).apply {
                connectTimeout = 8000
                readTimeout = 8000
            }
            if (conn.responseCode != 200) return@withContext null
            val text = conn.inputStream.bufferedReader().use { it.readText() }
            val events = parse(text)
            if (events.isEmpty()) return@withContext null
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit().putString(KEY_JSON, text).apply()
            events
        } catch (e: Exception) {
            null
        }
    }

    fun loadCached(context: Context): List<AgendaEvent>? {
        val text = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_JSON, null) ?: return null
        return runCatching { parse(text) }.getOrNull()?.takeIf { it.isNotEmpty() }
    }

    private fun JSONObject.str(key: String): String? =
        if (!has(key) || isNull(key)) null else getString(key).takeIf { it.isNotBlank() }

    fun parse(text: String): List<AgendaEvent> {
        val arr = JSONObject(text).getJSONArray("events")
        val result = mutableListOf<AgendaEvent>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            val event = runCatching {
                AgendaEvent(
                    id = o.getString("id"),
                    slug = o.getString("slug"),
                    judul = o.getString("judul"),
                    kategori = EventCategory.valueOf(o.getString("kategori")),
                    tanggalMulai = o.getString("tanggalMulai"),
                    tanggalSelesai = o.str("tanggalSelesai"),
                    keteranganTanggal = o.str("keteranganTanggal"),
                    jam = o.str("jam"),
                    lokasi = o.getString("lokasi"),
                    penyelenggara = o.getString("penyelenggara"),
                    deskripsi = o.getString("deskripsi"),
                    caraDaftar = o.str("caraDaftar"),
                    status = EventStatus.valueOf(o.getString("status")),
                    sumberLabel = o.getString("sumberLabel"),
                    sumberUrl = o.getString("sumberUrl"),
                    butuhKonsumsi = o.optBoolean("butuhKonsumsi", false)
                )
            }.getOrNull()
            if (event != null) result.add(event)
        }
        return result
    }
}
