package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.EventCategory
import com.example.data.EventStatus
import com.example.data.Wilayah
import com.example.ui.QuickTimeFilter
import com.example.ui.theme.CardWhite
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenSoft
import com.example.ui.theme.IndigoLurik
import com.example.ui.theme.TurmericAmber
import com.example.ui.theme.TurmericSoft
import com.example.ui.theme.WarmCreamBorder

@Composable
fun EventSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    testTag: String = "search_input"
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = ForestGreen
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.cd_search_clear)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CardWhite,
            unfocusedContainerColor = CardWhite,
            focusedBorderColor = ForestGreen,
            unfocusedBorderColor = WarmCreamBorder
        )
    )
}

@Composable
fun WilayahFilterRow(
    selectedWilayah: Wilayah?,
    onSelectWilayah: (Wilayah?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedWilayah == null,
            onClick = { onSelectWilayah(null) },
            label = { Text("Semua Wilayah") },
            modifier = Modifier
                .heightIn(min = 48.dp)
                .testTag("wilayah_chip_semua"),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = ForestGreen,
                selectedLabelColor = Color.White,
                containerColor = CardWhite
            ),
            border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = selectedWilayah == null,
                borderColor = WarmCreamBorder
            )
        )
        FilterChip(
            selected = selectedWilayah == Wilayah.KLATEN,
            onClick = {
                onSelectWilayah(if (selectedWilayah == Wilayah.KLATEN) null else Wilayah.KLATEN)
            },
            label = { Text("Klaten") },
            modifier = Modifier
                .heightIn(min = 48.dp)
                .testTag("wilayah_chip_klaten"),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = ForestGreen,
                selectedLabelColor = Color.White,
                containerColor = CardWhite
            ),
            border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = selectedWilayah == Wilayah.KLATEN,
                borderColor = WarmCreamBorder
            )
        )
        FilterChip(
            selected = selectedWilayah == Wilayah.BOYOLALI,
            onClick = {
                onSelectWilayah(if (selectedWilayah == Wilayah.BOYOLALI) null else Wilayah.BOYOLALI)
            },
            label = { Text("Boyolali") },
            modifier = Modifier
                .heightIn(min = 48.dp)
                .testTag("wilayah_chip_boyolali"),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = IndigoLurik,
                selectedLabelColor = Color.White,
                containerColor = CardWhite
            ),
            border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = selectedWilayah == Wilayah.BOYOLALI,
                borderColor = WarmCreamBorder
            )
        )
    }
}

@Composable
fun QuickTimeFilterRow(
    selectedFilter: QuickTimeFilter,
    onSelectFilter: (QuickTimeFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        QuickTimeFilter.entries.forEach { filter ->
            val selected = selectedFilter == filter
            Surface(
                onClick = { onSelectFilter(filter) },
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .testTag("quick_filter_${filter.name.lowercase()}"),
                shape = RoundedCornerShape(14.dp),
                color = if (selected) TurmericAmber else CardWhite,
                border = BorderStroke(1.dp, if (selected) TurmericAmber else WarmCreamBorder)
            ) {
                Text(
                    text = filter.label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryFilterRow(
    selectedCategory: EventCategory?,
    onSelectCategory: (EventCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedCategory == null,
            onClick = { onSelectCategory(null) },
            label = { Text("Semua Kategori") },
            modifier = Modifier.heightIn(min = 48.dp),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = ForestGreen,
                selectedLabelColor = Color.White,
                containerColor = CardWhite
            ),
            border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = selectedCategory == null,
                borderColor = WarmCreamBorder
            )
        )
        EventCategory.entries.forEach { category ->
            val selected = selectedCategory == category
            FilterChip(
                selected = selected,
                onClick = { onSelectCategory(category) },
                leadingIcon = {
                    CategoryIllustrationIcon(category = category, size = 18.dp)
                },
                label = { Text(category.label) },
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .testTag("category_chip_${category.name.lowercase()}"),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ForestGreen,
                    selectedLabelColor = Color.White,
                    containerColor = CardWhite
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selected,
                    borderColor = WarmCreamBorder
                )
            )
        }
    }
}

@Composable
fun StatusFilterRow(
    selectedStatus: EventStatus?,
    onSelectStatus: (EventStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedStatus == null,
                onClick = { onSelectStatus(null) },
                label = { Text("Semua Status") },
                modifier = Modifier.heightIn(min = 48.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ForestGreenSoft,
                    selectedLabelColor = ForestGreen,
                    containerColor = CardWhite
                )
            )
            EventStatus.entries.forEach { status ->
                val selected = selectedStatus == status
                FilterChip(
                    selected = selected,
                    onClick = { onSelectStatus(status) },
                    label = { Text(status.label) },
                    modifier = Modifier.heightIn(min = 48.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TurmericSoft,
                        selectedLabelColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = CardWhite
                    )
                )
            }
        }
    }
}
