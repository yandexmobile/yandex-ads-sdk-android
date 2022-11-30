/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adunits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.FragmentAdInfoBinding
import com.yandex.ads.sample.network.Network
import com.yandex.ads.sample.network.NetworkAdapter

class AdInfoFragment : Fragment(R.layout.fragment_ad_info) {

    val selectedNetwork get() = networks[selectedIndex]

    var onLoadClickListener: (() -> Unit)? = null

    private val binding get() = _binding!!

    private var selectedIndex = 0
    private var _binding: FragmentAdInfoBinding? = null

    private lateinit var networks: ArrayList<Network>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            networks = getSerializable(NETWORKS) as ArrayList<Network>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAdInfoBinding.inflate(layoutInflater, container, false).apply {
            loadAdButton.setOnClickListener {
                showLoading()
                onLoadClickListener?.invoke()
            }
            if (networks.size <= 1) {
                networkMenu.isVisible = false
                return@apply
            }
            networkMenu.setStartIconDrawable(selectedNetwork.iconId)
            networkTextView.setText(selectedNetwork.titleId)
            networkTextView.setAdapter(NetworkAdapter(requireContext(), networks))
            networkTextView.setOnItemClickListener { _, _, position, _ ->
                selectedIndex = position
                networkMenu.setStartIconDrawable(selectedNetwork.iconId)
                hideLoading()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun log(message: String) {
        binding.log.text = "${binding.log.text}\n$message"
    }

    fun showLoading() {
        binding.loadAdButton.apply {
            isEnabled = false
            text = getString(R.string.loading_ad)
        }
    }

    fun hideLoading() {
        binding.loadAdButton.apply {
            isEnabled = true
            text = getString(R.string.load_ad)
        }
    }

    companion object {

        private const val NETWORKS = "networks"

        @JvmStatic
        fun newInstance(
            networks: ArrayList<Network>,
        ) = AdInfoFragment().apply {
            arguments = Bundle().apply {
                putSerializable(NETWORKS, networks)
            }
        }
    }
}
