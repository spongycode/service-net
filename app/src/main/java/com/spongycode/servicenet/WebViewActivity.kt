package com.spongycode.servicenet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.spongycode.servicenet.ui.theme.ServiceAppTheme

class WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra("URL")
        setContent {
            ServiceAppTheme {
                WebViewCompose(url.toString())
            }
        }
    }
}
