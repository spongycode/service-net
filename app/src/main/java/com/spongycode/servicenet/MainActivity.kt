package com.spongycode.servicenet

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.spongycode.servicenet.Utils.changeStatusBarColor
import com.spongycode.servicenet.ui.theme.ServiceAppTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContent {
            ServiceAppTheme {
                Container(viewModel)
            }
        }
        startNetworkRequest()
        triggerStatusColor()
    }

    private fun triggerStatusColor() {
        lifecycleScope.launch {
            viewModel.backgroundColor.collect {
                changeStatusBarColor(
                    it, false
                )
            }
        }
    }

    private fun startNetworkRequest() {
        lifecycleScope.launch {
            viewModel.foregroundServiceStarted.collect { it ->
                if (!it) {
                    Intent(applicationContext, NetworkRequestService::class.java).also {
                        it.action = NetworkRequestService.Actions.START.toString()
                        startService(it)
                    }
                    viewModel.startForegroundService()
                }
            }
        }
    }

    companion object {
        val viewModel = MainViewModel()
    }
}

