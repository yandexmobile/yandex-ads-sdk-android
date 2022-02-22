/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.navigation

import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NavigationListConfigurator {

    fun configure(
        context: Context,
        navigationList: RecyclerView,
    ) {
        navigationList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            val decorator = DividerItemDecoration(context, RecyclerView.VERTICAL)
            addItemDecoration(decorator)
        }
    }
}
