/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.network

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ListItemBinding

class NetworkAdapter(
    context: Context,
    private val items: List<Network>,
) : ArrayAdapter<String>(context, R.layout.list_item, items.map { context.getString(it.titleId) }) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView != null) return convertView
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        val item = items[position]
        binding.run {
            icon.setImageDrawable(ContextCompat.getDrawable(context, item.iconId))
            title.text = context.getString(item.titleId)
        }
        return binding.root
    }
}
