package com.example

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BrazilMarketData
import com.example.model.BrazilianStock
import com.example.ui.components.MarketHeader
import com.example.ui.screens.AnalysisScreen
import com.example.ui.screens.ChartScreen
import com.example.ui.screens.EconomyScreen
import com.example.ui.screens.MarketScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.BrazilGold
import com.example.ui.theme.BrazilGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.util.AppLanguage
import com.example.util.LocalAppLanguage
import com.example.util.Strings

enum class NavTab(val titleKey: String, val icon: ImageVector, val tag: String) {
    CHARTS(Strings.NAV_CHARTS, Icons.Default.ShowChart, "nav_charts"),
    MARKET(Strings.NAV_MARKET, Icons.Default.GridOn, "nav_market"),
    ANALYSIS(Strings.NAV_ANALYSIS, Icons.Default.Speed, "nav_analysis"),
    ECONOMY(Strings.NAV_ECONOMY, Icons.Default.Public, "nav_economy"),
    WATCHLIST(Strings.NAV_WATCHLIST, Icons.Default.Star, "nav_watchlist")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BrazilMarketApp()
        }
    }
}

@Composable
fun BrazilMarketApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("brazil_market_tracker_prefs", Context.MODE_PRIVATE) }

    var isDarkTheme by remember {
        mutableStateOf(prefs.getBoolean("dark_theme", true))
    }

    val savedLangCode = prefs.getString("app_language", AppLanguage.PORTUGUESE.code)
    var currentLanguage by remember {
        mutableStateOf(AppLanguage.fromCode(savedLangCode))
    }

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    var selectedStock by remember {
        mutableStateOf(BrazilMarketData.topStocks[0]) // PETR4
    }

    var favoriteSymbols by remember {
        val saved = prefs.getStringSet("favorites", null)
        val initial = saved ?: setOf(
            "BMFBOVESPA:IBOV",
            "BMFBOVESPA:PETR4",
            "BMFBOVESPA:VALE3",
            "BMFBOVESPA:ITUB4",
            "BMFBOVESPA:WEGE3",
            "FX_IDC:USDBRL"
        )
        mutableStateOf(initial)
    }

    val toggleTheme: () -> Unit = {
        val newTheme = !isDarkTheme
        isDarkTheme = newTheme
        prefs.edit().putBoolean("dark_theme", newTheme).apply()
    }

    val toggleLanguage: () -> Unit = {
        val nextLang = if (currentLanguage == AppLanguage.PORTUGUESE) AppLanguage.ENGLISH else AppLanguage.PORTUGUESE
        currentLanguage = nextLang
        prefs.edit().putString("app_language", nextLang.code).apply()
    }

    val toggleFavorite: (String) -> Unit = { symbol ->
        val updated = if (favoriteSymbols.contains(symbol)) {
            favoriteSymbols - symbol
        } else {
            favoriteSymbols + symbol
        }
        favoriteSymbols = updated
        prefs.edit().putStringSet("favorites", updated).apply()
    }

    CompositionLocalProvider(LocalAppLanguage provides currentLanguage) {
        MyApplicationTheme(darkTheme = isDarkTheme) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    MarketHeader(
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = toggleTheme,
                        onToggleLanguage = toggleLanguage,
                        onOpenSearch = { selectedTab = 0 }
                    )
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .border(
                                width = 0.5.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            )
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .testTag("bottom_nav_bar")
                    ) {
                        NavTab.values().forEachIndexed { index, tab ->
                            val isSelected = selectedTab == index
                            val title = Strings.get(tab.titleKey, currentLanguage)
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { selectedTab = index },
                                modifier = Modifier.testTag(tab.tag),
                                icon = {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = title,
                                        tint = if (isSelected) BrazilGold else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                label = {
                                    Text(
                                        text = title,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) BrazilGreen else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = BrazilGreen.copy(alpha = 0.2f)
                                )
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (selectedTab) {
                        0 -> ChartScreen(
                            isDarkTheme = isDarkTheme,
                            selectedStock = selectedStock,
                            onSelectStock = { selectedStock = it },
                            favoriteSymbols = favoriteSymbols,
                            onToggleFavorite = toggleFavorite
                        )
                        1 -> MarketScreen(
                            isDarkTheme = isDarkTheme
                        )
                        2 -> AnalysisScreen(
                            isDarkTheme = isDarkTheme,
                            selectedStock = selectedStock,
                            onSelectStock = { selectedStock = it }
                        )
                        3 -> EconomyScreen(
                            isDarkTheme = isDarkTheme
                        )
                        4 -> WatchlistScreen(
                            favoriteSymbols = favoriteSymbols,
                            onToggleFavorite = toggleFavorite,
                            onOpenChart = {
                                selectedStock = it
                                selectedTab = 0
                            },
                            onOpenAnalysis = {
                                selectedStock = it
                                selectedTab = 2
                            },
                            onAddNewSymbol = {
                                selectedTab = 0
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme { Greeting("Android") }
}

