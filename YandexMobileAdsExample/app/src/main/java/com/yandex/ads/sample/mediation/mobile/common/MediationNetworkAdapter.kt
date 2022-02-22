/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.mobile.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.SpinnerItemBinding

class MediationNetworkAdapter(private val networks: List<MediationNetwork>, context: Context) :
    ArrayAdapter<MediationNetwork>(context, R.layout.spinner_item, networks) {

    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val inflater: LayoutInflater = LayoutInflater.from(parent?.context)
        val binding = SpinnerItemBinding.inflate(inflater, parent, false)
        binding.item.text = parent?.context?.getString(networks[position].name)
        return binding.root
    }
}