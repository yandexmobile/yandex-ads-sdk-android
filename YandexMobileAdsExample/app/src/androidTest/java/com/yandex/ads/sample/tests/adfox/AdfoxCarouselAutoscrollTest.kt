package com.yandex.ads.sample.tests.adfox

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.AdfoxCarouselScreen
import com.yandex.ads.sample.pageobjects.checkAdIsLoaded
import com.yandex.ads.sample.pageobjects.clickLoadAd
import com.yandex.ads.sample.pageobjects.getCurrentPage
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

internal class AdfoxCarouselAutoscrollTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun shouldAutoscrollCarouselSlides() = run {
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

        var initialPage: Int
        step("Подождать 5 секунд не производя никаких действий") {
            onScreen<AdfoxCarouselScreen> {
                initialPage = getCurrentPage()
                Thread.sleep(5500)

                step("Происходит автопрокрутка на следующий слайд") {
                    val currentPage = getCurrentPage()
                    Assert.assertNotEquals(
                        "Автопрокрутка не произошла, остались на том же слайде",
                        initialPage,
                        currentPage
                    )
                    Assert.assertEquals(
                        "Автопрокрутка перешла не на следующий слайд",
                        (initialPage + 1) % 3,
                        currentPage
                    )
                }
            }
        }

        step("Подождать ещё 5 секунд не производя никаких действий") {
            onScreen<AdfoxCarouselScreen> {
                val previousPage = getCurrentPage()
                Thread.sleep(5500)

                step("После последнего слайда происходит автопрокрутка обратно на первый") {
                    val currentPage = getCurrentPage()
                    Assert.assertNotEquals(
                        "Автопрокрутка не произошла, остались на том же слайде",
                        previousPage,
                        currentPage
                    )
                    Assert.assertEquals(
                        "Автопрокрутка перешла не на следующий слайд",
                        (previousPage + 1) % 3,
                        currentPage
                    )
                }
            }
        }
    }
}

