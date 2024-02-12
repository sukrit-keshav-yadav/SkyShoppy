package com.hyskytech.skyshoppy.View.Fragment.LoginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hyskytech.skyshoppy.data.User
import com.hyskytech.skyshoppy.databinding.FragmentRegisterBinding
import com.hyskytech.skyshoppy.util.Resource
import com.hyskytech.skyshoppy.viewModel.RegisterViewModel

class RegisterFragment : Fragment() {

    private lateinit var binding : FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnRegister.setOnClickListener {
                val user = User(
                    EdtName.text.toString().trim(),
                    EdtEmail.text.toString().trim()
                )
                val password = EdtPassword.text.toString()

                viewModel.createAccountWithEmailAndPassword(user,password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.btnRegister.startAnimation()
                    }
                    is Resource.Success ->{
                        Log.d("TAG",it.data.toString() )
                        binding.btnRegister.revertAnimation()
                    }
                    is Resource.Error ->{
                        Log.e("TAG",it.message.toString() )
                        binding.btnRegister.revertAnimation()
                    }
                    else -> {}
                }

            }
        }
    }
}