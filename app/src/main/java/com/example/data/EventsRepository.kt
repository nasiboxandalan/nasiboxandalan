package com.example.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

/**
 * Repositori acara mendukung Google Sheets -> Apps Script JSON endpoint + cache lokal offline.
 *
 * Cara menghubungkan Google Sheets:
 * 1. Buat Google Sheet dengan kolom:
 *    id, slug, judul, kategori, wilayah, kecamatan, tanggalMulai, tanggalSelesai,
 *    keteranganTanggal, jam, lokasi, penyelenggara, deskripsi, caraDaftar, status,
 *    sumberLabel, sumberUrl, posterUrl, butuhKonsumsi
 * 2. Deploy Apps Script Web App (akses: Anyone) yang mengembalikan JSON array dari sheet tersebut.
 * 3. Tempel URL Web App ke konstanta REMOTE_EVENTS_JSON_URL di bawah ini.
 */
object EventsRepository {
    // Isi dengan URL Google Apps Script Web App Anda (mis. "https://script.google.com/macros/s/.../exec")
    const val REMOTE_EVENTS_JSON_URL: String = ""

    private const val PREFS_NAME = "agenda_klaten_cache"
    private const val KEY_CACHED_JSON = "cached_events_json"
    private const val KEY_CUSTOM_URL = "custom_remote_json_url"

    fun getActiveRemoteUrl(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val saved = prefs.getString(KEY_CUSTOM_URL, "")?.trim().orEmpty()
        return saved.ifBlank { REMOTE_EVENTS_JSON_URL }
    }

