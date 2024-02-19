package com.hyskytech.skyshoppy.View.Fragment.LoginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.hyskytech.skyshoppy.R
import com.hyskytech.skyshoppy.databinding.FragmentIntroBinding
import com.hyskytech.skyshoppy.databinding.FragmentLoginBinding
import com.hyskytech.skyshoppy.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroductionFragment : Fragment(R.layout.fragment_intro) {

    private lateinit var binding : FragmentIntroBinding

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