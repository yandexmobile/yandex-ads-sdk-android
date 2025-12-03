package com.yandex.ads.sample.pageobjects

import com.yandex.ads.sample.components.OrdinalItem
import com.yandex.ads.sample.components.OrdinalSpinner

fun HasNetworkMenu<*>.openNetworkMenu() = networkMenu {
    isVisible()
    open()
}

inline fun <reified I : T, T : OrdinalItem> HasNetworkMenu<T>.clickNetworkItem() =
    clickNetworkItem(I::class.java)

fun <I : T, T : OrdinalItem> HasNetworkMenu<T>.clickNetworkItem(
    clazz: Class<I>
) = networkMenu.onItem(clazz) {
    isVisible()
    click()
}

fun <I : T, T : OrdinalItem> HasNetworkMenu<T>.clickNetworkItemByText(
    clazz: Class<I>,
    text: String
) = networkMenu.onItemByText(clazz, text) {
    isVisible()
    click()
}

interface HasNetworkMenu<T : OrdinalItem> {

    val networkMenu: OrdinalSpinner<T>
}