    fun saveCustomRemoteUrl(context: Context, url: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_CUSTOM_URL, url.trim())
            .apply()
    }

    fun loadCachedOrFallbackEvents(context: Context): List<AgendaEvent> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val cached = prefs.getString(KEY_CACHED_JSON, null)
        if (!cached.isNullOrBlank()) {
            val parsed = runCatching { parseEventsJson(cached) }.getOrNull()
            if (!parsed.isNullOrEmpty()) return parsed
        }
        return EventsData.initialEvents
    }

    suspend fun refreshFromRemote(context: Context): Result<List<AgendaEvent>> = withContext(Dispatchers.IO) {
        val url = getActiveRemoteUrl(context)
        if (url.isBlank()) {
            return@withContext Result.success(loadCachedOrFallbackEvents(context))
        }
        return@withContext try {
            val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 10_000
                readTimeout = 10_000
                setRequestProperty("Accept", "application/json")
            }
            val code = connection.responseCode
            if (code !in 200..299) {
                return@withContext Result.failure(IllegalStateException("HTTP $code"))
            }
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            val parsed = parseEventsJson(body)
            if (parsed.isEmpty()) {
                Result.failure(IllegalStateException("Data JSON kosong"))
            } else {
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString(KEY_CACHED_JSON, body)
                    .apply()
                Result.success(parsed)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun parseEventsJson(rawJson: String): List<AgendaEvent> {
        val root = SimpleJsonParser(rawJson).parseValue()
        val rows: List<Map<String, Any?>> = when (root) {
            is List<*> -> root.filterIsInstance<Map<String, Any?>>()
            is Map<*, *> -> {
                val nested = root["events"] ?: root["data"]
                (nested as? List<*>)?.filterIsInstance<Map<String, Any?>>().orEmpty()
            }
            else -> emptyList()
        }

        val list = mutableListOf<AgendaEvent>()
        rows.forEachIndexed { index, item ->
            val judul = item.str("judul").orEmpty()
            val tanggalMulai = item.str("tanggalMulai").orEmpty()
            if (judul.isEmpty() || tanggalMulai.isEmpty()) return@forEachIndexed

            val slug = item.str("slug").orEmpty().ifEmpty {
                judul.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')
            }
            val id = item.str("id").orEmpty().ifEmpty { "sheet-$index" }

            list.add(
                AgendaEvent(
                    id = id,
                    slug = slug,
                    judul = judul,
                    kategori = parseCategory(item.str("kategori").orEmpty()),
                    kategoriLabel = item.str("kategoriLabel"),
                    wilayah = parseWilayah(item.str("wilayah").orEmpty()),
                    kecamatan = item.str("kecamatan"),
                    tanggalMulai = tanggalMulai,
                    tanggalSelesai = item.str("tanggalSelesai"),
                    keteranganTanggal = item.str("keteranganTanggal"),
                    jam = item.str("jam"),
                    lokasi = item.str("lokasi") ?: "Klaten",
                    penyelenggara = item.str("penyelenggara") ?: "Penyelenggara",
                    deskripsi = item.str("deskripsi").orEmpty(),
                    artikelLengkap = item.str("artikelLengkap"),
                    caraDaftar = item.str("caraDaftar"),
                    status = parseStatus(item.str("status").orEmpty()),
                    sumberLabel = item.str("sumberLabel") ?: "Info Penyelenggara",
                    sumberUrl = item.str("sumberUrl").orEmpty(),
                    sumberSekunderLabel = item.str("sumberSekunderLabel"),
                    sumberSekunderUrl = item.str("sumberSekunderUrl"),
                    posterUrl = item.str("posterUrl"),
                    butuhKonsumsi = item.bool("butuhKonsumsi"),
                    isEditorialInfo = item.bool("isEditorialInfo")
                )
            )
        }
        return list
    }

    private fun Map<String, Any?>.str(key: String): String? {
        val raw = this[key]?.toString()?.trim() ?: return null
        return raw.takeIf { it.isNotEmpty() && !it.equals("null", ignoreCase = true) }
    }

    private fun Map<String, Any?>.bool(key: String): Boolean {
        return when (val v = this[key]) {
            is Boolean -> v
            is String -> v.equals("true", ignoreCase = true) || v == "1" || v.equals("ya", ignoreCase = true)
            is Number -> v.toInt() != 0
            else -> false
        }
    }

    private fun parseCategory(raw: String): EventCategory {
        val norm = raw.trim().lowercase()
        return EventCategory.entries.find {
            it.label.lowercase() == norm || it.name.lowercase() == norm
        } ?: EventCategory.KOMUNITAS
    }

    private fun parseWilayah(raw: String): Wilayah {
        val norm = raw.trim().lowercase()
        return if (norm.contains("boyolali")) Wilayah.BOYOLALI else Wilayah.KLATEN
    }

    private fun parseStatus(raw: String): EventStatus {
        val norm = raw.trim().lowercase()
        return EventStatus.entries.find {
            it.label.lowercase() == norm || it.name.lowercase() == norm
        } ?: EventStatus.DIJADWALKAN
    }

    private class SimpleJsonParser(private val src: String) {
        private var pos = 0

        fun parseValue(): Any? {
            skipWhitespace()
            if (pos >= src.length) return null
            return when (src[pos]) {
                '{' -> parseObject()
                '[' -> parseArray()
                '"' -> parseString()
                't', 'f' -> parseBoolean()
                'n' -> parseNull()
                else -> parseNumber()
            }
        }

        private fun parseObject(): Map<String, Any?> {
            pos++ // '{'
            val map = linkedMapOf<String, Any?>()
            skipWhitespace()
            if (pos < src.length && src[pos] == '}') {
                pos++
                return map
            }
            while (pos < src.length) {
                skipWhitespace()
                val key = parseString()
                skipWhitespace()
                if (pos < src.length && src[pos] == ':') pos++
                val value = parseValue()
                map[key] = value
                skipWhitespace()
                if (pos < src.length && src[pos] == ',') {
                    pos++
                } else {
                    if (pos < src.length && src[pos] == '}') pos++
                    break
                }
            }
            return map
        }

        private fun parseArray(): List<Any?> {
            pos++ // '['
            val list = mutableListOf<Any?>()
            skipWhitespace()
            if (pos < src.length && src[pos] == ']') {
                pos++
                return list
            }
            while (pos < src.length) {
                list.add(parseValue())
                skipWhitespace()
                if (pos < src.length && src[pos] == ',') {
                    pos++
                } else {
                    if (pos < src.length && src[pos] == ']') pos++
                    break
                }
            }
            return list
        }

        private fun parseString(): String {
            if (pos >= src.length || src[pos] != '"') return ""
            pos++ // '"'
            val sb = StringBuilder()
            while (pos < src.length) {
                val ch = src[pos++]
                when (ch) {
                    '"' -> return sb.toString()
                    '\\' -> {
                        if (pos >= src.length) break
                        when (val esc = src[pos++]) {
                            '"', '\\', '/' -> sb.append(esc)
                            'b' -> sb.append('\b')
                            'f' -> sb.append('\u000C')
                            'n' -> sb.append('\n')
                            'r' -> sb.append('\r')
                            't' -> sb.append('\t')
                            'u' -> {
                                if (pos + 4 <= src.length) {
                                    val hex = src.substring(pos, pos + 4)
                                    sb.append(hex.toInt(16).toChar())
                                    pos += 4
                                }
                            }
                            else -> sb.append(esc)
                        }
                    }
                    else -> sb.append(ch)
                }
            }
            return sb.toString()
        }

        private fun parseBoolean(): Boolean {
            return if (src.startsWith("true", pos)) {
                pos += 4
                true
            } else {
                pos += 5
                false
            }
        }

        private fun parseNull(): Any? {
            if (src.startsWith("null", pos)) pos += 4
            return null
        }

        private fun parseNumber(): Number {
            val start = pos
            while (pos < src.length && (src[pos].isDigit() || src[pos] in "-+.eE")) {
                pos++
            }
            val raw = src.substring(start, pos)
            return raw.toLongOrNull() ?: raw.toDoubleOrNull() ?: 0
        }

        private fun skipWhitespace() {
            while (pos < src.length && src[pos].isWhitespace()) pos++
        }
    }
}
