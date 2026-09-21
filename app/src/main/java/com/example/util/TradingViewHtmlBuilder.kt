package com.example.util

import com.example.model.BrazilMarketData

object TradingViewHtmlBuilder {

    private fun wrapHtml(
        isDark: Boolean,
        bodyContent: String,
        extraCss: String = ""
    ): String {
        val bgColor = if (isDark) "#0a0f1d" else "#f8fafc"
        val textColor = if (isDark) "#f8fafc" else "#0f172a"
        val themeClass = if (isDark) "dark" else "light"

        return """
<!DOCTYPE html>
<html lang="en" class="$themeClass">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=5.0, user-scalable=yes">
    <style>
        * {
            box-sizing: border-box;
            -webkit-tap-highlight-color: transparent;
        }
        html, body {
            margin: 0;
            padding: 0;
            width: 100%;
            height: 100%;
            background-color: $bgColor;
            color: $textColor;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
            overflow-x: hidden;
            overflow-y: auto;
        }
        .widget-wrapper {
            width: 100%;
            height: 100%;
            display: flex;
            flex-direction: column;
            align-items: stretch;
            justify-content: stretch;
        }
        .tradingview-widget-container {
            width: 100% !important;
            height: 100% !important;
        }
        .tradingview-widget-container__widget {
            width: 100% !important;
            height: 100% !important;
        }
        .tradingview-widget-copyright {
            display: none !important;
        }
        tv-ticker-tape, tv-economic-map, tv-technical-analysis, tv-company-profile {
            display: block;
            width: 100% !important;
            height: 100% !important;
        }
        $extraCss
    </style>
</head>
<body>
    <div id="tv-init-paint" style="position:fixed;top:0;left:0;width:100%;height:100%;background-color:$bgColor;z-index:0;pointer-events:none;"></div>
    <div class="widget-wrapper" style="position:relative;z-index:1;">
        $bodyContent
    </div>
</body>
</html>
        """.trimIndent()
    }

    fun buildAdvancedChartHtml(
        symbol: String = "BMFBOVESPA:IBOV",
        isDark: Boolean = true,
        interval: String = "D",
        style: String = "1",
        locale: String = "br"
    ): String {
        val theme = if (isDark) "dark" else "light"
        val bgColor = if (isDark) "#0a0f1d" else "#ffffff"
        val gridColor = if (isDark) "rgba(255, 255, 255, 0.06)" else "rgba(46, 46, 46, 0.12)"
        val watchlistJson = BrazilMarketData.getWatchlistArrayJson()

        val body = """
<div class="tradingview-widget-container" style="height:100%;width:100%">
    <div class="tradingview-widget-container__widget" style="height:100%;width:100%"></div>
    <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-advanced-chart.js" async>
    {
      "allow_symbol_change": true,
      "calendar": false,
      "details": true,
      "hide_side_toolbar": false,
      "hide_top_toolbar": false,
      "hide_legend": false,
      "hide_volume": false,
      "hotlist": true,
      "interval": "$interval",
      "locale": "$locale",
      "save_image": true,
      "style": "$style",
      "symbol": "$symbol",
      "theme": "$theme",
      "timezone": "America/Sao_Paulo",
      "backgroundColor": "$bgColor",
      "gridColor": "$gridColor",
      "watchlist": $watchlistJson,
      "withdateranges": true,
      "range": "YTD",
      "compareSymbols": [],
      "support_host": "https://www.tradingview.com",
      "studies": [],
      "autosize": true
    }
    </script>
</div>
        """.trimIndent()
        return wrapHtml(isDark, body)
    }

    fun buildTickerTapeHtml(
        symbols: String = BrazilMarketData.getTickerTapeString(),
        isDark: Boolean = true,
        locale: String = "br"
    ): String {
        val theme = if (isDark) "dark" else "light"
        val body = """
<script type="module" src="https://widgets.tradingview-widget.com/w/$locale/tv-ticker-tape.js"></script>
<tv-ticker-tape symbols="$symbols" show-hover symbol-url="https://wealthorbitcenter.com/free-live-trading-real-time-chart-stocks-forex-crypto/" theme="$theme"></tv-ticker-tape>
        """.trimIndent()
        return wrapHtml(isDark, body, "html, body { overflow: hidden !important; }")
    }

    fun buildHeatmapHtml(
        isDark: Boolean = true,
        dataSource: String = "AllBrazil",
        locale: String = "br"
    ): String {
        val theme = if (isDark) "dark" else "light"
        val body = """
<div class="tradingview-widget-container">
    <div class="tradingview-widget-container__widget"></div>
    <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-stock-heatmap.js" async>
    {
      "dataSource": "$dataSource",
      "blockSize": "market_cap_basic",
      "blockColor": "change",
      "grouping": "sector",
      "locale": "$locale",
      "symbolUrl": "https://wealthorbitcenter.com/free-live-trading-real-time-chart-stocks-forex-crypto/",
      "colorTheme": "$theme",
      "exchanges": ["BMFBOVESPA"],
      "hasTopBar": true,
      "isDataSetEnabled": true,
      "isZoomEnabled": true,
      "hasSymbolTooltip": true,
      "isMonoSize": false,
      "width": "100%",
      "height": "100%"
    }
    </script>
</div>
        """.trimIndent()
        return wrapHtml(isDark, body)
    }

