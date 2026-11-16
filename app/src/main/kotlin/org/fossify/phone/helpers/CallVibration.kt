package org.fossify.phone.helpers

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Short haptic feedback used for "vibrate on answered" / "vibrate on hang up".
 * Kept independent from the dialpad vibration setting on purpose, so the user can
 * enable one without the other.
 */
object CallVibration {

    private const val ANSWER_DURATION_MS = 120L
    private const val HANG_UP_DURATION_MS = 80L

    fun vibrateOnAnswered(context: Context) = vibrate(context, ANSWER_DURATION_MS)

    fun vibrateOnHangUp(context: Context) = vibrate(context, HANG_UP_DURATION_MS)

    private fun vibrate(context: Context, durationMs: Long) {
        val vibrator = getVibrator(context) ?: return
        if (!vibrator.hasVibrator()) {
            return
        }

        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        }
    }

    private fun getVibrator(context: Context): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            manager?.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }
}
