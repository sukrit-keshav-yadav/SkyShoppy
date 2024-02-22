package com.hyskytech.skyshoppy.View.Fragment.LoginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.hyskytech.skyshoppy.R
import com.hyskytech.skyshoppy.View.Activity.ShoppingActivity
import com.hyskytech.skyshoppy.databinding.FragmentIntroBinding
import com.hyskytech.skyshoppy.databinding.FragmentLoginBinding
import com.hyskytech.skyshoppy.viewModel.IntroductionViewModel
import com.hyskytech.skyshoppy.viewModel.IntroductionViewModel.Companion.CURRENT_PAGE
import com.hyskytech.skyshoppy.viewModel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import com.hyskytech.skyshoppy.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class IntroductionFragment : Fragment(R.layout.fragment_intro) {

    private lateinit var binding : FragmentIntroBinding
    private val viewModel by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.navigate.collect{
                when(it){
                    SHOPPING_ACTIVITY -> {
                        delay(3000)
                        Intent(requireContext(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    CURRENT_PAGE ->{
                        binding.btnLoginInfo.visibility=View.VISIBLE
                        binding.btnRegisterInfo.visibility=View.VISIBLE
                    }
                }
            }
        }

        binding.btnLoginInfo.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                binding.AppName to "TransSkyShoppy",
                binding.btnRegisterInfo to "TransButton",
            )
            findNavController().navigate(R.id.action_introductionFragment_to_loginFragment,null,null,extras)
        }
        binding.btnRegisterInfo.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                binding.AppName to "TransSkyShoppy",
                binding.btnRegisterInfo to "TransButton",
            )
            findNavController().navigate(R.id.action_introductionFragment_to_registerFragment,null,null,extras)
        }
    }
}