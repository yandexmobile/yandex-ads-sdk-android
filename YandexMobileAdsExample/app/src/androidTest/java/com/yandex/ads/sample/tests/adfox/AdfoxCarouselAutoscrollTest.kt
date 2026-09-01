package com.yandex.ads.sample.tests.adfox

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.AdfoxCarouselScreen
import com.yandex.ads.sample.pageobjects.checkAdIsLoaded
import com.yandex.ads.sample.pageobjects.clickLoadAd
import com.yandex.ads.sample.pageobjects.getCurrentPage
import com.yandex.ads.sample.pageobjects.getPageCount
import com.yandex.ads.sample.pageobjects.setCurrentPage
import com.yandex.ads.sample.pageobjects.waitForNextPage
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

        var pageCount = 0
        step("Получить количество слайдов в карусели") {
            onScreen<AdfoxCarouselScreen> {
                pageCount = getPageCount()
                Assert.assertTrue(
                    "Количество слайдов должно быть больше 1 для проверки автопрокрутки",
                    pageCount > 1
                )
            }
        }

        step("Дождаться автопрокрутки, не производя никаких действий") {
            onScreen<AdfoxCarouselScreen> {
                step("Происходит автопрокрутка на следующий слайд") {
                    val (initialPage, currentPage) = waitForNextPage()
                    Assert.assertEquals(
                        "Автопрокрутка перешла не на следующий слайд",
                        (initialPage + 1) % pageCount,
                        currentPage
                    )
                }
            }
        }

        step("На последнем слайде дождаться автопрокрутки, не производя никаких действий") {
            onScreen<AdfoxCarouselScreen> {
                setCurrentPage(pageCount - 1)
                flakySafely {
                    Assert.assertEquals(
                        "Должны быть на последнем слайде",
                        pageCount - 1,
                        getCurrentPage()
                    )
                }

                step("После последнего слайда происходит автопрокрутка обратно на первый") {
                    val (initialPage, currentPage) = waitForNextPage()
                    Assert.assertEquals(
                        "Автопрокрутка после последнего слайда не перешла на первый",
                        0,
                        currentPage
                    )
                    Assert.assertEquals(
                        "Ожидали автопрокрутку с последнего слайда",
                        pageCount - 1,
                        initialPage
                    )
                }
            }
        }
    }
}
