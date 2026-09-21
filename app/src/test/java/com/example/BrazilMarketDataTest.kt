package com.example

import com.example.model.BrazilMarketData
import com.example.util.TradingViewHtmlBuilder
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BrazilMarketDataTest {

    @Test
    fun testBrazilianStocksLoaded() {
        val allStocks = BrazilMarketData.allAssets
        assertTrue(allStocks.isNotEmpty())
        assertTrue(allStocks.any { it.ticker == "PETR4" })
        assertTrue(allStocks.any { it.ticker == "VALE3" })
        assertTrue(allStocks.any { it.ticker == "IBOV" })
        assertTrue(allStocks.any { it.ticker == "USD/BRL" })
    }

    @Test
    fun testFindStock() {
        val petr4 = BrazilMarketData.findStock("PETR4")
        assertNotNull(petr4)
        assertTrue(petr4?.symbol == "BMFBOVESPA:PETR4")

        val ibov = BrazilMarketData.findStock("BMFBOVESPA:IBOV")
        assertNotNull(ibov)
    }

    @Test
    fun testChartHtmlGeneration() {
        val htmlDark = TradingViewHtmlBuilder.buildAdvancedChartHtml("BMFBOVESPA:PETR4", isDark = true)
        assertTrue(htmlDark.contains("embed-widget-advanced-chart.js"))
        assertTrue(htmlDark.contains("BMFBOVESPA:PETR4"))
        assertTrue(htmlDark.contains("\"theme\": \"dark\""))
        assertTrue(htmlDark.contains("America/Sao_Paulo"))

        val htmlLight = TradingViewHtmlBuilder.buildAdvancedChartHtml("BMFBOVESPA:VALE3", isDark = false)
        assertTrue(htmlLight.contains("\"theme\": \"light\""))
    }

    @Test
    fun testHeatmapAndScreenerHtml() {
        val heatmapHtml = TradingViewHtmlBuilder.buildHeatmapHtml(isDark = true)
        assertTrue(heatmapHtml.contains("embed-widget-stock-heatmap.js"))
        assertTrue(heatmapHtml.contains("BMFBOVESPA"))

        val screenerHtml = TradingViewHtmlBuilder.buildScreenerHtml(isDark = true)
        assertTrue(screenerHtml.contains("embed-widget-screener.js"))
        assertTrue(screenerHtml.contains("\"market\": \"brazil\""))
    }

    @Test
    fun testCalendarAndEconomyHtml() {
        val calendarHtml = TradingViewHtmlBuilder.buildEconomicCalendarHtml(isDark = true)
        assertTrue(calendarHtml.contains("embed-widget-events.js"))
        assertTrue(calendarHtml.contains("\"countryFilter\": \"br,us\""))

        val mapHtml = TradingViewHtmlBuilder.buildEconomicMapHtml(isDark = true)
        assertTrue(mapHtml.contains("tv-economic-map"))
        assertTrue(mapHtml.contains("region=\"south-america\""))
    }

    @Test
    fun testTechnicalAnalysisAndProfileHtml() {
        val technicalHtml = TradingViewHtmlBuilder.buildTechnicalAnalysisHtml("BMFBOVESPA:PETR4", isDark = true)
        assertTrue(technicalHtml.contains("tv-technical-analysis"))
        assertTrue(technicalHtml.contains("BMFBOVESPA:PETR4"))

        val profileHtml = TradingViewHtmlBuilder.buildCompanyProfileHtml("BMFBOVESPA:PETR4", isDark = true)
        assertTrue(profileHtml.contains("tv-company-profile"))

        val financialsHtml = TradingViewHtmlBuilder.buildFinancialsHtml("BMFBOVESPA:PETR4", isDark = true)
        assertTrue(financialsHtml.contains("embed-widget-financials.js"))
    }
}
