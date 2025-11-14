package com.yandex.ads.sample.tests.adfox

import android.webkit.WebView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.AdfoxCarouselScreen
import com.yandex.ads.sample.pageobjects.checkAdIsLoaded
import com.yandex.ads.sample.pageobjects.clickFeedback
import com.yandex.ads.sample.pageobjects.clickLoadAd
import com.yandex.ads.sample.pageobjects.getCurrentPage
import com.yandex.ads.sample.pageobjects.getPageCount
import com.yandex.ads.sample.pageobjects.setCurrentPage
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

internal class AdfoxCarouselFeedbackTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun shouldOpenFeedbackForEachCarouselSlide() = run {
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
                    "Количество слайдов должно быть больше 0",
                    pageCount > 0
                )
            }
        }

        step("У каждого рекламного объявления нажать на кебаб") {
            for (i in 0 until pageCount) {
                step("Обработка слайда ${i + 1} из $pageCount") {
                    onScreen<AdfoxCarouselScreen> {
                        if (getCurrentPage() != i) {
                            setCurrentPage(i)
                            Thread.sleep(1000)
                        }

                        step("Нажать на кебаб (три точки)") {
                            clickFeedback()
                        }

                        step("Открывается новое окно в WebView") {
                            flakySafely {
                                KView {
                                    withMatcher(
                                        Matchers.allOf(
                                            ViewMatchers.isAssignableFrom(WebView::class.java),
                                            ViewMatchers.isDisplayed()
                                        )
                                    )
                                }.isVisible()
                            }
                        }

                        step("Вернуться назад") {
                            device.uiDevice.pressBack()
                            Thread.sleep(500)
                        }
                    }
                }
            }
        }
    }
}

