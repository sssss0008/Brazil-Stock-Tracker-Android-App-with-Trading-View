package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TradingViewWebView
import com.example.ui.theme.BrazilGold
import com.example.ui.theme.BrazilGreen
import com.example.util.LocalAppLanguage
import com.example.util.Strings
import com.example.util.TradingViewHtmlBuilder

@Composable
fun MarketScreen(
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    var selectedTab by remember { mutableIntStateOf(0) }

    val heatmapHtml = remember(isDarkTheme, lang.tvLocale) {
        TradingViewHtmlBuilder.buildHeatmapHtml(
            isDark = isDarkTheme,
            dataSource = "AllBrazil",
            locale = lang.tvLocale
        )
    }

    val screenerHtml = remember(isDarkTheme, lang.tvLocale) {
        TradingViewHtmlBuilder.buildScreenerHtml(
            isDark = isDarkTheme,
            locale = lang.tvLocale
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Modern Segmented Pill Header for Heatmap vs Screener
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp)) {
                // Segmented Pill Container
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Heatmap Tab
                    val isHeatmapSelected = selectedTab == 0
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isHeatmapSelected) BrazilGreen else Color.Transparent)
                            .clickable { selectedTab = 0 }
                            .padding(vertical = 8.dp)
                            .testTag("tab_heatmap"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.GridOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isHeatmapSelected) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = Strings.get(Strings.HEATMAP_TAB, lang),
                                fontWeight = if (isHeatmapSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp,
                                color = if (isHeatmapSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Screener Tab
                    val isScreenerSelected = selectedTab == 1
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isScreenerSelected) BrazilGreen else Color.Transparent)
                            .clickable { selectedTab = 1 }
                            .padding(vertical = 8.dp)
                            .testTag("tab_screener"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isScreenerSelected) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = Strings.get(Strings.SCREENER_TAB, lang),
                                fontWeight = if (isScreenerSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp,
                                color = if (isScreenerSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Helpful Visual Guide Banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = BrazilGold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (selectedTab == 0) {
                            Strings.get(Strings.HEATMAP_GUIDE, lang)
                        } else {
                            Strings.get(Strings.SCREENER_GUIDE, lang)
                        },
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Web Content Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (selectedTab) {
                0 -> {
                    TradingViewWebView(
                        htmlContent = heatmapHtml,
                        key = "heatmap_${isDarkTheme}_${lang.code}",
                        testTag = "stock_heatmap_webview",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                1 -> {
                    TradingViewWebView(
                        htmlContent = screenerHtml,
                        key = "screener_${isDarkTheme}_${lang.code}",
                        testTag = "stock_screener_webview",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
