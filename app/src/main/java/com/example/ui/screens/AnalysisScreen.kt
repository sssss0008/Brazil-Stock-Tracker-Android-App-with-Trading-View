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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BrazilMarketData
import com.example.model.BrazilianStock
import com.example.ui.components.TradingViewWebView
import com.example.ui.theme.BrazilGold
import com.example.ui.theme.BrazilGreen
import com.example.util.LocalAppLanguage
import com.example.util.Strings
import com.example.util.TradingViewHtmlBuilder

data class AnalysisTab(
    val title: String,
    val icon: ImageVector
)

@Composable
fun AnalysisScreen(
    isDarkTheme: Boolean,
    selectedStock: BrazilianStock,
    onSelectStock: (BrazilianStock) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabs = remember(lang) {
        listOf(
            AnalysisTab(Strings.get(Strings.TECH_ANALYSIS, lang), Icons.Default.Speed),
            AnalysisTab(Strings.get(Strings.FINANCIALS, lang), Icons.Default.Assessment),
            AnalysisTab(Strings.get(Strings.COMPANY_PROFILE, lang), Icons.Default.Business),
            AnalysisTab(Strings.get(Strings.OVERVIEW, lang), Icons.Default.Info)
        )
    }

    // Prepare HTML content according to selected tab, stock, and TV locale
    val currentHtml = remember(selectedStock.symbol, selectedTabIndex, isDarkTheme, lang.tvLocale) {
        when (selectedTabIndex) {
            0 -> TradingViewHtmlBuilder.buildTechnicalAnalysisHtml(
                symbol = selectedStock.symbol,
                isDark = isDarkTheme,
                locale = lang.tvLocale
            )
            1 -> TradingViewHtmlBuilder.buildFinancialsHtml(
                symbol = selectedStock.symbol,
                isDark = isDarkTheme,
                locale = lang.tvLocale
            )
            2 -> TradingViewHtmlBuilder.buildCompanyProfileHtml(
                symbol = selectedStock.symbol,
                isDark = isDarkTheme,
                locale = lang.tvLocale
            )
            else -> TradingViewHtmlBuilder.buildSymbolDetailHtml(
                symbol = selectedStock.symbol,
                isDark = isDarkTheme,
                locale = lang.tvLocale
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Stock Picker & Valuation Header
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
                // Stock Identity & Current Price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = selectedStock.ticker,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(BrazilGreen.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = selectedStock.sector,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrazilGreen
                                )
                            }
                        }
                        Text(
                            text = selectedStock.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Price & Change
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = selectedStock.price,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = selectedStock.changePercent,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedStock.isPositive) Color(0xFF10B981) else Color(0xFFEF4444)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quick Valuation Ratios
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnalysisStatPill("P/L", selectedStock.peRatio)
                    AnalysisStatPill("Div. Yield", selectedStock.dividendYield)
                    AnalysisStatPill("Volume 24h", selectedStock.volume)
                    AnalysisStatPill("Cap. Mercado", selectedStock.marketCap)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quick Stock Switcher Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BrazilMarketData.topStocks.forEach { stock ->
                        val isSelected = stock.symbol == selectedStock.symbol
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) BrazilGold else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { onSelectStock(stock) }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = stock.ticker,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Sub Tabs: Technical Analysis, Financials, Company Profile, Symbol Info
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
            contentColor = MaterialTheme.colorScheme.primary,
            edgePadding = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedTabIndex == index
                Tab(
                    selected = isSelected,
                    onClick = { selectedTabIndex = index },
                    modifier = Modifier.testTag("analysis_tab_$index"),
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isSelected) BrazilGold else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tab.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp,
                                color = if (isSelected) BrazilGold else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
            }
        }

        // WebView Content Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            TradingViewWebView(
                htmlContent = currentHtml,
                key = "${selectedStock.symbol}_tab_${selectedTabIndex}_$isDarkTheme",
                testTag = "analysis_webview_$selectedTabIndex",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun AnalysisStatPill(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$label: ",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
