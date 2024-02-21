package com.hyskytech.skyshoppy.View.Fragment.Shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.hyskytech.skyshoppy.Adapter.HomeViewpagerAdapter
import com.hyskytech.skyshoppy.R
import com.hyskytech.skyshoppy.View.Fragment.Categories.AccessoryFragment
import com.hyskytech.skyshoppy.View.Fragment.Categories.CapFragment
import com.hyskytech.skyshoppy.View.Fragment.Categories.MainCategoryFragment
import com.hyskytech.skyshoppy.View.Fragment.Categories.TShirtFragment
import com.hyskytech.skyshoppy.View.Fragment.Categories.TrouserFragment
import com.hyskytech.skyshoppy.View.Fragment.Categories.VestsFragment
import com.hyskytech.skyshoppy.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            TShirtFragment(),
            TrouserFragment(),
            VestsFragment(),
            CapFragment(),
            AccessoryFragment()
        )

        val viewPager2Adapter = HomeViewpagerAdapter(categoriesFragment, childFragmentManager, lifecycle)
        binding.viewPagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.TabLayoutHome,binding.viewPagerHome){tab, position ->
            when(position){
                0 -> {
                    tab.text="Deals"
                    tab.icon=AppCompatResources.getDrawable(requireContext(),R.drawable.ic_hotdeal)
                }
                1 -> {
                    tab.text="TShirt"
                    tab.icon=AppCompatResources.getDrawable(requireContext(),R.drawable.ic_tshirt)
                }
                2 -> {
                    tab.text="Trousers"
                    tab.icon=AppCompatResources.getDrawable(requireContext(),R.drawable.ic_trousers)
                }
                3 -> {
                    tab.text="Vests"
                    tab.icon=AppCompatResources.getDrawable(requireContext(),R.drawable.ic_vest)
                }
                4 -> {
                    tab.text="Caps"
                    tab.icon=AppCompatResources.getDrawable(requireContext(),R.drawable.ic_cap)
                }
                5 -> {
                    tab.text="Accessories"
                    tab.icon=AppCompatResources.getDrawable(requireContext(),R.drawable.ic_accessories)
                }
            }
        }.attach()
    }
}