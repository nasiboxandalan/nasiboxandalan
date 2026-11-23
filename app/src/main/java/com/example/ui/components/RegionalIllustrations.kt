package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.Wilayah
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenLight
import com.example.ui.theme.ForestGreenSoft
import com.example.ui.theme.IndigoLurik
import com.example.ui.theme.IndigoSoft
import com.example.ui.theme.SogaBrown
import com.example.ui.theme.SogaSoft
import com.example.ui.theme.TurmericAmber
import com.example.ui.theme.TurmericSoft
import com.example.ui.theme.WarmCream

/**
 * Pemisah bermotif kain lurik khas Klaten (hijau, kunyit, soga, dan indigo).
 */
@Composable
fun LurikDivider(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(7.dp)
    ) {
        val pattern = listOf(
            ForestGreen to 6f,
            Color.Transparent to 2.5f,
            TurmericAmber to 2.5f,
            Color.Transparent to 2.5f,
            SogaBrown to 3f,
            Color.Transparent to 2.5f,
            IndigoLurik to 2f,
            Color.Transparent to 2.5f
        )
        val unit = 2.dp.toPx()
        var x = 0f
        var i = 0
        while (x < size.width) {
            val (color, widthUnits) = pattern[i % pattern.size]
            val segW = widthUnits * unit
            if (color != Color.Transparent) {
                drawRect(color = color, topLeft = Offset(x, 0f), size = Size(segW, size.height))
            }
            x += segW
            i++
        }
    }
}

/**
 * Logo siluet Joglo + Gunungan Wayang bergaya senada dengan ilustrasi hero.
 */
@Composable
fun JogloGununganEmblem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(ForestGreen),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(28.dp)) {
            val w = size.width
            val h = size.height

            // Gunungan / Joglo roof peak (Turmeric Amber)
            val upperRoof = Path().apply {
                moveTo(w * 0.50f, h * 0.10f)
                lineTo(w * 0.72f, h * 0.36f)
                lineTo(w * 0.28f, h * 0.36f)
                close()
            }
            drawPath(upperRoof, color = TurmericAmber)

            // Joglo wide eaves (Warm Cream)
            val lowerEaves = Path().apply {
                moveTo(w * 0.22f, h * 0.38f)
                lineTo(w * 0.78f, h * 0.38f)
                lineTo(w * 0.90f, h * 0.54f)
                lineTo(w * 0.10f, h * 0.54f)
                close()
            }
            drawPath(lowerEaves, color = WarmCream)

            // Pendopo pillars
            val pillarW = w * 0.08f
            drawRect(WarmCream, Offset(w * 0.22f, h * 0.57f), Size(pillarW, h * 0.23f))
            drawRect(WarmCream, Offset(w * 0.46f, h * 0.57f), Size(pillarW, h * 0.23f))
            drawRect(WarmCream, Offset(w * 0.70f, h * 0.57f), Size(pillarW, h * 0.23f))

            // Floor base
            drawRoundRect(
                color = TurmericAmber,
                topLeft = Offset(w * 0.14f, h * 0.80f),
                size = Size(w * 0.72f, h * 0.09f),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}

/**
 * Banner ilustrasi khas wilayah:
 * - Klaten: Siluet candi + sawah + aksen lurik
 * - Boyolali: Siluet Gunung Merapi-Merbabu + sapi perah
 */
@Composable
fun RegionalThemeCard(
    wilayah: Wilayah,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val isBoyolali = wilayah == Wilayah.BOYOLALI
    val containerColor = if (isBoyolali) IndigoSoft else ForestGreenSoft
    val titleColor = if (isBoyolali) IndigoLurik else ForestGreenDark

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = containerColor
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isBoyolali) Color.White else TurmericSoft)
            ) {
                if (isBoyolali) {
                    BoyolaliMerapiCowCanvas(modifier = Modifier.fillMaxSize())
                } else {
                    KlatenCandiSawahCanvas(modifier = Modifier.fillMaxSize())
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun KlatenCandiSawahCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Sun
        drawCircle(
            color = TurmericAmber,
            radius = w * 0.14f,
            center = Offset(w * 0.76f, h * 0.24f)
        )

        // Candi silhouette (center-left)
        val candi = Path().apply {
            moveTo(w * 0.36f, h * 0.18f)
            lineTo(w * 0.44f, h * 0.34f)
            lineTo(w * 0.48f, h * 0.34f)
            lineTo(w * 0.52f, h * 0.50f)
            lineTo(w * 0.56f, h * 0.72f)
            lineTo(w * 0.16f, h * 0.72f)
            lineTo(w * 0.20f, h * 0.50f)
            lineTo(w * 0.24f, h * 0.34f)
            lineTo(w * 0.28f, h * 0.34f)
            close()
        }
        drawPath(candi, color = SogaBrown)

        // Small stupa/candi beside
        val smallCandi = Path().apply {
            moveTo(w * 0.66f, h * 0.42f)
            lineTo(w * 0.76f, h * 0.72f)
            lineTo(w * 0.56f, h * 0.72f)
            close()
        }
        drawPath(smallCandi, color = SogaBrown.copy(alpha = 0.8f))

        // Green rice fields foreground
        drawRect(ForestGreenLight, Offset(0f, h * 0.70f), Size(w, h * 0.16f))
        drawRect(ForestGreen, Offset(0f, h * 0.84f), Size(w, h * 0.16f))
    }
}

@Composable
fun BoyolaliMerapiCowCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Sky tint
        drawRect(IndigoSoft, Offset.Zero, size)

        // Merbabu & Merapi twin silhouettes
        val merbabu = Path().apply {
            moveTo(w * 0.02f, h * 0.72f)
            lineTo(w * 0.34f, h * 0.26f)
            lineTo(w * 0.66f, h * 0.72f)
            close()
        }
        drawPath(merbabu, color = IndigoLurik.copy(alpha = 0.78f))

        val merapi = Path().apply {
            moveTo(w * 0.42f, h * 0.72f)
            lineTo(w * 0.72f, h * 0.22f)
            lineTo(w * 0.98f, h * 0.72f)
            close()
        }
        drawPath(merapi, color = IndigoLurik)

        // Green pasture hill
        drawRect(ForestGreen, Offset(0f, h * 0.70f), Size(w, h * 0.30f))

        // Stylized dairy cow (sapi perah) silhouette in foreground
        drawRoundRect(
            color = WarmCream,
            topLeft = Offset(w * 0.24f, h * 0.58f),
            size = Size(w * 0.34f, h * 0.20f),
            cornerRadius = CornerRadius(10f, 10f)
        )
        // Cow black spots
        drawCircle(Color(0xFF1E293B), radius = w * 0.05f, center = Offset(w * 0.36f, h * 0.65f))
        drawCircle(Color(0xFF1E293B), radius = w * 0.04f, center = Offset(w * 0.48f, h * 0.70f))
        // Cow head
        drawRoundRect(
            color = WarmCream,
            topLeft = Offset(w * 0.54f, h * 0.54f),
            size = Size(w * 0.14f, h * 0.13f),
            cornerRadius = CornerRadius(6f, 6f)
        )
        // Cow legs
        drawRect(WarmCream, Offset(w * 0.28f, h * 0.76f), Size(w * 0.05f, h * 0.10f))
        drawRect(WarmCream, Offset(w * 0.49f, h * 0.76f), Size(w * 0.05f, h * 0.10f))
    }
}
