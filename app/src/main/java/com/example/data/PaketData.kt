package com.example.data

import com.example.R

/**
 * Daftar menu resmi Nasi Box Andalan.
 * Mudah diedit untuk menyesuaikan menu, foto kemasan, dan harga terbaru.
 */
object PaketData {
    const val BRAND_OWNER = "Nasi Box Andalan"
    const val TAGLINE = "Lezat • Rapi • Terpercaya"
    const val ALAMAT_LENGKAP = "Depan Pintu Masuk RSI Klaten"
    const val WHATSAPP_NUMBER = "6281229203175"
    const val WHATSAPP_DISPLAY = "0812-2920-3175"
    const val AREA_LAYANAN = "Klaten dan Boyolali"
    const val PRICE_RANGE_LABEL = "Rp20.000–Rp34.000"
    const val MAPS_QUERY = "Nasi Box Andalan Klaten RSI Klaten"

    val paketList: List<PaketKonsumsi> = listOf(
        PaketKonsumsi(
            id = "paket-1",
            nama = "Paket 1",
            hargaLabel = "Rp20.000 / box",
            subjudul = "Praktis & mengenyangkan untuk pengajian, kerja bakti, rapat, dan acara warga",
            badge = "Ekonomis",
            isiMenu = listOf(
                "Nasi Putih",
                "Ayam Suwir Kecap",
                "Telur",
                "Oseng Sayur",
                "Kerupuk"
            ),
            catatan = "Porsi mengenyangkan dengan kemasan rapi & higienis. Bisa kirim on time ke lokasi acara.",
            imageRes = R.drawable.img_paket_1
        ),
        PaketKonsumsi(
            id = "paket-2",
            nama = "Paket 2",
            hargaLabel = "Rp22.000 / box",
            subjudul = "Cocok untuk seminar, workshop, kegiatan komunitas, dan pertemuan kantor",
            badge = "Populer",
            isiMenu = listOf(
                "Nasi Putih",
                "Ayam Goreng Krispy",
                "Oseng Sayur",
                "Sambal",
                "Kerupuk",
                "Buah"
            ),
            catatan = "Lauk ayam goreng krispy renyah dilengkapi oseng sayur, sambal, kerupuk, dan buah segar.",
            imageRes = R.drawable.img_paket_2
        ),
        PaketKonsumsi(
            id = "paket-3",
            nama = "Paket 3",
            hargaLabel = "Rp25.000 / box",
            subjudul = "Pilihan favorit dengan ayam bakar & telur untuk panitia maupun tamu undangan",
            badge = "Favorit",
            isiMenu = listOf(
                "Nasi Putih",
                "Ayam Bakar",
                "Telur",
                "Oseng Sayur",
                "Buah",
                "Kerupuk"
            ),
            catatan = "Perpaduan ayam bakar mantap dan telur plus oseng sayur, buah, dan kerupuk.",
            imageRes = R.drawable.img_paket_3
        ),
        PaketKonsumsi(
            id = "paket-resmi",
            nama = "Paket Resmi",
            hargaLabel = "Rp34.000 / box",
            subjudul = "Hidangan spesial untuk tamu VIP, rapat dinas, kunjungan kerja, & acara resmi",
            badge = "Acara Resmi",
            isiMenu = listOf(
                "Nasi Ayam Pejantan 1/4",
                "Sambal Lalapan",
                "Telor Asin",
                "Buah"
            ),
            catatan = "Menggunakan ayam pejantan 1/4 ekor dan telor asin pilihan dalam kemasan rapi.",
            imageRes = R.drawable.img_paket_resmi
        ),
        PaketKonsumsi(
            id = "paket-resepsi",
            nama = "Paket Hemat Resepsi",
            hargaLabel = "Rp20.000 / box",
            subjudul = "Paket lengkap sudah termasuk snack untuk resepsi, syukuran, dan hajatan keluarga",
            badge = "Hemat Resepsi",
            isiMenu = listOf(
                "Nasi",
                "Telur Separuh",
                "Kreni",
                "Acar",
                "Kerupuk",
                "Snack"
            ),
            catatan = "Dikemas dalam dus spesial hijau-merah Nasi Box Andalan yang bersih, kokoh, dan siap antar.",
            imageRes = R.drawable.img_paket_resepsi
        )
    )

    val cocokUntukList: List<String> = listOf(
        "Seminar",
        "Workshop",
        "Gathering Kantor",
        "Pengajian",
        "Kegiatan Desa",
        "Wisata Rombongan",
        "Acara Komunitas"
    )
}