    fun buildScreenerHtml(
        isDark: Boolean = true,
        locale: String = "br"
    ): String {
        val theme = if (isDark) "dark" else "light"
        val body = """
<div class="tradingview-widget-container">
    <div class="tradingview-widget-container__widget"></div>
    <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-screener.js" async>
    {
      "market": "brazil",
      "showToolbar": true,
      "defaultColumn": "overview",
      "defaultScreen": "most_capitalized",
      "isTransparent": false,
      "locale": "$locale",
      "colorTheme": "$theme",
      "largeChartUrl": "https://wealthorbitcenter.com/free-live-trading-real-time-chart-stocks-forex-crypto/",
      "width": "100%",
      "height": "100%"
    }
    </script>
</div>
        """.trimIndent()
        return wrapHtml(isDark, body)
    }

    fun buildEconomicCalendarHtml(
        isDark: Boolean = true,
        locale: String = "br"
    ): String {
        val theme = if (isDark) "dark" else "light"
        val body = """
<div class="tradingview-widget-container">
    <div class="tradingview-widget-container__widget"></div>
    <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-events.js" async>
    {
      "colorTheme": "$theme",
      "isTransparent": false,
      "locale": "$locale",
      "countryFilter": "br,us",
      "importanceFilter": "-1,0,1",
      "width": "100%",
      "height": "100%"
    }
    </script>
</div>
        """.trimIndent()
        return wrapHtml(isDark, body)
    }

    fun buildEconomicMapHtml(
        isDark: Boolean = true,
        locale: String = "br"
    ): String {
        val theme = if (isDark) "dark" else "light"
        val body = """
<script type="module" src="https://widgets.tradingview-widget.com/w/$locale/tv-economic-map.js"></script>
<tv-economic-map region="south-america" hide-legend symbol-url="https://wealthorbitcenter.com/free-live-trading-real-time-chart-stocks-forex-crypto/" theme="$theme"></tv-economic-map>
        """.trimIndent()
        return wrapHtml(isDark, body)
    }

    fun buildSymbolDetailHtml(
        symbol: String = "BMFBOVESPA:PETR4",
        isDark: Boolean = true,
        locale: String = "br"
    ): String {
        val theme = if (isDark) "dark" else "light"
        val body = """
<div class="tradingview-widget-container">
    <div class="tradingview-widget-container__widget"></div>
    <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-symbol-info.js" async>
    {
      "symbol": "$symbol",
      "colorTheme": "$theme",
      "isTransparent": false,
      "locale": "$locale",
      "largeChartUrl": "https://wealthorbitcenter.com/free-live-trading-real-time-chart-stocks-forex-crypto/",
      "width": "100%"
    }
    </script>
</div>
        """.trimIndent()
        return wrapHtml(isDark, body)
    }

    fun buildTechnicalAnalysisHtml(
        symbol: String = "BMFBOVESPA:PETR4",
        isDark: Boolean = true,
        locale: String = "br"
    ): String {
        val theme = if (isDark) "dark" else "light"
        val body = """
<script type="module" src="https://widgets.tradingview-widget.com/w/$locale/tv-technical-analysis.js"></script>
<tv-technical-analysis symbol="$symbol" interval="1M" ratings-mode="multiple" symbol-url="https://wealthorbitcenter.com/free-live-trading-real-time-chart-stocks-forex-crypto/" theme="$theme"></tv-technical-analysis>
        """.trimIndent()
        return wrapHtml(isDark, body)
    }

    fun buildFinancialsHtml(
        symbol: String = "BMFBOVESPA:PETR4",
        isDark: Boolean = true,
        locale: String = "br"
    ): String {
        val theme = if (isDark) "dark" else "light"
        val body = """
<div class="tradingview-widget-container">
    <div class="tradingview-widget-container__widget"></div>
    <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-financials.js" async>
    {
      "symbol": "$symbol",
      "colorTheme": "$theme",
      "displayMode": "compact",
      "isTransparent": false,
      "locale": "$locale",
      "largeChartUrl": "https://wealthorbitcenter.com/free-live-trading-real-time-chart-stocks-forex-crypto/",
      "width": "100%",
      "height": "100%"
    }
    </script>
</div>
        """.trimIndent()
        return wrapHtml(isDark, body)
    }

    fun buildCompanyProfileHtml(
        symbol: String = "BMFBOVESPA:PETR4",
        isDark: Boolean = true,
        locale: String = "br"
    ): String {
        val theme = if (isDark) "dark" else "light"
        val body = """
<script type="module" src="https://widgets.tradingview-widget.com/w/$locale/tv-company-profile.js"></script>
<tv-company-profile symbol="$symbol" symbol-url="https://wealthorbitcenter.com/free-live-trading-real-time-chart-stocks-forex-crypto/" theme="$theme"></tv-company-profile>
        """.trimIndent()
        return wrapHtml(isDark, body)
    }
}
