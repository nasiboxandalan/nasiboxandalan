package com.example

import com.example.data.EventCategory
import com.example.data.EventStatus
import com.example.data.EventsData
import com.example.data.EventsRepository
import com.example.data.PaketData
import com.example.data.Wilayah
import com.example.ui.AgendaUiState
import com.example.util.IndoDateUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun indonesianDateFormatting_andDateBlockParts_areAccurate() {
    assertEquals("Senin, 5 April 2027", IndoDateUtils.formatIndoFull("2027-04-05"))
    assertEquals("Jumat, 20 November 2026", IndoDateUtils.formatIndoFull("2026-11-20"))

    val sambungRasa = EventsData.initialEvents.first { it.slug == "sambung-rasa-klaten-2027" }
    val block = IndoDateUtils.getDateBlockParts(sambungRasa)
    assertEquals("05", block.topLabel)
    assertEquals("APR", block.bottomLabel)
    assertEquals("4 bulan lagi", IndoDateUtils.getCountdownLabel(sambungRasa, "2026-11-22"))

    val sambangCandi = EventsData.initialEvents.first { it.slug == "promo-sambang-candi-desember-2026" }
    assertEquals("DES", IndoDateUtils.getDateBlockParts(sambangCandi).topLabel)
    assertEquals("2026", IndoDateUtils.getDateBlockParts(sambangCandi).bottomLabel)
  }

  @Test
  fun javaneseGreeting_matchesHourOfDay() {
    assertEquals("Sugeng enjang", IndoDateUtils.getTimeGreetingJavanese(7))
    assertEquals("Sugeng siang", IndoDateUtils.getTimeGreetingJavanese(12))
    assertEquals("Sugeng sonten", IndoDateUtils.getTimeGreetingJavanese(16))
    assertEquals("Sugeng dalu", IndoDateUtils.getTimeGreetingJavanese(21))
  }

  @Test
  fun pastEvents_automaticallyMoveToArchive_andNewArticlesAreActive() {
    val state = AgendaUiState(todayIso = "2026-11-22", allEvents = EventsData.initialEvents)
    // 5 active (Sambang Candi, Swara Prambanan, Pilkades 2027, Sambung Rasa 2027, COE Jateng 2027)
    assertEquals(5, state.upcomingEvents.size)
    // 2 archived (Festival Kopi 2026, Wayang Kulit 2026)
    assertEquals(2, state.archivedEvents.size)
    assertEquals(5, state.radarEvents2027.size)

    assertTrue(state.upcomingEvents.any { it.slug == "promo-sambang-candi-desember-2026" })
    assertTrue(state.upcomingEvents.any { it.slug == "swara-prambanan-2026-desember" })
    assertTrue(state.upcomingEvents.any { it.slug == "pilkades-serentak-klaten-2027" })
    assertTrue(state.upcomingEvents.any { it.slug == "calendar-of-events-jateng-2027-klaten" })

    assertTrue(state.upcomingEvents.none { it.slug == "festival-kopi-klaten-2026" })
    assertTrue(state.archivedEvents.any { it.slug == "festival-kopi-klaten-2026" })
    val archivedCoffee = state.archivedEvents.first { it.slug == "festival-kopi-klaten-2026" }
    assertEquals(EventStatus.SUDAH_LEWAT, IndoDateUtils.effectiveStatus(archivedCoffee, "2026-11-22"))
  }

  @Test
  fun wilayahAndCategoryFilters_workCorrectly() {
    val boyolaliState = AgendaUiState(
      todayIso = "2026-11-22",
      homeSelectedWilayah = Wilayah.BOYOLALI
    )
    assertTrue(boyolaliState.filteredHomeUpcomingEvents.isEmpty())

    val klatenState = AgendaUiState(
      todayIso = "2026-11-22",
      homeSelectedWilayah = Wilayah.KLATEN
    )
    assertEquals(5, klatenState.filteredHomeUpcomingEvents.size)

    val categoryState = AgendaUiState(
      todayIso = "2026-11-22",
      homeSelectedCategory = EventCategory.BUDAYA
    )
    assertFalse(categoryState.filteredHomeUpcomingEvents.any { it.kategori == EventCategory.PEMERINTAHAN })
  }

  @Test
  fun remoteJsonParser_parsesGoogleSheetsRows() {
    val json = """
      [
        {
          "id": "byl-1",
          "slug": "pengajian-akbar-boyolali",
          "judul": "Pengajian Akbar Boyolali",
          "kategori": "Desa & Pengajian",
          "wilayah": "Boyolali",
          "kecamatan": "Mojosongo",
          "tanggalMulai": "2027-02-14",
          "lokasi": "Alun-Alun Kidul Boyolali",
          "penyelenggara": "Warga Mojosongo",
          "deskripsi": "Pengajian rutin warga.",
          "status": "Terkonfirmasi",
          "sumberLabel": "Panitia",
          "sumberUrl": "https://example.com",
          "posterUrl": "https://example.com/poster.jpg",
          "butuhKonsumsi": true
        }
      ]
    """.trimIndent()
    val parsed = EventsRepository.parseEventsJson(json)
    assertEquals(1, parsed.size)
    assertEquals(Wilayah.BOYOLALI, parsed[0].wilayah)
    assertEquals("Mojosongo", parsed[0].kecamatan)
    assertEquals(EventCategory.DESA_PENGAJIAN, parsed[0].kategori)
    assertEquals("https://example.com/poster.jpg", parsed[0].posterUrl)
    assertTrue(parsed[0].butuhKonsumsi)
  }

  @Test
  fun paketData_containsAllFiveOfficialPackages() {
    val paketList = PaketData.paketList
    assertEquals(5, paketList.size)
    assertEquals(
      listOf("Paket 1", "Paket 2", "Paket 3", "Paket Resmi", "Paket Hemat Resepsi"),
      paketList.map { it.nama }
    )
    assertEquals(
      listOf("Rp20.000 / box", "Rp22.000 / box", "Rp25.000 / box", "Rp34.000 / box", "Rp20.000 / box"),
      paketList.map { it.hargaLabel }
    )
    assertTrue(paketList.all { it.isiMenu.isNotEmpty() })
  }
}
