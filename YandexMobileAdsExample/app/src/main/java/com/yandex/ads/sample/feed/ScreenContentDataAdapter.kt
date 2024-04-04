/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2024 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ListItemBinding

class ScreenContentDataAdapter(
    private val items: List<Int>
): RecyclerView.Adapter<ScreenContentDataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.icon.setImageDrawable(AppCompatResources
                .getDrawable(binding.root.context, R.drawable.ic_outline_feed_24))
            binding.title.text = binding.root.context
                .getString(R.string.another_adapter_item_title, position)
        }
    }
}
