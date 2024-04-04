/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yandex.ads.sample.databinding.NavigationItemBinding

class NavigationAdapter(private val items: List<NavigationItem>) :
    RecyclerView.Adapter<NavigationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NavigationItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class ViewHolder(val binding: NavigationItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NavigationItem) = binding.run {
            val context = root.context
            icon.setImageDrawable(ContextCompat.getDrawable(context, item.iconId))
            title.text = context.getString(item.titleId)
            root.setOnClickListener {
                item.navigate(context)
            }
        }
    }
}
