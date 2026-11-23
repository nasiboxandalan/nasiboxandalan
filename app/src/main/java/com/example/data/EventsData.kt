package com.example.data

/**
 * Data cadangan (fallback offline) Agenda Klaten & Boyolali.
 * Jika Google Sheets JSON belum diisi atau perangkat sedang offline, data ini ditampilkan.
 */
object EventsData {
    val initialEvents: List<AgendaEvent> = listOf(
        // ARTIKEL 1 — PROMO SAMBANG CANDI
        AgendaEvent(
            id = "evt-sambang-candi-2026",
            slug = "promo-sambang-candi-desember-2026",
            judul = "Promo Sambang Candi Desember 2026: Warga Klaten Bisa Wisata Candi Mulai Rp25 Ribu",
            kategori = EventCategory.WISATA_ROMBONGAN,
            kategoriLabel = "Wisata Klaten & Sekitarnya",
            wilayah = Wilayah.KLATEN,
            kecamatan = "Prambanan & Sekitarnya",
            tanggalMulai = "2026-12-12",
            tanggalSelesai = "2026-12-19",
            keteranganTanggal = "12–19 Desember 2026 (weekday)",
            jam = "06.30–09.30 WIB (pembelian tiket loket)",
            lokasi = "Candi Borobudur, Candi Prambanan, dan Keraton Ratu Boko",
            penyelenggara = "InJourney Destination Management",
            deskripsi = "Warga ber-KTP Klaten bisa menikmati Promo Sambang Candi Desember 2026 dengan tiket khusus Rp25.000 per destinasi. Simak jadwal dan syaratnya.",
            artikelLengkap = """
                Kabar menarik untuk warga Klaten yang ingin menikmati wisata sejarah dan budaya dengan harga lebih hemat. InJourney Destination Management menghadirkan kembali program Promo Sambang Candi pada Desember 2026.

                Program ini memberikan kesempatan bagi warga lokal untuk mengunjungi tiga destinasi unggulan, yaitu Candi Borobudur, Candi Prambanan, dan Keraton Ratu Boko dengan harga tiket khusus Rp25.000 per orang untuk masing-masing destinasi.

                Kapan promo berlangsung?
                Periode promo Desember 2026 berlangsung pada 12–19 Desember 2026. Penawaran khusus ini berlaku untuk kunjungan weekday sesuai ketentuan penyelenggara.

                Siapa yang bisa mendapatkan promo?
                Promo ditujukan bagi pemegang KTP dari wilayah:
                • Kabupaten Klaten
                • Kota Yogyakarta
                • Kabupaten Magelang
                • Kota Solo

                Bagaimana cara mendapatkan tiket?
                Pembelian tiket dilakukan secara langsung di loket destinasi pada pukul 06.30–09.30 WIB.

                Perlu diperhatikan bahwa tiket promo Borobudur mencakup akses ke area pelataran, bukan akses naik ke struktur candi.

                Program ini bisa menjadi pilihan liburan bersama keluarga, teman, maupun komunitas dari Klaten dan sekitarnya. Sebelum berangkat, pastikan kembali syarat dan ketentuan promo melalui informasi resmi penyelenggara.
            """.trimIndent(),
            caraDaftar = "Pembelian tiket langsung di loket destinasi pukul 06.30–09.30 WIB dengan membawa KTP Klaten/Yogya/Magelang/Solo.",
            status = EventStatus.TERKONFIRMASI,
            sumberLabel = "InJourney Destination Management",
            sumberUrl = "https://injourneydestination.id/promo-sambang-candi/",
            posterUrl = null,
            butuhKonsumsi = true
        ),

        // ARTIKEL 2 — SWARA PRAMBANAN 2026
        AgendaEvent(
            id = "evt-swara-prambanan-2026",
            slug = "swara-prambanan-2026-desember",
            judul = "Swara Prambanan 2026 Hadir 31 Desember, Catat Jadwal dan Lokasinya!",
            kategori = EventCategory.FESTIVAL,
            kategoriLabel = "Musik & Festival",
            wilayah = Wilayah.KLATEN,
            kecamatan = "Perbatasan Prambanan (Kab. Sleman, DIY)",
            tanggalMulai = "2026-12-31",
            tanggalSelesai = null,
            keteranganTanggal = null,
            jam = "15.00–22.00 WIB",
            lokasi = "Candi Prambanan, Kabupaten Sleman, DI Yogyakarta",
            penyelenggara = "Penyelenggara Swara Prambanan",
            deskripsi = "Swara Prambanan 2026 dijadwalkan berlangsung pada 31 Desember 2026 di kawasan Candi Prambanan. Simak informasi waktu, lokasi, dan tiketnya.",
            artikelLengkap = """
                Menjelang pergantian tahun, kawasan Candi Prambanan menjadi salah satu pilihan agenda hiburan bagi masyarakat Klaten dan sekitarnya.

                Swara Prambanan 2026 dijadwalkan berlangsung pada Kamis, 31 Desember 2026, mulai pukul 15.00 hingga 22.00 WIB.

                Mengusung konsep yang terinspirasi dari tradisi, kebiasaan, dan budaya lokal Yogyakarta, acara ini hadir dengan nuansa retro era 1980–1990-an.

                Jadwal Swara Prambanan 2026:
                • Hari: Kamis
                • Tanggal: 31 Desember 2026
                • Waktu: 15.00–22.00 WIB
                • Lokasi: Candi Prambanan
                • Wilayah: Kabupaten Sleman, DI Yogyakarta

                Laman tiket Fasticket mencantumkan harga tiket mulai Rp50.000. Harga dan ketersediaan tiket dapat berubah mengikuti ketentuan penjualan.

                Bagi masyarakat Klaten yang ingin mengisi malam pergantian tahun dengan agenda musik dan budaya, Swara Prambanan dapat menjadi pilihan yang menarik. Pastikan memeriksa informasi tiket dan ketentuan terbaru sebelum berangkat.

                Catatan: Lokasi acara secara administratif berada di Kabupaten Sleman, bukan Kabupaten Klaten.
            """.trimIndent(),
            caraDaftar = "Tiket tersedia melalui laman Fasticket (mulai Rp50.000, cek ketersediaan terbaru).",
            status = EventStatus.TERKONFIRMASI,
            sumberLabel = "Fasticket — Swara Prambanan 2026",
            sumberUrl = "https://fasticket.id/acara/swara-prambanan-2026",
            posterUrl = null,
            butuhKonsumsi = false
        ),

        // ARTIKEL 3 — PILKADES KLATEN 2027
        AgendaEvent(
            id = "evt-2",
            slug = "pilkades-serentak-klaten-2027",
            judul = "Pilkades Serentak Gelombang II Klaten Direncanakan Maret atau Awal April 2027",
            kategori = EventCategory.PEMERINTAHAN,
            kategoriLabel = "Info Pemerintahan & Masyarakat",
            wilayah = Wilayah.KLATEN,
            kecamatan = "256 Desa Se-Kabupaten Klaten",
            tanggalMulai = "2027-03-15",
            tanggalSelesai = null,
            keteranganTanggal = "Maret atau awal April 2027 (rencana awal)",
            jam = null,
            lokasi = "Kabupaten Klaten (256 desa penyelenggara)",
            penyelenggara = "Pemkab Klaten (Dinas Pemberdayaan Masyarakat dan Desa)",
            deskripsi = "Pemkab Klaten mematangkan persiapan Pilkades Serentak Gelombang II tahun 2027 yang direncanakan melibatkan 256 desa.",
            artikelLengkap = """
                Pemerintah Kabupaten Klaten mulai mematangkan persiapan pelaksanaan Pemilihan Kepala Desa (Pilkades) Serentak Gelombang II tahun 2027.

                Dalam rapat koordinasi yang dilaksanakan pada 4 November 2026, pemerintah daerah bersama unsur terkait membahas persiapan pelaksanaan Pilkades, termasuk aspek keamanan dan tahapan penyelenggaraan.

                Kapan Pilkades dilaksanakan?
                Berdasarkan keterangan Kepala Dinas Pemberdayaan Masyarakat dan Desa Kabupaten Klaten, Pilkades Gelombang II tahun 2027 direncanakan berlangsung sekitar Maret atau awal April 2027. Sebanyak 256 desa masuk dalam rencana pelaksanaan gelombang ini.

                Apa saja tahapan Pilkades?
                1. Persiapan dan penyusunan daftar pemilih.
                2. Pendaftaran dan pencalonan.
                3. Penjaringan serta seleksi administrasi bakal calon.
                4. Penetapan calon dan pengundian nomor urut.
                5. Kampanye dan masa tenang.
                6. Pemungutan serta penghitungan suara.
                7. Penetapan, pengesahan, dan pelantikan kepala desa terpilih.

                Masyarakat diharapkan mengikuti informasi resmi dari pemerintah daerah terkait jadwal dan tahapan berikutnya.

                Status informasi: Periode Maret atau awal April 2027 masih merupakan rencana awal. Tanggal pemungutan suara dan rincian pelaksanaan belum diumumkan dalam sumber yang dirujuk.
            """.trimIndent(),
            caraDaftar = "Ikuti pengumuman resmi dari pemerintah desa dan Pemkab Klaten.",
            status = EventStatus.MENUNGGU_KONFIRMASI,
            sumberLabel = "Prokopim Klaten",
            sumberUrl = "https://prokopim.klaten.go.id/pemkab-klaten-matangkan-persiapan-pilkades-serentak-gelombang-ii-tahun-2027",
            posterUrl = null,
            butuhKonsumsi = true
        ),

        // ARTIKEL 4 — BERITA CALENDAR OF EVENTS JATENG 2027
        AgendaEvent(
            id = "evt-coe-jateng-2027",
            slug = "calendar-of-events-jateng-2027-klaten",
            judul = "Jateng Siapkan 426 Event pada 2027, Bagaimana Agenda Wisata Klaten?",
            kategori = EventCategory.BUDAYA,
            kategoriLabel = "Berita Pariwisata",
            wilayah = Wilayah.KLATEN,
            kecamatan = "Kabupaten Klaten",
            tanggalMulai = "2026-11-22",
            tanggalSelesai = null,
            keteranganTanggal = "Update 22 November 2026 • Kalender Event 2027",
            jam = null,
            lokasi = "Kabupaten Klaten & Jawa Tengah",
            penyelenggara = "Pemprov Jawa Tengah & Pemkab Klaten",
            deskripsi = "Pemerintah Provinsi Jawa Tengah menyiapkan 426 event sepanjang 2027. Masyarakat menantikan informasi agenda dan festival di Kabupaten Klaten.",
            artikelLengkap = """
                Pemerintah Provinsi Jawa Tengah resmi meluncurkan Calendar of Events 2027 dengan total 426 agenda yang disiapkan untuk 35 kabupaten/kota. Peluncuran tersebut dilakukan pada 21 November 2026 di Kabupaten Jepara.

                Program ini bertujuan memperkuat sektor pariwisata berkelanjutan, mengenalkan potensi daerah, dan mendorong wisatawan berkunjung lebih lama ke berbagai wilayah Jawa Tengah. Pemprov Jawa Tengah juga menyampaikan rencana pengembangan aplikasi untuk menghubungkan kalender event di berbagai kabupaten dan kota agar masyarakat lebih mudah menemukan informasi kegiatan.

                Bagaimana dengan agenda Kabupaten Klaten?
                Kabupaten Klaten memiliki potensi wisata budaya, tradisi, desa wisata, serta kegiatan komunitas yang menarik untuk dikunjungi. Namun, rincian nama, tanggal, dan lokasi event Klaten dalam kalender 2027 masih perlu menunggu publikasi resmi yang lebih lengkap.

                Agenda Klaten akan terus memantau informasi dari pemerintah daerah, penyelenggara, dan komunitas lokal untuk membantu masyarakat menemukan kegiatan menarik sepanjang tahun 2027.

                Event tahunan Klaten yang perlu masuk radar 2027 (belum jadwal terkonfirmasi):
                • Grebeg Syawalan – Bukit Sidoguro (Bayat, Klaten)
                • Klaten Lurik Carnival
                • Yaa Qowiyyu – Jatinom
                • Festival Desa Wisata
                • Festival Candi Sojiwan
            """.trimIndent(),
            caraDaftar = "Pantau pembaruan resmi agenda wisata daerah melalui portal Pemerintah Provinsi Jawa Tengah dan Dinas Pariwisata Klaten.",
            status = EventStatus.MENUNGGU_KONFIRMASI,
            sumberLabel = "Pemprov Jawa Tengah",
            sumberUrl = "https://jatengprov.go.id/publik/jateng-siapkan-426-event-pada-2027-bidik-dampak-ekonomi-pariwisata/",
            sumberSekunderLabel = "Pantau Agenda Wisata Klaten",
            sumberSekunderUrl = "https://wisata.klaten.go.id/event",
            posterUrl = null,
            butuhKonsumsi = false,
            isEditorialInfo = true
        ),

        // Acara Awal Lainnya
        AgendaEvent(
            id = "evt-1",
            slug = "sambung-rasa-klaten-2027",
            judul = "Sambung Rasa Klaten 2027",
            kategori = EventCategory.PEMERINTAHAN,
            wilayah = Wilayah.KLATEN,
            kecamatan = null,
            tanggalMulai = "2027-04-05",
            tanggalSelesai = null,
            keteranganTanggal = null,
            jam = null,
            lokasi = "Klaten (lokasi menunggu informasi resmi)",
            penyelenggara = "Pemkab Klaten",
            deskripsi = "Program tahunan Pemkab Klaten, jadwal pelaksanaan berikutnya dijadwalkan 5 April 2027.",
            caraDaftar = "Terbuka untuk informasi warga sesuai jadwal resmi Pemkab Klaten.",
            status = EventStatus.DIJADWALKAN,
            sumberLabel = "Metro TV News",
            sumberUrl = "https://www.metrotvnews.com/play/b7WCrE3J-program-sambung-rasa-klaten-2026-resmi-ditutup",
            posterUrl = null,
            butuhKonsumsi = true
        ),
        AgendaEvent(
            id = "evt-3",
            slug = "festival-kopi-klaten-2026",
            judul = "Festival Kopi Klaten 2026",
            kategori = EventCategory.KULINER_UMKM,
            wilayah = Wilayah.KLATEN,
            kecamatan = "Klaten Tengah",
            tanggalMulai = "2026-11-20",
            tanggalSelesai = null,
            keteranganTanggal = null,
            jam = null,
            lokasi = "Alun-Alun Klaten",
            penyelenggara = "Pemkab Klaten",
            deskripsi = "Festival yang menampilkan kedai kopi kecil dan produk lokal dari pelaku UMKM Klaten.",
            caraDaftar = "Terbuka untuk umum di area Alun-Alun Klaten.",
            status = EventStatus.TERKONFIRMASI,
            sumberLabel = "Prokopim Klaten",
            sumberUrl = "https://prokopim.klaten.go.id/tag/plt-bupati-klaten",
            posterUrl = null,
            butuhKonsumsi = false
        ),
        AgendaEvent(
            id = "evt-4",
            slug = "pagelaran-wayang-kulit-wahyu-katentreman-2026",
            judul = "Pagelaran Wayang Kulit \"Wahyu Katentreman\" (Hari Wayang Dunia)",
            kategori = EventCategory.BUDAYA,
            wilayah = Wilayah.KLATEN,
            kecamatan = "Klaten Tengah",
            tanggalMulai = "2026-11-20",
            tanggalSelesai = null,
            keteranganTanggal = null,
            jam = "Malam hari",
            lokasi = "Halaman Gedung Sunan Pandanaran / RSPD Klaten",
            penyelenggara = "Pemkab Klaten bersama Dewan Kesenian Klaten",
            deskripsi = "Pementasan wayang kulit dalam rangka memperingati Hari Wayang Dunia dengan lakon Wahyu Katentreman.",
            caraDaftar = "Terbuka untuk masyarakat umum.",
            status = EventStatus.TERKONFIRMASI,
            sumberLabel = "Solopos",
            sumberUrl = "https://solopos.espos.id/hari-wayang-dunia-lakon-wahyu-katentreman-dipentaskan-di-rspd-klaten-2268859",
            posterUrl = null,
            butuhKonsumsi = false
        )
    )

    val radarEvents2027: List<RadarEvent> = listOf(
        RadarEvent(
            id = "radar-1",
            nama = "Grebeg Syawalan – Bukit Sidoguro",
            keterangan = "Bayat, Klaten. Pantau pengumuman menjelang Idulfitri/Syawal 2027."
        ),
        RadarEvent(
            id = "radar-2",
            nama = "Klaten Lurik Carnival",
            keterangan = "Pantau pengumuman pendaftaran peserta dan tanggal parade."
        ),
        RadarEvent(
            id = "radar-3",
            nama = "Yaa Qowiyyu – Jatinom",
            keterangan = "Pantau pengumuman resmi rangkaian tradisi dan puncak acara."
        ),
        RadarEvent(
            id = "radar-4",
            nama = "Festival Desa Wisata",
            keterangan = "Pantau desa peserta, jadwal festival, dan lokasi kegiatan."
        ),
        RadarEvent(
            id = "radar-5",
            nama = "Festival Candi Sojiwan",
            keterangan = "Pantau kegiatan seni budaya di kawasan Prambanan, Klaten."
        )
    )
}
