package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.EventCategory
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.IndigoLurik
import com.example.ui.theme.SogaBrown
import com.example.ui.theme.TurmericAmber
import com.example.ui.theme.WarmCream

/**
 * Ikon ilustratif bergaya flat selaras dengan palet hero untuk 9 kategori acara.
 */
@Composable
fun CategoryIllustrationIcon(
    category: EventCategory,
    size: Dp = 20.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        when (category) {
            EventCategory.FESTIVAL -> {
                // Festive pennant banner + spark
                drawRect(SogaBrown, Offset(w * 0.14f, h * 0.18f), Size(w * 0.08f, h * 0.70f))
                val flag1 = Path().apply {
                    moveTo(w * 0.22f, h * 0.20f)
                    lineTo(w * 0.86f, h * 0.34f)
                    lineTo(w * 0.22f, h * 0.50f)
                    close()
                }
                drawPath(flag1, TurmericAmber)
                drawCircle(ForestGreen, radius = w * 0.11f, center = Offset(w * 0.74f, h * 0.70f))
                drawCircle(IndigoLurik, radius = w * 0.07f, center = Offset(w * 0.48f, h * 0.74f))
            }

            EventCategory.SEMINAR_WORKSHOP -> {
                // Presentation board + stand
                drawRoundRect(
                    color = IndigoLurik,
                    topLeft = Offset(w * 0.14f, h * 0.16f),
                    size = Size(w * 0.72f, h * 0.48f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
                drawRect(WarmCream, Offset(w * 0.24f, h * 0.28f), Size(w * 0.42f, h * 0.08f))
                drawRect(TurmericAmber, Offset(w * 0.24f, h * 0.42f), Size(w * 0.52f, h * 0.08f))
                drawRect(SogaBrown, Offset(w * 0.46f, h * 0.64f), Size(w * 0.08f, h * 0.22f))
                drawRect(SogaBrown, Offset(w * 0.28f, h * 0.82f), Size(w * 0.44f, h * 0.07f))
            }

            EventCategory.KOMUNITAS -> {
                // Three people silhouettes gathering
                drawCircle(TurmericAmber, radius = w * 0.14f, center = Offset(w * 0.50f, h * 0.28f))
                drawRoundRect(ForestGreen, Offset(w * 0.34f, h * 0.44f), Size(w * 0.32f, h * 0.38f), CornerRadius(6f, 6f))
                drawCircle(IndigoLurik, radius = w * 0.11f, center = Offset(w * 0.22f, h * 0.38f))
                drawRoundRect(IndigoLurik, Offset(w * 0.10f, h * 0.52f), Size(w * 0.22f, h * 0.30f), CornerRadius(5f, 5f))
                drawCircle(SogaBrown, radius = w * 0.11f, center = Offset(w * 0.78f, h * 0.38f))
                drawRoundRect(SogaBrown, Offset(w * 0.68f, h * 0.52f), Size(w * 0.22f, h * 0.30f), CornerRadius(5f, 5f))
            }

            EventCategory.GATHERING_KANTOR -> {
                // Briefcase + warm star accent
                drawRoundRect(
                    color = SogaBrown,
                    topLeft = Offset(w * 0.14f, h * 0.30f),
                    size = Size(w * 0.72f, h * 0.54f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                drawRoundRect(
                    color = IndigoLurik,
                    topLeft = Offset(w * 0.36f, h * 0.16f),
                    size = Size(w * 0.28f, h * 0.16f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
                drawRect(TurmericAmber, Offset(w * 0.14f, h * 0.50f), Size(w * 0.72f, h * 0.10f))
            }

            EventCategory.WISATA_ROMBONGAN -> {
                // Mountain + tour bus badge
                val hill = Path().apply {
                    moveTo(w * 0.08f, h * 0.62f)
                    lineTo(w * 0.38f, h * 0.20f)
                    lineTo(w * 0.68f, h * 0.62f)
                    close()
                }
                drawPath(hill, ForestGreen)
                drawCircle(TurmericAmber, radius = w * 0.12f, center = Offset(w * 0.76f, h * 0.26f))
                drawRoundRect(
                    color = IndigoLurik,
                    topLeft = Offset(w * 0.18f, h * 0.54f),
                    size = Size(w * 0.64f, h * 0.28f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                drawCircle(TurmericAmber, radius = w * 0.07f, center = Offset(w * 0.32f, h * 0.84f))
                drawCircle(TurmericAmber, radius = w * 0.07f, center = Offset(w * 0.68f, h * 0.84f))
            }

            EventCategory.DESA_PENGAJIAN -> {
                // Mosque dome + crescent/joglo pavilion
                drawCircle(ForestGreen, radius = w * 0.26f, center = Offset(w * 0.50f, h * 0.46f))
                drawRect(WarmCream, Offset(w * 0.20f, h * 0.48f), Size(w * 0.60f, h * 0.38f))
                drawRect(ForestGreen, Offset(w * 0.20f, h * 0.48f), Size(w * 0.60f, h * 0.38f))
                drawRoundRect(
                    color = TurmericAmber,
                    topLeft = Offset(w * 0.40f, h * 0.58f),
                    size = Size(w * 0.20f, h * 0.28f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawCircle(TurmericAmber, radius = w * 0.07f, center = Offset(w * 0.50f, h * 0.16f))
            }

            EventCategory.BUDAYA -> {
                // Gunungan wayang silhouette
                val gunungan = Path().apply {
                    moveTo(w * 0.50f, h * 0.08f)
                    lineTo(w * 0.80f, h * 0.62f)
                    lineTo(w * 0.70f, h * 0.86f)
                    lineTo(w * 0.30f, h * 0.86f)
                    lineTo(w * 0.20f, h * 0.62f)
                    close()
                }
                drawPath(gunungan, SogaBrown)
                val inner = Path().apply {
                    moveTo(w * 0.50f, h * 0.24f)
                    lineTo(w * 0.64f, h * 0.62f)
                    lineTo(w * 0.36f, h * 0.62f)
                    close()
                }
                drawPath(inner, TurmericAmber)
            }

            EventCategory.PEMERINTAHAN -> {
                // Pendopo pemerintahan pillars
                val roof = Path().apply {
                    moveTo(w * 0.50f, h * 0.14f)
                    lineTo(w * 0.88f, h * 0.38f)
                    lineTo(w * 0.12f, h * 0.38f)
                    close()
                }
                drawPath(roof, IndigoLurik)
                drawRect(SogaBrown, Offset(w * 0.22f, h * 0.42f), Size(w * 0.10f, h * 0.34f))
                drawRect(SogaBrown, Offset(w * 0.45f, h * 0.42f), Size(w * 0.10f, h * 0.34f))
                drawRect(SogaBrown, Offset(w * 0.68f, h * 0.42f), Size(w * 0.10f, h * 0.34f))
                drawRect(TurmericAmber, Offset(w * 0.14f, h * 0.78f), Size(w * 0.72f, h * 0.10f))
            }

            EventCategory.KULINER_UMKM -> {
                // Steaming bowl / tumpeng culinary icon
                drawArc(
                    color = SogaBrown,
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(w * 0.14f, h * 0.28f),
                    size = Size(w * 0.72f, h * 0.84f)
                )
                drawRect(TurmericAmber, Offset(w * 0.22f, h * 0.46f), Size(w * 0.56f, h * 0.08f))
                drawCircle(ForestGreen, radius = w * 0.08f, center = Offset(w * 0.38f, h * 0.26f))
                drawCircle(TurmericAmber, radius = w * 0.08f, center = Offset(w * 0.60f, h * 0.22f))
            }
        }
    }
}
