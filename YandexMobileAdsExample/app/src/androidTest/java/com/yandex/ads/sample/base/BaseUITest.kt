package com.yandex.ads.sample.base

import androidx.test.rule.GrantPermissionRule
import com.kaspersky.components.alluresupport.withAllureSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.yandex.ads.sample.interceptors.addBehaviorInterceptors
import org.junit.Rule

internal abstract class BaseUITest : TestCase(
    kaspressoBuilder = Kaspresso.Builder
        .withAllureSupport()
        .apply {
            activities = DefaultActivitiesExt(
                instrumentation = instrumentation,
                activities = activities,
                packageName = MAIN_PACKAGE_NAME
            )

            addBehaviorInterceptors()
        }
) {

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private companion object {

        const val MAIN_PACKAGE_NAME = "com.yandex.ads.sample"
    }
}
