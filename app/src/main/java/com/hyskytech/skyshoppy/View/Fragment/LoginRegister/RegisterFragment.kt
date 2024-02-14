package com.hyskytech.skyshoppy.View.Fragment.LoginRegister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hyskytech.skyshoppy.R
import com.hyskytech.skyshoppy.View.Activity.ShoppingActivity
import com.hyskytech.skyshoppy.data.User
import com.hyskytech.skyshoppy.databinding.FragmentRegisterBinding
import com.hyskytech.skyshoppy.util.RegisterValidation
import com.hyskytech.skyshoppy.util.Resource
import com.hyskytech.skyshoppy.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

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
            btnRegisterRegister.setOnClickListener {
                val user = User(
                    EdtFirstName.text.toString().trim(),
                    EdtLastName.text.toString().trim(),
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
                        binding.btnRegisterRegister.startAnimation()
                    }
                    is Resource.Success ->{
                        Log.d("SuccessRegister",it.data.toString())
                        Toast.makeText(requireContext(), "User Registered Successfully", Toast.LENGTH_LONG).show()
                        binding.btnRegisterRegister.revertAnimation()
                        Intent(requireContext(),ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    is Resource.Error ->{
                        Log.e("ErrorRegister",it.message.toString())
                        Toast.makeText(activity, it.message.toString(), Toast.LENGTH_LONG).show()
                        binding.btnRegisterRegister.revertAnimation()
                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect{ validation ->
                if (validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.EdtEmail.apply {
                            requestFocus()
                            error= validation.email.message
                        }
                    }
                }
                if (validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.EdtPassword.apply {
                            requestFocus()
                            error= validation.password.message
                        }
                    }
                }
                if (validation.firstName is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.EdtFirstName.apply {
                            requestFocus()
                            error= validation.firstName.message
                        }
                    }
                }
            }
        }

        binding.goToLoginPage.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}