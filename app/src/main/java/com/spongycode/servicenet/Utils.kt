package com.spongycode.servicenet

import android.app.Activity
import android.view.WindowManager
import android.graphics.Color
import androidx.core.view.WindowInsetsControllerCompat

object Utils {
    private const val darkenFactor: Float = 0.75f
    fun Activity.changeStatusBarColor(color: androidx.compose.ui.graphics.Color, isLight: Boolean) {
        val darkenedColor = androidx.compose.ui.graphics.Color(
            color.red * darkenFactor,
            color.green * darkenFactor,
            color.blue * darkenFactor,
            color.alpha
        )

        val colorString = String.format(
            "#%02X%02X%02X", (darkenedColor.red * 255).toInt(),
            (darkenedColor.green * 255).toInt(), (darkenedColor.blue * 255).toInt()
        )
        val finalColor = Color.parseColor(colorString)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = finalColor

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLight
    }
}