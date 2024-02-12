package com.yandex.ads.sample.interceptors

import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.kaspersky.kaspresso.instrumental.InstrumentalDependencyProvider
import com.kaspersky.kaspresso.logger.UiTestLogger
import com.yandex.ads.sample.BuildConfig
import java.util.concurrent.TimeUnit

internal class DefaultWarningDialogSuppressor(
    private val logger: UiTestLogger,
    private val instrumentalDependencyProvider: InstrumentalDependencyProvider
) : WarningDialogSuppressor {

    private val uiDevice: UiDevice
        get() = instrumentalDependencyProvider.uiDevice

    private val attemptsToSuppress: List<(UiDevice) -> Unit> = listOf(
        { uiDevice ->
            uiDevice.wait(
                Until.findObject(By.res("android:id/button1")),
                DEFAULT_TIMEOUT
            ).click()
        },
        { uiDevice -> uiDevice.pressBack() }
    )

    override fun <T> suppressWarningDialog(activity: () -> T): T {
        return try {
            activity.invoke()
        } catch (error: Throwable) {
            if (isWarningDialogDetected()) {
                return suppressWarningDialog(error, activity)
            }
            throw error
        }
    }

    private fun <R> suppressWarningDialog(firstError: Throwable, action: () -> R): R {
        logger.i("The suppressing of warning dialog starts")
        var cachedError: Throwable = firstError

        attemptsToSuppress.forEachIndexed { index, attemptToSuppress ->
            try {
                logger.i("The suppressing of warning dialog on the try #$index starts")

                attemptToSuppress.invoke(uiDevice)
                val result = action.invoke()

                logger.i("The suppressing of warning dialog succeeds on the try #$index")
                return result
            } catch (error: Throwable) {
                logger.i("The try #$index of the suppressing of warning dialog failed")

                cachedError = error

                if (!isWarningDialogDetected()) {
                    logger.i(
                        "The try #$index of the suppressing of warning dialog failed. " +
                                "The reason is the error is not allowed or " +
                                "warning dialog is suppressed but the error is existing"
                    )
                    throw cachedError
                }
            }
        }

        logger.i("The suppressing of SystemDialogs totally failed")
        throw cachedError
    }

    private fun isWarningDialogDetected(): Boolean {
        with(uiDevice) {
            if (isVisible(By.pkg(BuildConfig.APPLICATION_ID).text("Warning"))) {
                logger.i("The warning dialog/window was detected")
                return true
            }
        }
        return false
    }

    private fun UiDevice.isVisible(
        selector: BySelector,
        timeMs: Long = TimeUnit.SECONDS.toMillis(1)
    ): Boolean {
        wait(Until.findObject(selector), timeMs)
        return findObject(selector) != null
    }

    private companion object {
        private const val DEFAULT_TIMEOUT: Long = 2000
    }
}
