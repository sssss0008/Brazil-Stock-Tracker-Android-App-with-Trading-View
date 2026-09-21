package com.example.ui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import android.view.View
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.util.LocalAppLanguage
import com.example.util.Strings

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TradingViewWebView(
    htmlContent: String,
    modifier: Modifier = Modifier,
    key: Any? = null,
    testTag: String = "trading_view_webview"
) {
    var reloadTrigger by remember(key) { mutableStateOf(0) }
    var isLoading by remember(key, reloadTrigger) { mutableStateOf(true) }
    var hasError by remember(key, reloadTrigger) { mutableStateOf(false) }

    val lang = LocalAppLanguage.current
    val isDark = MaterialTheme.colorScheme.background.red < 0.5f

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag(testTag)
            .background(if (isDark) Color(0xFF0A0F1D) else Color(0xFFF8FAFC)),
        contentAlignment = Alignment.Center
    ) {
        key(key, reloadTrigger) {
            AndroidView<WebView>(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        val bgInt = if (isDark) AndroidColor.parseColor("#0a0f1d") else AndroidColor.parseColor("#f8fafc")
                        setBackgroundColor(bgInt)
                        isVerticalScrollBarEnabled = true
                        isHorizontalScrollBarEnabled = false

                        // Set layer type safely to prevent emulator render node crashes
                        try {
                            setLayerType(View.LAYER_TYPE_HARDWARE, null)
                        } catch (_: Throwable) {
                            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                        }

                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            databaseEnabled = true
                            loadWithOverviewMode = true
                            useWideViewPort = true
                            cacheMode = WebSettings.LOAD_DEFAULT
                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            setSupportZoom(true)
                            builtInZoomControls = true
                            displayZoomControls = false
                            userAgentString = "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Mobile Safari/537.36"
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true
                                hasError = false
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                if (request?.isForMainFrame == true) {
                                    hasError = true
                                    isLoading = false
                                }
                            }

                            override fun onRenderProcessGone(
                                view: WebView?,
                                detail: RenderProcessGoneDetail?
                            ): Boolean {
                                // Prevent app crash if headless emulator Mesa GPU process fails
                                hasError = true
                                isLoading = false
                                return true
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                if (newProgress >= 80) {
                                    isLoading = false
                                }
                            }
                        }

                        tag = htmlContent
                        loadDataWithBaseURL(
                            "https://www.tradingview.com",
                            htmlContent,
                            "text/html",
                            "UTF-8",
                            null
                        )
                    }
                },
                update = { webView: WebView ->
                    val bgInt = if (isDark) AndroidColor.parseColor("#0a0f1d") else AndroidColor.parseColor("#f8fafc")
                    webView.setBackgroundColor(bgInt)
                    val current = webView.tag as? String
                    if (current != htmlContent) {
                        webView.tag = htmlContent
                        webView.loadDataWithBaseURL(
                            "https://www.tradingview.com",
                            htmlContent,
                            "text/html",
                            "UTF-8",
                            null
                        )
                    }
                },
                onRelease = { webView: WebView ->
                    webView.stopLoading()
                    webView.destroy()
                }
            )
        }

        // Loading overlay
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDark) Color(0xCC111827) else Color(0xCCFFFFFF))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = Strings.get(Strings.LOADING_WIDGET, lang),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Error Fallback
        if (hasError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isDark) Color(0xF00A0F1D) else Color(0xF0FFFFFF))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = Strings.get(Strings.WIDGET_LOAD_ERROR, lang),
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = Strings.get(Strings.WIDGET_LOAD_ERROR, lang),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = Strings.get(Strings.CHECK_CONNECTION, lang),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            hasError = false
                            isLoading = true
                            reloadTrigger++
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(Strings.get(Strings.RETRY_LOAD, lang))
                    }
                }
            }
        }
    }
}
