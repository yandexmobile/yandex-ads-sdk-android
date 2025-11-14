package com.yandex.ads.sample.tests.adfox

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.AdfoxCarouselScreen
import com.yandex.ads.sample.pageobjects.checkAdIsLoaded
import com.yandex.ads.sample.pageobjects.checkLogsContain
import com.yandex.ads.sample.pageobjects.clickLoadAd
import com.yandex.ads.sample.pageobjects.clickMedia
import com.yandex.ads.sample.pageobjects.setCurrentPage
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.checkAnyBrowserOrStoreIsOpened
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import com.yandex.ads.sample.shared_steps.returnToApplication
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Rule
import org.junit.Test

internal class AdfoxCarouselCallbacksTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun shouldTriggerCarouselAdCallbacks() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.ADFOX_CAROUSEL)

        step("Нажать на кнопку \"Load ad\"") {
            onScreen<AdfoxCarouselScreen> {
                clickLoadAd()

                step("В нижней части экрана отобразились слайды с рекламой, верстка не поехала") {
                    checkAdIsLoaded()
                }
            }
        }

        step("Кликнуть на картинку первого слайда") {
            onScreen<AdfoxCarouselScreen> {
                clickMedia()
            }

            checkAnyBrowserOrStoreIsOpened("Происходит переход в браузер или магазин приложений Google Play")
        }

        returnToApplication()

        step("Кликнуть на картинку второго слайда") {
            onScreen<AdfoxCarouselScreen> {
                setCurrentPage(1)
                Thread.sleep(1000)

                clickMedia()
            }

            checkAnyBrowserOrStoreIsOpened("Происходит переход в браузер или магазин приложений Google Play")
        }

        returnToApplication()

        step("В окне Logs отображаются колбеки") {
            onScreen<AdfoxCarouselScreen> {
                checkLogsContain(
                    "Ads loaded",
                    "Impression: null",
                    "Ad clicked",
                    "Left application",
                    "Returned to application",
                    "Impression: null",
                    "Ad clicked",
                    "Left application",
                    "Returned to application"
                )
            }
        }
    }
}

