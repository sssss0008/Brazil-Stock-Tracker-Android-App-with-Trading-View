package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CandlestickChart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BrazilMarketData
import com.example.model.BrazilianStock
import com.example.ui.components.MarketTickerTape
import com.example.ui.components.TradingViewWebView
import com.example.ui.theme.BrazilGold
import com.example.ui.theme.BrazilGreen
import com.example.util.LocalAppLanguage
import com.example.util.Strings
import com.example.util.TradingViewHtmlBuilder

@Composable
fun ChartScreen(
    isDarkTheme: Boolean,
    selectedStock: BrazilianStock,
    onSelectStock: (BrazilianStock) -> Unit,
    favoriteSymbols: Set<String>,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    var selectedInterval by remember { mutableStateOf("D") }
    var selectedStyle by remember { mutableStateOf("1") } // 1: Candles, 2: Line, 3: Area
    var isFullscreenChart by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var customSearchText by remember { mutableStateOf("") }

    val chartHtml = remember(selectedStock.symbol, isDarkTheme, selectedInterval, selectedStyle, lang.tvLocale) {
        TradingViewHtmlBuilder.buildAdvancedChartHtml(
            symbol = selectedStock.symbol,
            isDark = isDarkTheme,
            interval = selectedInterval,
            style = selectedStyle,
            locale = lang.tvLocale
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Ticker Tape with quick navigation (Hidden in fullscreen mode)
        if (!isFullscreenChart) {
            MarketTickerTape(
                onSelectStock = onSelectStock,
                modifier = Modifier.fillMaxWidth()
            )

            // Hero Active Stock Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    // Header Row: Ticker, Name, Price & Action Icons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = selectedStock.ticker,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = selectedStock.sector,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }

                            Text(
                                text = selectedStock.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Price & Badges
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = selectedStock.price,
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            val isPositive = selectedStock.isPositive
                            val changeColor = if (isPositive) Color(0xFF10B981) else Color(0xFFEF4444)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(changeColor.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${selectedStock.changePercent} (${selectedStock.changeValue})",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = changeColor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 24h High/Low Visual Range Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = Strings.get(Strings.TIME_24H_MIN, lang),
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = selectedStock.dailyLow,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Gradient range track
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 12.dp)
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.65f)
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(Color(0xFFEF4444), BrazilGold, Color(0xFF10B981))
                                        )
                                    )
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = Strings.get(Strings.TIME_24H_MAX, lang),
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = selectedStock.dailyHigh,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick Financial Metrics Chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MetricBadge(label = Strings.get(Strings.VOLUME, lang), value = selectedStock.volume)
                        MetricBadge(label = Strings.get(Strings.PE_RATIO, lang), value = selectedStock.peRatio)
                        MetricBadge(label = Strings.get(Strings.DIV_YIELD, lang), value = selectedStock.dividendYield)
                        MetricBadge(label = Strings.get(Strings.MARKET_CAP, lang), value = selectedStock.marketCap)
                        MetricBadge(label = Strings.get(Strings.HIGH_52W, lang), value = selectedStock.high52w)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Blue Chips Selector Row + Action Icons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val quickAssets = remember {
                                listOf(
                                    BrazilMarketData.benchmarkIndices[0], // IBOV
                                    BrazilMarketData.benchmarkIndices[1], // USDBRL
                                    BrazilMarketData.topStocks[0],       // PETR4
                                    BrazilMarketData.topStocks[1],       // VALE3
                                    BrazilMarketData.topStocks[2],       // ITUB4
                                    BrazilMarketData.topStocks[3],       // BBDC4
                                    BrazilMarketData.topStocks[4],       // BBAS3
                                    BrazilMarketData.topStocks[6],       // WEGE3
                                    BrazilMarketData.topStocks[11],      // EMBR3
                                    BrazilMarketData.topStocks[12]       // PRIO3
                                )
                            }

                            quickAssets.forEach { stock ->
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
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = stock.ticker,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
                                        color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Favorite Star Button
                        val isFav = favoriteSymbols.contains(selectedStock.symbol)
                        IconButton(
                            onClick = { onToggleFavorite(selectedStock.symbol) },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Icon(
                                imageVector = if (isFav) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Favoritar",
                                tint = if (isFav) BrazilGold else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Search Dialog Button
                        IconButton(
                            onClick = { showSearchDialog = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .testTag("search_ticker_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar Ativo",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Trading Toolbar (Timeframes + Style + Fullscreen Toggle)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
            tonalElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Timeframe Selectors
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val intervals = listOf(
                        "1" to "1m",
                        "5" to "5m",
                        "15" to "15m",
                        "60" to "1h",
                        "D" to "1D",
                        "W" to "1S",
                        "M" to "1M"
                    )
                    intervals.forEach { (code, label) ->
                        val isSelected = selectedInterval == code
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) BrazilGold else Color.Transparent)
                                .clickable { selectedInterval = code }
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Chart Style & Fullscreen Action Group
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Candles toggle
                    IconButton(
                        onClick = { selectedStyle = "1" },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CandlestickChart,
                            contentDescription = "Candlesticks",
                            tint = if (selectedStyle == "1") BrazilGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Line toggle
                    IconButton(
                        onClick = { selectedStyle = "2" },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShowChart,
                            contentDescription = "Linha",
                            tint = if (selectedStyle == "2") BrazilGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Area toggle
                    IconButton(
                        onClick = { selectedStyle = "3" },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timeline,
                            contentDescription = "Área",
                            tint = if (selectedStyle == "3") BrazilGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Fullscreen Toggle
                    IconButton(
                        onClick = { isFullscreenChart = !isFullscreenChart },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = if (isFullscreenChart) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                            contentDescription = "Tela Cheia",
                            tint = BrazilGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Interactive TradingView Advanced Chart Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            TradingViewWebView(
                htmlContent = chartHtml,
                key = "${selectedStock.symbol}_${selectedInterval}_${selectedStyle}_$isDarkTheme",
                testTag = "advanced_chart_webview",
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    // High-Fidelity Search & Asset Explorer Dialog
    if (showSearchDialog) {
        AlertDialog(
            onDismissRequest = { showSearchDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = Strings.get(Strings.EXPLORE_B3_ASSETS, lang),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = { showSearchDialog = false }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = Strings.get(Strings.CANCEL, lang))
                    }
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = customSearchText,
                        onValueChange = { customSearchText = it },
                        placeholder = { Text(Strings.get(Strings.SEARCH_TICKER_PLACEHOLDER, lang)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = BrazilGold)
                        },
                        trailingIcon = {
                            if (customSearchText.isNotEmpty()) {
                                IconButton(onClick = { customSearchText = "" }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_ticker_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = Strings.get(Strings.SUGGESTIONS, lang),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    val filteredAssets = remember(customSearchText) {
                        val query = customSearchText.trim().uppercase()
                        if (query.isEmpty()) BrazilMarketData.allAssets.take(8)
                        else BrazilMarketData.allAssets.filter {
                            it.ticker.contains(query, ignoreCase = true) ||
                            it.name.contains(query, ignoreCase = true) ||
                            it.sector.contains(query, ignoreCase = true)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(filteredAssets) { stock ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .clickable {
                                        onSelectStock(stock)
                                        showSearchDialog = false
                                        customSearchText = ""
                                    }
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = stock.ticker.take(4),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = stock.ticker,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = stock.name,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = stock.price,
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = stock.changePercent,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (stock.isPositive) Color(0xFF10B981) else Color(0xFFEF4444)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val input = customSearchText.trim().uppercase()
                        if (input.isNotEmpty()) {
                            val found = BrazilMarketData.findStock(input) ?: BrazilianStock(
                                symbol = if (input.contains(":")) input else "BMFBOVESPA:$input",
                                ticker = if (input.contains(":")) input.substringAfter(":") else input,
                                name = "$input (B3)",
                                sector = "Ação / B3",
                                description = "Ativo da bolsa de valores B3."
                            )
                            onSelectStock(found)
                        }
                        showSearchDialog = false
                    },
                    modifier = Modifier.testTag("search_ticker_confirm")
                ) {
                    Text(Strings.get(Strings.OPEN_ASSET, lang), color = BrazilGreen, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSearchDialog = false }) {
                    Text(Strings.get(Strings.CANCEL, lang))
                }
            }
        )
    }
}

@Composable
private fun MetricBadge(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
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
