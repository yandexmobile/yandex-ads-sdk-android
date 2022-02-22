/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yandex.ads.sample.databinding.NavigationItemLayoutBinding
import com.yandex.ads.sample.navigation.NavigationItem
import com.yandex.ads.sample.navigation.NavigationItemViewHolder

class NavigationListAdapter(private val navigationItems: List<NavigationItem>) :
    RecyclerView.Adapter<NavigationItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NavigationItemLayoutBinding.inflate(inflater, parent, false)

        return NavigationItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NavigationItemViewHolder, position: Int) {
        holder.bind(navigationItems[position])
    }

    override fun getItemCount(): Int {
        return navigationItems.size
    }
}