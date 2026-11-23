package com.example.data

import androidx.annotation.DrawableRes

enum class Wilayah(val label: String) {
    KLATEN("Klaten"),
    BOYOLALI("Boyolali")
}

enum class EventCategory(val label: String) {
    FESTIVAL("Festival"),
    SEMINAR_WORKSHOP("Seminar & Workshop"),
    KOMUNITAS("Komunitas"),
    GATHERING_KANTOR("Gathering & Kantor"),
    WISATA_ROMBONGAN("Wisata Rombongan"),
    DESA_PENGAJIAN("Desa & Pengajian"),
    BUDAYA("Budaya"),
    PEMERINTAHAN("Pemerintahan"),
    KULINER_UMKM("Kuliner & UMKM")
}

enum class EventStatus(val label: String) {
    TERKONFIRMASI("Terkonfirmasi"),
    DIJADWALKAN("Dijadwalkan"),
    MENUNGGU_KONFIRMASI("Menunggu Konfirmasi"),
    SUDAH_LEWAT("Sudah Lewat")
}

data class AgendaEvent(
    val id: String,
    val slug: String,
    val judul: String,
    val kategori: EventCategory,
    val kategoriLabel: String? = null,
    val wilayah: Wilayah = Wilayah.KLATEN,
    val kecamatan: String? = null,
    val tanggalMulai: String, // Format ISO: YYYY-MM-DD
    val tanggalSelesai: String? = null,
    val keteranganTanggal: String? = null,
    val jam: String? = null,
    val lokasi: String,
    val penyelenggara: String,
    val deskripsi: String,
    val artikelLengkap: String? = null,
    val caraDaftar: String? = null,
    val status: EventStatus,
    val sumberLabel: String,
    val sumberUrl: String,
    val sumberSekunderLabel: String? = null,
    val sumberSekunderUrl: String? = null,
    val posterUrl: String? = null,
    val butuhKonsumsi: Boolean = false,
    val isEditorialInfo: Boolean = false
) {
    val displayKategori: String
        get() = kategoriLabel?.takeIf { it.isNotBlank() } ?: kategori.label
}

data class RadarEvent(
    val id: String,
    val nama: String,
    val keterangan: String
)

data class PaketKonsumsi(
    val id: String,
    val nama: String,
    val hargaLabel: String,
    val subjudul: String,
    val badge: String? = null,
    val isiMenu: List<String>,
    val catatan: String,
    @DrawableRes val imageRes: Int
)
