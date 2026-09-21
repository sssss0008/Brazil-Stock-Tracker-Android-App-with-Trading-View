package com.example.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

enum class AppLanguage(val code: String, val displayName: String, val flag: String, val tvLocale: String) {
    PORTUGUESE("pt", "Português (BR)", "🇧🇷", "br"),
    ENGLISH("en", "English", "🇺🇸", "en");

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return when (code?.lowercase()) {
                "en" -> ENGLISH
                else -> PORTUGUESE
            }
        }
    }
}

val LocalAppLanguage = compositionLocalOf { AppLanguage.PORTUGUESE }

object Strings {
    fun get(key: String, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.PORTUGUESE -> PT[key] ?: key
            AppLanguage.ENGLISH -> EN[key] ?: key
        }
    }

    // Bottom Navigation
    const val NAV_CHARTS = "nav_charts"
    const val NAV_MARKET = "nav_market"
    const val NAV_ANALYSIS = "nav_analysis"
    const val NAV_ECONOMY = "nav_economy"
    const val NAV_WATCHLIST = "nav_watchlist"

    // Header
    const val APP_TITLE = "app_title"
    const val MARKET_OPEN = "market_open"
    const val MARKET_CLOSED = "market_closed"
    const val SEARCH_ASSET = "search_asset"
    const val TOGGLE_THEME = "toggle_theme"
    const val SWITCH_LANG = "switch_lang"

    // Chart Screen
    const val B3_LIVE_CHART = "b3_live_chart"
    const val FULLSCREEN = "fullscreen"
    const val EXIT_FULLSCREEN = "exit_fullscreen"
    const val SEARCH_TICKER_PLACEHOLDER = "search_ticker_placeholder"
    const val EXPLORE_B3_ASSETS = "explore_b3_assets"
    const val OPEN_ASSET = "open_asset"
    const val CANCEL = "cancel"
    const val TIME_24H_MIN = "time_24h_min"
    const val TIME_24H_MAX = "time_24h_max"
    const val VOLUME = "volume"
    const val PE_RATIO = "pe_ratio"
    const val DIV_YIELD = "div_yield"
    const val MARKET_CAP = "market_cap"
    const val HIGH_52W = "high_52w"
    const val LOW_52W = "low_52w"
    const val LIVE_STREAMING = "live_streaming"
    const val POWERED_BY_TRADINGVIEW = "powered_by_tradingview"
    const val TIP_CHART = "tip_chart"
    const val ALL = "all"
    const val STOCKS = "stocks"
    const val INDICES = "indices"
    const val FOREX = "forex"

    // Market Screen
    const val HEATMAP_TAB = "heatmap_tab"
    const val SCREENER_TAB = "screener_tab"
    const val HEATMAP_GUIDE = "heatmap_guide"
    const val SCREENER_GUIDE = "screener_guide"

    // Analysis Screen
    const val TECH_ANALYSIS_TAB = "tech_analysis_tab"
    const val FINANCIALS_TAB = "financials_tab"
    const val PROFILE_TAB = "profile_tab"
    const val OVERVIEW_TAB = "overview_tab"
    const val TECH_RATING_NOTE = "tech_rating_note"

    // Analysis Screen Aliases
    const val TECH_ANALYSIS = TECH_ANALYSIS_TAB
    const val FINANCIALS = FINANCIALS_TAB
    const val COMPANY_PROFILE = PROFILE_TAB
    const val OVERVIEW = OVERVIEW_TAB

    // Economy Screen
    const val MACRO_TITLE = "macro_title"
    const val MACRO_SOURCE = "macro_source"
    const val CALENDAR_TAB = "calendar_tab"
    const val MAP_TAB = "map_tab"
    const val ECONOMIC_MAP_TAB = MAP_TAB
    const val CALENDAR_SUBTITLE = "calendar_subtitle"
    const val MAP_SUBTITLE = "map_subtitle"
    const val SELIC = "selic"
    const val IPCA = "ipca"
    const val USDBRL = "usdbrl"
    const val GDP = "gdp"
    const val RISK = "risk"

    // Watchlist Screen
    const val WATCHLIST_EMPTY_TITLE = "watchlist_empty_title"
    const val WATCHLIST_EMPTY_SUBTITLE = "watchlist_empty_subtitle"
    const val EMPTY_FAVORITES = WATCHLIST_EMPTY_TITLE
    const val EMPTY_FAVORITES_SUB = WATCHLIST_EMPTY_SUBTITLE
    const val FAVORITES_TITLE = "monitored_assets"
    const val TAP_TO_OPEN_CHART = "tap_to_view_chart"
    const val SUGGESTIONS = "suggestions"
    const val MONITORED_ASSETS = "monitored_assets"
    const val TAP_TO_VIEW_CHART = "tap_to_view_chart"
    const val VIEW_CHART = "view_chart"
    const val TECHNICALS = "technicals"
    const val REMOVE = "remove"

    // Widget states
    const val LOADING_WIDGET = "loading_widget"
    const val WIDGET_LOAD_ERROR = "widget_load_error"
    const val CHECK_CONNECTION = "check_connection"
    const val RETRY_LOAD = "retry_load"

    private val PT = mapOf(
        NAV_CHARTS to "Gráficos",
        NAV_MARKET to "Mercado",
        NAV_ANALYSIS to "Análise",
        NAV_ECONOMY to "Economia",
        NAV_WATCHLIST to "Favoritos",

        APP_TITLE to "BRAZIL MARKET",
        MARKET_OPEN to "PREGÃO AO VIVO",
        MARKET_CLOSED to "MERCADO FECHADO",
        SEARCH_ASSET to "Buscar Ativo B3",
        TOGGLE_THEME to "Alternar Tema",
        SWITCH_LANG to "Mudar Idioma",

        B3_LIVE_CHART to "Gráfico em Tempo Real B3",
        FULLSCREEN to "Tela Cheia",
        EXIT_FULLSCREEN to "Sair da Tela Cheia",
        SEARCH_TICKER_PLACEHOLDER to "Digite o código (ex: PETR4, VALE3, IBOV)...",
        EXPLORE_B3_ASSETS to "Buscar Ativos B3",
        OPEN_ASSET to "Abrir no Gráfico",
        CANCEL to "Cancelar",
        TIME_24H_MIN to "24h Mín",
        TIME_24H_MAX to "24h Máx",
        VOLUME to "Volume",
        PE_RATIO to "P/L",
        DIV_YIELD to "Div. Yield",
        MARKET_CAP to "Cap. Mercado",
        HIGH_52W to "52s Máx",
        LOW_52W to "52s Mín",
        LIVE_STREAMING to "Cotações e Gráficos TradingView em Tempo Real",
        POWERED_BY_TRADINGVIEW to "Dados em tempo real fornecidos por TradingView B3",
        TIP_CHART to "Arraste para mover, use pinça para zoom ou toque nos indicadores.",
        ALL to "Tudo",
        STOCKS to "Ações",
        INDICES to "Índices",
        FOREX to "Câmbio",

        HEATMAP_TAB to "Mapa de Calor B3",
        SCREENER_TAB to "Rastreador B3",
        HEATMAP_GUIDE to "O tamanho dos blocos reflete o valor de mercado. As cores verde e vermelha indicam a variação percentual ao vivo via TradingView.",
        SCREENER_GUIDE to "Filtre todas as ações listadas na B3 por preço, volume diário, P/L, variação e setores com dados oficiais.",

        TECH_ANALYSIS_TAB to "Análise Técnica",
        FINANCIALS_TAB to "Demonstrativos",
        PROFILE_TAB to "Perfil da Empresa",
        OVERVIEW_TAB to "Visão Geral",
        TECH_RATING_NOTE to "Indicadores osciladores e médias móveis consolidados em tempo real.",

        MACRO_TITLE to "Indicadores Macroeconômicos Brasil",
        MACRO_SOURCE to "TradingView • Eventos Econômicos",
        CALENDAR_TAB to "Calendário Econômico",
        MAP_TAB to "Mapa Macroeconômico",
        CALENDAR_SUBTITLE to "Divulgação de índices de inflação, taxa de juros (Copom/Fed) e balanços corporativos.",
        MAP_SUBTITLE to "Mapa econômico geográfico interativo da América do Sul e Brasil.",
        SELIC to "SELIC (Meta)",
        IPCA to "IPCA (12m)",
        USDBRL to "USD/BRL",
        GDP to "PIB Brasil",
        RISK to "Risco País",

        WATCHLIST_EMPTY_TITLE to "Nenhum ativo salvo na sua lista",
        WATCHLIST_EMPTY_SUBTITLE to "Adicione ações, o índice Ibovespa ou dólar para acompanhar cotações e gráficos em tempo real TradingView.",
        SUGGESTIONS to "Sugestões de Ativos B3:",
        MONITORED_ASSETS to "Ativos Monitorados",
        TAP_TO_VIEW_CHART to "Toque para abrir gráfico ao vivo",
        VIEW_CHART to "Ver Gráfico",
        TECHNICALS to "Técnicos",
        REMOVE to "Remover",
        LOADING_WIDGET to "Carregando TradingView...",
        WIDGET_LOAD_ERROR to "Não foi possível carregar o gráfico",
        CHECK_CONNECTION to "Verifique sua conexão com a internet e tente novamente.",
        RETRY_LOAD to "Recarregar"
    )

    private val EN = mapOf(
        NAV_CHARTS to "Charts",
        NAV_MARKET to "Market",
        NAV_ANALYSIS to "Analysis",
        NAV_ECONOMY to "Economy",
        NAV_WATCHLIST to "Watchlist",

        APP_TITLE to "BRAZIL MARKET",
        MARKET_OPEN to "LIVE TRADING",
        MARKET_CLOSED to "MARKET CLOSED",
        SEARCH_ASSET to "Search B3 Asset",
        TOGGLE_THEME to "Toggle Theme",
        SWITCH_LANG to "Change Language",

        B3_LIVE_CHART to "B3 Real-Time Chart",
        FULLSCREEN to "Fullscreen",
        EXIT_FULLSCREEN to "Exit Fullscreen",
        SEARCH_TICKER_PLACEHOLDER to "Type ticker (e.g., PETR4, VALE3, IBOV)...",
        EXPLORE_B3_ASSETS to "Search B3 Assets",
        OPEN_ASSET to "Open in Chart",
        CANCEL to "Cancel",
        TIME_24H_MIN to "24h Low",
        TIME_24H_MAX to "24h High",
        VOLUME to "Volume",
        PE_RATIO to "P/E",
        DIV_YIELD to "Div. Yield",
        MARKET_CAP to "Market Cap",
        HIGH_52W to "52w High",
        LOW_52W to "52w Low",
        LIVE_STREAMING to "Live Streaming Quotes & Charts by TradingView",
        POWERED_BY_TRADINGVIEW to "Real-time market data powered by TradingView B3",
        TIP_CHART to "Drag to pan, pinch to zoom, or tap to customize chart indicators.",
        ALL to "All",
        STOCKS to "Stocks",
        INDICES to "Indices",
        FOREX to "Forex",

        HEATMAP_TAB to "B3 Stock Heatmap",
        SCREENER_TAB to "B3 Stock Screener",
        HEATMAP_GUIDE to "Block size represents market capitalization. Green and red colors reflect real-time percentage change via TradingView.",
        SCREENER_GUIDE to "Filter and rank all B3 Brazilian equities by price, daily volume, P/E ratio, performance, and sectors.",

        TECH_ANALYSIS_TAB to "Technical Analysis",
        FINANCIALS_TAB to "Financials",
        PROFILE_TAB to "Company Profile",
        OVERVIEW_TAB to "Overview",
        TECH_RATING_NOTE to "Oscillators and moving averages aggregated in real time.",

        MACRO_TITLE to "Brazil Macroeconomic Indicators",
        MACRO_SOURCE to "TradingView • Economic Events",
        CALENDAR_TAB to "Economic Calendar",
        MAP_TAB to "Macroeconomic Map",
        CALENDAR_SUBTITLE to "Upcoming release dates for inflation, interest rate decisions (Copom/Fed), and corporate earnings.",
        MAP_SUBTITLE to "Interactive macroeconomic map of South America & Brazil.",
        SELIC to "SELIC (Target)",
        IPCA to "IPCA (12m)",
        USDBRL to "USD/BRL",
        GDP to "Brazil GDP",
        RISK to "Country Risk",

        WATCHLIST_EMPTY_TITLE to "No assets in your watchlist",
        WATCHLIST_EMPTY_SUBTITLE to "Bookmark equities, the Ibovespa index, or USD/BRL to monitor real-time TradingView charts and market quotes.",
        SUGGESTIONS to "Suggested B3 Assets:",
        MONITORED_ASSETS to "Monitored Assets",
        TAP_TO_VIEW_CHART to "Tap card to open live chart",
        VIEW_CHART to "View Chart",
        TECHNICALS to "Technicals",
        REMOVE to "Remove",
        LOADING_WIDGET to "Loading TradingView...",
        WIDGET_LOAD_ERROR to "Unable to load chart",
        CHECK_CONNECTION to "Please check your internet connection and try again.",
        RETRY_LOAD to "Reload"
    )
}
