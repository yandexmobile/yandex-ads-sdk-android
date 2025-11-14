package com.yandex.ads.sample.tests.adfox

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.AdfoxCarouselScreen
import com.yandex.ads.sample.pageobjects.checkAdIsLoaded
import com.yandex.ads.sample.pageobjects.clickLeftArrow
import com.yandex.ads.sample.pageobjects.clickLoadAd
import com.yandex.ads.sample.pageobjects.clickRightArrow
import com.yandex.ads.sample.pageobjects.getCurrentPage
import com.yandex.ads.sample.pageobjects.getPageCount
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

internal class AdfoxCarouselArrowsTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun shouldNavigateCarouselWithArrows() = run {
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

        var pageCount = 0
        step("Получить количество слайдов в карусели") {
            onScreen<AdfoxCarouselScreen> {
                pageCount = getPageCount()
                Assert.assertTrue(
                    "Количество слайдов должно быть больше 1 для проверки навигации",
                    pageCount > 1
                )
            }
        }

        step("На первом слайде нажать стрелку влево") {
            onScreen<AdfoxCarouselScreen> {
                Assert.assertEquals("Должны быть на первом слайде", 0, getCurrentPage())

                clickLeftArrow()
                Thread.sleep(500)

                step("Происходит переход на последний слайд") {
                    val currentPage = getCurrentPage()
                    Assert.assertEquals(
                        "Должен быть переход на последний слайд",
                        pageCount - 1,
                        currentPage
                    )
                }
            }
        }

        step("На последнем слайде нажать стрелку вправо") {
            onScreen<AdfoxCarouselScreen> {
                clickRightArrow()
                Thread.sleep(500)

                step("Происходит переход на первый слайд") {
                    val currentPage = getCurrentPage()
                    Assert.assertEquals(
                        "Должен быть переход на первый слайд",
                        0,
                        currentPage
                    )
                }
            }
        }
    }
}

