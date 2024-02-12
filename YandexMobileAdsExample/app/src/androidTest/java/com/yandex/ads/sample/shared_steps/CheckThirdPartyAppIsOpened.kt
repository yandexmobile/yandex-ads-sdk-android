package com.yandex.ads.sample.shared_steps

import com.kaspersky.kaspresso.testcases.api.scenario.BaseScenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import org.junit.Assert

internal class CheckThirdPartyAppIsOpened<ScenarioData>(
    private val apps: Set<ThirdPartyApp>,
    private val description: String? = null
) : BaseScenario<ScenarioData>() {

    init {
        require(apps.isNotEmpty())
    }

    override val steps: TestContext<ScenarioData>.() -> Unit = {
        val description = description ?: createDescription(apps)

        step(description) {
            flakySafely {
                val currentPackageName = device.uiDevice.currentPackageName

                apps
                    .any { it.packageName == currentPackageName }
                    .let { containsApp ->
                        Assert.assertTrue(
                            "$currentPackageName could contains in $apps",
                            containsApp
                        )
                    }
            }
        }
    }

    sealed interface ThirdPartyApp {

        val packageName: String

        enum class Browsers(override val packageName: String) : ThirdPartyApp {
            CHROME("com.android.chrome");

            override fun toString(): String {
                return packageName
            }
        }

        enum class Stores(override val packageName: String) : ThirdPartyApp {
            GOOGLE_PLAY_STORE("com.android.vending"),
            APP_GALLERY("com.huawei.appmarket");

            override fun toString(): String {
                return packageName
            }
        }

        @JvmInline
        value class Unknown(override val packageName: String) : ThirdPartyApp
    }

    companion object {

        private fun createDescription(apps: Set<ThirdPartyApp>): String {
            return if (apps.size > 1) {
                "Проверить, что открылось одно из приложений с package name из $apps"
            } else {
                "Проверить, что открылось приложение с package name = ${apps.single()}"
            }
        }
    }
}
