/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.policy

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.yandex.ads.sample.databinding.PolicyItemBinding
import com.yandex.ads.sample.settings.PoliciesActivity

class PolicyAdapter(
    private val context: FragmentActivity,
    private val preferences: SharedPreferences,
    private val items: List<PolicyItem>,
) : RecyclerView.Adapter<PolicyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PolicyItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount() = items.size

    inner class ViewHolder(val binding: PolicyItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) = binding.run {
            val item = items[position]
            val enabled = preferences.getBoolean(item.tag, false)

            icon.setImageDrawable(ContextCompat.getDrawable(context, item.iconId))
            title.setText(if (enabled) item.enabledTitleId else item.disabledTitleId)
            context.supportFragmentManager.setFragmentResultListener(
                item.tag,
                context
            ) { _, bundle ->
                val value = bundle.getBoolean(PoliciesActivity.VALUE)
                preferences.edit { putBoolean(item.tag, value) }
                item.onDialogResult(value)
                notifyItemChanged(position)
            }
            dialogButton.setOnClickListener {
                item.dialogFactory()
                    .show(context.supportFragmentManager, item.tag)
            }
        }
    }
}
