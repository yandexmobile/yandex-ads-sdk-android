/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.navigation

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.yandex.ads.sample.databinding.NavigationItemLayoutBinding

class NavigationItemViewHolder(private val binding: NavigationItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(navigationItem: NavigationItem) {
        binding.item.apply {
            text = navigationItem.name
            setOnClickListener {
                val intent = Intent(itemView.context, navigationItem.activityClass)
                itemView.context.startActivity(intent)
            }
        }
    }
}