package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.text.font.FontFamily
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
fun EconomyScreen(
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    var selectedTab by remember { mutableIntStateOf(0) }

    val calendarHtml = remember(isDarkTheme, lang.tvLocale) {
        TradingViewHtmlBuilder.buildEconomicCalendarHtml(
            isDark = isDarkTheme,
            locale = lang.tvLocale
        )
    }

    val economicMapHtml = remember(isDarkTheme, lang.tvLocale) {
        TradingViewHtmlBuilder.buildEconomicMapHtml(
            isDark = isDarkTheme,
            locale = lang.tvLocale
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Macro Dashboard Banner (Key Brazilian Economic Indicators)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = Strings.get(Strings.MACRO_TITLE, lang),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "BACEN / IBGE",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Horizontal Macro Ticker Cards
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MacroIndicatorCard("SELIC (Copom)", "10,50%", "Meta Anual", BrazilGreen)
                    MacroIndicatorCard("IPCA (12m)", "4,24%", "Inflação Oficial", BrazilGold)
                    MacroIndicatorCard("USD/BRL", "R$ 5,42", "PTAX Spot", Color(0xFF38BDF8))
                    MacroIndicatorCard("PIB / GDP", "+2,1%", "Crescimento Anual", BrazilGreen)
                    MacroIndicatorCard("CDS 5Y", "210 pts", "Risco País", Color(0xFFA78BFA))
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Segmented Pill Tab Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Economic Calendar Tab
                    val isCalendarSelected = selectedTab == 0
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isCalendarSelected) BrazilGreen else Color.Transparent)
                            .clickable { selectedTab = 0 }
                            .padding(vertical = 8.dp)
                            .testTag("tab_calendar"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isCalendarSelected) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = Strings.get(Strings.CALENDAR_TAB, lang),
                                fontWeight = if (isCalendarSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp,
                                color = if (isCalendarSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Global Economic Map Tab
                    val isMapSelected = selectedTab == 1
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isMapSelected) BrazilGreen else Color.Transparent)
                            .clickable { selectedTab = 1 }
                            .padding(vertical = 8.dp)
                            .testTag("tab_economic_map"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isMapSelected) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = Strings.get(Strings.ECONOMIC_MAP_TAB, lang),
                                fontWeight = if (isMapSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp,
                                color = if (isMapSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // WebView Content Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (selectedTab) {
                0 -> {
                    TradingViewWebView(
                        htmlContent = calendarHtml,
                        key = "calendar_${isDarkTheme}_${lang.code}",
                        testTag = "economic_calendar_webview",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                1 -> {
                    TradingViewWebView(
                        htmlContent = economicMapHtml,
                        key = "economic_map_${isDarkTheme}_${lang.code}",
                        testTag = "economic_map_webview",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun MacroIndicatorCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = title,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Black,
            color = accentColor
        )
        Text(
            text = subtitle,
            fontSize = 9.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
        )
    }
}
