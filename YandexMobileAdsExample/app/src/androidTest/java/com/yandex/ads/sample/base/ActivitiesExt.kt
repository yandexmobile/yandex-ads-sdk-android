package com.yandex.ads.sample.base

import android.app.Instrumentation
import android.content.pm.PackageManager
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import com.kaspersky.kaspresso.device.activities.Activities
import com.yandex.ads.sample.BuildConfig
import org.junit.Assert

internal fun Activities.currentIsExternal() {
    this as ActivitiesExt
    this.currentIsExternal()
}

internal interface ActivitiesExt : Activities {

    fun currentIsExternal()
}

internal class DefaultActivitiesExt(
    private val instrumentation: Instrumentation,
    private val activities: Activities,
    private val applicationId: String = BuildConfig.APPLICATION_ID,
    private val packageName: String = applicationId
) : ActivitiesExt,
    Activities by activities {

    override fun currentIsExternal() {
        UiThreadStatement.runOnUiThread {
            val resumedActivity = requireNotNull(getResumed()) {
                "Continued activity could not be found"
            }

            val targetContext = instrumentation.targetContext

            val (internalActivities, externalActivities) = targetContext
                .packageManager
                .getPackageInfo(
                    applicationId,
                    PackageManager.GET_ACTIVITIES
                )
                .activities
                .asSequence()
                .map { it.name }
                .partition { it.startsWith(packageName) }

            val activityName = requireNotNull(resumedActivity::class.java.canonicalName)

            Assert.assertTrue(
                "$activityName is not external.",
                externalActivities.contains(activityName)
            )
            Assert.assertFalse(
                "$activityName is internal",
                internalActivities.contains(activityName)
            )
        }
    }
}
